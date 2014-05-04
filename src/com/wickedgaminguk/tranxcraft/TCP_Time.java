package com.wickedgaminguk.tranxcraft;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TCP_Time {

    public static final int second = 1;
    public static final int minute = second * 60;
    public static final int hour = minute * 60;
    public static final int day = hour * 24;
    public static final int week = day * 7;
    public static final int month = week * 4;
    public static final int year = month * 12;

    public static String getDate() {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        String date = df.format(new Date());
        return date;
    }

    public static String getLongDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        String date = sdf.format(new Date());
        return date;
    }

    public static long getUnixTimestamp() {
        long unixTime = System.currentTimeMillis() / 1000L;
        return unixTime;
    }
}
