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
}
