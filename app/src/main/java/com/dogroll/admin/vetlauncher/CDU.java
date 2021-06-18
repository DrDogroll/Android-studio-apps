package com.dogroll.admin.vetlauncher;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;


public class CDU extends AppCompatActivity {

    DBHandler MasterDB;
    public String k_used = "";
    public String pam_used = "";
    public String P300_used = "";
    public String P500_used = "";
    public String xyla_used = "";
    public String SimpleSave = "";
    public String fromVV = "";
    String kvolCurrent, pvolCurrent, p300volCurrent, p500volCurrent, xvolCurrent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cdu);
        setTitle("Controlled Drug Use");

        final Button cdSavebtn = findViewById(R.id.cdSavebtn);

        cdSavebtn.setOnClickListener(v -> {
            savCD();
            if (SimpleSave.equals("y")) {
                Toast.makeText(CDU.this,"Controlled Drug Import recorded",Toast.LENGTH_LONG).show();
                finish();
                //how to record the changes -> database with date, time, user

            }else{
                if (fromVV.equals("y")) {
                    Intent VVIntent = new Intent(getApplicationContext(), Visit.class);
                    Bundle extras = new Bundle();
                    extras.putString("kUsed", k_used);
                    extras.putString("pUsed", pam_used);
                    extras.putString("p300Used", P300_used);
                    extras.putString("p500Used", P500_used);
                    extras.putString("xylaUsed", xyla_used);
                    VVIntent.putExtras(extras);
                    Toast.makeText(CDU.this, "Transferring", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK, VVIntent);
                    finish();
                }
            }
        });
        Button cdEmailBTN = findViewById(R.id.cdEmailBTN);
        cdEmailBTN.setOnClickListener(v -> {
            String user = "";
            Cursor cursor = MasterDB.getuserinfo();
            if (cursor != null){
                cursor.moveToLast();
                user = cursor.getString(cursor.getColumnIndex("username"));
            }
            Intent email = new Intent(Intent.ACTION_SEND);
            Calendar today = Calendar.getInstance();
            String subject = "Controlled Drug Levels for "+user+" as of "+today.toString();
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            String cdLevels = "Ketamine: " + kvolCurrent +
                        "mls\nDiazepam: " + pvolCurrent +
                        "mls\nPentobarb 300: " + p300volCurrent +
                        "mls\nPentobarb 500: " + p500volCurrent +
                        "mls\nXylaket: " + xvolCurrent +
                        "mls\n\nRegards\n"+user;
            email.putExtra(Intent.EXTRA_TEXT, cdLevels);
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Select email app"));
        });
    }

    public void savCD (){
        final Spinner usage= findViewById(R.id.cduseSpinner);
        String selectedUsage = String.valueOf(usage.getSelectedItem());

        if (selectedUsage.equals("Select use:")){
            Toast.makeText(CDU.this,"Please select use type",Toast.LENGTH_LONG).show();
            return;
        }

        final EditText editTextK = findViewById(R.id.editTextK);
        final EditText editTextPam = findViewById(R.id.editTextPam);
        final EditText editTextP300 = findViewById(R.id.editTextP300);
        final EditText editTextP500 = findViewById(R.id.editTextP500);
        final EditText editTextXyl = findViewById(R.id.editTextXyl);

        k_used = editTextK.getText().toString();
        pam_used = editTextPam.getText().toString();
        P300_used = editTextP300.getText().toString();
        P500_used = editTextP500.getText().toString();
        xyla_used = editTextXyl.getText().toString();

        if (TextUtils.isEmpty(k_used)) {
            editTextK.setText(String.valueOf(0));}

        if (TextUtils.isEmpty(pam_used)) {
            editTextPam.setText(String.valueOf(0)); }

        if (TextUtils.isEmpty(P300_used)) {
            editTextP300.setText(String.valueOf(0));}

        if (TextUtils.isEmpty(P500_used)) {
            editTextP500.setText(String.valueOf(0)); }

        if (TextUtils.isEmpty(xyla_used)) {
            editTextXyl.setText(String.valueOf(0)); }

        final TextView k_vol_text = findViewById(R.id.K_vol_text);
        final TextView p_vol_text = findViewById(R.id.P_vol_text);
        final TextView p300_vol_text = findViewById(R.id.P300_vol_text);
        final TextView p500_vol_text = findViewById(R.id.P500_vol_text);
        final TextView x_vol_text = findViewById(R.id.X_vol_text);

        if (selectedUsage.equals("Import")){
            double kvol = Double.parseDouble(k_vol_text.getText().toString()) + Double.parseDouble(editTextK.getText().toString());
            double pvol = Double.parseDouble(p_vol_text.getText().toString()) + Double.parseDouble(editTextPam.getText().toString());
            double p300vol = Double.parseDouble(p300_vol_text.getText().toString()) + Double.parseDouble(editTextP300.getText().toString());
            double p500vol = Double.parseDouble(p500_vol_text.getText().toString()) + Double.parseDouble(editTextP500.getText().toString());
            double xvol = Double.parseDouble(x_vol_text.getText().toString()) + Double.parseDouble(editTextXyl.getText().toString());
            MasterDB.UPDCDVol(kvol,pvol,p300vol,p500vol,xvol);
            SimpleSave = "y";
        }

        else {
            if (fromVV.equals("y")) {
                double kvol = Double.parseDouble(k_vol_text.getText().toString()) - Double.parseDouble(editTextK.getText().toString());
                double pvol = Double.parseDouble(p_vol_text.getText().toString()) - Double.parseDouble(editTextPam.getText().toString());
                double p300vol = Double.parseDouble(p300_vol_text.getText().toString()) - Double.parseDouble(editTextP300.getText().toString());
                double p500vol = Double.parseDouble(p500_vol_text.getText().toString()) - Double.parseDouble(editTextP500.getText().toString());
                double xvol = Double.parseDouble(x_vol_text.getText().toString()) - Double.parseDouble(editTextXyl.getText().toString());
                MasterDB.UPDCDVol(kvol, pvol, p300vol, p500vol, xvol);
            }else {
                Toast.makeText(CDU.this,"Controlled Drug Usage should be recorded with a Clinical Record",Toast.LENGTH_LONG).show();
                Intent pushtoVV = new Intent(getApplicationContext(),Visit.class);
                startActivity(pushtoVV);
                finish();

            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        final TextView k_vol_text = findViewById(R.id.K_vol_text);
        final TextView p_vol_text = findViewById(R.id.P_vol_text);
        final TextView p300_vol_text = findViewById(R.id.P300_vol_text);
        final TextView p500_vol_text = findViewById(R.id.P500_vol_text);
        final TextView x_vol_text = findViewById(R.id.X_vol_text);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        fromVV = extras.getString("fromVV");

        MasterDB = new DBHandler(this);

        Cursor result = MasterDB.getCDvol();{
            result.moveToLast();
            kvolCurrent = result.getString(result.getColumnIndex("ketamine"));
            k_vol_text.setText(kvolCurrent);
            pvolCurrent = result.getString(result.getColumnIndex("pamlin"));
            p_vol_text.setText(pvolCurrent);
            p300volCurrent = result.getString(result.getColumnIndex("p300"));
            p300_vol_text.setText(p300volCurrent);
            p500volCurrent = result.getString(result.getColumnIndex("p500"));
            p500_vol_text.setText(p500volCurrent);
            xvolCurrent = result.getString(result.getColumnIndex("xyla"));
            x_vol_text.setText(xvolCurrent);
            result.close();
            MasterDB.close();
        }
    }
}