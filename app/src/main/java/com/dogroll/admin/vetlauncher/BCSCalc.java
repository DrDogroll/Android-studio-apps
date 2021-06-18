package com.dogroll.admin.vetlauncher;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class BCSCalc extends AppCompatActivity {

    DBHandler MasterDB;
    AlertDialog alertDialog;
    int count;
    int recordID;
    String farm;
    String date;
    String herd;

    EditText etCalc3rd;
    EditText etCalc2nd;
    EditText etCalcLast;
    TextView tvCalcCount;
    TextView tvCalcStored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcs_calc);
        setTitle("BCS Calculator");

        MasterDB = new DBHandler(this);
        etCalc3rd = findViewById(R.id.etCalc3rd);
        etCalc2nd = findViewById(R.id.etCalc2nd);
        etCalcLast = findViewById(R.id.etCalclast);
        tvCalcCount = findViewById(R.id.tvCalcCount);
        tvCalcStored = findViewById(R.id.tvCalcStored);

    Bundle extras = getIntent().getExtras();
    if (extras != null){
        recordID = extras.getInt("recordID");
        date = extras.getString("date");
        farm = extras.getString("farm");
        herd = extras.getString("herd");
    }
    TextView tvCalcFarm = findViewById(R.id.tvCalcFarm);
    String title = herd+", "+farm+", "+date;
    tvCalcFarm.setText(title);
    tvCalcStored.setVisibility(View.INVISIBLE);

        Button btn_2_5 = findViewById(R.id.btn_2_5);
        Button btn_3_0 = findViewById(R.id.btn_3_0);
        Button btn_3_5 = findViewById(R.id.btn_3_5);
        Button btn_4_0 = findViewById(R.id.btn_4_0);
        Button btn_4_5 = findViewById(R.id.btn_4_5);
        Button btn_5_0 = findViewById(R.id.btn_5_0);
        Button btn_5_5 = findViewById(R.id.btn_5_5);
        Button btn_6_0 = findViewById(R.id.btn_6_0);
        Button btn_6_5 = findViewById(R.id.btn_6_5);
        Button btnCalcClear = findViewById(R.id.btnCalcClear);
        Button btnCalcSummary = findViewById(R.id.btnCalcSummary);

        btn_2_5.setOnClickListener(v -> record("_2_5"));
        btn_3_0.setOnClickListener(v -> record("_3_0"));
        btn_3_5.setOnClickListener(v -> record("_3_5"));
        btn_4_0.setOnClickListener(v -> record("_4_0"));
        btn_4_5.setOnClickListener(v -> record("_4_5"));
        btn_5_0.setOnClickListener(v -> record("_5_0"));
        btn_5_5.setOnClickListener(v -> record("_5_5"));
        btn_6_0.setOnClickListener(v -> record("_6_0"));
        btn_6_5.setOnClickListener(v -> record("_6_5"));
        btnCalcClear.setOnClickListener(v -> {
            alertDialog = new AlertDialog.Builder(BCSCalc.this).create();
            alertDialog.setTitle("Clear record?");
            alertDialog.setMessage("Are you sure you want to clear this record?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Clear", (dialog, which) -> {
                MasterDB.clearBCSRecord(recordID);
                etCalc3rd.setText("");
                etCalc2nd.setText("");
                etCalcLast.setText("");
                tvCalcCount.setText("");
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
            alertDialog.show();

        });
        btnCalcSummary.setOnClickListener(v -> {
            if(!etCalc3rd.getText().toString().equals("")){
                String Calc3rd = etCalc3rd.getText().toString();
                if(Calc3rd.equals("2.5") || Calc3rd.equals("3") || Calc3rd.equals("3.5") || Calc3rd.equals("4") || Calc3rd.equals("4.5") || Calc3rd.equals("5") || Calc3rd.equals("5.5") || Calc3rd.equals("6") || Calc3rd.equals("6.5")){
                    String tCalc3rd = translator(Calc3rd);
                    MasterDB.updateBCSRecord(recordID, tCalc3rd);
                }else{etCalc3rd.setError("Typo detected"); return;}
            }
            if(!etCalc2nd.getText().toString().equals("")){
                String Calc2nd = etCalc2nd.getText().toString();
                if(Calc2nd.equals("2.5") || Calc2nd.equals("3") || Calc2nd.equals("3.5") || Calc2nd.equals("4") || Calc2nd.equals("4.5") || Calc2nd.equals("5") || Calc2nd.equals("5.5") || Calc2nd.equals("6") || Calc2nd.equals("6.5")){
                    String tCalc2nd = translator(Calc2nd);
                    MasterDB.updateBCSRecord(recordID, tCalc2nd);
                }else{etCalc2nd.setError("Typo detected");return;}
            }
            if(!etCalcLast.getText().toString().equals("")){
                String CalcLast = etCalcLast.getText().toString();
                if(CalcLast.equals("2.5") || CalcLast.equals("3") || CalcLast.equals("3.5") || CalcLast.equals("4") || CalcLast.equals("4.5") || CalcLast.equals("5") || CalcLast.equals("5.5") || CalcLast.equals("6") || CalcLast.equals("6.5")){
                    String tCalcLast = translator(CalcLast);
                    MasterDB.updateBCSRecord(recordID, tCalcLast);
                }else{etCalcLast.setError("Typo detected");return;}
            }
            Bundle bundle = new Bundle();
            bundle.putInt("recordID", recordID);
            Intent intent = new Intent(getApplicationContext(), BCSSummary.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
void record (String selected){
        int result;
        if(!etCalc3rd.getText().toString().equals("")){
            String Calc3rd = etCalc3rd.getText().toString();
            if(Calc3rd.equals("2.5") || Calc3rd.equals("3") || Calc3rd.equals("3.5") || Calc3rd.equals("4") || Calc3rd.equals("4.5") || Calc3rd.equals("5") || Calc3rd.equals("5.5") || Calc3rd.equals("6") || Calc3rd.equals("6.5"))
            {
                String tCalc3rd = translator(Calc3rd);
                result = MasterDB.updateBCSRecord(recordID, tCalc3rd);
                if (result !=0){
                    tvCalcStored.setVisibility(View.VISIBLE);
                    ObjectAnimator colorFade = ObjectAnimator.ofObject(tvCalcStored, "backgroundColor", new ArgbEvaluator(), Color.argb(250,24, 230, 32), 0x00000000);
                    colorFade.setDuration(2000);
                    colorFade.start();
                    //tvCalcStored.setVisibility(View.INVISIBLE);
                }
            }else{
                etCalc3rd.setError("Typo detected. Please correct to a single value of 2.5, 3 etc");
                return;
            }
        }
        if(!etCalc2nd.getText().toString().equals("")){
            String calc2nd = etCalc2nd.getText().toString();
            etCalc3rd.setText(calc2nd);

        }
        if (!etCalcLast.getText().toString().equals("")){
            String calcLast = etCalcLast.getText().toString();
            etCalc2nd.setText(calcLast);
        }
        etCalcLast.setText(translator(selected));
        count++;
        String sCount = Integer.toString(count);
        tvCalcCount.setText(sCount);
}
String translator(String columnName){
        String result = "";
        switch (columnName){
            case "_2_5":
                result = "2.5";
                break;
            case "_3_0":
                result =  "3";
                break;
            case "_3_5":
                result =  "3.5";
            break;
            case "_4_0":
                result =  "4";
            break;
            case "_4_5":
                result =  "4.5";
            break;
            case "_5_0":
                result =  "5";
            break;
            case "_5_5":
                result =  "5.5";
            break;
            case "_6_0":
                result =  "6";
            break;
            case "_6_5":
                result =  "6.5";
            break;
            case "2.5":
                result = "_2_5";
                break;
            case "3":
                result =  "_3_0";
                break;
            case "3.5":
                result =  "_3_5";
                break;
            case "4":
                result =  "_4_0";
                break;
            case "4.5":
                result =  "_4_5";
                break;
            case "5":
                result =  "_5_0";
                break;
            case "5.5":
                result =  "_5_5";
                break;
            case "6":
                result =  "_6_0";
                break;
            case "6.5":
                result =  "_6_5";
                break;
        }
        return result;
}
}
