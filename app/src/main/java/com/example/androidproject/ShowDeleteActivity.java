package com.example.androidproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowDeleteActivity extends AppCompatActivity {
    private TextView res;
    private Button deleteTaskButton;
    private String dateString;
    private String nameString;

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
                if(dateString!=null && nameString!=null ){
                    DeleteTask(nameString, dateString);
                    Intent intent= getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

        String pattern = "([0-9]{4}-{1}[0-9]{1,2}-{1}[0-9]{1,2}$)";
        final Pattern r=Pattern.compile(pattern);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String rawString=listResults.get(position);
                Matcher m= r.matcher(rawString);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
                dateFormat.setLenient(false);
                if(m.find()){
                    dateString=m.group(1);
                    nameString=rawString.replaceAll(dateString, "").trim();
                    Toast.makeText(ShowDeleteActivity.this,"you clicked on: " + nameString+" "+dateString, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ShowPendingTasks(ArrayList<String> listResults, ArrayAdapter<String> arrayAdapter,ListView simpleList) {
        res = findViewById(R.id.result);

        String q;

        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "zadachiOpit2.db", null);
            q = "SELECT * FROM zadachiopit2 where finishedDate is null";
            Cursor c = db.rawQuery(q, null);
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


    private void DeleteTask(String name,String date) {
        res = findViewById(R.id.result);

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "zadachiOpit2.db", null);
        db.delete("zadachiOpit2", "taskName=? and endDate=?", new String[]{name, date});
        Toast.makeText(ShowDeleteActivity.this,"Task deleted successfully" + nameString+" "+dateString, Toast.LENGTH_SHORT).show();

        db.close();
    }
}
