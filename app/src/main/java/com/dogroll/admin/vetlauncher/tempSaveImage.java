package com.dogroll.admin.vetlauncher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class tempSaveImage {


    static File BitmaptoJPEGSave(Context context, Bitmap bitmap, String fileName){
        File file = null;
        try{
            file  = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName+".jpeg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();


        }catch (Exception e){
            Log.e("Error saving Bitmap", "Error converting Bitmap to JPEG and saving to temp directory");
        }
        return file;
    }

    static boolean BitmaptoJPEGUsingURI (Context context, Bitmap bitmap, Intent data){
        boolean result = false;
        if (data.getData() != null){
            try{
                OutputStream outputStream = context.getContentResolver().openOutputStream(data.getData());
                if(outputStream != null){
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    result = true;
                }
            }catch (FileNotFoundException e){
                Log.e("ERROR", "Could not find temp cpd file for outputstream");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
