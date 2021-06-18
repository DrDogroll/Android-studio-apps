package com.dogroll.admin.vetlauncher;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class httpSendandReceive {

    String HttpSendandReceive (String stringUrl, String userData, String dataToUpload) {

        URL url;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection;
        BufferedWriter bufferedWriter;

        try {

            url = new URL(stringUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");

            outputStream = httpURLConnection.getOutputStream();

            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            if (dataToUpload == null){
                bufferedWriter.write(userData);
            }else{bufferedWriter.write(userData+"&"+dataToUpload);}
            bufferedWriter.flush();

            int statusCode = httpURLConnection.getResponseCode();
            Log.d("Server status", " The status code is " + statusCode);

            if (statusCode == 200) {
                try{
                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = bReader.readLine()) != null) {
                        result.append(line);}
                    Log.d("Response reader", "Response from server is good");
                    return result.toString();
                }catch (Exception e){
                    Log.e("Error", "Unable to process incoming data");
                    return "ERROR - Unable to process incoming data";
                }
            } else if (statusCode == 401){
                Log.e("Error 401", "Response from server suggests authentication error");
                return " ERROR "+statusCode+" - check credentials";
            }else if (statusCode == 204){
                Log.e("Error 204", "Response from server suggests no data");
                return " ERROR "+statusCode+" - no data on server";
            }
            else {
                Log.e("Error", "Bad response from server");
                return " ERROR "+statusCode+" - can't reach server";
            }
        } catch (Exception e) {
            Log.e("Error","Error in building connection to server");
            return "ERROR - Could not build connection to server";

        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                    Log.d("InputStream", "closed");
                }
                if (outputStream != null) {
                    outputStream.close();
                    Log.d("OutputStream", "closed");
                }
            } catch (Exception e) {
                Log.e("Error", "Unable to close Input/Output streams");
            }
        }
    }

}
