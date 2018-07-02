package com.clikshow.Service.Toast;

import android.app.Activity;
import android.widget.Toast;

public class ToastClass {

    public static void longo(Activity activity, String mensagem){
        Toast.makeText(activity, mensagem, Toast.LENGTH_LONG).show();
    }

    public static void curto(Activity activity, String mensagem){
        Toast.makeText(activity, mensagem, Toast.LENGTH_SHORT).show();
    }
}
