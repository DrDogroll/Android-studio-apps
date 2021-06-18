package com.dogroll.admin.vetlauncher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.print.PrintHelper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class CPDRecord extends AppCompatActivity {

    String cpdRecordYear, cpdRecordTitle, fileName;
    int REQUEST_STORAGE_WRITE = 404;
    String date, title, description, provider, selectedClass, selectedSubclass, hours, points, evidence, outcome, impact, function;
    Bitmap bitmap;
    Boolean jpgCreated = false;
    PrintHelper printHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_p_d_record);

        date = title = description = provider = selectedClass = selectedSubclass = hours = points = evidence = outcome = impact = function ="";

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            cpdRecordYear = bundle.getString("year");
            date = bundle.getString("date");
            title = bundle.getString("title");
            cpdRecordTitle = title;
            description = bundle.getString("description");
            provider = bundle.getString("provider");
            selectedClass = bundle.getString("selectedClass");
            selectedSubclass = bundle.getString("selectedSubclass");
            hours = bundle.getString("hours");
            points = bundle.getString("points");
            evidence = bundle.getString("evidence");
            outcome = bundle.getString("outcome");
            impact = bundle.getString("impact");
            function = bundle.getString("function");
        }
        TextView dateTV = findViewById(R.id.cpdRecordTVDate);
        dateTV.setText(date);
        TextView titleTV = findViewById(R.id.cpdRecordTitle);
        titleTV.setText(title);
        if (!provider.equals("")){
            TextView providerTV = findViewById(R.id.cpdRecordProvider);
            providerTV.setVisibility(View.VISIBLE);
            providerTV.setText(provider);
        }
        TextView descriptionTV = findViewById(R.id.cpdRecordDescription);
        descriptionTV.setText(description);
        TextView selectedClassTV = findViewById(R.id.cpdRecordClassSubClass);
        String sClass = "";
        switch (selectedClass){
            case "cve":
                sClass = "Continuing Veterinary Education";
                break;
            case "sdl":
                sClass = "Self Directed Learning";
                break;
            case "cla":
                sClass = "Collegial Learning Activity";
        }
        String classSubclass = sClass.toUpperCase();
        selectedClassTV.setText(classSubclass);
        TextView hoursTV = findViewById(R.id.cpdRecordHours);
        hoursTV.setText(hours);
        TextView pointsTV = findViewById(R.id.cpdRecordPoints);
        pointsTV.setText(points);
        TextView evidenceTV = findViewById(R.id.cpdRecordEvidence);
        if (!evidence.equals("")){
            evidenceTV.setText(evidence);
        }else{
            String text = "Reflective Record:";
            evidenceTV.setText(text);
        }
        if (!outcome.equals("")){
            TextView outcomeTV = findViewById(R.id.cpdRecordOutcomes);
            outcomeTV.setText(outcome);
            TextView impactTV = findViewById(R.id.cpdRecordImpact);
            impactTV.setText(impact);
            LinearLayout LLreflective = findViewById(R.id.LLReflective);
            LLreflective.setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(this::afterOnCreate, 500);
        //Toast.makeText(this, "Your CPD record. Tap to proceed.", Toast.LENGTH_SHORT).show();
        //LinearLayout recordView = findViewById(R.id.cpdRecordLinear1);
        //recordView.setOnClickListener(v -> afterOnCreate());
    }

    void afterOnCreate (){
        prepFileNameAndBitmap();
        if (function.equals("jpg")){
            printToJPEG();
        }
        if (function.equals("pdf")) {
            printToPDF();
        }
    }

    void prepFileNameAndBitmap (){
        ScrollView recordScroll = findViewById(R.id.cpdRecordScroll);
        String cleanDate = date.replace("/","_");
        String cleanTitle = cpdRecordTitle.replace("/","_");
        fileName = cleanDate+" - "+cleanTitle;
        bitmap = imageProcessor.createBitmap(recordScroll);
    }
    void printToJPEG(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
                preKitKatFileSave(bitmap, fileName);
            }else{
                Intent saveIntent = new Intent();
                saveIntent.setAction(Intent.ACTION_CREATE_DOCUMENT);
                saveIntent.setType("image/jpeg");
                saveIntent.addCategory(Intent.CATEGORY_OPENABLE);
                saveIntent.putExtra(Intent.EXTRA_TITLE, fileName);
                startActivityForResult(saveIntent, REQUEST_STORAGE_WRITE);
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(getApplicationContext(), "Write External Storage permission is needed to create and export your diary entries", Toast.LENGTH_LONG).show();
                }
            }
            ActivityCompat.requestPermissions(CPDRecord.this, new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_WRITE);
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            if (requestCode == REQUEST_STORAGE_WRITE){
                jpgCreated = tempSaveImage.BitmaptoJPEGUsingURI(getApplicationContext(), bitmap, data);
            }
        }
    }
    void preKitKatFileSave(Bitmap bitmap, String fileName){
        try{
            String folderPath = Environment.getExternalStorageDirectory() + "/Documents/Vet Launcher";
            File folder = new File(folderPath);
            if (!folder.exists()){
                if (!folder.mkdirs()){
                    Toast.makeText(this, "Error creating save directories", Toast.LENGTH_SHORT).show();
                }
            }
            String path = folder.getAbsolutePath();
            File file = new File (path,fileName+".jpeg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            jpgCreated = true;
        }catch(Exception e){
            Log.e("ERROR", "Could not process CPD image preKitkat");
        }
    }
    void printToPDF (){
        printHelper = new PrintHelper(this);
        printHelper.setColorMode(PrintHelper.COLOR_MODE_COLOR);
        printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        printHelper.printBitmap(fileName, bitmap, () -> jpgCreated = true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (jpgCreated){
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}
