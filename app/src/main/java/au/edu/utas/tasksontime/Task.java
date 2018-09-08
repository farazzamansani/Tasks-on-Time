package au.edu.utas.tasksontime;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Brad on 19/05/2017.
 */

public class Task {
    View me;
    TextView name;
    TextView sortby;    //Displays a copy of the value task list is sorted by
    TextView dueDate;
    TextView unitCode;
    TextView assWeight;
    TextView priority;
    int colour;
    Switch complete;
    Button edit;
    Button delete;

    //Containers
    View root;          //absolute parent view
    View head;         //contains name and sort by value
    View body;       //expand & collapse area containing data and buttons.

    Context activity;

    public Task(Context context){
        activity = context;
        //cant find recource by id in here...

    }
}