package com.dogroll.admin.vetlauncher;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

public class setting extends AppCompatActivity {

    DBHandler MasterDB;
    String path, dbName, function, recordCount;
    String titleS = "Manage your Databases";
    String messageS = "Here you can Export, Import and Wipe your Diary, Body Condition Score and Continuing Professional Development records. Wiping databases will be useful if you plan on bulk editing the exported CVS file, then importing back into the app. If you don't wipe the exported database, after you import the CSV you'll have \"double-ups\"";
    int REQUEST_STORAGE_WRITE = 404;
    static final int READ_REQ = 24;
    static final int WRITE_REQ = 25;
    CSVProcessor csvProcessor;
    int noExported;
    ContentValues imported;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Settings");

        MasterDB = new DBHandler(this);
        csvProcessor = new CSVProcessor();

        dbName = MasterDB.getDatabaseName();

        final EditText k_vol_text= findViewById(R.id.K_vol_text);
        final EditText p_vol_text= findViewById(R.id.P_vol_text);
        final EditText p300_vol_text= findViewById(R.id.P300_vol_text);
        final EditText p500_vol_text= findViewById(R.id.P500_vol_text);
        final EditText x_vol_text= findViewById(R.id.X_vol_text);
        final EditText Useredittxt = findViewById(R.id.Useredittxt);
        final EditText email_text = findViewById(R.id.emailedittext);
        final EditText DBname_text = findViewById(R.id.DBname);
        final EditText DBpass_text = findViewById(R.id.DBpassword);

        Button savebtn = findViewById(R.id.savebtn);
        savebtn.setOnClickListener(view -> {

            String user = Useredittxt.getText().toString();
            String email = email_text.getText().toString();
            String DBname = DBname_text.getText().toString();
            String DBuser = DBname_text.getText().toString();
            String DBpass = DBpass_text.getText().toString();
            String kvolstr = k_vol_text.getText().toString();
            String pvolstr = p_vol_text.getText().toString();
            String p300volstr = p300_vol_text.getText().toString();
            String p500volstr = p500_vol_text.getText().toString();
            String xvolstr = x_vol_text.getText().toString();
            int save = 1;
            if (TextUtils.isEmpty(user)){
                Useredittxt.setError("Enter User name");
                save = 0;
            }
            if (TextUtils.isEmpty(kvolstr)){
                k_vol_text.setError("Enter Ketamine Volume");
                save = 0;
            }
            if (TextUtils.isEmpty(pvolstr)) {
                p_vol_text.setError("Enter Pamlin Volume");
                save = 0;
            }
            if (TextUtils.isEmpty(p300volstr)) {
                p300_vol_text.setError("Enter Pentobarb 300 Volume");
                save = 0;
            }
            if (TextUtils.isEmpty(p500volstr)) {
                p500_vol_text.setError("Enter Pentobarb 500 Volume");
                save = 0;
            }
            if (TextUtils.isEmpty(xvolstr)) {
                x_vol_text.setError("Enter Xylaket Volume");
                save = 0;
            }
            if (save ==1){
                double kvol = Double.parseDouble(k_vol_text.getText().toString());
                double pvol = Double.parseDouble(p_vol_text.getText().toString());
                double p300vol = Double.parseDouble(p300_vol_text.getText().toString());
                double p500vol = Double.parseDouble(p500_vol_text.getText().toString());
                double xvol = Double.parseDouble(x_vol_text.getText().toString());

                MasterDB.UPDuserinfo(user,email,DBname, DBuser,DBpass);
                MasterDB.UPDCDVol(kvol,pvol,p300vol,p500vol,xvol);
                finish();
            }
        });


