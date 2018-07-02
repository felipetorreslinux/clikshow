package com.clikshow.Utils;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

public class Relogio {
    public static void start (TextView relogio) {
        Date date = new Date();
        String response = new SimpleDateFormat("HH:mm:ss").format(date);
        relogio.setText(response);
    };

    public static String hora () {
        Date date = new Date();
        String response = new SimpleDateFormat("dd'/'MM'/'yyyy ' - ' HH:mm:ss").format(date);
        return response;
    };
}
