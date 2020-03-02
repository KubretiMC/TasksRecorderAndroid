package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowDeleteActivity extends AppCompatActivity {


    protected TextView res;
    protected Button deleteTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_delete);


        final ArrayList<String> listResults = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_list_view,
                R.id.textView,
                listResults
        );
        final ListView simpleList = findViewById(R.id.simpleListView);

        ShowPendingTasks(listResults, arrayAdapter,simpleList);
        deleteTaskButton = findViewById(R.id.DeleteTaskButton);
        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletePendingTask();
            }
        });

    }

    protected void ShowPendingTasks(ArrayList<String> listResults, ArrayAdapter<String> arrayAdapter,ListView simpleList) {
        res = findViewById(R.id.result);

        String q, resultText;

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

            db.close();


            simpleList.setAdapter(arrayAdapter);


        } catch (SQLiteException e) {
            res.setText("Грешка при работа с БД: " + e.getLocalizedMessage() + e.getStackTrace());

        }
    }


    protected void DeletePendingTask() {
        res = findViewById(R.id.result);

        String q, resultText;

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "zadachiOpit2.db", null);
        db.delete("zadachiOpit2", "taskName=?", new String[]{"Train"});
        db.close();
    }
}
