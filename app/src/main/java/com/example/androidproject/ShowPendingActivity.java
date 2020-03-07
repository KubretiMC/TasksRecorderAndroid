package com.example.androidproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ShowPendingActivity extends AppCompatActivity {

    private TextView res;
    private ListView simpleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pending);
        ShowPendingTasks();

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);

                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                res = findViewById(R.id.result);

                String[] parts = item.split(" ");

                String taskName = parts[0];
                String taskDate = parts[parts.length-1];

                String dadada=parts[1];

                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "zadachiOpit2.db", null);

                ContentValues cv = new ContentValues();
                cv.put("finishedDate",date);
                db.update("zadachiOpit2", cv, "taskName=? and endDate=?", new String[]{taskName, taskDate});
                db.close();

                ShowPendingTasks();
                Toast.makeText(ShowPendingActivity.this,"You finished task:"+taskName+" on date:"+taskDate, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void ShowPendingTasks() {
        res = findViewById(R.id.result);
        simpleList= findViewById(R.id.simpleListView);
        String q, resultText;
        ArrayList<String> listResults = new ArrayList<>();
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "zadachiOpit2.db", null);
            q = "SELECT * FROM zadachiopit2 WHERE finishedDate is null";
            Cursor c = db.rawQuery(q, null);
            resultText = "Задача\t Краен Срок\n";
            while (c.moveToNext()) {
                String taskName = c.getString(c.getColumnIndex("taskName"));
                String endDate = c.getString(c.getColumnIndex("endDate"));
                resultText += taskName + " \t " + endDate + "\n";
                listResults.add(taskName + " \t " + endDate);
            }
            c.close();
            db.close();

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    ShowPendingActivity.this,
                    android.R.layout.simple_list_item_single_choice,
                    listResults
            );


            simpleList.setAdapter(arrayAdapter);
            simpleList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        } catch (SQLiteException e) {
            res.setText("Грешка при работа с БД: " + e.getLocalizedMessage() + e.getStackTrace());
        }

    }
}