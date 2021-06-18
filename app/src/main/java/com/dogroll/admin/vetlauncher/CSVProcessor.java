package com.dogroll.admin.vetlauncher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class CSVProcessor {

    DBHandler MasterDB;
    String path;

    int exportData(Context context, String function, Uri uri) {

        MasterDB = new DBHandler(context);
        int recordCount = 0;

        try {
            CSVWriter csvWrite;
            if (uri != null) {
                OutputStream os = context.getContentResolver().openOutputStream(uri);
                if (os == null) {
                    return 0;
                }
                csvWrite = new CSVWriter(new OutputStreamWriter(os));
            } else {
                File file;
                File exportDir = new File(path);
                String fileName = function + ".csv";
                file = new File(exportDir, fileName);
                csvWrite = new CSVWriter(new FileWriter(file));
            }
            SQLiteDatabase db = MasterDB.getReadableDatabase();
            if (function.contains("Diary")) {
                Cursor curCSV = db.rawQuery("SELECT * FROM vetDiary", null);
                String[] headings = {"Date", "Client Name", "Animal Name", "Clinical Record", "Meds and Materials", "Controlled Drugs Used", "Reminder added", "Reminder Notes", "Reminder Date"};
                csvWrite.writeNext(headings);
                while (curCSV.moveToNext()) {
                    String[] arrStr = {curCSV.getString(1), curCSV.getString(2),
                            curCSV.getString(3), curCSV.getString(4), curCSV.getString(5),
                            curCSV.getString(6), curCSV.getString(7), curCSV.getString(8), curCSV.getString(9)};
                    csvWrite.writeNext(arrStr);
                    recordCount++;
                }
                csvWrite.close();
                curCSV.close();
            }
            if (function.contains("BCS")) {
                Cursor curCSV = db.rawQuery("SELECT * FROM bcsRecords", null);
                String[] headings = {"Date", "Farm Name", "Herd Name", "No. Cows BCS 2.5", "No. Cows BCS 3", "No. Cows BCS 3.5", "No. Cows BCS 4", "No. Cows BCS 4.5", "No. Cows BCS 5", "No. Cows BCS 5.5", "No. Cows BCS 6", "No. Cows BCS 6.5", "Total Counted"};
                csvWrite.writeNext(headings);
                while (curCSV.moveToNext()) {
                    String[] arrStr = {curCSV.getString(1), curCSV.getString(2),
                            curCSV.getString(3), curCSV.getString(4), curCSV.getString(5),
                            curCSV.getString(6), curCSV.getString(7), curCSV.getString(8),
                            curCSV.getString(9), curCSV.getString(10), curCSV.getString(11),
                            curCSV.getString(12), curCSV.getString(13)};
                    csvWrite.writeNext(arrStr);
                    recordCount++;
                }
                csvWrite.close();
                curCSV.close();
            }
            if (function.contains("CPD")) {
                Cursor curCSV = db.rawQuery("SELECT * FROM cpdRecords", null);
                String[] headings = {"Year", "Points", "CPD Code", "Title"};
                csvWrite.writeNext(headings);
                while (curCSV.moveToNext()) {
                    String[] arrStr = {curCSV.getString(1), curCSV.getString(2),
                            curCSV.getString(3), curCSV.getString(4)};
                    csvWrite.writeNext(arrStr);
                    recordCount++;
                }
                csvWrite.close();
                curCSV.close();
            }
            db.close();
        } catch (Exception sqlEx) {
            Log.e("CSV Export", "Error with export");
            recordCount = 0;
        }
        return recordCount;
    }

    ContentValues importData(Context context, String function, Uri uri) {
        MasterDB = new DBHandler(context);
        ContentValues cv;
        int success = 0;
        int fail = 0;
        CSVReader csvReader;

        try {
            if (uri != null) {
                InputStream is = context.getContentResolver().openInputStream(uri);
                if (is == null) {
                    return importErrorCV(0, 0);
                }
                csvReader = new CSVReader(new InputStreamReader(is));
            } else {
                File importtDir = new File(path);
                String fileName = function + ".csv";
                File file = new File(importtDir, fileName);
                csvReader = new CSVReader(new FileReader(file));
            }
            String[] check = csvReader.peek();
            if (check[0].equals("Date") || check[0].equals("Year")) {
                csvReader.skip(1);
            }
            String[] record;
            if (function.contains("Diary")) {
                while ((record = csvReader.readNext()) != null) {
                    String date = record[0];
                    String clientName = record[1];
                    String animalName = record[2];
                    String clinicalRecord = record[3];
                    String medsAndMatt = record[4];
                    String isCD = record[5];
                    String reminder = record[6];
                    String reminderNotes = record[7];
                    String reminderDate = record[8];
                    boolean result = MasterDB.saveVVWithReminder(date, clientName, animalName, clinicalRecord, medsAndMatt, isCD, reminder, reminderNotes, reminderDate);
                    if (result) {
                        success++;
                    } else {
                        fail++;
                    }
                }
            }
            if (function.contains("BCS")) {
                while ((record = csvReader.readNext()) != null) {
                    String date = record[0];
                    String farm = record[1];
                    String herd = record[2];
                    int _2_5 = Integer.parseInt(record[3]);
                    int _3_0 = Integer.parseInt(record[4]);
                    int _3_5 = Integer.parseInt(record[5]);
                    int _4_0 = Integer.parseInt(record[6]);
                    int _4_5 = Integer.parseInt(record[7]);
                    int _5_0 = Integer.parseInt(record[8]);
                    int _5_5 = Integer.parseInt(record[9]);
                    int _6_0 = Integer.parseInt(record[10]);
                    int _6_5 = Integer.parseInt(record[11]);
                    int _counted = Integer.parseInt(record[12]);
                    boolean result = MasterDB.insertBCSFromCSV(date, farm, herd, _2_5, _3_0, _3_5, _4_0, _4_5, _5_0, _5_5, _6_0, _6_5, _counted);
                    if (result) {
                        success++;
                    } else {
                        fail++;
                    }
                }
            }
            if (function.contains("CPD")) {
                while ((record = csvReader.readNext()) != null) {
                    String year = record[0];
                    double points = Double.parseDouble(record[1]);
                    String code = record[2];
                    String title = record[3];
                    boolean result = MasterDB.insertcpdValues(year, points, code, title);
                    if (result) {
                        success++;
                    } else {
                        fail++;
                    }
                }
            }
            csvReader.close();
        } catch (Exception e) {
            Log.e("CSVImport", "Error with import");
            int errorSpot = success + fail + 1;
            return importErrorCV(success, errorSpot);
        }
        cv = new ContentValues();
        cv.put("success", success);
        cv.put("fail", fail);
        return cv;
    }

    ContentValues importErrorCV(int success, int errorSpot) {
        ContentValues cv = new ContentValues();
        cv.put("success", success);
        cv.put("fail", "unknown");
        cv.put("exemption", errorSpot);
        return cv;
    }
}