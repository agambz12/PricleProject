package com.example.myapplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtils {

    public static String dateToStr(long date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }
}
