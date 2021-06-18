package com.dogroll.admin.vetlauncher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.dogroll.admin.vetlauncher.Adapter.ListAdapterStockUpload;
import com.dogroll.admin.vetlauncher.Models.uploadItems;
import java.util.ArrayList;

public class UploadStocktake extends AppCompatActivity {

    DB2Handler PaidDB;
    RecyclerView recyclerView;
    ProgressBar pb;
    httpSendandReceive hsar;
    ListAdapterStockUpload adapter;
    Integer countofrecords;
    String url = "http://software.blackdogservices.co.nz/getStockCountsVL.php";
    private final UserInfoForUpload uIFU = new UserInfoForUpload();
    String userData;
    Boolean result;
    AlertDialog alertDialog;
    Button uploadListToCloud;

    public static ArrayList<uploadItems> itemsToUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_stocktake);
        setTitle("Upload Stock Counts");

        PaidDB = new DB2Handler(this);
        hsar = new httpSendandReceive();
        recyclerView = findViewById(R.id.recyclerUploadList);
        itemsToUpload = PaidDB.showStockToUpload();
        adapter = new ListAdapterStockUpload(this, itemsToUpload);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setRecyclerListener(holder -> refreshCount());

        countofrecords = adapter.getItemCount();
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        uploadListToCloud = findViewById(R.id.uploadListtoCloud);
        refreshCount();

        uploadListToCloud.setOnClickListener(v -> {
            boolean connected = new NetworkUtil().isConnectedToNetwork(getApplicationContext());
            if (connected) {
                userData = uIFU.userInfoForUpload(this, "ping");
                if(!userData.equals("Stop")){
                BuildandSendStockCounts bAndSSC = new BuildandSendStockCounts();
                bAndSSC.execute();}
            }
        });

        uploadListToCloud.setOnLongClickListener(v -> {
            AlertDialog.Builder adBuilder = new AlertDialog.Builder(this)
                .setTitle("Delete all?")
                .setMessage("Are you sure you wish to delete all counted stock records?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    PaidDB.stockUploadRefresh();
                    result = true;
                })
                .setNegativeButton("No", (dialog, which) -> {
                    result = false;
                    alertDialog.dismiss();
                });
            alertDialog = adBuilder.show();
            alertDialog.show();
            return result;
        });

    }
    class BuildandSendStockCounts extends AsyncTask<String, String, String>{

        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            Integer count = PaidDB.getStockUploadCount();
            Integer id;
            String serverResponse = hsar.HttpSendandReceive(url, userData, null);

            if (serverResponse.contains("success")) {
                Log.d("Send and Receive", "Successful");

                for (int i = 0; i < count; i++) {

                    ContentValues cv = PaidDB.getStockToUpload();
                    ContentValues dataAndID = PaidDB.processStocktoURIFormat(cv);
                    String dataToSend = dataAndID.getAsString("URI");
                    id = dataAndID.getAsInteger("id");
                    userData = uIFU.userInfoForUpload(UploadStocktake.this,"upload");
                    String serverResponseFromUpload = hsar.HttpSendandReceive(url, userData, dataToSend);
                        if (serverResponseFromUpload.contains("success")) {
                            Log.d("Send and Receive", "Successful");
                            if (id != null){
                                try {
                                    PaidDB.UpdateCountedStockItem(id);
                                } catch (Exception e) {
                                    Log.e("Error", "Could not update app database RE send");
                                    error = "ERROR - Could not finalise data sync";}
                            } else {
                                error = "ERROR - Record id does not match database for update";
                            }
                        } else {
                            error = "ERROR - Unsuccessful response from server";
                        }
                }
            }else{error = serverResponse;}
            return error;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pb.setVisibility(View.INVISIBLE);
            int iCount = PaidDB.getStockUploadCount();
                if (iCount == 0){
                    PaidDB.stockUploadRefresh();
                    setResult(RESULT_OK);
                    finish();
                }else {
                    Toast.makeText(UploadStocktake.this, error, Toast.LENGTH_LONG).show();
                }
        }
    }
    public void refreshCount (){
        int cCount = PaidDB.getStockUploadCount();
        String sCount = "Upload List (" + cCount + ")";
        uploadListToCloud.setText(sCount);
    }
}
