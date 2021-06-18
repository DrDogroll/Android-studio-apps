package com.dogroll.admin.vetlauncher;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;

public class departmentTransfer extends AppCompatActivity {

    boolean connected;
    private final UserInfoForUpload uIFU = new UserInfoForUpload();
    DB2Handler PaidDB;
    DBHandler MasterDB;
    DatePickerDialog datePickerDialog;
    ProgressBar pb;
    String userData, bCode, product, unit, date, user, amount, from, to, readyToTransfer;
    httpSendandReceive hsar;
    String urlList = "http://software.blackdogservices.co.nz/getBarcodeListVL.php";
    String urlTransfers = "http://software.blackdogservices.co.nz/getTransferListVL.php";
    String error = "Error - Server response is empty (no stock items to download?)";
    Boolean uploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_transfer);
        setTitle("Department Transfer");

        PaidDB = new DB2Handler(this);
        hsar = new httpSendandReceive();
        MasterDB = new DBHandler(this);

        Cursor result = MasterDB.getuserinfo();
        if (result != null){
            result.moveToLast();
            user = result.getString(result.getColumnIndex("username"));
            result.close();
            MasterDB.close();
        }


        EditText transferDate = findViewById(R.id.transferDate);
        ImageButton dateButton = findViewById(R.id.datebtnReminder);
        dateButton.setOnClickListener(v -> {
            final Calendar selected_date = Calendar.getInstance();
            int mDay = selected_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = selected_date.get(Calendar.MONTH);
            int mYear = selected_date.get(Calendar.YEAR);
            datePickerDialog = new DatePickerDialog(departmentTransfer.this, (view1, year, monthOfYear, dayOfMonth) -> {
                int truemonth = monthOfYear +1;
                date = dayOfMonth + "/" + truemonth + "/" + year;
                transferDate.setText(date);
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
            transferDate.setError(null);
        });

        Button barcodeButton = findViewById(R.id.btnScanBarcode);
        barcodeButton.setOnClickListener(view -> {
            if (PaidDB.isBarcodeList()){
                Intent bCode = new Intent(getApplicationContext(), BarcodeScanner.class);
                startActivityForResult(bCode, 20);
            }else{
                Toast.makeText(this, "Please download Barcode Lists, available in the paid version", Toast.LENGTH_LONG).show();
            }
        });

        Button btnGetBarCodeList = findViewById(R.id.btnGetBarcodeList);
        Button btnTransfer = findViewById(R.id.btnTransfer);
        pb = findViewById(R.id.progressBarDT);
        AutoCompleteTextView actvProductTrans = findViewById(R.id.actvProductTrans);
        EditText etDTFrom = findViewById(R.id.etDTFrom);
        EditText etDTTo = findViewById(R.id.etDTTo);
        EditText etDTAmount = findViewById(R.id.etDTAmount);
        EditText etDTUnits  = findViewById(R.id.etDTUnits);

        btnGetBarCodeList.setVisibility(View.INVISIBLE);
        btnTransfer.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.INVISIBLE);

        connected = new NetworkUtil().isConnectedToNetwork(getApplicationContext());
        if (connected) {
            userData = uIFU.userInfoForUpload(this, "");
            if(!userData.equals("Stop")){
                btnGetBarCodeList.setVisibility(View.VISIBLE);
            }
        }

        btnGetBarCodeList.setOnClickListener(v -> {
            PaidDB.dropProductBarcodeList();
            new JsonStringJsonDB().execute(urlList);
        });

        ArrayList<String> products = PaidDB.transferProductsList();

        String[] options = new String[products.size()];
        for (int i = 0; i < products.size(); i++){
            options[i] = products.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
        actvProductTrans.setAdapter(adapter);

        btnTransfer.setOnClickListener(v -> {
            product = actvProductTrans.getText().toString();
            unit = etDTUnits.getText().toString();
            from = etDTFrom.getText().toString();
            to = etDTTo.getText().toString();
            amount = etDTAmount.getText().toString();
            int send = 1;
            if (TextUtils.isEmpty(date)){
                transferDate.setError("Date required");
                send = 0;
            }
            if(TextUtils.isEmpty(product)){
                actvProductTrans.setError("Product Name Required");
                send = 0;
            }
            if (TextUtils.isEmpty(from)){
                etDTFrom.setError("Department required");
                send = 0;
            }
            if (TextUtils.isEmpty(to)){
                etDTTo.setError("Department required");
                send = 0;
            }
            if (TextUtils.isEmpty(amount)){
                etDTAmount.setError("Amount required");
                send = 0;
            }
            if (send == 1){
                new BuildandSendTransfer().execute(urlTransfers);
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == RESULT_OK) {
                ContentValues cv = null;
                bCode = data.getStringExtra("Barcode");
                try {
                    cv = PaidDB.findProductFromBarcode(bCode);
                }catch (Exception e){
                    Log.d("Barcode", "No barcode associations");
                    Toast.makeText(this, "Barcode does not match any product listed", Toast.LENGTH_LONG).show();
                }
                if (cv != null){
                    EditText etProductTrans = findViewById(R.id.actvProductTrans);
                    EditText etDTUnits  = findViewById(R.id.etDTUnits);
                    etProductTrans.setText(cv.getAsString("product"));
                    etDTUnits.setText(cv.getAsString("unit"));
                }
            }
        }
    }
    private class JsonStringJsonDB extends AsyncTask<String, String, String> {

        JSONObject jObjDL;

        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        protected String doInBackground (String...params){
            userData = uIFU.userInfoForUpload(departmentTransfer.this, "DL");
            String serverResponse = hsar.HttpSendandReceive(urlList, userData, null);
            if (serverResponse.contains("success")) {
                Log.d("Send and Receive", "Successful");

                try {
                    jObjDL = new JSONObject(serverResponse);

                } catch (JSONException e) {
                    Log.e("Error", "Error compiling records");
                    error = "ERROR - Could not compile records";
                }

                try {
                    int success = jObjDL.getInt("success");
                    JSONArray barcodeList;
                    if (success == 1) {
                        barcodeList = jObjDL.getJSONArray("data");
                        Log.d("JSONArray", "Output is " + barcodeList);

                        for (int i = 0; i < barcodeList.length(); i++) {
                            JSONObject barcodeOBJ = barcodeList.getJSONObject(i);
                            String barcode = barcodeOBJ.getString("barcode");
                            String product = barcodeOBJ.getString("product");
                            String unit = barcodeOBJ.getString("unit");
                            PaidDB.insertProductBarcodeList(barcode, product, unit);
                        }
                        error = "";
                    }
                } catch (JSONException e) {
                    Log.e("Error", "Error parsing data from server to JSON object");
                    error = "ERROR - Could not convert server data to app data";
                }
            } else {
                error = serverResponse;
            }
            return null;
        }

        @Override
        protected void onPostExecute (String result){
            super.onPostExecute(result);
            pb.setVisibility(View.INVISIBLE);
            if (!error.equals("")) {
                Toast.makeText(departmentTransfer.this, "Tried checking, but there was an error: " + error, Toast.LENGTH_LONG).show();
            } else {
                readyToTransfer = "y";
                refreshView();
            }
        }
    }class BuildandSendTransfer extends AsyncTask<String, String, String>{

        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            userData = uIFU.userInfoForUpload(departmentTransfer.this, "ping");
            String serverResponse = hsar.HttpSendandReceive(urlTransfers, userData, null);

            if (serverResponse.contains("success")) {
                Log.d("Send and Receive", "Successful");
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("date", date)
                        .appendQueryParameter("tUser", user)
                        .appendQueryParameter("barcode", bCode)
                        .appendQueryParameter("product", product)
                        .appendQueryParameter("unit", unit)
                        .appendQueryParameter("amount", amount)
                        .appendQueryParameter("from", from)
                        .appendQueryParameter("to", to);
                String dataToUpload = builder.build().getEncodedQuery();

                    userData = uIFU.userInfoForUpload(departmentTransfer.this,"upload");
                    String serverResponseFromUpload = hsar.HttpSendandReceive(urlTransfers, userData, dataToUpload);
                    if (serverResponseFromUpload.contains("success")) {
                        uploaded = true;
                        Log.d("Send and Receive", "Successful");
                    } else {
                        uploaded = false;
                        error = "ERROR - "+serverResponseFromUpload;
                    }
                }else{
                error = serverResponse;
                uploaded = false;
            }
            return error;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pb.setVisibility(View.INVISIBLE);
            if (uploaded){
                refreshView();
            }else {
                Toast.makeText(departmentTransfer.this, error, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void refreshView() {
        finish();
        overridePendingTransition(0, 0);
        Intent intent = new Intent(this, departmentTransfer.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        PaidDB = new DB2Handler(this);
        if (PaidDB.isBarcodeList()){
            Button btnTransfer = findViewById(R.id.btnTransfer);
            btnTransfer.setVisibility(View.VISIBLE);
        }
    }
}