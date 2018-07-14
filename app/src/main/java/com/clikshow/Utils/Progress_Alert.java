package com.clikshow.Utils;

import android.app.Activity;
import android.app.ProgressDialog;

public class Progress_Alert {
    static ProgressDialog progressDialog;
    public static void open (Activity activity, String title, String message){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void close (){
        progressDialog.dismiss();
    }

}
