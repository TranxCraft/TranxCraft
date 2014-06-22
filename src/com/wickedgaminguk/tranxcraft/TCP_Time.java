package com.wickedgaminguk.tranxcraft;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TCP_Time {

    public String getTime() {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        String date = df.format(new Date());
        return date;
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(new Date());
        return date;
    }

    public String getLongDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        String date = sdf.format(new Date());
        return date;
    }

    public long getUnixTimestamp() {
        long unixTime = System.currentTimeMillis() / 1000L;
        return unixTime;
    }
}
