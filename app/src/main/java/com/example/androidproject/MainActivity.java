package com.example.androidproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button ShowPendingButton;
    private Button ShowPreviousButton;
    private Button ShowAddButton;
    private Button ShowDeleteButton;
    private Button ShowEditButton;
    private Button ShowExpiredButton;


    public static final String TAG_MY_WORK = "mywork";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShowPendingButton = findViewById(R.id.ShowPending);
        ShowPendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenShowPendingActivity();
            }
        });

        ShowPreviousButton = findViewById(R.id.ShowPrevious);
        ShowPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenShowPreviousActivity();
            }
        });

        ShowExpiredButton = findViewById(R.id.ShowExpired);
        ShowExpiredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenShowExpiredActivity();
            }
        });

        ShowAddButton = findViewById(R.id.ShowAdd);
        ShowAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                OpenShowAddActivity();
            }
        });

        ShowDeleteButton=findViewById(R.id.ShowDelete);
        ShowDeleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                OpenShowDeleteActivity();
            }
        });

        
        ShowEditButton=findViewById(R.id.ShowEdit);
        ShowEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenShowEditActivity();
            }
        });
        //DeleteExpiredTasks();
        startAlarm();
    }

    private void startAlarm(){

        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(Constraints.NONE)
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(TAG_MY_WORK, ExistingPeriodicWorkPolicy.REPLACE, work);
    }

    private void OpenShowEditActivity() {
        Intent intent = new Intent(this, ShowEditActivity.class);
        startActivity(intent);
    }

    private void OpenShowDeleteActivity() {
        Intent intent = new Intent(this, ShowDeleteActivity.class);
        startActivity(intent);
    }

    private void OpenShowAddActivity() {
        Intent intent = new Intent(this, ShowAddActivity.class);
        startActivity(intent);
    }

    private void OpenShowPendingActivity() {
        Intent intent = new Intent(this, ShowPendingActivity.class);
        startActivity(intent);
    }

    private void OpenShowPreviousActivity() {
        Intent intent = new Intent(this, ShowPreviousActivity.class);
        startActivity(intent);
    }

    private void OpenShowExpiredActivity() {
        Intent intent = new Intent(this, ShowExpiredActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void DeleteExpiredTasks() {
        LocalDate today = LocalDate.now();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/" + "zadachiOpit2.db", null);
            db.delete("zadachiOpit2", "endDate<?", new String[]{String.valueOf(today)});
            db.close();
    }
}
