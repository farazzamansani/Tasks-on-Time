package au.edu.utas.tasksontime;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class questionnaire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkifempty()){

                } else {
                    score();
                    finish();
                }
            }
        });

//        Button backtomain = (Button)findViewById(R.id.Back);
//        backtomain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(questionnaire.this, MainActivity.class);
//                startActivity(i);
//            }
//        });

    }
    public boolean checkifempty()
    {
        final Dialog dialog =  new Dialog(questionnaire.this);
        dialog.setContentView(R.layout.error);
        TextView errMsg = (TextView)dialog.findViewById(R.id.error_message);
        Button errOK = (Button)dialog.findViewById(R.id.error_ok);
        errOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        int message=0;
        errMsg.setText("");

        Spinner mySpinner=(Spinner) findViewById(R.id.spinnerq1);
        if (mySpinner.getSelectedItemPosition()== (int) 0)
        {
            dialog.setTitle("Please Answer Question 1");
            errMsg.setText("Please Answer Question 1");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq2);
        if (mySpinner.getSelectedItemPosition()== (int) 0)
        {
            dialog.setTitle("Please Answer Question 2");
            errMsg.setText("Please Answer Question 2");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq3);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 3");
            errMsg.setText("Please Answer Question 3");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq4);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 4");
            errMsg.setText("Please Answer Question 4");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq5);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 5");
            errMsg.setText("Please Answer Question 5");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq6);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 6");
            errMsg.setText("Please Answer Question 6");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq7);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 7");
            errMsg.setText("Please Answer Question 7");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq8);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 8");
            errMsg.setText("Please Answer Question 8");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq9);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 9");
            errMsg.setText("Please Answer Question 9");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq10);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 10");
            errMsg.setText("Please Answer Question 10");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq11);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 11");
            errMsg.setText("Please Answer Question 11");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq12);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 12");
            errMsg.setText("Please Answer Question 12");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq13);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 13");
            errMsg.setText("Please Answer Question 13");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq14);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 14");
            errMsg.setText("Please Answer Question 14");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq15);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 15");
            errMsg.setText("Please Answer Question 15");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq16);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 16");
            errMsg.setText("Please Answer Question 16");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq17);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 17");
            errMsg.setText("Please Answer Question 17");
            dialog.show();
        }
        mySpinner=(Spinner) findViewById(R.id.spinnerq18);
        if (mySpinner.getSelectedItemPosition()== 0)
        {
            dialog.setTitle("Please Answer Question 18");
            errMsg.setText("Please Answer Question 18");
            
        }
        if(errMsg.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    public void score()
    {
        double score=0;

        Spinner mySpinner=(Spinner) findViewById(R.id.spinnerq1);
        score =score+mySpinner.getSelectedItemPosition()-1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq2);
        score =score+mySpinner.getSelectedItemPosition()-1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq3);
        score =score+mySpinner.getSelectedItemPosition()-1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq4);
        score =score+mySpinner.getSelectedItemPosition()-1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq5);
        score =score+mySpinner.getSelectedItemPosition()-1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq6);
        score =score+mySpinner.getSelectedItemPosition()-1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq7);
        score =score+mySpinner.getSelectedItemPosition()-1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq8);
        score =score-mySpinner.getSelectedItemPosition()+1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq9);
        score =score+mySpinner.getSelectedItemPosition()-1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq10);
        score =score-mySpinner.getSelectedItemPosition()+1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq11);
        score =score-mySpinner.getSelectedItemPosition()+1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq12);
        score =score+mySpinner.getSelectedItemPosition()-1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq13);
        score =score-mySpinner.getSelectedItemPosition()+1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq14);
        score =score+mySpinner.getSelectedItemPosition()-1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq15);
        score =score+mySpinner.getSelectedItemPosition()-1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq16);
        score =score-mySpinner.getSelectedItemPosition()+1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq17);
        score =score+mySpinner.getSelectedItemPosition()-1;
        mySpinner=(Spinner) findViewById(R.id.spinnerq18);
        score =score+mySpinner.getSelectedItemPosition()-1;

        score=score/52;//gives you percentage as a decimal
        score=score*100;//multiply by 100 so is no longer a decimal

        SharedPreferences.Editor editor = getSharedPreferences("scorestore", MODE_PRIVATE).edit();
        editor.putString("score", (int) score+"%");
        editor.commit();
    }
}