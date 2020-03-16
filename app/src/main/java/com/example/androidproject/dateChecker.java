package com.example.androidproject;

import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Date;

public class dateChecker {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
        dateFormat.setLenient(false);
        Date currentDate = new Date();
        Date date1;
        try {
            date1 = dateFormat.parse(inDate.trim());
        } catch (ParseException | java.text.ParseException pe) {
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean isDateLegit(String inDate) throws java.text.ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
        dateFormat.setLenient(false);

        Date today = new Date();
        Date todayWithZeroTime = dateFormat.parse(dateFormat.format(today));




        Date inputDate;
        inputDate = dateFormat.parse(inDate.trim());

        Date lastDate = dateFormat.parse("2099-12-31");
        if(inputDate.compareTo(todayWithZeroTime)==0)
        {
            return true;
        }
        if (inputDate.compareTo(todayWithZeroTime)<0) {
            return false;
        }
        else if(inputDate.compareTo(lastDate)>0)
        {
            return false;
        }
        return true;
    }
}
