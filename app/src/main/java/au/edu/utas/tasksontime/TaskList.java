package au.edu.utas.tasksontime;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskList extends AppCompatActivity {

    public ViewGroup root;

    @Override
    public void onResume() {
        super.onResume();
        Spinner SortOption = (Spinner) findViewById(R.id.sort_filter);
        String sortMode = SortOption.getSelectedItem().toString();
        root = (ViewGroup) findViewById(R.id.taskListRoot);
        root.removeAllViewsInLayout();
        populate(sortMode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        final Spinner SortOption = (Spinner) findViewById(R.id.sort_filter);
        SortOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String sortMode = SortOption.getSelectedItem().toString();
                SharedPreferences.Editor editor = getSharedPreferences("sortby", MODE_PRIVATE).edit();
                sortMode = sortMode.toLowerCase();
                editor.putString("sortby", sortMode);
                editor.commit();

                //NEW TASK
                Button newTaskButton = (Button) findViewById(R.id.newTaskFromListButton);
                newTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(TaskList.this, newTask.class);
                        startActivity(i);
                    }
                });

                root = (ViewGroup) findViewById(R.id.taskListRoot);
                root.removeAllViewsInLayout();
                populate(sortMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        SharedPreferences prefs = getSharedPreferences("sortby", MODE_PRIVATE);
        String sortMode = prefs.getString("sortby", "date");

        if(sortMode.contains("date"))
            SortOption.setSelection(0);
        if(sortMode.contains("unit") || sortMode.contains("code"))
            SortOption.setSelection(1);
        if(sortMode.contains("pri"))
            SortOption.setSelection(2);

        populate(sortMode);
    }

    public void populate(String SortMode){
        SQLiteDatabase mydatabase = openOrCreateDatabase("Mclovin List",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Danktable(Title VARCHAR,UnitCode VARCHAR,Weight INT,Important INT,Urgent INT,Checkbox INT,DueDate DATE, sortDate VARCHAR);");
        //check the sort mode
        Spinner SortOption = (Spinner) findViewById(R.id.sort_filter);
        SortMode = SortOption.getSelectedItem().toString();
        SortMode = SortMode.toLowerCase();
        Cursor resultSet;
        resultSet = mydatabase.rawQuery("Select * from Danktable ",null);
        if(SortMode.contains("date"))
            resultSet = mydatabase.rawQuery("Select * from Danktable ORDER BY sortDate, Title, Unitcode",null);
        if(SortMode.contains("unit"))
            resultSet = mydatabase.rawQuery("Select * from Danktable ORDER BY Unitcode, sortDate, Title",null);
        if(SortMode.contains("pri"))
            resultSet = mydatabase.rawQuery("SELECT * FROM `danktable` WHERE 1 ORDER BY (Urgent AND Important) desc, Urgent desc, Important desc, sortDate desc",null);

        int count = resultSet.getCount();
        resultSet.moveToFirst();

        while (count > 0){
            //do the things
            String thetitle = resultSet.getString(0);
            String unitcode = resultSet.getString(1);
            int weighty = resultSet.getInt(2);
            boolean important = (resultSet.getInt(3) == 1)? true : false;
            boolean urgent = (resultSet.getInt(4) == 1)? true : false;
            boolean complete = (resultSet.getInt(5) == 1)? true : false;
            String datey = resultSet.getString(6);
            //format the date string

            //put in the view
            Log.d("About to inflate", thetitle);
            inflateTask(thetitle, unitcode ,datey, important, urgent, weighty, complete, SortMode);
            //move the courser
            resultSet.moveToNext();
            //reduce count
            count --;
        }
    }

    public void delete(String titlegiven, String unitcodegiven)
    {
        Log.d("About to delte : " , titlegiven + unitcodegiven);
        SQLiteDatabase mydatabase = openOrCreateDatabase("Mclovin List",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Danktable(Title VARCHAR,UnitCode VARCHAR,Weight INT,Important INT,Urgent INT,Checkbox INT,DueDate DATE, sortDate VARCHAR);");
        mydatabase.delete("Danktable","Title='"+titlegiven+"' AND UnitCode='"+unitcodegiven+"'",null); //deletes row
    }

    public void complete_task(String titlegiven, String unitcodegiven, Boolean checkbox)
    {
        SQLiteDatabase mydatabase = openOrCreateDatabase("Mclovin List",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Danktable(Title VARCHAR,UnitCode VARCHAR,Weight INT,Important INT,Urgent INT,Checkbox INT,DueDate DATE, sortDate VARCHAR);");
        if (checkbox==true)
            mydatabase.execSQL("UPDATE Danktable SET Checkbox=1 WHERE Title='"+ titlegiven +"' AND UnitCode='"+unitcodegiven+"'");
        else
            mydatabase.execSQL("UPDATE Danktable SET Checkbox=0 WHERE Title='"+ titlegiven +"' AND UnitCode='"+unitcodegiven+"'");
    }

    void inflateTask(final String name, final String unitCode, String dueDate, boolean important, boolean urgent, int assWeight, boolean complete, String sortBy){
        //create task object to store references
        final Task task = new Task(TaskList.this);

        //INFLATE LAYOUT XML
        final LinearLayout root = (LinearLayout)findViewById(R.id.taskListRoot);
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout taskItem;
        taskItem = (LinearLayout) layoutInflater.inflate(R.layout.task_item,root);


        //SETUP TASK TO REFERENCE LAYOUT ELEMENTS
        checkChildren(taskItem, task);

        //NOW YOU CAN USE THE TASK
        task.name.setText(name);
        task.unitCode.setText("Unit Code: " + unitCode);
        task.dueDate.setText("Due Date: " + dueDate);

        //PRIORITY
        String priority = "Normal";
        int color = rColor(R.color.normal);
        if (important){
            color = rColor(R.color.important);
            priority = "Important";
        }
        if (urgent){
            color = rColor(R.color.urgent);
            priority = "Urgent";
        }
        if (important && urgent){
            color = rColor(R.color.urgAndImp);
            priority = "Urgent AND Important";
        }
        task.priority.setText("Priority: " + priority);
        task.colour = color;
        task.head.setBackgroundColor(color);

        task.assWeight.setText("Assesment Weight: " + Integer.toString(assWeight) + "%");

        //COMPLETE??
        task.complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int color = task.colour;
                if (isChecked)
                    color = rColor(R.color.complete);
                task.head.setBackgroundColor(color);
                String ucode = task.unitCode.getText().toString();
                ucode = ucode.substring(11, ucode.length());
                complete_task(task.name.getText().toString(),ucode, isChecked);
            }
        });
        task.complete.setChecked(complete); //Hoping this triggeres the code above/\

        //SORT BY???
        sortBy = sortBy.toLowerCase();
        if (sortBy.contains("date")){
            task.sortby.setText(dueDate);
            //task.dueDate.setVisibility(View.GONE);
        }
        if (sortBy.contains("unit") || sortBy.contains("code")){
            task.sortby.setText(unitCode);
            //task.unitCode.setVisibility(View.GONE);
        }
        if (sortBy.contains("pri")){
            task.sortby.setText(priority);
            //task.priority.setVisibility(View.GONE);
        }

        task.body.setVisibility(View.GONE);
        //SHOW HIDE BODY FUNCTION
        task.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task.body.getVisibility() == View.GONE){
                    task.body.setVisibility(View.VISIBLE);
                } else {
                    task.body.setVisibility(View.GONE);
                }
            }
        });

        //EDIT FUNCTION
        task.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("About EditTask", name);
                editTask(name, unitCode);
            }
        });

        //DELETE BUTTON
        task.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //THE CONFIRMATION DIALOG
                String Message = "This will permanently delete the task named:\n" +
                        name + "\n Are you sure?";
                Log.d("Validation Error :", Message);
                final Dialog dialog =  new Dialog(TaskList.this);
                dialog.setContentView(R.layout.delete);
                dialog.setTitle("WARNING!");
                TextView errMsg = (TextView)dialog.findViewById(R.id.delete_message);
                errMsg.setText(Message);
                //YES BUTTON
                Button delYes = (Button)dialog.findViewById(R.id.delete_yes);
                delYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //THE DELETION!
                        delete(name, unitCode);
                        task.me.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });
                //YES BUTTON
                Button delNo = (Button)dialog.findViewById(R.id.delete_no);
                delNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //SHOW DIALOG
                dialog.show();
            }
        });
        Log.d("Finished inflating ", task.name.getText().toString());
    }


    int rColor(int resource){
        return ContextCompat.getColor(this, resource);
        //return getResources().getColor(resource, null);
    }

    void checkChildren(View view, final Task task){
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            int childCount = viewGroup.getChildCount();
            //tag = viewGroup.getTag().toString();          //Get tag always crashes on a view group or a linear layout no idea why.
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(i);
                checkChildren(child, task);
            }
        }

        int id = view.getId();
        if (id == R.id.taskItem)
            task.root = view;
        if (id == R.id.head)
            task.head = view;
        if (id == R.id.name)
            task.name = (TextView)view;
        if (id == R.id.sortBy)
            task.sortby = (TextView)view;
        if (id == R.id.body)
            task.body=view;
        if (id == R.id.date)
            task.dueDate=(TextView)view;
        if (id == R.id.unitCode)
            task.unitCode = (TextView)view;
        if (id == R.id.weight)
            task.assWeight = (TextView)view;
        if (id == R.id.priority)
            task.priority = (TextView)view;
        if (id == R.id.complete)
            task.complete = (Switch)view;
        //if (id == R.id.buttons) //dont need buttons container in code but it exists
        if (id == R.id.edit)
            task.edit = (Button)view;
        if (id == R.id.delete)
            task.delete = (Button)view;
        if (id == R.id.taskItem)
            task.me = view;

    }

    void editTask(String name, String unitCode){
        Log.d("EditTask", name + unitCode);
        SharedPreferences.Editor edittask = getSharedPreferences("editstore", MODE_PRIVATE).edit();
        edittask.putString("edit?", "Yes");
        edittask.putString("name", name);
        edittask.putString("unitcode", unitCode);
        edittask.commit();

        Intent i = new Intent(TaskList.this, newTask.class);
        startActivity(i);
    }
}
