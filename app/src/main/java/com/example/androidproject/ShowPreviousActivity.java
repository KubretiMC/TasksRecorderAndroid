package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowPreviousActivity extends AppCompatActivity {

    private TextView res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_previous);
        ShowPreviousTasks();
    }

    private void ShowPreviousTasks() {
        res = findViewById(R.id.result);
        final ListView simpleList = findViewById(R.id.simpleListView);
        String q, resultText;
        ArrayList<String> listResults = new ArrayList<>();
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "zadachiOpit2.db", null);
            q = "SELECT * FROM zadachiopit2 WHERE finishedDate is not null";
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
