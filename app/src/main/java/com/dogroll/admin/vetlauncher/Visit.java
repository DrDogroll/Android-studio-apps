package com.dogroll.admin.vetlauncher;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.Calendar;


public class Visit extends AppCompatActivity {

    DatePickerDialog Date_Picker_Dia;
    DBHandler MasterDB;

    public int REQUEST_CODE = 10;
    String kUsed = "0";
    String pUsed = "0";
    String p300Used = "0";
    String p500Used = "0";
    String xylaUsed = "0";
    String vVDate = "";
    String client = "";
    String animal = "";
    String CR = "";
    String meds = "";
    String emails = "";
    String isCD = "n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);
        setTitle("Clinical Record");
        MasterDB = new DBHandler(this);

        final ImageButton datebtn = findViewById(R.id.datebtn);
        final EditText dateBox = findViewById(R.id.hiddenET);

        datebtn.setOnClickListener(view -> {
            final Calendar selected_date = Calendar.getInstance();
            int mDay = selected_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = selected_date.get(Calendar.MONTH);
            int mYear = selected_date.get(Calendar.YEAR);
            Date_Picker_Dia = new DatePickerDialog(Visit.this, (view1, year, monthOfYear, dayOfMonth) -> {
                int truemonth = monthOfYear +1;
                vVDate = dayOfMonth + "/" + truemonth + "/" + year;
                dateBox.setText(vVDate);
            }, mYear, mMonth, mDay);
            Date_Picker_Dia.show();
            dateBox.setError(null);
        }

        );

        Button CDUbtn = findViewById(R.id.CDbtn);
        CDUbtn.setOnClickListener(view -> {
            Intent CDUIntent = new Intent(getApplicationContext(), CDU.class);
            CDUIntent.putExtra("fromVV", "y");
            startActivityForResult(CDUIntent,REQUEST_CODE);
        });

        Button toDiary = findViewById(R.id.diarybtn);
        toDiary.setOnClickListener(view -> reminder("diary"));

        Button emailBtn = findViewById(R.id.editorEmailBTN);
        emailBtn.setOnClickListener(view -> reminder("email"));

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    kUsed = extras.getString("kUsed");
                    pUsed = extras.getString("pUsed");
                    p300Used = extras.getString("p300Used");
                    p500Used = extras.getString("p500Used");
                    xylaUsed = extras.getString("xylaUsed");
                    //warning above is get.String might grab nothing causing a null exemption, but all values are set to "0" on CDU - should never be null.
                    isCD = "y";
                    extrasToMedicationsBox();
                }

            }
        }
    }

    public void extrasToMedicationsBox (){
        EditText editTextMed = findViewById(R.id.editTextMed);
        if (!kUsed.equals("")){
            String edittxtMed = editTextMed.getText().toString();
            meds = edittxtMed + "\n" + kUsed + "mls Ketamine";
            editTextMed.setText(meds);
        }
        if (!pUsed.equals("")){
            String edittxtMed = editTextMed.getText().toString();
            meds = edittxtMed + "\n" + pUsed + "mls Diazepam";
            editTextMed.setText(meds);
        }
        if (!p300Used.equals("")){
            String edittxtMed = editTextMed.getText().toString();
            meds = edittxtMed + "\n" + p300Used + "mls Pentobarb 300";
            editTextMed.setText(meds);
        }
        if (!p500Used.equals("")){
            String edittxtMed = editTextMed.getText().toString();
            meds = edittxtMed + "\n" + p500Used + "mls Pentobarb 500";
            editTextMed.setText(meds);
        }
        if (!xylaUsed.equals("")) {
            String edittxtMed = editTextMed.getText().toString();
            meds = edittxtMed + "\n" + xylaUsed + "mls Xylaket";
            editTextMed.setText(meds);
        }
    }
    boolean vVRecord () {
        EditText hiddenDate = findViewById(R.id.hiddenET);
        String date = hiddenDate.getText().toString();
        if (date.equals("Select Date")){
            hiddenDate.setError("Please select a date");
            return false;}
        EditText editTextClient = findViewById(R.id.editTextClient);
        client = editTextClient.getText().toString();
        if (TextUtils.isEmpty(client)){
            editTextClient.setError("Please enter client name" + date);
            return false;}
        EditText editTextAnimal = findViewById(R.id.editTextAnimal);
        animal = editTextAnimal.getText().toString();
        if (TextUtils.isEmpty(animal)){
            editTextAnimal.setError("Please enter animal name");
            return false;}
        EditText editTextCR = findViewById(R.id.editTextCR);
        CR = editTextCR.getText().toString();
        if (TextUtils.isEmpty(CR)){
            editTextCR.setError("Please enter a Clinical Record name");
            return false;}
        EditText editTextMed = findViewById(R.id.editTextMed);
        meds = editTextMed.getText().toString();
        //don't have to enter materials and medications if none were used.
        return true;
    }
    public void toDB (){
        MasterDB = new DBHandler(this);
        boolean result = MasterDB.Recordvisit(vVDate, client, animal, CR, meds, isCD);
        if (!result) {
            Toast.makeText(Visit.this, "Error storing your clinical record to database", Toast.LENGTH_LONG).show();
        }
    }
    public void reminder (String initiator){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Reminder")
                .setMessage("Will you be needing to set a reminder?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    vVRecord();
                    if (vVRecord()){

                        Intent intent = new Intent(getApplicationContext(),Reminders.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("client", client);
                        bundle.putString("animal", animal);
                        bundle.putString("vVDate",vVDate);
                        bundle.putString("CR",CR);
                        bundle.putString("meds", meds);
                        bundle.putString("isCD", isCD);
                        bundle.putString("initiator",initiator);
                        intent.putExtras(bundle);
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", (dialog, which) ->{
                    vVRecord();
                    if (vVRecord()){
                        toDB();
                        Toast.makeText(Visit.this,"Stored",Toast.LENGTH_LONG).show();
                        finish();
                        if (initiator.equals("email")){
                            buildEmail();
                        }
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.show();
        alertDialog.show();
    }
    public void buildEmail(){
        Cursor result = MasterDB.getuserinfo();
        result.moveToLast();
        emails = result.getString(result.getColumnIndex("email"));
        result.close();
        MasterDB.close();
        String[] recipients = emails.split(",");
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, recipients);
        String subject = "Clinical Record for " + animal + " , owner " + client + " on the " + vVDate;
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        String CRandMeds = "Clinical Record: \n" + CR + "\n\nMedications and Materials used: \n" + meds;
        email.putExtra(Intent.EXTRA_TEXT, CRandMeds);
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Select email app"));
    }
}
