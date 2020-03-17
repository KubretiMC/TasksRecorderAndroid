package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button ShowPendingButton;
    private Button ShowPreviousbutton;
    private Button ShowAddButton;
    private Button ShowDeleteButton;
    private Button ShowEditButton;

    public static final String TAG_MY_WORK = "mywork";

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

        ShowPreviousbutton = findViewById(R.id.ShowPrevious);
        ShowPreviousbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenShowPreviousActivity();
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
}
