package com.dogroll.admin.vetlauncher;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


class NetworkUtil extends AppCompatActivity {
    int REQUEST_NETWORK_PERMISSION = 101;
    int REQUEST_INTERNET_PERMISSION = 202;

    boolean isConnectedToNetwork(Context context) {
        boolean result = false;
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (cm != null) {
                            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                            if (capabilities != null) {
                                result = true;
                            }
                        }
                    } else if (cm != null) {
                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        if (activeNetwork != null) {
                            result = true;
                        }
                    }return result;

                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.INTERNET)){
                            Toast.makeText(getApplicationContext(), "Internet permission is needed to connect to the internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET_PERMISSION);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_NETWORK_STATE)) {
                        Toast.makeText(getApplicationContext(), "Network State is needed to check if your device is connected to the internet", Toast.LENGTH_SHORT).show();
                    }
                }
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_NETWORK_PERMISSION);
            }
        } catch (Exception e) {
            Log.e("Internet Permission", "Permission error; checking, Android M");
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_NETWORK_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isConnectedToNetwork(getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        }if(requestCode == REQUEST_INTERNET_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                isConnectedToNetwork(getApplicationContext());
            } else{
                Toast.makeText(getApplicationContext(), "Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            NetworkUtil.super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }
}