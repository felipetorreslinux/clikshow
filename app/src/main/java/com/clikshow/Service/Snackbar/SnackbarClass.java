package com.clikshow.Service.Snackbar;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarClass {

    public static void curto (View view, String mensagem){
        Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG).show();
    }

    public static void longo (View view, String mensagem){
        Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG).show();
    }
}
