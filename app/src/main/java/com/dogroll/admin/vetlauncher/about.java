package com.dogroll.admin.vetlauncher;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

public class about extends AppCompatActivity {

    String urlPrivacyString = "http://software.blackdogservices.co.nz/privacypolicy.html";
    String urlTermsString = "http://software.blackdogservices.co.nz/terms.html";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");
        String about = "Dave graduated with a Bachelor of Veterinary Science from Massey University, NZ in 2010. For 8 years he practiced as a Large Animal Veterinarian in Waikato and the Bay of Plenty.\n" +
                        "During this time he noted that LA Vets had very little support in the way of applications on their mobile phones. " +
                        "In 2015 the vet practice employed a new graduate veterinarian and Dave saw the opportunity to pass on his skills and experience in the form of an android application. " +
                        "Having no experience in writing android applications but fueled with his passion for IT, Dave used the MIT App Inventor 2 GUI to build a simple app. " +
                        "In challenging situations, where a less experienced veterinarian could neither refer to textbooks or contact a more senior veterinarian, Dave's app provided notes and guidance from an offline database.\n" +
                        "But in 2018, in a dramatic turn of events, Dave's veterinary career underwent significant change.\n"+
                        "Adult onset of Bipolar type 2 with Major Depressive Disorder or a touch of the JK's, whatever you wish to call it, Dave's mental health hit rock bottom. "+
                        "After 18 months on a rollercoaster from hell, Dave recovery was at the point where he"+
                        " needed something to set his mind to. So he decided to push past the limitations of the MIT GUI and learn to truly code\n" +
                        "Given Android devices were more robust, more common in vet practices, and easier to code, test and get app's approved, Dave has self-learnt Android coding. "+
                        "If this app is received well, and should there be a need for it, Dave will consider coding the app for Apple.";
        TextView story = findViewById(R.id.Story);
        story.setMovementMethod(new ScrollingMovementMethod());
        story.setText(about);

        TextView version = findViewById(R.id.tvVersion);
        int versionCode = BuildConfig.VERSION_CODE;
        version.setText(Integer.toString(versionCode));

        TextView info = findViewById(R.id.tvInfo);
        int androidCode = Build.VERSION.SDK_INT;
        String androidOS = getAndroidOS(androidCode);

        DBHandler Masterdb = new DBHandler(this);
        int databaseCode = Masterdb.getDatabaseVersion();
        String infoText = "Android OS: "+androidOS+", Internal db #: "+databaseCode;
        info.setText(infoText);

        Button privacy = findViewById(R.id.privacyBTN);
        privacy.setOnClickListener(view -> {
            Uri privacyURI = Uri.parse(urlPrivacyString);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, privacyURI);
            startActivity(launchBrowser);
        });

        Button terms = findViewById(R.id.termsBTN);
        terms.setOnClickListener(view -> {
            Uri termsURI = Uri.parse(urlTermsString);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, termsURI);
            startActivity(launchBrowser);
        });
    }
    String getAndroidOS(int androidCode){
        String androidOS;
        switch(androidCode){
            case 15:
                androidOS = "Ice Cream Sandwich";
                break;
            case 16:
                androidOS = "Jelly Bean 4.1";
                break;
            case 17:
                androidOS = "Jelly Bean 4.2";
                break;
            case 18:
                androidOS = "Jelly Bean 4.3";
                break;
            case 19:
                androidOS = "Kitkat";
                break;
            case 21:
                androidOS = "Lollipop 5.0";
                break;
            case 22:
                androidOS = "Lollipop 5.1";
                break;
            case 23:
                androidOS = "Marshmallow";
                break;
            case 24:
                androidOS = "Nougat 7.0";
                break;
            case 25:
                androidOS = "Nougat 7.1";
                break;
            case 26:
                androidOS = "Oreo 8.0";
                break;
            case 27:
                androidOS = "Oreo 8.1";
                break;
            case 28:
                androidOS = "Pie";
                break;
            case 29:
                androidOS = "Android10";
                break;
            case 30:
                androidOS = "Android11";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + androidCode);
        }
        return androidOS;
    }
}