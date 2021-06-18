package com.dogroll.admin.vetlauncher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.HorizontalBarChart;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ContinuingPD extends AppCompatActivity {

    DBHandler MasterDB;
    ContentValues cpdpoints;
    String adTitleString = "Add quick CPD points";
    String adMessageString = "Select the year of completion, CPD class and enter the hours spent." +
            "No record will be displayed for sharing, printing or emailing, this simply \"calibrates\" your graphs and " +
            "assumes you have the detailed records stored elsewhere, i.e. NZVACPD";
    String quickYear = "";
    String quickClass = "";
    String cpdCode = "";
    double points = 0;
    double hours;
    RadioGroup classGroup;
    float yearFloat;
    AlertDialog alertDialog;
    ArrayList<Integer> colourarray;
    int REQUEST_CODE_REFRESH = 57;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuing_p_d);
        setTitle("Continuing Professional Development");

        MasterDB = new DBHandler(this);

        boolean warned = MasterDB.cpdWarned("check");
        if (!warned){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                    .setTitle("WARNING")
                    .setMessage("This module does NOT store your CPD record in any way. " +
                            "What is does do is hold a tally of your CPD points (on this device ONLY) for the individual CPD classes, providing a running total." +
                            "\nIt also allows you to build a CPD record (as jpeg or pdf) for you to save somewhere safe, i.e. your cloud storage, email to yourself or phone/SD Card backup.\n" +
                            "Personally, I build the CPD record, Print to \"PDF printer\" which stores the pdf on my device, then I use Google Drive to automatically backup my device folder to the cloud. " +
                            "I add any photos of meeting minutes, screenshots of completed online courses, received email attachments and audio or video recordings, as CPD evidence, to the same backed up folder.")
                    .setPositiveButton("Acknowledged", (dialog, which) -> MasterDB.cpdWarned("warned"))
                    ;
            AlertDialog alertDialog = alertDialogBuilder.show();
            alertDialog.show();
        }
        ImageButton infoBtn = findViewById(R.id.cpdInfoBtn);
        infoBtn.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContinuingPD.this)
                    .setTitle("CPD Requirements")
                    .setMessage("The VCNZ requires:\n" +
                            "In the 3 calendar years (1 Jan -> 31 Dec) prior to applying for your APC for practice 1 April:\n" +
                            "A total of 60 points overall\n" +
                            "A total of 15 points in both CLA and CVE\n" +
                            "CLA Mentoring/Supervision is capped at 9 points\n" +
                            "CLA Meetings is capped at 9 points\n" +
                            "CVE Publications is capped at 20 points\n" +
                            "CVE Refereeing Peer Publications is capped at 4 points\n" +
                            "SDL Reading Publications is capped at 20 points\n\n" +
                            "SDL Developing a CPD Plan is capped at 2 points per year\n" +
                            "CVE Presentations gain 4 points per 1 hour presentation - first time only, otherwise 1:1\n" +
                            "This app adjusts these restrictions, and displays only applicable points");
            AlertDialog alertDialog = alertDialogBuilder.show();
            alertDialog.show();
        });

        cpdpoints = new cpdProcessor().processCDP(getApplicationContext());

        DecimalFormat formatYear = new DecimalFormat();
        formatYear.applyPattern("####");

        Calendar calendar = Calendar.getInstance();
        int yearInt = calendar.get(Calendar.YEAR);
        yearFloat = yearInt;
        String year = Integer.toString(yearInt);
        String yearlast = Integer.toString(yearInt - 1);
        String yearprevlast = Integer.toString(yearInt - 2);
        String yearprevprevlast = Integer.toString(yearInt - 3);

        String[] stackLabels = {"Continuing Veterinary Ed", "Collegial Learning", "Self-Directed Learning"};
        TextView cpdPieTVexclude = findViewById(R.id.cpdPieTVexcludes);
        String text = "Your points accumulated between "+yearprevprevlast+" and "+yearlast+" are the values you use when applying for your "+year+" APC. These values are displayed in the Piechart below";
        cpdPieTVexclude.setText(text);

        HorizontalBarChart stackedChart;
        stackedChart = findViewById(R.id.CPDBarchart);

        colourarray = new ArrayList<>();
        colourarray.add(getResources().getColor(R.color._4_5));
        colourarray.add(getResources().getColor(R.color._5_0));
        colourarray.add(getResources().getColor(R.color._5_5));

        BarDataSet barDataSet = new BarDataSet(createBarValues(), null);
        barDataSet.setStackLabels(stackLabels);
        barDataSet.setColors(colourarray);
        BarData barData = new BarData(barDataSet);
        stackedChart.setData(barData);
        XAxis xAxis = stackedChart.getXAxis();
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return formatYear.format(value);
            }
        });
        YAxis lYAxis = stackedChart.getAxisLeft();
        YAxis rYAxis = stackedChart.getAxisRight();
        lYAxis.setGranularity(1f);
        rYAxis.setGranularity(1f);
        stackedChart.getAxisLeft().setDrawGridLines(false);
        stackedChart.getAxisRight().setDrawGridLines(false);
        stackedChart.getDescription().setEnabled(false);
        stackedChart.setFitBars(true);
        stackedChart.animateY(2000);

        Button newCPDRecord = findViewById(R.id.btnNewCPDRecord);
        newCPDRecord.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CPDEntry.class);
            startActivityForResult(intent,REQUEST_CODE_REFRESH);
        });

        Button btnQuickCPD = findViewById(R.id.btnQuickCPD);
        btnQuickCPD.setOnClickListener(v -> {
            LayoutInflater inflater = this.getLayoutInflater();
            View customDialog = inflater.inflate(R.layout.custom_alertdialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(customDialog);
            TextView adTitle = customDialog.findViewById(R.id.adTitle);
            TextView adMessage = customDialog.findViewById(R.id.adMessage);
            RadioButton checkBoxThis = customDialog.findViewById(R.id.checkBoxThis);
            RadioButton checkBoxLast = customDialog.findViewById(R.id.checkBoxLast);
            RadioButton checkBoxPrevLast = customDialog.findViewById(R.id.checkBoxPrevLast);
            RadioButton checkBoxPrevPrevLast = customDialog.findViewById(R.id.checkBoxPrevPrevLast);
            RadioGroup yearGroup = customDialog.findViewById(R.id.yearGroup);
            classGroup = customDialog.findViewById(R.id.classGroup);
            Button btnCustomAlert = customDialog.findViewById(R.id.btnCustomAlert);
            adTitle.setText(adTitleString);
            adMessage.setText(adMessageString);
            checkBoxThis.setText(year);
            checkBoxLast.setText(yearlast);
            checkBoxPrevLast.setText(yearprevlast);
            checkBoxPrevPrevLast.setText(yearprevprevlast);
            btnCustomAlert.setOnClickListener(v1 -> {
                setCPDhours(customDialog);
                try{
                    int selectedyearID = yearGroup.getCheckedRadioButtonId();
                    RadioButton selectedyearbtn = customDialog.findViewById(selectedyearID);

                    quickYear = selectedyearbtn.getText().toString();
                }catch (Exception e){
                    Log.e("CPD Error", "Unable to determine selected radio buttons for quick year");
                    quickYear = "Error";
                }
                if (quickYear.equals("Error")) {
                    Toast.makeText(this, "Make sure you've selected a year and class", Toast.LENGTH_SHORT).show();
                }else if (cpdCode.equals("")) {
                    Toast.makeText(this, "Make sure you've selected sub category", Toast.LENGTH_SHORT).show();
                }else if (hours == 0) {
                    Toast.makeText(this, "Make sure you've entered your hours", Toast.LENGTH_SHORT).show();
                }else{
                    String cpdCoded = cpdCode;
                    if (cpdCode.equals("CVEFirstPresentation")){cpdCoded = "CVEPresentation";}
                    MasterDB.insertcpdValues(quickYear, points, cpdCoded, "Quick Added");
                    alertDialog.dismiss();
                    refreshView();
                }
            });
            EditText etCPDHours = customDialog.findViewById(R.id.etCPDHours);
            etCPDHours.setEnabled(false);

            classGroup.setOnCheckedChangeListener((group, checkedId) -> {
                etCPDHours.setEnabled(true);
                int selectedclassID = classGroup.getCheckedRadioButtonId();
                RadioButton selectedclassbtn = customDialog.findViewById(selectedclassID);
                Spinner spinner = customDialog.findViewById(R.id.cpdQuickSpinner);
                List<String> subclasses = new ArrayList<>();
                List<String> codes = new ArrayList<>();
                quickClass = selectedclassbtn.getHint().toString();
                switch (quickClass){
                    case "cve":
                        subclasses.clear();
                        subclasses.add("Touch Here to Select Subcategory:");
                        subclasses.add("Tertiary Institution Courses");
                        subclasses.add("Other Structured and Assessed Post-grad Training");
                        subclasses.add("Conference, Seminars, Lectures and Workshops");
                        subclasses.add("First time CVE Presentation");
                        subclasses.add("Formal Scientific or CVE presentations");
                        subclasses.add("Peer Reviewed Publications");
                        subclasses.add("Refereeing for Peer Reviewed Publications");
                        subclasses.add("Assessed lectures, reading, audio/video/onine");
                        subclasses.add("Other");
                        codes.clear();
                        codes.add("");
                        codes.add("CVECourse");
                        codes.add("CVETraining");
                        codes.add("CVEConference");
                        codes.add("CVEFirstPresentation");
                        codes.add("CVEPresentation");
                        codes.add("CVEPublications");
                        codes.add("CVERefereeing");
                        codes.add("CVELearning");
                        codes.add("CVEOther");
                        break;
                    case"cla":
                        subclasses.clear();
                        subclasses.add("Touch Here to Select Subcategory:");
                        subclasses.add("Peer-to-Peer Learning");
                        subclasses.add("Providing Direct Mentoring or Supervision");
                        subclasses.add("Peer Group Activities with an Educational Focus");
                        subclasses.add("Professional Body or Group Meetings");
                        subclasses.add("Components of CVE or SDL that are Collegial Learning");
                        subclasses.add("Quality/Performance Management Activities");
                        subclasses.add("Other");
                        codes.clear();
                        codes.add("");
                        codes.add("CLAP2P");
                        codes.add("CLAMentor");
                        codes.add("CLAActivity");
                        codes.add("CLAMeeting");
                        codes.add("CLAComponents");
                        codes.add("CLAManagement");
                        codes.add("CLAOther");
                        break;
                    case "sdl":
                        subclasses.clear();
                        subclasses.add("Touch Here to Select Subcategory:");
                        subclasses.add("Updating Knowledge or Prep Reading/Research");
                        subclasses.add("Non-assessed Audio/Video/Online Learning");
                        subclasses.add("Case/Procedure/Topic Research and/or Review");
                        subclasses.add("Non-Peer Reviewed Publications");
                        subclasses.add("Developing a CPD Plan");
                        subclasses.add("Completing Non-Required Reflective Records");
                        subclasses.add("Other");
                        codes.clear();
                        codes.add("");
                        codes.add("SDLUpdate");
                        codes.add("SDLLearning");
                        codes.add("SDLResearch");
                        codes.add("SDLPublications");
                        codes.add("SDLPlan");
                        codes.add("SDLRecording");
                        codes.add("SDLOther");
                        break;
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ContinuingPD.this, android.R.layout.simple_spinner_item, subclasses);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cpdCode = codes.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                String etCDPHoursString = etCPDHours.getText().toString();
                if (!etCDPHoursString.equals("")){
                    setCPDpoints(customDialog);
                }
            });

            etCPDHours.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals("")){
                        setCPDhours(customDialog);
                        setCPDpoints(customDialog);
                    }
                }
            });
            alertDialog = alertDialogBuilder.show();
            alertDialog.show();
        });
        Thread secondThread = new Thread(piechart());
        secondThread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REFRESH){
            if (resultCode == RESULT_OK){
                refreshView();
            }
        }
    }

    private ArrayList<BarEntry>createBarValues(){
        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(yearFloat, new float[]{cpdpoints.getAsFloat("thisCVE"), cpdpoints.getAsFloat("thisCLA"),cpdpoints.getAsFloat("thisSDL") }));
        values.add(new BarEntry(yearFloat-1, new float[]{cpdpoints.getAsFloat("lastCVE"), cpdpoints.getAsFloat("lastCLA"), cpdpoints.getAsFloat("lastSDL") }));
        values.add(new BarEntry(yearFloat-2, new float[]{cpdpoints.getAsFloat("prevlastCVE"), cpdpoints.getAsFloat("prevlastCLA"),cpdpoints.getAsFloat("prevlastSDL") }));
        values.add(new BarEntry(yearFloat-3, new float[]{cpdpoints.getAsFloat("prevprevlastCVE"), cpdpoints.getAsFloat("prevprevlastCLA"), cpdpoints.getAsFloat("prevprevlastSDL") }));
        return values;
    }
    public void refreshView (){
        finish();
        overridePendingTransition(0,0);
        Intent intent = new Intent(this, ContinuingPD.class );
        startActivity(intent);
    }
    public void setCPDpoints(View v) {
        double multiplier;
        if (quickClass.equals("cve")) {
            multiplier = 1;
            if (cpdCode.equals("CVEFirstPresentation")){
                multiplier = 4;
            }
        } else {
            multiplier = 0.5;
        }
        points = (multiplier * hours);
        String converted = points + " points";
        TextView tvCPDconverted = v.findViewById(R.id.tvCDPconverted);
        tvCPDconverted.setText(converted);
    }
    public void setCPDhours(View v){
        EditText etCPDHours = v.findViewById(R.id.etCPDHours);
        if (etCPDHours.getText().toString().equals("")){
            hours = 0;
        }else {
            hours = Double.parseDouble(etCPDHours.getText().toString());
        }
    }
    Runnable piechart(){
        float cveTotal = cpdpoints.getAsFloat("lastCVE") + cpdpoints.getAsFloat("prevlastCVE") + cpdpoints.getAsFloat("prevprevlastCVE");
        float clTotal = cpdpoints.getAsFloat("lastCLA") + cpdpoints.getAsFloat("prevlastCLA") + cpdpoints.getAsFloat("prevprevlastCLA");
        float sdlTotal = cpdpoints.getAsFloat("lastSDL") + cpdpoints.getAsFloat("prevlastSDL") + cpdpoints.getAsFloat("prevprevlastSDL");

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(cveTotal, "CVE\n"+ cveTotal));
        entries.add(new PieEntry(clTotal, "CLA\n"+ clTotal));
        entries.add(new PieEntry(sdlTotal, "SDL\n"+ sdlTotal));
        float total = cveTotal + clTotal + sdlTotal;
        String totalS = Float.toString(total);

        int cveColour = getResources().getColor(R.color._4_5);
        int claColour = getResources().getColor(R.color._5_0);

        if (cveTotal < 15){cveColour = getResources().getColor(R.color.warningBackground);}
        if (clTotal < 15){claColour = getResources().getColor(R.color.warningBackground);}

        ArrayList<Integer> pieColours = new ArrayList<>();
        pieColours.add(cveColour);
        pieColours.add(claColour);
        pieColours.add(getResources().getColor(R.color._5_5));

        PieDataSet pieDataSet = new PieDataSet(entries, null);
        pieDataSet.setColors(pieColours);
        ValueFormatter valueFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        };
        pieDataSet.setValueFormatter(valueFormatter);
        pieDataSet.setSliceSpace(2f);
        PieData pieData = new PieData(pieDataSet);
        PieChart pieChart = findViewById(R.id.CPDPiechart);
        pieChart.setData(pieData);
        pieChart.getLegend().setEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setHoleRadius(40f);
        pieChart.setCenterText(totalS);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(20);
        if (total < 60){
            pieChart.setHoleColor(getResources().getColor(R.color.warningBackground));
            pieChart.setCenterTextColor(Color.WHITE);
        }
        if (cveTotal < 15 || clTotal < 15){
            pieChart.setTransparentCircleColor(getResources().getColor(R.color.BloodRed));
        }
        pieChart.setTransparentCircleRadius(50f);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateXY(3000, 3000);

        return null;
    }
}