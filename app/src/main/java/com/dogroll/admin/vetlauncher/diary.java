package com.dogroll.admin.vetlauncher;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.dogroll.admin.vetlauncher.Models.vVisits;
import com.dogroll.admin.vetlauncher.Adapter.SearchAdapter;

import java.util.ArrayList;
import java.util.Calendar;


public class diary extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView recyclerView;
    SearchAdapter adapter;
    SearchView searchbar;
    DBHandler MasterDB;
    DatePickerDialog datePickerDialog;

    public static ArrayList<vVisits> visitModelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        setTitle("Vet Diary");

        MasterDB = new DBHandler(this);

        recyclerView = findViewById(R.id.recycler);
        visitModelArrayList = MasterDB.getallVVtoArrayL();

        adapter = new SearchAdapter(this,visitModelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),recyclerView, new ClickListener() {

            @Override
            public void onClick (View view, int position) {

                Intent editor  = new Intent(getApplicationContext(), EditVisit.class);
                        Bundle extras = new Bundle();
                        extras.putInt("ID",visitModelArrayList.get(position).getId());
                        extras.putString("vVDate", visitModelArrayList.get(position).getDate());
                        extras.putString("client", visitModelArrayList.get(position).getClient());
                        extras.putString("animal", visitModelArrayList.get(position).getAnimal());
                        extras.putString("CR", visitModelArrayList.get(position).getCR());
                        extras.putString("meds", visitModelArrayList.get(position).getmAndM());
                        extras.putString("isCD", visitModelArrayList.get(position).getIsCD());
                        editor.putExtras(extras);
                        startActivity(editor);
                        finish();
            }

            @Override
            public void onLongClick (View view, int position){

            }

        }));
        searchbar = findViewById(R.id.searchView);

        searchbar.setOnQueryTextListener(this);

        ImageButton searchDateBtn = findViewById(R.id.searchDateBtn);
        searchDateBtn.setOnClickListener(v -> {
            final Calendar selected_date = Calendar.getInstance();
            int mDay = selected_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = selected_date.get(Calendar.MONTH);
            int mYear = selected_date.get(Calendar.YEAR);
            datePickerDialog = new DatePickerDialog(diary.this, (view, year, monthOfYear, dayOfMonth) -> {
                int truemonth = monthOfYear +1;
                searchbar.setQuery (dayOfMonth + "/" + truemonth + "/" + year, false);
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
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
