package com.dogroll.admin.vetlauncher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.ProgressBar;
import android.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.dogroll.admin.vetlauncher.Adapter.SearchAdapterC;
import com.dogroll.admin.vetlauncher.Models.client;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class clients extends AppCompatActivity implements SearchView.OnQueryTextListener {

    int REQUEST_PHONE_PERMISSION = 303;
    RecyclerView recyclerView;
    SearchAdapterC adapter;
    SearchView searchview;
    DB2Handler PaidDB;
    Integer id;
    String dairyNumber;
    String farmName;
    String contactName;
    String number;
    String address;
    String url = "http://software.blackdogservices.co.nz/getClientListVL.php";
    ProgressBar pb;
    String error = "";
    private final httpSendandReceive hsar = new httpSendandReceive();
    private final UserInfoForUpload uIFU = new UserInfoForUpload();
    String userData;
    private int uptCheck = 0;

    public static ArrayList<client> clientFullArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        setTitle("Client List");

        PaidDB = new DB2Handler(this);
        recyclerView = findViewById(R.id.clientRecycler);
        clientFullArrayList = PaidDB.getAllClientDetails();
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        final TextView dairyNumberSelected = findViewById(R.id.dairyNumberTXT);
        final TextView farmNameSelected = findViewById(R.id.farmNameTXT);
        final TextView contactNameSelected = findViewById(R.id.contactNameTXT);
        final TextView numberSelected = findViewById(R.id.numberTXT);
        final TextView addressSelected = findViewById(R.id.addressTXT);

        adapter = new SearchAdapterC(this, clientFullArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                id = clientFullArrayList.get(position).getId();
                dairyNumber = clientFullArrayList.get(position).getDairyNumber();
                farmName = clientFullArrayList.get(position).getFarmName();
                contactName = clientFullArrayList.get(position).getContactName();
                number = clientFullArrayList.get(position).getNumber();
                address = clientFullArrayList.get(position).getAddress();
                closeKeyboard();
                dairyNumberSelected.setText(dairyNumber);
                farmNameSelected.setText(farmName);
                contactNameSelected.setText(contactName);
                SpannableString numberUnderlined = new SpannableString(number);
                numberUnderlined.setSpan(new UnderlineSpan(), 0, number.length(), 0);
                numberSelected.setText(numberUnderlined);
                SpannableString addressUnderlined = new SpannableString(address);
                addressUnderlined.setSpan(new UnderlineSpan(), 0, address.length(), 0);
                addressSelected.setText(addressUnderlined);
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        numberSelected.setOnClickListener(v -> callClient());

        addressSelected.setOnClickListener(v -> {
            String query = "";
            if (address != null) {
                try {
                    query = URLEncoder.encode(address, "UTF-8");
                } catch (Exception e) {
                    Log.e("address encode", "Error encoding address to URL");
                }
                String urlString = "https://www.google.com/maps/search/?api=1&query=" + query;
                Intent launchMap = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                startActivity(launchMap);
            }
        });

        searchview = findViewById(R.id.clientSearchView);
        searchview.setOnQueryTextListener(this);

        Button cUpdateBTN = findViewById(R.id.clientUpdateButton);
        cUpdateBTN.setVisibility(View.INVISIBLE);
        boolean connected = new NetworkUtil().isConnectedToNetwork(getApplicationContext());
        if (connected) {
            userData = uIFU.userInfoForUpload(this, "UTD");
            if(!userData.equals("Stop")){new getOnlineDBDate().execute();}
        }

        cUpdateBTN.setOnClickListener(v -> {
            PaidDB.clientListRefresh();
            userData = uIFU.userInfoForUpload(this, "DL");
            new JsonStringJsonDB().execute(url);
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
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

    private class JsonStringJsonDB extends AsyncTask<String, String, String> {

        JSONObject jObjDL;

        protected void onPreExecute() {
            super.onPreExecute();

            pb.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... params) {

            String serverResponse = hsar.HttpSendandReceive(url, userData, null);
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
                JSONArray clients;
                    if (success == 1) {
                        clients = jObjDL.getJSONArray("data");

                        for (int i = 0; i < clients.length(); i++) {
                            JSONObject clientOBJ = clients.getJSONObject(i);
                            String dairyNumber = clientOBJ.getString("dairyNumber");
                            String farmName = clientOBJ.getString("farmName");
                            String contactName = clientOBJ.getString("contactName");
                            String number = clientOBJ.getString("number");
                            String address = clientOBJ.getString("address");
                            PaidDB.AddClient(dairyNumber, farmName, contactName, number, address);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("Error", "Error parsing data from server to JSON object");
                    error = "ERROR - Could not convert server data to app data";
                }
            }else{error = serverResponse;}
            return error;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pb.setVisibility(View.INVISIBLE);
            if (!error.equals("")){
                Toast.makeText(clients.this, "Tried checking, but there was an error: "+error, Toast.LENGTH_LONG).show();
            }else{refreshView();}
        }
    }
    public void refreshView() {
        finish();
        overridePendingTransition(0,0);
        Intent intent = new Intent(this, clients.class );
        startActivity(intent);

    }

    public class getOnlineDBDate extends AsyncTask<String, String, String> {

        private String onlineDBDate;

        @Override
        protected String doInBackground(String... strings) {

            onlineDBDate = hsar.HttpSendandReceive(url, userData, null);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String appDBDate = PaidDB.getAppDBDate("CL");

            if (!onlineDBDate.contains("ERROR")) {
                Date appDBNo = null;
                Date onlineDBNo = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.ENGLISH);

                if (!appDBDate.equals("")) {
                    try {
                        format.setTimeZone(TimeZone.getTimeZone("GMT"));
                        appDBNo = format.parse(appDBDate);
                    } catch (ParseException e) {
                        Log.e("Error Parsing", "Error parsing acquired app string to date");
                    }
                }
                if (appDBDate.equals("")) {
                    try {
                        appDBNo = format.parse("1990-01-01 01:01:01");
                    } catch (ParseException e) {
                        Log.e("Error Parsing", "Error parsing default app string to date");
                    }
                }
                if (!onlineDBDate.equals("")) {
                    try {
                        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                        onlineDBNo = format.parse(onlineDBDate);

                    } catch (ParseException e) {
                        Log.e("Error Parsing", "Error parsing acquired online string to date");
                    }
                }
                if (!(onlineDBNo == null) && !(appDBNo == null)) {
                    uptCheck = 1;
                    if (onlineDBNo.after(appDBNo)) {
                        Button cUpdateBTN = findViewById(R.id.clientUpdateButton);
                        cUpdateBTN.setVisibility(View.VISIBLE);
                    }
                }

            } else {
                error = onlineDBDate;
            }
            if (uptCheck == 0) {
                Toast.makeText(clients.this, "Tried checking for updates, but there was an error" + error, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PHONE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callClient();
            } else {
                Toast.makeText(getApplicationContext(), "Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void callClient() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                String phoneNumber = String.format("tel: %s",
                        number);
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse(phoneNumber));
                if (dialIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(dialIntent);
                } else {
                    Log.e("phone error", "Can't resolve app for ACTION_DIAL Intent.");
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                        Toast.makeText(getApplicationContext(), "Phone permission is needed to call clients from this app", Toast.LENGTH_SHORT).show();
                    }
                }
                ActivityCompat.requestPermissions(this, new
                        String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_PERMISSION);
            }
        } catch (Exception e) {
            Log.e("phone error", "Phone permission error");
        }
    }
}