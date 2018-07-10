package com.clikshow.Service;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Datas {

    public static String data(int data) {
        String response = null;
        Date date = new Date((long)data*1000);
        response = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy").format(date);
        return response;
    }

    public static String data_hora_evento_feed(int data) {
        String response = null;
        Date date = new Date((long)data*1000);
        response = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy '\u2022' HH'hs'").format(date);
        return response;
    }

    public static String data_bilheteria (int data) {
        String response = null;
        Date date = new Date((long)data*1000);
        response = new SimpleDateFormat("dd'/'MM '\u2022' HH'hs'").format(date);
        return response;
    }

    public static String data_extenso(int data) {
        String response = null;
        Date date = new Date((long)data*1000);
        response = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy").format(date);
        return response;
    }

    public static String time(int time) {
        String response = null;
        Date date = new Date((long)time*1000);
        response = new SimpleDateFormat("HH:mm:ss").format(date);
        return response;
    }

    public static String data_impressora(int data) {
        String response = null;
        Date date = new Date((long)data*1000);
        response = new SimpleDateFormat("dd'/'MM'/'yyyy").format(date);
        return response;
    }

    public static String hora_evento(int data) {
        String response = null;
        Date date = new Date((long)data*1000);
        response = new SimpleDateFormat("'Inicio as 'HH'hs'").format(date);
        return response;
    }

    public static String data_hora_aparelho (){
        String response = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd'/'MM'/'yyyy' - 'HH:mm:ss");
        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        response = dateFormat.format(data_atual);

        return response;
    };


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = currentDate().getTime();
        if (time > now || time <= 0) {
            return "in the future";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "agora";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "ha um minuto ";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return "ha "+diff / MINUTE_MILLIS + " minutos atrás";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "ha uma hora";
        } else if (diff < 24 * HOUR_MILLIS) {
            return "ha "+diff / HOUR_MILLIS + " horas atrás";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "ontem";
        } else {
            return "ha "+diff / DAY_MILLIS + " dias atrás";
        }
    }
}
