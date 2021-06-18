package com.dogroll.admin.vetlauncher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.dogroll.admin.vetlauncher.Adapter.SearchAdapterBCS;
import com.dogroll.admin.vetlauncher.Models.bcsRecord;

import java.util.ArrayList;
import java.util.Calendar;

public class BCSLaunch extends AppCompatActivity  implements SearchView.OnQueryTextListener{
    SearchAdapterBCS adapter;
    SearchView searchview;
    RecyclerView recyclerView;
    DBHandler MasterDB;
    DatePickerDialog datePickerDialog;
    AlertDialog alertDialog;
    public static ArrayList<bcsRecord> bcsRecordsFullArrayList;
    int id;
    String date;
    String farm;
    String herd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcs_launch);
        setTitle("Herd Body Condition Scoring");

        MasterDB = new DBHandler(this);
        searchview = findViewById(R.id.svBCS);
        recyclerView = findViewById(R.id.recycleBCS);
        searchview.setOnQueryTextListener(this);
        bcsRecordsFullArrayList = MasterDB.getallBCStoArray();

        adapter = new SearchAdapterBCS(this, bcsRecordsFullArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick (View view, int position) {
                id = bcsRecordsFullArrayList.get(position).getId();
                Bundle extras = new Bundle();
                extras.putInt("recordID",id);
                Intent intent = new Intent(getApplicationContext(),BCSSummary.class);
                intent.putExtras(extras);
                startActivity(intent);

            }

            @Override
            public void onLongClick (View view, final int position){
                alertDialog = new AlertDialog.Builder(BCSLaunch.this).create();
                alertDialog.setTitle("Delete record?");
                alertDialog.setMessage("Are you sure you want to delete this record?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", (dialog, which) -> {
                    id = bcsRecordsFullArrayList.get(position).getId();
                    MasterDB.deleteBCSRecord(id);
                    Intent intent = new Intent(getApplicationContext(),BCSLaunch.class);
                    startActivity(intent);
                    finish();
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }

        }));
        final EditText etDateTxt = findViewById(R.id.datetextBCS);
        ImageButton datebtn = findViewById(R.id.datebtnBCS);
        Button newRecord = findViewById(R.id.btnNewBCSRecord);
        date = "";

        datebtn.setOnClickListener(view -> {
            final Calendar selected_date = Calendar.getInstance();
            int mDay = selected_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = selected_date.get(Calendar.MONTH);
            int mYear = selected_date.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(BCSLaunch.this, (view1, year, monthOfYear, dayOfMonth) -> {
                    int truemonth = monthOfYear +1;
                    date = dayOfMonth + "/" + truemonth + "/" + year;
                    etDateTxt.setText(date);
                }, mYear, mMonth, mDay);
            datePickerDialog.show();
            etDateTxt.setError(null);
        });

        newRecord.setOnClickListener(v -> {
            EditText farmName = findViewById(R.id.etFarm);
            EditText herdName = findViewById(R.id.etHerd);
            final String farmtxt = farmName.getText().toString();
            final String herdtxt = herdName.getText().toString();
            if (date.equals("")){etDateTxt.setError("Please select date");return;}
            if (farmtxt.equals("")){farmName.setError("Please set Farm name");return;}
            if (herdtxt.equals("")){herdName.setError("Please set Herd name");return;}
            farm = farmtxt;
            herd = herdtxt;
            int recordID = MasterDB.newBCSRecord(date, farm, herd);
            Intent intent = new Intent(getApplicationContext(), BCSCalc.class);
            Bundle extra = new Bundle();
            extra.putInt("recordID", recordID);
            extra.putString("farm", farm);
            extra.putString("herd", herd);
            extra.putString("date", date);
            intent.putExtras(extra);
            startActivity(intent);
            finish();
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
        void onClick (View view, int position);

        void onLongClick (View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private final GestureDetector gestureDetector;
        private final ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress (MotionEvent e){
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
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)){
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
}