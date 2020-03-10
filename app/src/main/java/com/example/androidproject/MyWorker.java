package com.example.androidproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MyWorker extends Worker {

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
        String formattedDate=dateFormat.format(currentTime);
        int counter=0;
        ArrayList data=getEndDate();
        for(int i=0;i<data.size();i++){
            if(formattedDate.equals(data.get(i))){
                counter++;
                break;
            }
        }

        if(counter!=0){
            sendNotification("Title", "You have tasks pending for today!");
            Log.d("foundMatch", "found match");
        }else{
            sendNotification("Title", "No tasks pending for today!");
            Log.d("noMatch", "no match");
        }




        return Result.success();
    }

    private void sendNotification(String title, String s) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification=new NotificationCompat.Builder(getApplicationContext(), "default")
                .setContentTitle(title)
                .setContentText(s)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
    }

    private ArrayList<String> getEndDate() {

        ArrayList<String> data=new ArrayList<String>();
        String q;

        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getApplicationContext().getFilesDir().getPath() + "/" + "zadachiOpit2.db", null);
            q = "SELECT * FROM zadachiopit2 where finishedDate is null";
            Cursor c = db.rawQuery(q, null);
            while (c.moveToNext()) {
                String endDate = c.getString(c.getColumnIndex("endDate"));
                data.add(endDate);

            }
            c.close();
            db.close();

        } catch (SQLiteException e) {

        }

        return data;
    }



}
