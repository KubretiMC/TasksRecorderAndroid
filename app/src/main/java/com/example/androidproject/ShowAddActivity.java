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

import java.util.Date;

public class ShowAddActivity extends AppCompatActivity {
    //TODO Трябва да видим какви символи може да направят проблем и да ги забраним, например вмомента ако във наме се въеведе същото нещото като дате ще направим проблеми
    //TODO после като работи със стринговете в delete, достатъчно е да се забрани ":" например. Също да направим полето за add да си пише тиретата и : само.

    private EditText addTaskName;
    private EditText addEndDate;
    private Button addComfirmButton;


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
                    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "zadachiOpit2.db", null);
                    String q = "CREATE TABLE if not exists zadachiopit2(";
                    q += "ID integer primary key AUTOINCREMENT, ";
                    q += "taskName text not null, ";
                    q += "endDate date not null, ";
                    q += "finishedDate date);";
                    db.execSQL(q);
                    String taskName = addTaskName.getText().toString().trim();
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
                    } else if(!isDateLegit(endDate))
                    {
                        Toast.makeText(getApplicationContext(), "Date out of range!", Toast.LENGTH_LONG).show();
                        Notification notify = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Date out of range!")
                                .build();
                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    }
                    else {
                        q = "INSERT INTO zadachiOpit2(taskName, endDate) VALUES(?, ?);";
                        db.execSQL(q, new Object[]{taskName, endDate});
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
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Error while working with database!")
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        Date currentDate = new Date();
        Date date1;
        try {
            date1 = dateFormat.parse(inDate.trim());
        } catch (ParseException | java.text.ParseException pe) {
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean isDateLegit(String inDate) throws java.text.ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        Date currentDate = new Date();
        Date inputDate;
        inputDate = dateFormat.parse(inDate.trim());

        Date lastDate = dateFormat.parse("2099-12-31");

        if (inputDate.compareTo(currentDate)<0) {
            return false;
        }
        else if(inputDate.compareTo(lastDate)>0)
        {
            return false;
        }
        return true;
    }
}
