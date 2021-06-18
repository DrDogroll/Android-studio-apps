package com.dogroll.admin.vetlauncher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Base64;
import android.widget.ScrollView;
import java.io.ByteArrayOutputStream;

class imageProcessor {
    static Bitmap convertStringtoBitmap(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

     static String convertBitmaptoString(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    static Bitmap createBitmap(ScrollView view){
        Bitmap bitmap = Bitmap.createBitmap(view.getChildAt(0).getWidth()*2, view.getChildAt(0).getHeight()*2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(2.0f, 2.0f);
        canvas.drawColor(Color.WHITE);
        view.getChildAt(0).draw(canvas);
        return bitmap;
    }
}
