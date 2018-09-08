package au.edu.utas.tasksontime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onResume() {
        super.onResume();
        calculatescore();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("At the main menu", "nothing happening here");

        //NEW TASK
        Button newTaskButton = (Button) findViewById(R.id.newTaskButton);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, newTask.class);
                startActivity(i);
            }
        });

        //VIEW TASK LIST
        Button taskListButton = (Button) findViewById(R.id.taskListButton);
        taskListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TaskList.class);
                startActivity(i);
            }
        });
        //QUESTIONNAIRE
        Button questionnaireButton = (Button) findViewById(R.id.questionnaire);
        questionnaireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, questionnaire.class);
                startActivity(i);
                //finish();
            }
        });
        calculatescore();
    }

    public void calculatescore()
    {
        SharedPreferences prefs = getSharedPreferences("scorestore", MODE_PRIVATE);
        TextView schoreText = (TextView)findViewById(R.id.score);
        //get string first parameter is variable name, second is default value
        String result = prefs.getString("score", null);
        if(result!=null)
            schoreText.setText("your score is :"+ result );
    }
}
