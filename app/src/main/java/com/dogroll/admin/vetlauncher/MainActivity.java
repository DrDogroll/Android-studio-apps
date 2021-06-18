package com.dogroll.admin.vetlauncher;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    DBHandler MasterDB;
    DB2Handler paidDB;
    public String emptyDatabase = "y";
    String warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton settingbtn = findViewById(R.id.settingbtn);
        settingbtn.setOnClickListener(view -> {
            Intent settingsIntent = new Intent(getApplicationContext(), setting.class);
            startActivity(settingsIntent);
        });

        warning = "WARNING: User details are empty. You must enter details to use most functions. Settings is the top right cog.";

        Button CDUbtn = findViewById(R.id.CDUbtn);
        CDUbtn.setOnClickListener(view -> {
            if(emptyDatabase.equals("y")) {
                Toast.makeText(MainActivity.this,warning,Toast.LENGTH_LONG).show();
            }
            else{
                Intent CDUIntent = new Intent(getApplicationContext(), CDU.class);
                startActivity(CDUIntent);
            }
        });
        Button BCSbtn = findViewById(R.id.BCSbtn);
        BCSbtn.setOnClickListener(view -> {
            Intent BCSintent = new Intent(getApplicationContext(), BCSLaunch.class);
            startActivity(BCSintent);
        });
        Button CNbtn = findViewById(R.id.CNbtn);
        CNbtn.setOnClickListener(view -> {

                Intent CNnoteintent = new Intent(getApplicationContext(), clinicalnotes.class);
                startActivity(CNnoteintent);
        });
        Button Stocktakebtn = findViewById(R.id.Stocktakebtn);
        Stocktakebtn.setOnClickListener(view -> {
            Intent Stocktakeintent = new Intent(getApplicationContext(),Stocktake.class);
            startActivity(Stocktakeintent);
        });

        Button VVbtn = findViewById(R.id.vetVisitbtn);
        VVbtn.setOnClickListener(view -> {

            if(emptyDatabase.equals("y")) {
                Toast.makeText(MainActivity.this,warning,Toast.LENGTH_LONG).show();
            }
            else {
                Intent VVintent = new Intent(getApplicationContext(),Visit.class);
                VVintent.putExtra("fromCDU", "n");
                startActivity(VVintent);
            }
        });
        Button diary = findViewById(R.id.diarybtn);
        diary.setOnClickListener(view -> {
            Intent diaryintent = new Intent(getApplicationContext(), diary.class);
            startActivity(diaryintent);
        });
        Button cpd = findViewById(R.id.CPDRecordBTN);
        cpd.setOnClickListener(v -> {
            Intent cpdIntent = new Intent(getApplicationContext(),ContinuingPD.class);
            startActivity(cpdIntent);
        });

        Button clients = findViewById(R.id.clientsBTN);
        clients.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), clients.class);
            startActivity(intent);
        });
        Button notifications = findViewById(R.id.notificationsBTN);
        notifications.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), VetNotifications.class);
            startActivity(intent);
        });
        Button transfer = findViewById(R.id.transferBTN);
        transfer.setOnClickListener(v -> {
            if(emptyDatabase.equals("y")) {
                Toast.makeText(MainActivity.this,warning,Toast.LENGTH_LONG).show();
            }
            else {
            Intent intent = new Intent(getApplicationContext(), departmentTransfer.class);
            startActivity(intent);}
        });
        Button about = findViewById(R.id.aboutBTN);
        about.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), about.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        MasterDB = new DBHandler(this);

        TextView Usertxtview = findViewById(R.id.Usertxtview);

        Cursor result = MasterDB.getuserinfo();
        if(result.getCount()==0){
            Toast.makeText(MainActivity.this,warning,Toast.LENGTH_LONG).show();
        }
        else {
            result.moveToLast();
            String user = result.getString(result.getColumnIndex("username"));
            result.close();
            MasterDB.close();
            Usertxtview.setText(user);
            emptyDatabase = "n";
        }
        paidDB = new DB2Handler(this);
        Bitmap logo = paidDB.getLogo();
        ImageView imageView = findViewById(R.id.imageView);
        if (logo != null){imageView.setImageBitmap(logo);}

    }
}