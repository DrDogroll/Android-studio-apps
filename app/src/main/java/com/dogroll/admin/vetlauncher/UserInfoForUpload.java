package com.dogroll.admin.vetlauncher;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class UserInfoForUpload {

    public String userInfoForUpload(Context context, String action) {
        DBHandler MasterDB = new DBHandler(context);
        String DBname = "", DBuser = "", DBpass = "", dataToUpload;

        Cursor result = MasterDB.getuserinfo();
        if (result.getCount() != 0) {

            result.moveToLast();
            DBname = result.getString(result.getColumnIndex("DBname"));
            DBuser = result.getString(result.getColumnIndex("DBuser"));
            DBpass = result.getString(result.getColumnIndex("DBpass"));
            result.close();
            MasterDB.close();
        }
        if(DBname.length() < 1 || DBpass.length() < 1){return "Stop";}else {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("db", DBname)
                    .appendQueryParameter("user", DBuser)
                    .appendQueryParameter("password", DBpass)
                    .appendQueryParameter("action", action);
            dataToUpload = builder.build().getEncodedQuery();
            return dataToUpload;
        }
    }
}
