package com.dogroll.admin.vetlauncher;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class EditVisit extends AppCompatActivity {

    DBHandler MasterDB;
    String emails = "";
    String isCD = "";

    KeyListener listener;

    Integer recordID;
    TextView dateTV;
    EditText editTextClient;
    EditText editTextAnimal;
    EditText editTextCR;
    EditText editTextMed;

    String vVDate = "";
    String client = "";
    String animal = "";
    String CR = "";
    String meds = "";
    String fUnlocked = "n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_visit);
        setTitle("Edit Clinical Record");

        dateTV = findViewById(R.id.dateTV);
        editTextClient = findViewById(R.id.editorTextClient);
        editTextAnimal = findViewById(R.id.editorTextAnimal);
        editTextCR = findViewById(R.id.editorTextCR);
        editTextMed = findViewById(R.id.editorTextMed);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            recordID = extras.getInt("ID");
            dateTV.setText(extras.getString("vVDate"));
            editTextClient.setText (extras.getString("client"));
            editTextAnimal.setText(extras.getString("animal"));
            editTextCR.setText(extras.getString("CR"));
            editTextMed.setText(extras.getString("meds"));
            isCD = extras.getString("isCD");

            listener = editTextClient.getKeyListener();
            lockfields();

            vVRecord();
        }

        final Button unlock = findViewById(R.id.EDITORbtn);
        final RelativeLayout background = findViewById(R.id.editorRelativeL);
        unlock.setOnClickListener(v -> {
            if (!isCD.equals("y")){
                background.setBackgroundColor(Color.WHITE);
                unlockfields();
                fUnlocked = "y";

            }else{
                Toast.makeText(EditVisit.this, "You should not edit a Controlled Drug Record", Toast.LENGTH_LONG).show();
            }
        });
        unlock.setOnLongClickListener(v -> {
            background.setBackgroundColor((Color.YELLOW));
            unlockfields();
            fUnlocked = "y";
            return false;
        });

        MasterDB = new DBHandler(this);

        Button savebtn = findViewById(R.id.editorSaveBTN);
        savebtn.setOnClickListener(v -> {
            if (fUnlocked.equals("y")) {
                String clienttxt = editTextClient.getText().toString();
                String animaltxt = editTextAnimal.getText().toString();
                String CRtxt = editTextCR.getText().toString();
                String Medtxt = editTextMed.getText().toString();

                MasterDB.editRecordvisit(recordID, clienttxt, animaltxt, CRtxt, Medtxt);
                Toast.makeText(EditVisit.this, "Clinical Record Updated", Toast.LENGTH_SHORT).show();
                Intent freshDiary = new Intent(getApplicationContext(), diary.class);
                startActivity(freshDiary);
                finish();
            }
        });

        Button deletebtn = findViewById(R.id.editorDeleteBTN);
        deletebtn.setOnClickListener(v -> {
            if (fUnlocked.equals("y")) {
                MasterDB.deleteRecordvisit(recordID);
                Toast.makeText(EditVisit.this, "Clinical Record Deleted", Toast.LENGTH_SHORT).show();
                Intent freshDiary = new Intent(getApplicationContext(), diary.class);
                startActivity(freshDiary);
                finish();
            }else
                Toast.makeText(EditVisit.this, "Unlock a Clinical Record before Deleting", Toast.LENGTH_SHORT).show();
        });

        Button emailBtn = findViewById(R.id.editorEmailBTN);
        emailBtn.setOnClickListener(view -> {

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
        });

    }

    public void lockfields(){
        editTextClient.setKeyListener(null);
        editTextAnimal.setKeyListener(null);
        editTextCR.setKeyListener(null);
        editTextMed.setKeyListener(null);
}

    public void unlockfields(){
        editTextClient.setKeyListener(listener);
        editTextAnimal.setKeyListener(listener);
        editTextCR.setKeyListener(listener);
        editTextMed.setKeyListener(listener);
    }

    public void vVRecord () {

        vVDate = dateTV.getText().toString();
        client = editTextClient.getText().toString();
        animal = editTextAnimal.getText().toString();
        CR = editTextCR.getText().toString();
        meds = editTextMed.getText().toString();
    }

}
