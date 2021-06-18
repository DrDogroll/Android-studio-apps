package com.dogroll.admin.vetlauncher;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class VetNotifications extends AppCompatActivity {

    DB2Handler PaidDB;
    ProgressBar pb;
    String url = "http://software.blackdogservices.co.nz/getNotificationsVL.php";
    String onlineDBDate;
    String appDBDate;
    httpSendandReceive hsar;
    String error = "";
    Integer uptCheck = 0;
    private final UserInfoForUpload uIFU = new UserInfoForUpload();
    String userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_notifications);
        setTitle("Vet Notifications");

        PaidDB = new DB2Handler(this);
        hsar = new httpSendandReceive();
        final TextView notificationsTextView = findViewById(R.id.notificationsTextView);
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        Spinner spinner = findViewById(R.id.notificationSpinner);
        List<String> spinnerlist = PaidDB.getAllVetNotifications();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(VetNotifications.this, android.R.layout.simple_spinner_item, spinnerlist);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer index = position +1;
                List<String> vNote = PaidDB.getSpecificVetNotification(index);
                notificationsTextView.setText(vNote.get(0));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button vNUpdate = findViewById(R.id.updateNotesBTN);
        vNUpdate.setVisibility(View.INVISIBLE);

        boolean connected = new NetworkUtil().isConnectedToNetwork(getApplicationContext());
        if (connected) {
            userData = uIFU.userInfoForUpload(this, "UTD");
            if(!userData.equals("Stop")){new getOnlineDBDate().execute();}
        }
        vNUpdate.setOnClickListener(v -> {
            PaidDB.notificationRefresh();
            userData = uIFU.userInfoForUpload(this, "DL");
            new JsonStringJsonDB().execute(url);
        });
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
                    JSONArray notifications;
                    if (success == 1) {
                        notifications = jObjDL.getJSONArray("data");
                    for (int i = 0; i < notifications.length(); i++) {
                        JSONObject notificationOBJ = notifications.getJSONObject(i);
                        String category = notificationOBJ.getString("category");
                        String item = notificationOBJ.getString("item");
                        PaidDB.AddVetNotification(category, item);
                    }
                    String logo = jObjDL.getString("logo");
                    if (!logo.equals("")){
                        PaidDB.updateLogo(logo);
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
                Toast.makeText(VetNotifications.this, "Tried checking, but there was an error: "+error, Toast.LENGTH_LONG).show();
            }else{refreshView();}
        }
    }
    public void refreshView (){
        finish();
        overridePendingTransition(0,0);
        Intent intent = new Intent(this, VetNotifications.class );
        startActivity(intent);
    }

    public class getOnlineDBDate extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {

            onlineDBDate = hsar.HttpSendandReceive(url, userData, null);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            appDBDate = PaidDB.getAppDBDate("VN");

            if (!onlineDBDate.contains("ERROR")){
                Date appDBNo = null;
                Date onlineDBNo = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.ENGLISH);

                if (!appDBDate.equals("")) {
                    try {
                        format.setTimeZone(TimeZone.getTimeZone("GMT"));
                        appDBNo = format.parse(appDBDate);
                    } catch (ParseException e) {
                        Log.e("Error Parsing","Error parsing acquired app string to date");
                    }
                }
                if(appDBDate.equals("")){
                    try{
                        appDBNo = format.parse("1990-01-01 01:01:01");}
                    catch (ParseException e){
                        Log.e("Error Parsing","Error parsing default app string to date");
                    }
                }
                if(!onlineDBDate.equals("")){
                    try {
                        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                        onlineDBNo = format.parse(onlineDBDate);

                    } catch (ParseException e) {
                        Log.e("Error Parsing","Error parsing acquired online string to date");
                    }
                }
                if(!(onlineDBNo == null) && !(appDBNo == null)) {
                    uptCheck = 1;
                    if (onlineDBNo.after(appDBNo)) {
                        Button cUpdateBTN = findViewById(R.id.updateNotesBTN);
                        cUpdateBTN.setVisibility(View.VISIBLE);
                    }
                }

            }else{
                error = onlineDBDate;
            }
            if(uptCheck == 0){
                Toast.makeText(VetNotifications.this, "Tried checking for updates, but there was an error"+error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
