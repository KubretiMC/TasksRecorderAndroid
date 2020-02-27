package com.example.androidproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShowAddActivity extends AppCompatActivity {

    protected EditText addTaskName;
    protected EditText addEndDate;
    protected Button addComfirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_add);

        addTaskName=  findViewById(R.id.TaskNamePlainText);
        addEndDate= findViewById(R.id.EndDatePlainText);
        addComfirmButton= findViewById(R.id.ComfirmButton);


        addComfirmButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //on click za Insert
                try {
                    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "zadachiOpit1.db", null);
                    String q = "CREATE TABLE if not exists zadachiopit1(";
                    q += "ID integer primary key AUTOINCREMENT, ";
                    q += "taskName text not null, ";
                    q += "endDate datetime not null, ";
                    q += "finishedDate dateTime);";
                    db.execSQL(q);
                    String taskName = addTaskName.getText().toString();
                    String endDate = addEndDate.getText().toString();

                    if (taskName.isEmpty() || endDate.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Empty field", Toast.LENGTH_LONG).show();
                        Notification notify = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Empty field!")
                                .setContentText(taskName)
                                .build();
                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    } else if (!isValidDate(endDate)) {
                        Toast.makeText(getApplicationContext(), "Wrong date format!", Toast.LENGTH_LONG).show();
                        Notification notify = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Wrong date format!")
                                .setContentText(taskName)
                                .build();
                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    } else {
                        q = "INSERT INTO zadachiOpit1(taskName, endDate, finishedDate) VALUES(?, ?, ?);";
                        db.execSQL(q, new Object[]{taskName, endDate, null});
                        db.close();
                        Toast.makeText(getApplicationContext(), "Task added successful!", Toast.LENGTH_LONG).show();
                        Notification notify = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Task added successful")
                                .setContentText(taskName)
                                .build();
                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    }
                } catch (SQLiteException e) {

                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Error while working with database!")
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                } catch (Exception e) {
                    addComfirmButton.setText("Errpr: " + e.getLocalizedMessage());
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM HH:mm");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException | java.text.ParseException pe) {
            return false;
        }


        return true;
    }
}
