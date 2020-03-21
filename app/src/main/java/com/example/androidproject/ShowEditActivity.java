package com.example.androidproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowEditActivity extends AppCompatActivity {
    private TextView res;
    private Button editTaskButton;
    private EditText newTaskName;
    private EditText newTaskDate;
    private String dateString;
    private String nameString;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_edit);


        final ArrayList<String> listResults = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_list_view,
                R.id.textView,
                listResults
        );
        final ListView simpleList = findViewById(R.id.simpleListView);

        ShowPendingTasks(listResults, arrayAdapter, simpleList);

        newTaskName = findViewById(R.id.TaskNamePlainText);
        newTaskDate = findViewById(R.id.TaskDatePlainText);

        editTaskButton = findViewById(R.id.EditTaskButton);
        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (dateString != null && nameString != null) {
                    try {
                        EditTask(nameString, dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        String pattern = "([0-9]{4}-{1}[0-9]{1,2}-{1}[0-9]{1,2}$)";
        final Pattern r = Pattern.compile(pattern);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String rawString = listResults.get(position);
                Matcher m = r.matcher(rawString);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
                dateFormat.setLenient(false);

                if (m.find()) {
                    dateString = m.group(1);
                    nameString = rawString.replaceAll(dateString, "").trim();
                    Toast.makeText(ShowEditActivity.this, "you clicked on: " + nameString + " " + dateString, Toast.LENGTH_SHORT).show();

                    newTaskName.setText(nameString);
                    newTaskDate.setText(dateString);

                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void ShowPendingTasks(ArrayList<String> listResults, ArrayAdapter<String> arrayAdapter, ListView simpleList) {
        res = findViewById(R.id.result);

        String q;
        LocalDate today = LocalDate.now();
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "zadachiOpit2.db", null);
            q = "SELECT * FROM zadachiopit2 WHERE finishedDate is null and endDate>?";
            Cursor c = db.rawQuery(q, new String[]{String.valueOf(today)});
            while (c.moveToNext()) {
                String taskName = c.getString(c.getColumnIndex("taskName"));
                String endDate = c.getString(c.getColumnIndex("endDate"));
                listResults.add(taskName + " \t " + endDate);
            }
            c.close();
            db.close();

            simpleList.setAdapter(arrayAdapter);
        } catch (SQLiteException e) {
            res.setText("Грешка при работа с БД: " + e.getLocalizedMessage() + e.getStackTrace());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void EditTask(String name, String date) throws ParseException {
        String newDate = newTaskDate.getText().toString();
        String newName = newTaskName.getText().toString();

        dateChecker dateCheck = new dateChecker();

        if (!dateCheck.isValidDate(newDate)) {
            Toast.makeText(getApplicationContext(), "Wrong date format!", Toast.LENGTH_LONG).show();
            Notification notify = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Wrong date format!")
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
        } else if (!dateCheck.isDateLegit(newDate)) {
            Toast.makeText(getApplicationContext(), "Date out of range!", Toast.LENGTH_LONG).show();
            Notification notify = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Date out of range!")
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
        } else {
            res = findViewById(R.id.result);

            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "zadachiOpit2.db", null);
            ContentValues cv = new ContentValues();
            cv.put("taskName", newName);
            cv.put("endDate", newDate);
            db.update("zadachiOpit2", cv, "taskName=? and endDate=?", new String[]{name, date});
            Toast.makeText(ShowEditActivity.this, "Task updated successfully from " + nameString + " " + dateString + " to "+newName+" "+newDate, Toast.LENGTH_SHORT).show();

            db.close();

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}