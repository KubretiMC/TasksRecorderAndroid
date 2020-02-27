package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button ShowPendingButton;
    private Button ShowPreviousbutton;
    private Button ShowAddButton;
    private Button ShowDeleteButton;
    private Button ShowEditButton;


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
    }

    public void OpenShowEditActivity() {
        Intent intent = new Intent(this, ShowEditActivity.class);
        startActivity(intent);
    }

    public void OpenShowDeleteActivity() {
        Intent intent = new Intent(this, ShowDeleteActivity.class);
        startActivity(intent);
    }

    public void OpenShowAddActivity() {
        Intent intent = new Intent(this, ShowAddActivity.class);
        startActivity(intent);
    }

    public void OpenShowPendingActivity() {
        Intent intent = new Intent(this, ShowPendingActivity.class);
        startActivity(intent);
    }

    public void OpenShowPreviousActivity() {
        Intent intent = new Intent(this, ShowPreviousActivity.class);
        startActivity(intent);
    }
}
