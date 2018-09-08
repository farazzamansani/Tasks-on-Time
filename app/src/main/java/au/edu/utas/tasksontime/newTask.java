package au.edu.utas.tasksontime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Console;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class newTask extends AppCompatActivity {

    public static String taskDate;
    public static String sortDate;
    LinearLayout root;
    TextView name;
    protected static TextView dueDate; //Needed for date picker
    String dueDateOriginal;             //Store the value before selecting a date for validation.
    CheckBox important;
    CheckBox urgent;
    TextView unitCode;
    TextView assWeight;
    Button done;
    public boolean editMode;
    public String taskNameToEdit;
    public String taskCodeToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        //ACCESS FOR ALL
        root = (LinearLayout)findViewById(R.id.newTaskRoot);
        root.setBackgroundColor(Color.WHITE);
        name = (TextView)findViewById(R.id.et_newName);

        //DATE PICKER
        dueDate = (TextView)findViewById(R.id.tv_newDate);
        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment mDatePicker = new DatePickerFragment();
                mDatePicker.show(getFragmentManager(), "Select date");
            }
        });
        dueDateOriginal = dueDate.getText().toString();

        important = (CheckBox)findViewById(R.id.cb_important);
        important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor();
            }
        });
        urgent = (CheckBox)findViewById(R.id.cb_urgent);
        urgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor();
            }
        });
        unitCode = (TextView)findViewById(R.id.et_newUnit);
        assWeight = (TextView)findViewById(R.id.et_newWeight);

        //DONE BUTTON
        done = (Button)findViewById(R.id.btn_saveNewtask);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateInput();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        //check if we are in edit mode
        SharedPreferences prefs = getSharedPreferences("editstore", MODE_PRIVATE);

        String prefEdit = prefs.getString("edit?", "No");
        Log.d("EditPrefs is", prefEdit);
        editMode = (prefEdit.equals("Yes")) ? true : false;
        if(editMode) {
            Log.d("Edit mode is", "true");
            taskNameToEdit = prefs.getString("name", null);
            taskCodeToEdit = prefs.getString("unitcode", null);
            //keep this here in case they use back button and edit mode is still on
            SharedPreferences.Editor edittask = getSharedPreferences("editstore", MODE_PRIVATE).edit();
            edittask.putString("edit?", "No");
            edittask.commit();
            read(); //change the view with data from the database
        } else {
            Log.d("Edit mode is", "false");
        }
    }

    void validateInput() throws ParseException {
        String errorMessage = "";
        if (dueDate.getText() == dueDateOriginal)
            errorMessage = "You have not picked a Due Date. Come on set deadlines for your goals. :)";
        if (name.getText().toString().length() < 1) //had to check string length as checking if == """ doesn't always work
            errorMessage = "You must enter a name for your Task.";
        if (!editMode && taskExists()){
            errorMessage = "You already have a task with that name and unit code.";
        }
        if (errorMessage != ""){
            Log.d("Validation Error :", errorMessage);
            final Dialog dialog =  new Dialog(newTask.this);
            dialog.setContentView(R.layout.error);
            dialog.setTitle("Cant save this Task");
            TextView errMsg = (TextView)dialog.findViewById(R.id.error_message);
            errMsg.setText(errorMessage);
            Button errOK = (Button)dialog.findViewById(R.id.error_ok);
            errOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {

            //check do we save or update
            if(editMode){

                String namey = name.getText().toString();
                String codey = unitCode.getText().toString();
                String weighty = assWeight.getText().toString();
                boolean impy = important.isChecked();
                boolean urgy = urgent.isChecked();

                update_task(taskNameToEdit, taskCodeToEdit, namey, codey, weighty,impy, urgy);

            } else {
                save();
            }
            finish();
//            Intent i = new Intent(newTask.this, TaskList.class);
//            startActivity(i);
        }
    }

    boolean taskExists(){
        SQLiteDatabase mydatabase = openOrCreateDatabase("Mclovin List",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Danktable(Title VARCHAR,UnitCode VARCHAR,Weight INT,Important INT,Urgent INT,Checkbox INT,DueDate DATE, sortDate VARCHAR);");
        Cursor resultSet;
        resultSet = mydatabase.rawQuery("Select * from Danktable WHERE Title='"+name.getText().toString()+"' AND UnitCode='"+unitCode.getText().toString()+"';",null);
        int count = resultSet.getCount();

        if (count > 0){
            return true;
        }
        return false;
    }

    void save(){
        SQLiteDatabase mydatabase = openOrCreateDatabase("Mclovin List",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Danktable(Title VARCHAR,UnitCode VARCHAR,Weight INT,Important INT,Urgent INT,Checkbox INT,DueDate DATE, sortDate VARCHAR);");
        //convert booleans
        int impChecked = (important.isChecked()) ? 1 : 0;
        int urgChecked = (urgent.isChecked()) ? 1 : 0;
        mydatabase.execSQL("INSERT INTO Danktable VALUES('"+ name.getText() +
                "','" + unitCode.getText() +
                "','" + assWeight.getText() +
                "','" + impChecked +"','" +
                urgChecked + "',0,'" + taskDate + "','" + sortDate + "');");
    }

    public void read(){
        SQLiteDatabase mydatabase = openOrCreateDatabase("Mclovin List",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Danktable(Title VARCHAR,UnitCode VARCHAR,Weight INT,Important INT,Urgent INT,Checkbox INT,DueDate DATE, sortDate VARCHAR);");
        Cursor resultSet = mydatabase.rawQuery("Select * from Danktable WHERE Title='" + taskNameToEdit +
                "' AND UnitCode='" + taskCodeToEdit + "'",null);
        resultSet.moveToLast();// see last row in table

        String thetitle = resultSet.getString(0);
        String unitycode = resultSet.getString(1);
        String weighty = resultSet.getString(2);
        int imp = resultSet.getInt(3);
        int urg = resultSet.getInt(4);
//        int cmp = resultSet.getInt(5);
        String datey=resultSet.getString(6);
        //convert booleans
        boolean impChecked = (imp == 1) ? true : false;
        boolean urgChecked = (urg == 1) ? true : false;

        name.setText(thetitle);
        unitCode.setText(unitycode);
        assWeight.setText(weighty);
        important.setChecked(impChecked);
        urgent.setChecked(urgChecked);
        dueDate.setText(datey);
        changeColor();
    }

    public void update_task(String titlegiven, String unitcodegiven, String newtitle, String newcode, String newweight, boolean newimp, boolean newurg)
    {
        int impy=0;
        int urgy=0;
        int checky=0;
        if (newimp==true)
            impy=1;
        else
            impy=0;
        if (newurg==true)
            urgy=1;
        else
            urgy=0;

        SQLiteDatabase mydatabase = openOrCreateDatabase("Mclovin List",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Danktable(Title VARCHAR,UnitCode VARCHAR,Weight INT,Important INT,Urgent INT,Checkbox INT,DueDate DATE, sortDate VARCHAR);");
        if (sortDate == null){
            //sortDate =
        }
        mydatabase.execSQL("UPDATE Danktable SET Title='"+newtitle+"',UnitCode='"+newcode+"',Weight='"+newweight+"',Important="+impy+",Urgent="+urgy+",DueDate='"+ dueDate.getText().toString() +"' WHERE Title='"+ titlegiven +"' AND UnitCode='"+unitcodegiven+"'");
    }

    void changeColor(){
        int color = Color.WHITE;
        if (important.isChecked())
            color = rColor(R.color.important);
        if (urgent.isChecked())
            color = rColor(R.color.urgent);
        if (important.isChecked() && urgent.isChecked())
            color = rColor(R.color.urgAndImp);
        root.setBackgroundColor(color);
    }

    int rColor(int resource){
        return ContextCompat.getColor(this, resource);
        //return getResources().getColor(resource, null);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE d MMM yyyy", Locale.getDefault());
        SimpleDateFormat sortFormater = new SimpleDateFormat("d %m yyyy", Locale.getDefault());

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            c.set(year, month, day);
            taskDate = formatter.format(c.getTime());
            dueDate.setText(formatter.format(c.getTime()));
            int total = day + (month * 100) + (year * 1500);
            sortDate = "" + total;
            Log.d("Sort date ", sortDate);
        }
    }
}