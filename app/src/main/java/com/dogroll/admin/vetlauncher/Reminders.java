package com.dogroll.admin.vetlauncher;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

public class Reminders extends AppCompatActivity {
    String vVDate, client, animal, CR, meds, isCD, initiator, reminder, reminderNotes, reminderDate;
    DBHandler MasterDB;
    DatePickerDialog Date_Picker_Dia;
    int remDay, remMonth, remYear;
    Boolean reminderOffered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        setTitle("Reminders");

        reminderOffered = false;

        EditText etRDate = findViewById(R.id.reminderDate);
        ImageButton dateReminderButton = findViewById(R.id.datebtnReminder);
        dateReminderButton.setOnClickListener(v -> {
            final Calendar selected_date = Calendar.getInstance();
            int mDay = selected_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = selected_date.get(Calendar.MONTH);
            int mYear = selected_date.get(Calendar.YEAR);
            Date_Picker_Dia = new DatePickerDialog(Reminders.this, (view1, year, monthOfYear, dayOfMonth) -> {
                int truemonth = monthOfYear +1;
                remDay = dayOfMonth;
                remMonth = truemonth;
                remYear = year;
                reminderDate = dayOfMonth + "/" + truemonth + "/" + year;
                etRDate.setText(reminderDate);
            }, mYear, mMonth, mDay);
            Date_Picker_Dia.show();
            etRDate.setError(null);
        });

        EditText etReminderNotes = findViewById(R.id.editTextReminderNotes);

        Button save = findViewById(R.id.savReminderbtn);
        save.setOnClickListener(v -> {
            if (!etRDate.toString().equals("")){
                Calendar dateTime = Calendar.getInstance();
                int formattedMonth = remMonth - 1;
                dateTime.set(remYear, formattedMonth, remDay,8,0);
                String eventTitle = "Reminder: "+client+", "+animal;
                reminderNotes = etReminderNotes.getText().toString();
                String eventDetails = "Reminder notes: "+reminderNotes;
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, dateTime.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, dateTime.getTimeInMillis()+15*60*1000);
                intent.putExtra(CalendarContract.Events.TITLE, eventTitle);
                intent.putExtra(CalendarContract.Events.DESCRIPTION, eventDetails);
                intent.putExtra(CalendarContract.Events.HAS_ALARM,true);
                intent.putExtra(CalendarContract.Events.ALLOWED_REMINDERS, "METHOD_DEFAULT");
                startActivity(intent);
                reminderOffered = true;
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            vVDate = bundle.getString("vVDate");
            client = bundle.getString("client");
            animal = bundle.getString("animal");
            CR = bundle.getString("CR");
            meds = bundle.getString("meds");
            isCD = bundle.getString("isCD");
            initiator = bundle.getString("initiator");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (reminderOffered){
            saveVVWithReminder();
            finish();
        }
    }

    public void saveVVWithReminder(){
        MasterDB = new DBHandler(this);
        boolean result;
        reminder = "y";
        result = MasterDB.saveVVWithReminder(vVDate, client, animal, CR, meds, isCD, reminder, reminderNotes, reminderDate);
        String response;
        if (result){
            response = "Stored";
            finish();
            if (initiator.equals("email")){
                buildEmailWithReminder();
            }
        }else{response = "Error storing Record with Reminder";}
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    }
    public void buildEmailWithReminder(){
        Cursor result = MasterDB.getuserinfo();
        result.moveToLast();
        String emailStr = result.getString(result.getColumnIndex("email"));
        result.close();
        MasterDB.close();
        String[] recipients = emailStr.split(",");
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, recipients);
        String subject = "Clinical Record for " + animal + " , owner " + client + " on the " + vVDate;
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        String CRandMeds = "Clinical Record: \n" + CR + "\n\nMedications and Materials used: \n" + meds + "\n\nReminder set for " + reminderDate + ":\n" + reminderNotes;
        email.putExtra(Intent.EXTRA_TEXT, CRandMeds);
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Select email app"));
    }
}