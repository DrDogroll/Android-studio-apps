package com.dogroll.admin.vetlauncher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import com.dogroll.admin.vetlauncher.Models.bcsRecord;
import com.dogroll.admin.vetlauncher.Models.vVisits;
import java.util.ArrayList;
import java.util.List;


public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "vetlauncher.db";

    private static final String TABLE_NAME = "user"; //make a table with a name
    private static final String COLUMN_1 = "id"; //name a column
    private static final String COLUMN_2 = "username";
    private static final String COLUMN_3 = "email";
    private static final String COLUMN_4 = "DBname";
    private static final String COLUMN_5 = "DBuser";
    private static final String COLUMN_6 = "DBpass";

    private static final String TABLE_NAME_cd = "cdDrugs";
    private static final String COLUMN_1cd = "id";
    private static final String COLUMN_2cd = "ketamine";
    private static final String COLUMN_3cd = "pamlin";
    private static final String COLUMN_4cd = "p300";
    private static final String COLUMN_5cd = "p500";
    private static final String COLUMN_6cd = "xyla";

    private static final String TABLE_NAME_cn = "clinicalNotes";
    private static final String COLUMN_1cn = "id";
    private static final String COLUMN_2cn = "topic";
    private static final String COLUMN_3cn = "vitals";
    private static final String COLUMN_4cn = "extendedNotes";
    private static final String COLUMN_5cn = "imageLinks";
    private static final String COLUMN_6cn = "timestamp";

    private static final String TABLE_NAME_vd = "vetDiary";
    private static final String COLUMN_1vd = "id";
    private static final String COLUMN_2vd = "date";
    private static final String COLUMN_3vd = "clientName";
    private static final String COLUMN_4vd = "animalName";
    private static final String COLUMN_5vd = "clinicalR";
    private static final String COLUMN_6vd = "medsAndMatt";
    private static final String COLUMN_7vd = "isCD";
    private static final String COLUMN_8vd = "reminder";
    private static final String COLUMN_9vd = "reminderNotes";
    private static final String COLUMN_10vd = "reminderDate";

    private static final String TABLE_NAME_bcs = "bcsRecords";
    private static final String COLUMN_1bcs = "id";
    private static final String COLUMN_2bcs = "date";
    private static final String COLUMN_3bcs = "farm";
    private static final String COLUMN_4bcs = "herd";
    private static final String COLUMN_5bcs = "_2_5";
    private static final String COLUMN_6bcs = "_3_0";
    private static final String COLUMN_7bcs = "_3_5";
    private static final String COLUMN_8bcs = "_4_0";
    private static final String COLUMN_9bcs = "_4_5";
    private static final String COLUMN_10bcs = "_5_0";
    private static final String COLUMN_11bcs = "_5_5";
    private static final String COLUMN_12bcs = "_6_0";
    private static final String COLUMN_13bcs = "_6_5";
    private static final String COLUMN_14bcs = "counted";

    private static final String TABLE_NAME_cpdW = "cpdWarning";
    private static final String COLUMN_1cpdW = "id";
    private static final String COLUMN_2cpdW = "warned";

    private static final String TABLE_NAME_cpd = "cpdRecords";
    private static final String COLUMN_1cpd = "id";
    private static final String COLUMN_2cpd = "year";
    private static final String COLUMN_3cpd = "points";
    private static final String COLUMN_4cpd = "code";
    private static final String COLUMN_5cpd = "title";

    DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                        + COLUMN_2 + " TEXT, "
                                                        + COLUMN_3 + " TEXT, "
                                                        + COLUMN_4 + " TEXT, "
                                                        + COLUMN_5 + " TEXT, "
                                                        + COLUMN_6 + " TEXT "
                                                        + ");");

        db.execSQL("CREATE TABLE " + TABLE_NAME_cd + " (" + COLUMN_1cd + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                        + COLUMN_2cd + " REAL, "
                                                        + COLUMN_3cd + " REAL, "
                                                        + COLUMN_4cd + " REAL, "
                                                        + COLUMN_5cd + " REAL, "
                                                        + COLUMN_6cd + " REAL "
                                                        +");");

        db.execSQL(" CREATE TABLE " + TABLE_NAME_cn + " (" + COLUMN_1cn + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                    + COLUMN_2cn + " TEXT, "
                                                    + COLUMN_3cn + " TEXT, "
                                                    + COLUMN_4cn + " TEXT, "
                                                    + COLUMN_5cn + " BLOB, "
                                                    + COLUMN_6cn + " DATETIME DEFAULT CURRENT_TIMESTAMP "
                                                    + ");");

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_2cn, "Update");
        cv.put(COLUMN_3cn, "You need to update the Clinical Notes");
        cv.put(COLUMN_4cn, "To update you will need WIFI or Mobile Data Connection, and the UPDATE button should appear on the top left");
        cv.put(COLUMN_5cn, "");
        cv.put(COLUMN_6cn, "");
        db.insert(TABLE_NAME_cn, null, cv);

        db.execSQL("CREATE TABLE " + TABLE_NAME_vd + " (" + COLUMN_1vd + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_2vd + " REAL, "
                + COLUMN_3vd + " REAL, "
                + COLUMN_4vd + " REAL, "
                + COLUMN_5vd + " REAL, "
                + COLUMN_6vd + " REAL, "
                + COLUMN_7vd + " REAL, "
                + COLUMN_8vd + " REAL, "
                + COLUMN_9vd + " REAL, "
                + COLUMN_10vd + " REAL "
                +");");

        db.execSQL("CREATE TABLE " + TABLE_NAME_bcs + " (" + COLUMN_1bcs + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_2bcs + " INTEGER, "
                + COLUMN_3bcs + " INTEGER, "
                + COLUMN_4bcs + " INTEGER, "
                + COLUMN_5bcs + " INTEGER, "
                + COLUMN_6bcs + " INTEGER, "
                + COLUMN_7bcs + " INTEGER, "
                + COLUMN_8bcs + " INTEGER, "
                + COLUMN_9bcs + " INTEGER, "
                + COLUMN_10bcs + " INTEGER, "
                + COLUMN_11bcs + " INTEGER, "
                + COLUMN_12bcs + " INTEGER, "
                + COLUMN_13bcs + " INTEGER, "
                + COLUMN_14bcs + " REAL "
                +");");

        db.execSQL("CREATE TABLE " + TABLE_NAME_cpdW + " (" + COLUMN_1cpdW + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_2cpdW + " REAL "
                +");");

        db.execSQL("CREATE TABLE " + TABLE_NAME_cpd + " (" + COLUMN_1cpd + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_2cpd + " TEXT, "
                + COLUMN_3cpd + " DOUBLE, "
                + COLUMN_4cpd + " TEXT, "
                + COLUMN_5cpd + " TEXT "
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*for (int version = oldVersion +1; version <= newVersion; version++){
            switch (version){
                case 3:
                    db.execSQL("ALTER TABLE " + TABLE_NAME_vd + " ADD " + COLUMN_8vd + " REAL;");
                    db.execSQL("ALTER TABLE " + TABLE_NAME_vd + " ADD " + COLUMN_9vd + " REAL;");
                    db.execSQL("ALTER TABLE " + TABLE_NAME_vd + " ADD " + COLUMN_10vd + " REAL;");
                    break;
                case 4:
                    db.execSQL("CREATE TABLE " + TABLE_NAME_cpdW + " (" + COLUMN_1cpdW + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + COLUMN_2cpdW + " INTEGER DEFAULT 0"
                            +");");
                    db.execSQL("CREATE TABLE " + TABLE_NAME_cpd + " (" + COLUMN_1cpd + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + COLUMN_2cpd + " TEXT, "
                            + COLUMN_3cpd + " INTEGER, "
                            + COLUMN_4cpd + " INTEGER, "
                            + COLUMN_5cpd + " INTEGER "
                            + ");");
                    break;
            }
        }*/
    }

    int getDatabaseVersion(){
        return DATABASE_VERSION;
    }
    Cursor getuserinfo(){
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
    }
    void UPDuserinfo (String user, String email, String DBname, String DBuser, String DBpass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues userinfo = new ContentValues();
        userinfo.put(COLUMN_2,user);
        userinfo.put(COLUMN_3,email);
        userinfo.put(COLUMN_4,DBname);
        userinfo.put(COLUMN_5,DBuser);
        userinfo.put(COLUMN_6,DBpass);
        db.insert(TABLE_NAME,null,userinfo);
        db.close();
    }
    void UPDCDVol( double k, double pam, double p300, double p500, double xyla ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVvols = new ContentValues();
        cVvols.put(COLUMN_2cd,k);
        cVvols.put(COLUMN_3cd,pam);
        cVvols.put(COLUMN_4cd,p300);
        cVvols.put(COLUMN_5cd,p500);
        cVvols.put(COLUMN_6cd,xyla);
        db.insert(TABLE_NAME_cd, null,cVvols);
        db.close();
    }
   Cursor getCDvol (){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " +TABLE_NAME_cd,null);
   }
    boolean Recordvisit( String date, String clientName, String animalName, String clinicalRecord, String medsAndMatt, String isCD ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues clinicalR = new ContentValues();
        clinicalR.put(COLUMN_2vd,date);
        clinicalR.put(COLUMN_3vd,clientName);
        clinicalR.put(COLUMN_4vd,animalName);
        clinicalR.put(COLUMN_5vd,clinicalRecord);
        clinicalR.put(COLUMN_6vd,medsAndMatt);
        clinicalR.put(COLUMN_7vd,isCD);
        long result = db.insert(TABLE_NAME_vd, null,clinicalR);
        db.close();
        if (result < 0){
            Log.e("Clinical Record", "Error storing record");
            return false;
        }else{return true;}
    }
    void editRecordvisit (Integer id, String clientName, String animalName, String clinicalRecord, String medsAndMatt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues editedR = new ContentValues();
        editedR.put(COLUMN_3vd,clientName);
        editedR.put(COLUMN_4vd,animalName);
        editedR.put(COLUMN_5vd,clinicalRecord);
        editedR.put(COLUMN_6vd,medsAndMatt);
        db.update(TABLE_NAME_vd, editedR, COLUMN_1vd + "=" + id, null );
        db.close();
    }
    void deleteRecordvisit (Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_vd, COLUMN_1vd + "=" + id, null);
        db.close();
    }
    ArrayList<vVisits> getallVVtoArrayL(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_vd,null);
        ArrayList<vVisits> result = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                vVisits vVisits = new vVisits();
                vVisits.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_1vd)));
                vVisits.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_2vd)));
                vVisits.setClient(cursor.getString(cursor.getColumnIndex(COLUMN_3vd)));
                vVisits.setAnimal(cursor.getString(cursor.getColumnIndex(COLUMN_4vd)));
                vVisits.setCR(cursor.getString(cursor.getColumnIndex(COLUMN_5vd)));
                vVisits.setmAndM(cursor.getString(cursor.getColumnIndex(COLUMN_6vd)));
                vVisits.setIsCD(cursor.getString(cursor.getColumnIndex(COLUMN_7vd)));
                result.add(vVisits);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }
    List<String> getAllCNoteTopics (){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_cn,null);
        List<String> spinnerContent = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                spinnerContent.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return spinnerContent;
    }
    void AddClinicalNote( String topic, String vitals, String extendedNotes, String imageLinks ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues clinicalN = new ContentValues();
        clinicalN.put(COLUMN_2cn, topic);
        clinicalN.put(COLUMN_3cn, vitals);
        clinicalN.put(COLUMN_4cn, extendedNotes);
        clinicalN.put(COLUMN_5cn, imageLinks);
        db.insert(TABLE_NAME_cn, null, clinicalN);
        db.close();
    }
    List<String> getClinicalNote (Integer id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_cn+" WHERE "+COLUMN_1cn+ " = " +id, null);
        List<String> cNote = new ArrayList<>();
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                String vitals = cursor.getString(cursor.getColumnIndex(COLUMN_3cn));
                String extendedNotes = cursor.getString(cursor.getColumnIndex(COLUMN_4cn));
                String imageLinks = cursor.getString(cursor.getColumnIndex(COLUMN_5cn));
                cNote.add(vitals);
                cNote.add(extendedNotes);
                cNote.add(imageLinks);
            }
            cursor.close();
            db.close();
        }
        return cNote;
    }
    void dropCNTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_cn);
        db.execSQL(" CREATE TABLE " + TABLE_NAME_cn + " (" + COLUMN_1cn + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_2cn + " TEXT, "
                + COLUMN_3cn + " TEXT, "
                + COLUMN_4cn + " TEXT, "
                + COLUMN_5cn + " TEXT, "
                + COLUMN_6cn + " DATETIME DEFAULT CURRENT_TIMESTAMP "
                + ");");
        db.close();
    }
    String getAppCNDate(){
        String result ="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_6cn + " FROM " + TABLE_NAME_cn + " ORDER BY " + COLUMN_6cn + " DESC LIMIT 1", null);
        if (cursor.moveToFirst()){
            result = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return result;
    }
    ArrayList<bcsRecord> getallBCStoArray() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_bcs, null);
        ArrayList<bcsRecord> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                bcsRecord record = new bcsRecord();
                record.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_1bcs)));
                record.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_2bcs)));
                record.setFarm(cursor.getString(cursor.getColumnIndex(COLUMN_3bcs)));
                record.setHerd(cursor.getString(cursor.getColumnIndex(COLUMN_4bcs)));

                result.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }
    Bundle getBCSRecord(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_bcs+" WHERE "+COLUMN_1bcs+ " = " +id, null);
        Bundle cv = new Bundle();
        if (cursor != null && cursor.moveToFirst()){
            cv.putString("date", cursor.getString(cursor.getColumnIndex(COLUMN_2bcs)));
            cv.putString("farm", cursor.getString(cursor.getColumnIndex(COLUMN_3bcs)));
            cv.putString("herd", cursor.getString(cursor.getColumnIndex(COLUMN_4bcs)));
            cv.putInt("_2_5", cursor.getInt(cursor.getColumnIndex(COLUMN_5bcs)));
            cv.putInt("_3_0", cursor.getInt(cursor.getColumnIndex(COLUMN_6bcs)));
            cv.putInt("_3_5", cursor.getInt(cursor.getColumnIndex(COLUMN_7bcs)));
            cv.putInt("_4_0", cursor.getInt(cursor.getColumnIndex(COLUMN_8bcs)));
            cv.putInt("_4_5", cursor.getInt(cursor.getColumnIndex(COLUMN_9bcs)));
            cv.putInt("_5_0", cursor.getInt(cursor.getColumnIndex(COLUMN_10bcs)));
            cv.putInt("_5_5", cursor.getInt(cursor.getColumnIndex(COLUMN_11bcs)));
            cv.putInt("_6_0", cursor.getInt(cursor.getColumnIndex(COLUMN_12bcs)));
            cv.putInt("_6_5", cursor.getInt(cursor.getColumnIndex(COLUMN_13bcs)));
            cv.putInt("count", cursor.getInt(cursor.getColumnIndex(COLUMN_14bcs)));
            cursor.close();
            db.close();
        }
        return cv;
    }
    int newBCSRecord (String date, String farm, String herd){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_2bcs, date);
        cv.put(COLUMN_3bcs, farm);
        cv.put(COLUMN_4bcs, herd);
        long l = db.insert(TABLE_NAME_bcs,null,cv);
        int result = (int) l;
        db.close();
        return result;
    }
    int updateBCSRecord (int recordID, String columnName){
        SQLiteDatabase db = this.getWritableDatabase();
        int count;
        int previousValue;
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_bcs+" WHERE "+COLUMN_1bcs+ " = " +recordID, null);
        if (cursor != null && cursor.moveToFirst()){
            count = cursor.getInt(cursor.getColumnIndex(COLUMN_14bcs));
            previousValue = cursor.getInt(cursor.getColumnIndex(columnName));
            int newCount = count +1;
            int newValue = previousValue +1;
            ContentValues cv = new ContentValues();
            cv.put("counted", newCount);
            cv.put(columnName, newValue);
            result = db.update(TABLE_NAME_bcs, cv, COLUMN_1bcs + "=" + recordID,null);
            cursor.close();
            db.close();
        }
        return result;
    }
    boolean insertBCSFromCSV (String date, String farm, String herd, int _2_5, int _3_0, int _3_5, int _4_0, int _4_5, int _5_0, int _5_5, int _6_0, int _6_5, int _counted){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues bcsR = new ContentValues();
        bcsR.put(COLUMN_2bcs,date);
        bcsR.put(COLUMN_3bcs,farm);
        bcsR.put(COLUMN_4bcs,herd);
        bcsR.put(COLUMN_5bcs,_2_5);
        bcsR.put(COLUMN_6bcs,_3_0);
        bcsR.put(COLUMN_7bcs,_3_5);
        bcsR.put(COLUMN_8bcs,_4_0);
        bcsR.put(COLUMN_9bcs,_4_5);
        bcsR.put(COLUMN_10bcs,_5_0);
        bcsR.put(COLUMN_11bcs,_5_5);
        bcsR.put(COLUMN_12bcs,_6_0);
        bcsR.put(COLUMN_13bcs,_6_5);
        bcsR.put(COLUMN_14bcs,_counted);
        long result = db.insert(TABLE_NAME_bcs, null,bcsR);
        db.close();
        if (result < 0){
            Log.e("Clinical Record", "Error storing record");
            return false;
        }else{return true;}
    }
    void clearBCSRecord (int recordID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("_2_5", 0);
        cv.put("_3_0", 0);
        cv.put("_3_5",0);
        cv.put("_4_0",0);
        cv.put("_4_5",0);
        cv.put("_5_0",0);
        cv.put("_5_5",0);
        cv.put("_6_0",0);
        cv.put("_6_5",0);
        cv.put("counted",0);
        db.update(TABLE_NAME_bcs, cv, COLUMN_1bcs + "=" + recordID,null);
        db.close();
    }
    void deleteBCSRecord(int recordID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_bcs, COLUMN_1bcs + "=" + recordID, null);
        db.close();
    }
    boolean saveVVWithReminder (String vVDate, String client, String animal, String CR, String meds, String isCD, String reminder, String reminderNotes, String reminderDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues clinicalR = new ContentValues();
        clinicalR.put(COLUMN_2vd,vVDate);
        clinicalR.put(COLUMN_3vd,client);
        clinicalR.put(COLUMN_4vd,animal);
        clinicalR.put(COLUMN_5vd,CR);
        clinicalR.put(COLUMN_6vd,meds);
        clinicalR.put(COLUMN_7vd,isCD);
        clinicalR.put(COLUMN_8vd,reminder);
        clinicalR.put(COLUMN_9vd,reminderNotes);
        clinicalR.put(COLUMN_10vd,reminderDate);
        long result = db.insert(TABLE_NAME_vd, null,clinicalR);
        db.close();
        if (result < 0){
            Log.e("Clinical Record", "Error storing record");
            return false;
        }else{return true;}
    }
    boolean cpdWarned (String action){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;
        if (action.equals("warned")){
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_2cpdW, 1);
            db.insert(TABLE_NAME_cpdW, COLUMN_2cpdW,cv);
            result = true;
            db.close();
        }
        if(action.equals("check")){
            Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_cpdW, null);
            if (c.moveToFirst()){
                result = (c.getInt(c.getColumnIndex(COLUMN_2cpdW)) == 1);
                c.close();
                db.close();
            }
        }
        return result;
    }
    double getcpdValues(String year, String code){
        SQLiteDatabase db = this.getReadableDatabase();
        double points = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_cpd+" WHERE "+COLUMN_2cpd+" = ? AND "+COLUMN_4cpd+" = ?", new String[]{year, code});
        if (cursor.moveToFirst()){
            do {
                if (!cursor.isNull(cursor.getColumnIndex(COLUMN_3cpd))){
                    points = points + cursor.getDouble(cursor.getColumnIndex(COLUMN_3cpd));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return points;
    }
    boolean insertcpdValues(String year, double points, String code, String title){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_2cpd, year);
        cv.put(COLUMN_3cpd, points);
        cv.put(COLUMN_4cpd, code);
        cv.put(COLUMN_5cpd, title);
        long result = db.insert(TABLE_NAME_cpd, null, cv);
        db.close();
        if (result < 0){
            Log.e("Clinical Record", "Error storing record");
            return false;
        }else{return true;}
    }
    void refreshDiaryTable (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_vd);
        db.execSQL("CREATE TABLE " + TABLE_NAME_vd + " (" + COLUMN_1vd + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_2vd + " REAL, "
                + COLUMN_3vd + " REAL, "
                + COLUMN_4vd + " REAL, "
                + COLUMN_5vd + " REAL, "
                + COLUMN_6vd + " REAL, "
                + COLUMN_7vd + " REAL, "
                + COLUMN_8vd + " REAL, "
                + COLUMN_9vd + " REAL, "
                + COLUMN_10vd + " REAL "
                +");");
        db.close();
    }

    void refreshBCSTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_bcs);
        db.execSQL("CREATE TABLE " + TABLE_NAME_bcs + " (" + COLUMN_1bcs + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_2bcs + " INTEGER, "
                + COLUMN_3bcs + " INTEGER, "
                + COLUMN_4bcs + " INTEGER, "
                + COLUMN_5bcs + " INTEGER, "
                + COLUMN_6bcs + " INTEGER, "
                + COLUMN_7bcs + " INTEGER, "
                + COLUMN_8bcs + " INTEGER, "
                + COLUMN_9bcs + " INTEGER, "
                + COLUMN_10bcs + " INTEGER, "
                + COLUMN_11bcs + " INTEGER, "
                + COLUMN_12bcs + " INTEGER, "
                + COLUMN_13bcs + " INTEGER, "
                + COLUMN_14bcs + " REAL "
                +");");
        db.close();
    }

    void refreshCPDTable (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_cpd);
        db.execSQL("CREATE TABLE " + TABLE_NAME_cpd + " (" + COLUMN_1cpd + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_2cpd + " TEXT, "
                + COLUMN_3cpd + " DOUBLE, "
                + COLUMN_4cpd + " TEXT, "
                + COLUMN_5cpd + " TEXT "
                + ");");
        db.close();
    }

}

