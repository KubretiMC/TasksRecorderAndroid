package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ShowPreviousActivity extends AppCompatActivity {

    private TextView res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_previous);

        CalendarView calender;
        calender = (CalendarView)findViewById(R.id.calendarView);
        final String[] currentSelectedDate = new String[1];
        currentSelectedDate[0] =new String("2020-01-01");
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month++;
                currentSelectedDate[0] =new String(year+"-"+month+"-"+dayOfMonth).trim();
                Toast.makeText(ShowPreviousActivity.this,currentSelectedDate[0], Toast.LENGTH_SHORT).show();
                ShowPreviousTasks(currentSelectedDate[0]);

            }
        });
        ShowPreviousTasks(currentSelectedDate[0]);



    }

    private void ShowPreviousTasks(String fDate) {
        res = findViewById(R.id.result);
        final ListView simpleList = findViewById(R.id.simpleListView);
        String q, resultText;
        ArrayList<String> listResults = new ArrayList<>();
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "zadachiOpit2.db", null);
            q = "SELECT * FROM zadachiopit2 WHERE finishedDate =?";
            Cursor c = db.rawQuery(q, new String[] {fDate});
            resultText = "Задача\t Краен Срок\n";
            while (c.moveToNext()) {
                String taskName = c.getString(c.getColumnIndex("taskName"));
                String finishedDate = c.getString(c.getColumnIndex("finishedDate"));
                resultText += taskName + " \t " + finishedDate + "\n";
                listResults.add(taskName + " \t " +finishedDate);
            }
            c.close();
            db.close();

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.activity_list_view,
                    R.id.textView,
                    listResults
            );
            simpleList.setAdapter(arrayAdapter);


        } catch (SQLiteException e) {
            res.setText("Грешка при работа с БД: " + e.getLocalizedMessage() + e.getStackTrace());

        }
    }
}
