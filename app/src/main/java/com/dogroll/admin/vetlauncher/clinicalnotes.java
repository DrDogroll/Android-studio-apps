package com.dogroll.admin.vetlauncher;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

public class clinicalnotes extends AppCompatActivity {

    DBHandler MasterDB;
    ProgressBar pb;
    httpSendandReceive hsar;
    String url = "http://software.blackdogservices.co.nz/getClinicalNotesVL.php";
    String onlineDBDate;
    String appDBDate;
    String userData;
    Integer uptCheck = 0;
    String error = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinicalnotes);
        setTitle("Clinical Notes");

        MasterDB = new DBHandler(this);
        hsar = new httpSendandReceive();
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        TextView vitalsTV = findViewById(R.id.vitalsTV);
        TextView extendedNotes = findViewById(R.id.extendedNotesTV);
        TextView imageLinksTV = findViewById(R.id.imageLinksTV);


        Spinner spinner = findViewById(R.id.topicspin);
        List<String> spinnerlist = MasterDB.getAllCNoteTopics();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(clinicalnotes.this, android.R.layout.simple_spinner_item, spinnerlist);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer index = position +1;
                List<String> cNote = MasterDB.getClinicalNote(index);
                vitalsTV.setText(processText(cNote.get(0)));
                extendedNotes.setText(processText(cNote.get(1)));
                imageLinksTV.setText("");
                if (cNote.get(2).length()>5){
                    String linkChain = cNote.get(2);
                    String[] links = linkChain.split(",");
                    for (String singleLink : links){
                        String imageTitle = singleLink.substring(37,singleLink.lastIndexOf("."));
                        Spannable spannedTitle = new SpannableString(imageTitle);
                        spannedTitle.setSpan(
                         new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                Uri imageuri = Uri.parse(singleLink);
                                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, imageuri);
                                startActivity(launchBrowser);
                            }
                        }, 0, spannedTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        imageLinksTV.append(spannedTitle);
                        imageLinksTV.append("\n");
                        imageLinksTV.setClickable(true);
                        imageLinksTV.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
            Button cNUPDBTN = findViewById(R.id.cNUPDBTN);
            cNUPDBTN.setVisibility(View.INVISIBLE);

        boolean connected = new NetworkUtil().isConnectedToNetwork(getApplicationContext());
        if (connected) {
            new getOnlineDBDate().execute();

        }
        cNUPDBTN.setOnClickListener(v -> {
            if (MasterDB.getAppCNDate().equals("")){
                agreement();
            }else{
                MasterDB.dropCNTable();
                userData = userInfoForUpload("DL");
                new JsonStringJsonDB().execute(url);
            }
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
                    JSONArray topics;
                    if (success == 1) {
                        topics = jObjDL.getJSONArray("data");

                        for (int i = 0; i < topics.length(); i++) {
                            JSONObject topicOBJ = topics.getJSONObject(i);
                            String topic = topicOBJ.getString("topic");
                            String vitals = topicOBJ.getString("vitals");
                            String extendedNotes = topicOBJ.getString("extendedNotes");
                            String imageLinks = topicOBJ.getString("imageLinks");
                            MasterDB.AddClinicalNote(topic, vitals, extendedNotes, imageLinks);
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
                Toast.makeText(clinicalnotes.this, "Tried checking, but there was an error: "+error, Toast.LENGTH_LONG).show();
            }else{refreshView();}
        }
    }
    public void refreshView (){
        finish();
        overridePendingTransition(0,0);
        Intent intent = new Intent(this, clinicalnotes.class );
        startActivity(intent);
    }
    public String userInfoForUpload (String action){

        String dataToUpload;
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("action", action);
        dataToUpload = builder.build().getEncodedQuery();

        return dataToUpload;
    }

    public class getOnlineDBDate extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {

            String userDat = userInfoForUpload("UTD");
            onlineDBDate = hsar.HttpSendandReceive(url, userDat, null);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            appDBDate = MasterDB.getAppCNDate();

            if (!onlineDBDate.contains("Error")){
                Date appDBNo = null;
                Date onlineDBNo = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.ENGLISH);

                if (!appDBDate.equals("")) {
                    try {
                        format.setTimeZone(TimeZone.getTimeZone("GMT"));
                        appDBNo = format.parse(appDBDate);
    //Internal database is written at GMT
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
//Australian online Server is 4 hours ahead of NZ, so even when client updates, the app still thinks it's 4 hours behind.
                    } catch (ParseException e) {
                        Log.e("Error Parsing","Error parsing acquired online string to date");
                    }
                }
                if(!(onlineDBNo == null) && !(appDBNo == null)){
                    uptCheck = 1;
                    if (onlineDBNo.after(appDBNo)) {
                        Button cUpdateBTN = findViewById(R.id.cNUPDBTN);
                        cUpdateBTN.setVisibility(View.VISIBLE);
                    }
                }
            }else{
               error = onlineDBDate;
            }
            if(uptCheck == 0){
                Toast.makeText(clinicalnotes.this, "Tried checking for updates, but there was an error"+error, Toast.LENGTH_LONG).show();
            }
        }
    }
    void agreement (){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("User Agreement")
                .setMessage("By selecting Yes and downloading these clinical notes, you acknowledge that these notes will be used as a guide. You are the veterinarian, you must use your clinical judgement. Feedback, images, dose changes etc on these notes are very welcome, as collegial sharing of knowledge is the goal. Email Dave at dave@blackdogservices.co.nz")
                .setPositiveButton("Yes", (dialog, which) -> {
                    MasterDB.dropCNTable();
                    userData = userInfoForUpload("DL");
                    new JsonStringJsonDB().execute(url);
                })
                .setNegativeButton("No", (dialog, which) -> finish());
        AlertDialog alertDialog = alertDialogBuilder.show();
        alertDialog.show();
    }
    String processText (String string){
        String result;
        if (string.contains("\\n")){
            result = string.replace("\\n","\n");
        }else result = string;
        return result;
    }
}
