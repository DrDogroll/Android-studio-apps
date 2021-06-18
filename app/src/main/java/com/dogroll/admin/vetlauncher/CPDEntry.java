package com.dogroll.admin.vetlauncher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CPDEntry extends AppCompatActivity {

    DatePickerDialog Date_Picker_Dia;
    String cpdRecordYear, cpdRecordTitle;
    String cpdCode = "";
    String date, title, description, provider, selectedClass, selectedSubclass, hours, points, evidence, outcome, impact, function;
    int radioboxID = 0;
    int reflectiveRecord = 0;
    double cpdHours, cpdPoints;
    double multiplier = 0;
    int REQUEST_CODE_SAVE = 66;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_p_d_entry);
        setTitle("New CPD Record");

        date = title = description = provider = selectedClass = selectedSubclass = hours = points = evidence = outcome = impact = function ="";

        ImageButton cpdEntryBTNDate = findViewById(R.id.cpdEntryBTNDate);
        TextView cpdEntryTVDate = findViewById(R.id.cpdEntryTVDate);
        cpdEntryBTNDate.setOnClickListener(v -> {
            final Calendar selected_date = Calendar.getInstance();
            int mDay = selected_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = selected_date.get(Calendar.MONTH);
            int mYear = selected_date.get(Calendar.YEAR);
            Date_Picker_Dia = new DatePickerDialog(CPDEntry.this, (view1, year, monthOfYear, dayOfMonth) -> {
                int truemonth = monthOfYear +1;
                cpdRecordYear = Integer.toString (year);
                String cpdRecordDate = dayOfMonth + "/" + truemonth + "/" + year;
                cpdEntryTVDate.setText(cpdRecordDate);
            }, mYear, mMonth, mDay);
            Date_Picker_Dia.show();
            cpdEntryTVDate.setError(null);

        });

        LinearLayout cpdEntryLLReflective = findViewById(R.id.LLReflective);
        cpdEntryLLReflective.setVisibility(View.GONE);
        SwitchCompat cpdSwitch = findViewById(R.id.cpdSwitch);
        cpdSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                cpdEntryLLReflective.setVisibility(View.VISIBLE);
                reflectiveRecord = 1;
            }else{
                cpdEntryLLReflective.setVisibility(View.GONE);
                reflectiveRecord = 0;
            }
        });

        RadioGroup cpdEntryRGroup = findViewById(R.id.cpdEntryRGroup);
        cpdEntryRGroup.setOnCheckedChangeListener((group, checkedId) -> {
            radioboxID = cpdEntryRGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioBTN = findViewById(radioboxID);
            selectedClass = selectedRadioBTN.getHint().toString();
            Spinner cpdEntrySpinner = findViewById(R.id.cpdEntrySpinner);
            TextView cpdEntryTVEvidence = findViewById(R.id.inflateEvidenceExamples);
            TextView cpdEntryTVSubcat = findViewById(R.id.inflateSubCatExamples);
            clearHours();
            List<String> subclasses = new ArrayList<>();
            List<String> codes = new ArrayList<>();
            List<String> exEvidence = new ArrayList<>();
            List<String> exSubcat = new ArrayList<>();
            switch (selectedClass){
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
                    subclasses.add("Assessed lectures, reading, audio/video/online");
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
                    exEvidence.clear();
                    exEvidence.add("");
                    exEvidence.add("Certificate/Evidence of attainment (eg notification of results or transcript)");
                    exEvidence.add("Certificate/Evidence of attainment (eg notification of results or transcript)");
                    exEvidence.add("Certificate/Evidence of attendance/Employer Verification and/or reflective record");
                    exEvidence.add("Copy of presentation/Letter from host organisation/Employer verification");
                    exEvidence.add("Copy of presentation/Letter from host organisation/Employer verification");
                    exEvidence.add("Copy of Publication");
                    exEvidence.add("Acknowledgement from editor");
                    exEvidence.add("Certificate/Evidence of attainment (eg notification of results)/Employer verification");
                    exEvidence.add("Reflective record");
                    exSubcat.clear();
                    exSubcat.add("");
                    exSubcat.add("University/Polytechnic courses including distance and online options, eg vet postgrad\nrelevant postgrad studies such as Business Studies, Management, Education, Agriculture, etc");
                    exSubcat.add("ACVSc membership and fellowship programmes,\nCentre for Veterinary Education distance education programmes,\nRCVS Certificate/Diploma and Fellowship programmes");
                    exSubcat.add("NZVA / AVA / ACVSc / OIE events,\nVA National or Regional conferences,\nACVM Roadshows,\nMPI science seminars,\nTechnical/scientific courses provided by industry,\nIn-house workshops and seminars that meet the definition of CVE,\nWebinars that meet the definition of CVE(assessed)");
                    exSubcat.add("Presentations to conferences or seminars,\ntechnical presentations to industry or peer groups that meet the definition of CVE");
                    exSubcat.add("Presentations to conferences or seminars,\ntechnical presentations to industry or peer groups that meet the definition of CVE");
                    exSubcat.add("Published articles in peer-reviewed journals, books or reports eg NZVJ.\nFormal peer-reviewed reports on technical issues");
                    exSubcat.add("Refereeing for peer-reviewed publications");
                    exSubcat.add("VS e-learning,\nVetLearn VetScholar modules,\nCentre for Veterinary Education Time on Line courses,\nQuizzes based on presentations/seminars/journal articles,\nWritten or on-line assessment tests");
                    exSubcat.add("Other");
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
                    exEvidence.clear();
                    exEvidence.add("");
                    exEvidence.add("Appropriate record of activity and participation (eg case studies, copy of case presentation or review, letter from specialist or mentor, referral report) and/or reflective record");
                    exEvidence.add("Appropriate record of activity and participation (eg letter from recipient or their name and signature on CPD record) and/or reflective record");
                    exEvidence.add("Appropriate record of activity and participation (eg minutes or agenda of peer group meeting) and/or reflective record");
                    exEvidence.add("Appropriate record of activity and participation (eg excerpts from relevant meeting minutes, finalised policy or guideline document) and/or reflective record");
                    exEvidence.add("Appropriate record of activity and participation (eg certificate/employer verification) and/or reflective record");
                    exEvidence.add("Appropriate record of activity and participation (eg audit report, copy of performance assessment, employer verification) and/or reflective record");
                    exEvidence.add("Reflective record");
                    exSubcat.clear();
                    exSubcat.add("");
                    exSubcat.add("Case presentations\nIn-house training and instruction\nDiscussions with colleagues, specialists and experts\nLessons Learned sessions\nObserving Practice\nClinical/surgical rounds\nJoint treatment planning and patient management sessions\nRecieving mentoring and/or supervision");
                    exSubcat.add("Undergrad and Postgrad students\nSpecialist or College Membership candidates (including research supervision)\nNewgrads, NZNVE candidatesand veterinarians returning to work\nNew employees (eg MPI)");
                    exSubcat.add("Journal clubs\nStudy/discussion groups\nOnline discussion forums eg VetScholar\nTeam meetings eg VS regional team meetings");
                    exSubcat.add("Professional body or other meetings with an educational content such as guideline, standard or policy development\nWorking as an assessor or reviewer for the Council or other bodies");
                    exSubcat.add("Hands-on practical learning\nWetlabs and workshops\nInteractive sessions during conferences, seminars and workshops");
                    exSubcat.add("Accreditation and audit eg ISO, NZVA Best Practice, AssureQuality, MPI\nPeer review and self assessment questionnaires\nPerformance assessment/planning/360 degree feedback");
                    exSubcat.add("Other");
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
                    exEvidence.clear();
                    exEvidence.add("");
                    exEvidence.add("Name of publication/author/pages; Website URL and topic plus reflective record or critical literature review");
                    exEvidence.add("Appropriate record of activity (eg website URL and topic) plus reflective record");
                    exEvidence.add("Appropriate record of activity (eg name of publication/author or website URL/notes of discussions with colleagues) plus reflective record");
                    exEvidence.add("Copy of paper/article/newsletter/report/protocol/information leaflet/guidelines/business plan etc");
                    exEvidence.add("Copy of CPD plan or Personal Development Plan relevant to area of practice");
                    exEvidence.add("Reflective learning records documenting learning and impact on practice");
                    exEvidence.add("Reflective record");
                    exSubcat.clear();
                    exSubcat.add("");
                    exSubcat.add("For lecturing, teaching, tutoring, auditing, assessing, providing expert opinion, examining/studying for membership examinations, maintaining technical knowledge");
                    exSubcat.add("Multimedia or web-based education or research\nPodcasts, webinars, audio-tapes, CD's etc");
                    exSubcat.add("Web-based reading/research\nDiscussions with colleagues\nReading of Journals, articles and books\nWritten reviews, summaries or notes");
                    exSubcat.add("Articles for VetScript Surveillance, Biosecurity, Welfare Pulse, Conference proceedings, NZVA special interest branch or practice newsletters\nReview of books and articles\nInformation leaflets\nPractice protocols, procedures and policies\npublished case studies");
                    exSubcat.add("Reviewing CPD needs\nFormulating learning Objectives\nWriting a CPD plan");
                    exSubcat.add("Documenting actual learning outcomes and impacts on your veterinary abilities");
                    exSubcat.add("Other");
                    break;
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CPDEntry.this, android.R.layout.simple_spinner_item, subclasses);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cpdEntrySpinner.setAdapter(arrayAdapter);
            cpdEntrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    cpdCode = codes.get(position);
                    cpdEntryTVSubcat.setText(exSubcat.get(position));
                    selectedSubclass = exSubcat.get(position);
                    cpdEntryTVEvidence.setText(exEvidence.get(position));
                    clearHours();

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

        Button cpdEntryPDFBTN = findViewById(R.id.cpdEntryPDFBTN);
        Button cpdEntryJPGBTN = findViewById(R.id.cpdEntryJPGBTN);

        cpdEntryPDFBTN.setOnClickListener(v -> {
            function = "pdf";
            buildBundleAndBeginActivity();
        });

        cpdEntryJPGBTN.setOnClickListener(v -> {
            function = "jpg";
            buildBundleAndBeginActivity();
        });

        EditText cpdEntryETHours = findViewById(R.id.cpdEntryETHours);
        cpdEntryETHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")){
                    setCPDhours();
                }
            }
        });
    }

    int verifyFields(){
        int proceed = 1;
        TextView cpdEntryTVDate = findViewById(R.id.cpdEntryTVDate);
        if (cpdEntryTVDate.getText().toString().equals("Select Date")){
            cpdEntryTVDate.setError("Please select a date");
            proceed = 0;
        }else{date = cpdEntryTVDate.getText().toString();}

        EditText cpdEntryETTitle = findViewById(R.id.cpdEntryETTitle);
        if (cpdEntryETTitle.getText().toString().equals("")){
            cpdEntryETTitle.setError("Please enter a title");
            proceed = 0;
        }else{title = cpdEntryETTitle.getText().toString();}

        EditText cpdEntryProvider = findViewById(R.id.cpdEntryETProvider);
        if(!cpdEntryProvider.getText().toString().equals("")){
            provider = cpdEntryProvider.getText().toString();
        }

        EditText cpdEntryETDescription = findViewById(R.id.cpdEntryETDescription);
        if (cpdEntryETDescription.getText().toString().equals("")){
            cpdEntryETDescription.setError("Please enter a description");
            proceed = 0;
        }else{description = cpdEntryETDescription.getText().toString();}
        if (radioboxID == 0){
            Toast.makeText(this, "You must select a CPD class (CLA, CVE or SDL)", Toast.LENGTH_SHORT).show();
            proceed = 0;
        }
        if (cpdCode.equals("")){
            Toast.makeText(this, "You must select a subclass from the dropdown box", Toast.LENGTH_SHORT).show();
            proceed = 0;
        }
        if (reflectiveRecord == 0){
            EditText cpdEntryETEvidence = findViewById(R.id.cpdEntryETEvidence);
            if (cpdEntryETEvidence.getText().toString().equals("")){
                cpdEntryETEvidence.setError("Please enter your method");
            }else {evidence = cpdEntryETEvidence.getText().toString();}
        }
        if (reflectiveRecord == 1){
            EditText cpdEntryETLearningOutcomes = findViewById(R.id.cpdEntryETLearningOutcomes);
            if (cpdEntryETLearningOutcomes.getText().toString().equals("")){
                cpdEntryETLearningOutcomes.setError("Enter your Learning Outcomes");
                proceed = 0;
            }else{outcome = cpdEntryETLearningOutcomes.getText().toString();}
            EditText cpdEntryETImpact = findViewById(R.id.cpdEntryETImpact);
            if (cpdEntryETImpact.getText().toString().equals("")){
                cpdEntryETImpact.setError("Enter your Impact");
                proceed = 0;
            }else{impact = cpdEntryETImpact.getText().toString();}
        }
        EditText cpdEntryETHours = findViewById(R.id.cpdEntryETHours);
        try{
            cpdHours = Double.parseDouble(cpdEntryETHours.getText().toString());
        }catch (Exception e){
            cpdHours = 0;
        }
        if (cpdHours == 0){
            cpdEntryETHours.setError("Please enter your hours");
            proceed = 0;
        }
        return proceed;
    }

    public void setCPDpoints() {
        if (selectedClass.equals("cve")) {
            multiplier = 1;
            if (cpdCode.equals("CVEFirstPresentation")){
                multiplier = 4;
            }
        } else {
            multiplier = 0.5;
        }
        cpdPoints = (multiplier * cpdHours);
        String converted = Double.toString(cpdPoints);
        EditText cpdEntryETPoints = findViewById(R.id.cpdEntryETPoints);
        cpdEntryETPoints.setText(converted);
        points = converted;

    }
    public void setCPDhours(){
        EditText cpdEntryETHours = findViewById(R.id.cpdEntryETHours);
        if (cpdEntryETHours.getText().toString().equals("")){
            cpdHours = 0;
        }else {
            cpdHours = Double.parseDouble(cpdEntryETHours.getText().toString());
            hours = cpdEntryETHours.getText().toString();
        }
        setCPDpoints();
    }
    void clearHours(){
        EditText cpdEntryETHours = findViewById(R.id.cpdEntryETHours);
        cpdEntryETHours.setText("");
    }
    void buildBundleAndBeginActivity(){
        int proceed = verifyFields();
        if (proceed == 1){
            Bundle bundle = new Bundle();
            bundle.putString("year", cpdRecordYear);
            bundle.putString("date", date);
            bundle.putString("title", title);
            bundle.putString("provider", provider);
            bundle.putString("description", description);
            bundle.putString("selectedClass", selectedClass);
            bundle.putString("selectedSubclass", selectedSubclass);
            bundle.putString("hours", hours);
            bundle.putString("points", points);
            bundle.putString("evidence", evidence);
            bundle.putString("outcome", outcome);
            bundle.putString("impact", impact);
            bundle.putString("function", function);
            Intent intent = new Intent(getApplicationContext(), CPDRecord.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,REQUEST_CODE_SAVE);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SAVE) {
            EditText cpdEntryETTitle = findViewById(R.id.cpdEntryETTitle);
            cpdRecordTitle = cpdEntryETTitle.getText().toString();
            DBHandler MasterDB = new DBHandler(getApplicationContext());
            MasterDB.insertcpdValues(cpdRecordYear,cpdPoints,cpdCode,cpdRecordTitle);
            Toast.makeText(this, "CPD points saved to database", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }
    }

}