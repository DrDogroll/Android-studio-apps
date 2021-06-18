package com.dogroll.admin.vetlauncher;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.dogroll.admin.vetlauncher.Adapter.SearchAdapterST;
import com.dogroll.admin.vetlauncher.Models.stockItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Stocktake extends AppCompatActivity implements SearchView.OnQueryTextListener {

    DB2Handler PaidDB;
    DBHandler MasterDB;
    httpSendandReceive hsar;
    RecyclerView recyclerView;
    SearchAdapterST adapter;
    SearchView searchbar;
    ProgressBar pb;
    Integer id;
    String vmpsCode;
    String product;
    String grouping;
    String barcode;
    String onHand;
    String User;
    int requestCode = 765;
    boolean connected;
    private final UserInfoForUpload uIFU = new UserInfoForUpload();
    String userData;
    String urlList = "http://software.blackdogservices.co.nz/getStocklistVL.php";
    String error = "Error - Server response is empty (no stock items to download?)";


    public static ArrayList<stockItems> stockModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocktake);
        setTitle("Stocktake");

        PaidDB = new DB2Handler(this);
        hsar = new httpSendandReceive();
        recyclerView = findViewById(R.id.recycler);
        stockModelArrayList = PaidDB.getAllStockItems();
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        getUser();

        Button barcodeButton = findViewById(R.id.scanbarcode);
        barcodeButton.setOnClickListener(view -> {
            Intent bCode = new Intent(getApplicationContext(), BarcodeScanner.class);
            startActivityForResult(bCode, 10);
        });

        final TextView productSelected = findViewById(R.id.product);
        Button store = findViewById(R.id.buttonsubmit);

        adapter = new SearchAdapterST(this, stockModelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                id = stockModelArrayList.get(position).getId();
                vmpsCode = stockModelArrayList.get(position).getVpmsCode();
                product = stockModelArrayList.get(position).getProduct();
                grouping = stockModelArrayList.get(position).getGrouping();
                barcode = stockModelArrayList.get(position).getBarcode();
                onHand = stockModelArrayList.get(position).getOnHand();
                closeKeyboard();
                productSelected.setText(product);
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));
        searchbar = findViewById(R.id.searchstock);
        searchbar.setOnQueryTextListener(this);

        Button downloadList = findViewById(R.id.downloadlist);
        Button uploadListBtn = findViewById(R.id.uploadListBtn);

            store.setVisibility(View.INVISIBLE);
            downloadList.setVisibility(View.INVISIBLE);
            uploadListBtn.setVisibility(View.INVISIBLE);

        int cCount = PaidDB.getStockUploadCount();
        String sCount = "Upload List (" + cCount + ")";
        uploadListBtn.setText(sCount);

        store.setOnClickListener(view -> {
            EditText countET = findViewById(R.id.count);
            String count = countET.getText().toString();
            String response;
            if (count.isEmpty()) {
                response = "Please enter your count value";
            } else if (product.isEmpty()){
                response = "Please select a product";
            } else {
                Integer status = 1;
                PaidDB.AddCountedStockItem(vmpsCode, product, grouping, count, User, status);
                response = "Stored";
                countET.setText("");
                updateCounts();
                closeKeyboard();
            }
            Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        });
        connected = new NetworkUtil().isConnectedToNetwork(getApplicationContext());
        if (connected) {
            userData = uIFU.userInfoForUpload(this, "");
            if(!userData.equals("Stop")){
                downloadList.setVisibility(View.VISIBLE);
                }
        }
        downloadList.setOnClickListener(view -> {
            connected = new NetworkUtil().isConnectedToNetwork(getApplicationContext());
            if (connected) {
                userData = uIFU.userInfoForUpload(this, "");
                if(!userData.equals("Stop")){
                PaidDB.stockListRefresh();
                new JsonStringJsonDB().execute(urlList);}
            }
        });

        uploadListBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UploadStocktake.class);
            startActivityForResult(intent, requestCode );
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private final GestureDetector gestureDetector;
        private final ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)) {
                clickListener.onClick(child, recyclerView.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                String bcode = data.getStringExtra("Barcode");
                searchbar.setQuery(bcode, true);
            }
        }
        if (requestCode == 765){
            if (resultCode == RESULT_OK){
                refreshView();
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
                userData = uIFU.userInfoForUpload(Stocktake.this, "");
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
                        JSONArray stockList;
                        if (success == 1) {
                            stockList = jObjDL.getJSONArray("data");
                            Log.d("JSONArray", "Output is " + stockList);

                            for (int i = 0; i < stockList.length(); i++) {
                                JSONObject stockOBJ = stockList.getJSONObject(i);
                                String vpmsCode = stockOBJ.getString("vpmsCode");
                                String product = stockOBJ.getString("productName");
                                String grouping = stockOBJ.getString("grouping");
                                String barcode = stockOBJ.getString("barcode");
                                String onHand = stockOBJ.getString("onHand");
                                PaidDB.AddStockItem(vpmsCode, product, grouping, barcode, onHand);
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
                    Toast.makeText(Stocktake.this, "Tried checking, but there was an error: " + error, Toast.LENGTH_LONG).show();
                } else {
                    refreshView();
                }
            }
        }

        public void refreshView() {
            finish();
            overridePendingTransition(0, 0);
            Intent intent = new Intent(this, Stocktake.class);
            startActivity(intent);
        }
        public void updateCounts(){
            Button uploadListBtn = findViewById(R.id.uploadListBtn);
            int iCount = PaidDB.getStockUploadCount();
            String sCount = "Upload List (" + iCount + ")";
            uploadListBtn.setText(sCount);
        }
    public void getUser() {
        MasterDB = new DBHandler(getApplicationContext());
        Cursor result = MasterDB.getuserinfo();
        if (result.getCount() == 0) {
            Toast.makeText(this, "WARNING: Database is empty", Toast.LENGTH_LONG).show();
        } else {
            result.moveToLast();
            User = result.getString(result.getColumnIndex("username"));
            result.close();
            MasterDB.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int iCount = PaidDB.getStocklistCount();
        if (iCount > 1){
            Button store = findViewById(R.id.buttonsubmit);
            Button uploadlist = findViewById(R.id.uploadListBtn);
            store.setVisibility(View.VISIBLE);
            uploadlist.setVisibility(View.VISIBLE);
        }
    }
    }
