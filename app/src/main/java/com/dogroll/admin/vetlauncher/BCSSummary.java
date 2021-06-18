package com.dogroll.admin.vetlauncher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BCSSummary extends AppCompatActivity {

    String date, farmName, herdName, user;
    int recordID, icount, _2_5, _3_0, _3_5, _4_0, _4_5, _5_0, _5_5, _6_0, _6_5;
    double average, total, count;
    DBHandler MasterDB;
    DB2Handler paidDB;
    DecimalFormat decimalFormat;
    TextView tvFarmBCSSummary, tvDateBCSSummary, tvHerdBCSSummary, tvCountAverageBCSSummary;
    ArrayList<Integer> colours;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcs_summary);
        setTitle("BCS Summary");

        tvCountAverageBCSSummary = findViewById(R.id.tvCountAverageBCSSummary);
        tvDateBCSSummary = findViewById(R.id.tvDateBCSSummary);
        tvFarmBCSSummary = findViewById(R.id.tvFarmBCSSummary);
        tvHerdBCSSummary = findViewById(R.id.tvHerdBCSSummary);
        Button bcsSummaryShare = findViewById(R.id.bcsSummaryShare);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recordID = extras.getInt("recordID");
        }
        MasterDB = new DBHandler(this);

        paidDB = new DB2Handler(this);
        Bitmap logo = paidDB.getLogo();
        ImageView imageView = findViewById(R.id.imageView2);
        if (logo != null) {
            imageView.setImageBitmap(logo);
        }

        Cursor result = MasterDB.getuserinfo();
        if (result.getCount() == 0) {
            Toast.makeText(BCSSummary.this, "WARNING: Database is empty", Toast.LENGTH_LONG).show();
        } else {
            result.moveToLast();
            user = result.getString(result.getColumnIndex("username"));
            result.close();
            MasterDB.close();
            TextView userNameBCSSummary = findViewById(R.id.tvUserBCSSummary);
            userNameBCSSummary.setText(user);
        }


        bcsSummaryShare.setOnClickListener(v -> {
            ScrollView scrollView = findViewById(R.id.scrollview);
            Bitmap bitmap = imageProcessor.createBitmap(scrollView);
            String cleanDate = date.replace("/","_");
            String cleanFarmName = farmName.replace("/","_");
            String fileName = cleanFarmName+" - "+cleanDate;
            File saved = tempSaveImage.BitmaptoJPEGSave(this, bitmap,fileName);
            if (saved !=null){
                Uri uri = FileProvider.getUriForFile(BCSSummary.this, "com.dogroll.admin.vetlauncher.fileprovider", saved);
                Intent sendintent = new Intent();
                sendintent.setAction(Intent.ACTION_SEND);
                sendintent.putExtra(Intent.EXTRA_TEXT, "Herd Body Condition Scoring for "+farmName);
                sendintent.putExtra(Intent.EXTRA_STREAM, uri);
                sendintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sendintent.setType("image/jpeg");

                try{
                    Intent shareIntent = Intent.createChooser(sendintent,null);
                    startActivity(shareIntent);
                }catch (ActivityNotFoundException e){
                    Log.e("App Error", "Sharing function error");
                    Toast.makeText(BCSSummary.this, "No App Available", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(BCSSummary.this, "Error processing this BCS Summary to sharable file", Toast.LENGTH_SHORT).show();
            }

        });

        colours = new ArrayList<>();
        colours.add(getResources().getColor(R.color._2_5));
        colours.add(getResources().getColor(R.color._3_0));
        colours.add(getResources().getColor(R.color._3_5));
        colours.add(getResources().getColor(R.color._4_0));
        colours.add(getResources().getColor(R.color._4_5));
        colours.add(getResources().getColor(R.color._5_0));
        colours.add(getResources().getColor(R.color._5_5));
        colours.add(getResources().getColor(R.color._6_0));
        colours.add(getResources().getColor(R.color._6_5));

        Thread secondThread = new Thread(secondThreadPieChart());
        secondThread.start();
        firstThreadBarChart();
    }

    void firstThreadBarChart(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f, _2_5));
        entries.add(new BarEntry(2f, _3_0));
        entries.add(new BarEntry(3f, _3_5));
        entries.add(new BarEntry(4f, _4_0));
        entries.add(new BarEntry(5f, _4_5));
        entries.add(new BarEntry(6f, _5_0));
        entries.add(new BarEntry(7f, _5_5));
        entries.add(new BarEntry(8f, _6_0));
        entries.add(new BarEntry(9f, _6_5));

        ArrayList<String> xAxisValues = new ArrayList<>();
        xAxisValues.add("2.5");
        xAxisValues.add("3.0");
        xAxisValues.add("3.5");
        xAxisValues.add("4.0");
        xAxisValues.add("4.5");
        xAxisValues.add("5.0");
        xAxisValues.add("5.5");
        xAxisValues.add("6.0");
        xAxisValues.add("6.5");

        BarDataSet bardataset = new BarDataSet(entries, null);
        bardataset.setColors(colours);
        BarData data = new BarData(bardataset);
        data.setBarWidth(0.4f);
        BarChart barChart = findViewById(R.id.barchart);
        barChart.setData(data);
        XAxis axis = barChart.getXAxis();
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setDrawGridLines(false);
        axis.setGranularity(0.5f);
        axis.setCenterAxisLabels(true);
        axis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0){
                    if (value <= xAxisValues.size() - 1){
                        return xAxisValues.get((int) value);
                    }
                    return "";
                }
                return "";
            }
        });
        YAxis lYAxis = barChart.getAxisLeft();
        YAxis rYAxis = barChart.getAxisRight();
        lYAxis.setGranularity(1f);
        rYAxis.setGranularity(1f);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(3000);
    }


    Runnable secondThreadPieChart(){
        Bundle data = MasterDB.getBCSRecord(recordID);
        date = data.getString("date");
        farmName = data.getString("farm");
        herdName = data.getString("herd");
        _2_5 = data.getInt("_2_5");
        _3_0 = data.getInt("_3_0");
        _3_5 = data.getInt("_3_5");
        _4_0 = data.getInt("_4_0");
        _4_5 = data.getInt("_4_5");
        _5_0 = data.getInt("_5_0");
        _5_5 = data.getInt("_5_5");
        _6_0 = data.getInt("_6_0");
        _6_5 = data.getInt("_6_5");
        icount = data.getInt("count");
        count = icount;
        total = _2_5*2.5+_3_0*3+_3_5*3.5+_4_0*4+_4_5*4.5+_5_0*5+_5_5*5.5+_6_0*6+_6_5*6.5;
        average = total/icount;

        decimalFormat = new DecimalFormat("#.#");
        String sCount = Integer.toString(icount);
        String sAverage = decimalFormat.format(average);
        String summary = sCount + " cows counted, with an average of " + sAverage;
        tvCountAverageBCSSummary.setText(summary);
        tvDateBCSSummary.setText(date);
        tvFarmBCSSummary.setText(farmName);
        tvHerdBCSSummary.setText(herdName);
        String s_2_5 = decimalFormat.format(100*(_2_5 / count)) +"%";
        String s_3_0 = decimalFormat.format(100*(_3_0 / count)) +"%";
        String s_3_5 = decimalFormat.format(100*(_3_5 / count)) +"%";
        String s_4_0 = decimalFormat.format(100*(_4_0 / count)) +"%";
        String s_4_5 = decimalFormat.format(100*(_4_5 / count)) +"%";
        String s_5_0 = decimalFormat.format(100*(_5_0 / count)) +"%";
        String s_5_5 = decimalFormat.format(100*(_5_5 / count)) +"%";
        String s_6_0 = decimalFormat.format(100*(_6_0 / count)) +"%";
        String s_6_5 = decimalFormat.format(100*(_6_5 / count)) +"%";

        ArrayList<PieEntry> PieEntries = new ArrayList<>();
        PieEntries.add(new PieEntry(_2_5, s_2_5));
        PieEntries.add(new PieEntry(_3_0, s_3_0));
        PieEntries.add(new PieEntry(_3_5, s_3_5));
        PieEntries.add(new PieEntry(_4_0, s_4_0));
        PieEntries.add(new PieEntry(_4_5, s_4_5));
        PieEntries.add(new PieEntry(_5_0, s_5_0));
        PieEntries.add(new PieEntry(_5_5, s_5_5));
        PieEntries.add(new PieEntry(_6_0, s_6_0));
        PieEntries.add(new PieEntry(_6_5, s_6_5));

        PieDataSet pieDataSet = new PieDataSet(PieEntries, null);
        pieDataSet.setColors(colours);
        ValueFormatter valueFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        };
        pieDataSet.setValueFormatter(valueFormatter);
        PieData pieData = new PieData(pieDataSet);
        PieChart pieChart = findViewById(R.id.piechart);
        pieChart.setData(pieData);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateXY(3000, 3000);

        return null;
    }

}