        if (Environment.getExternalStorageState().equals("mounted")) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                String folderPath = Environment.getExternalStorageDirectory() + "/Documents/Vet Launcher";
                File folder = new File(folderPath);
                if (!folder.exists()){
                    if (!folder.mkdirs()){
                        Toast.makeText(this, "Error creating save directories", Toast.LENGTH_SHORT).show();
                    }
                }
                path = folder.getAbsolutePath();
            }
        }else{
            Toast.makeText(this,"External storage of your device is not available", Toast.LENGTH_LONG ).show();
            TableLayout tableLayout = findViewById(R.id.Table1);
            tableLayout.setVisibility(View.GONE);
        }
        Button manage = findViewById(R.id.settingsManageBTN);
        manage.setOnClickListener(v -> {
            LayoutInflater inflater = this.getLayoutInflater();
            View adDatabase = inflater.inflate(R.layout.alert_databases,null);
            AlertDialog.Builder alertDialogB = new AlertDialog.Builder(this);
            alertDialogB.setView(adDatabase);
            TextView title = adDatabase.findViewById(R.id.alertDatabaseTitle);
            TextView message = adDatabase.findViewById(R.id.alertDatabaseMessage);
            RadioGroup actionGroup = adDatabase.findViewById(R.id.actionGroup);
            Button execute = adDatabase.findViewById(R.id.btnAlertDatabase);
            title.setText(titleS);
            message.setText(messageS);
            execute.setOnClickListener(v1 -> {
                try{
                    int radioButtonID = actionGroup.getCheckedRadioButtonId();
                    RadioButton selectedFunction = adDatabase.findViewById(radioButtonID);
                    function = selectedFunction.getText().toString();
                }catch (Exception e){
                    Log.e("Error","No database management function selected");
                }
                alertDialog.dismiss();
                permissionCheck();
            });
            alertDialog = alertDialogB.show();
            alertDialog.show();
        });
    }
    void permissionCheck(){
        try{
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    if (function.contains("Import")){
                        imported = csvProcessor.importData(getApplicationContext(),function, null);
                    }
                    if (function.contains("Export")){
                        noExported = csvProcessor.exportData(getApplication(), function, null);
                    }
                }else {
                    if (function.contains("Import")){
                        readFileByIntent();
                    }
                    if (function.contains("Export")){
                        createFileByIntent();
                    }
                }
                if (function.contains("Delete")){
                    dropTable();
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        Toast.makeText(getApplicationContext(), "Read External Storage permission is needed to import and export", Toast.LENGTH_LONG).show();
                    }
                }
                ActivityCompat.requestPermissions(new setting(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_WRITE);
            }
        }catch (Exception e){
            Log.e("Permission error", "Permission request or check error");
        }
    }
    void dropTable(){
        switch (function){
            case "aDDELDAI":
                MasterDB.refreshDiaryTable();
                break;
            case "aDDELBCS":
                MasterDB.refreshBCSTable();
                break;
            case "aDDELCPD":
                MasterDB.refreshCPDTable();
                break;
        }
    }
    public void readFileByIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
       startActivityForResult(intent, READ_REQ);
    }

    public void createFileByIntent() {
        @SuppressLint("InlinedApi")
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        String fileName = function+".csv";
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQ);
    }
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String [] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == REQUEST_STORAGE_WRITE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionCheck();
            } else {
                Toast.makeText(getApplicationContext(), "Permission was not granted", Toast.LENGTH_LONG).show();
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
            }
            if (uri != null){
                if (requestCode == READ_REQ) {
                    imported = csvProcessor.importData(this, function, uri);

                } else if (requestCode == WRITE_REQ) {
                    noExported = csvProcessor.exportData(getApplicationContext(), function, uri);
                }
            }else {
                Toast.makeText(this, "Unable to determine Android 10+ save uri", Toast.LENGTH_LONG).show();
            }
            postImportOrExport();
        }
    }

    void postImportOrExport(){
        if (imported != null){
            String success = imported.getAsString("success");
            String fail = imported.getAsString("fail");
            if (imported.getAsString("fail").equals("unknown")){

                Toast.makeText(this, success + " successful imports, " + fail + " failed imports. A syntax error occurred at row " + imported.getAsString("exemption"),Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, success + " successful imports, " + fail + " failed imports.",Toast.LENGTH_LONG).show();
            }
        }
        if (noExported >= 0){
            recordCount = Integer.toString(noExported);
            Toast.makeText(this, recordCount+" records successfully exported to CSV", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MasterDB = new DBHandler(this);

        Cursor result = MasterDB.getuserinfo();
        if(result.getCount()!=0) {

            TextView Usertxtview = findViewById(R.id.Useredittxt);
            TextView Emailtxtview = findViewById(R.id.emailedittext);
            TextView DBnametxtview = findViewById(R.id.DBname);
            TextView DBpasstxtview = findViewById(R.id.DBpassword);

            result.moveToLast();
            String user = result.getString(result.getColumnIndex("username"));
            Usertxtview.setText(user);
            //same process below but without having to make a string as an intermediate
            Emailtxtview.setText(result.getString(result.getColumnIndex("email")));
            DBnametxtview.setText(result.getString(result.getColumnIndex("DBname")));
            DBpasstxtview.setText(result.getString(result.getColumnIndex("DBpass")));
            result.close();
            MasterDB.close();
        }
        Cursor cursor = MasterDB.getCDvol();
        if (result.getCount() != 0){
            EditText k_vol_text= findViewById(R.id.K_vol_text);
            EditText p_vol_text= findViewById(R.id.P_vol_text);
            EditText p300_vol_text= findViewById(R.id.P300_vol_text);
            EditText p500_vol_text= findViewById(R.id.P500_vol_text);
            EditText x_vol_text= findViewById(R.id.X_vol_text);
            cursor.moveToLast();
            k_vol_text.setText(cursor.getString(cursor.getColumnIndex("ketamine")));
            p_vol_text.setText(cursor.getString(cursor.getColumnIndex("pamlin")));
            p300_vol_text.setText(cursor.getString(cursor.getColumnIndex("p300")));
            p500_vol_text.setText(cursor.getString(cursor.getColumnIndex("p500")));
            x_vol_text.setText(cursor.getString(cursor.getColumnIndex("xyla")));
            cursor.close();
            MasterDB.close();
        }
    }
}
