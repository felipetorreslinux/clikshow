package com.clikshow.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.clikshow.R;

public class Loading {

    private static AlertDialog dialog_loading;
    private static AlertDialog.Builder builder_loading;
    private static View view;

    public static void open (final Activity activity){
        builder_loading = new AlertDialog.Builder(activity);
        view = activity.getLayoutInflater().inflate(R.layout.view_loading, null);
        builder_loading.setView(view);
        builder_loading.setCancelable(false);
        dialog_loading = builder_loading.create();
        dialog_loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_loading.show();
    };

    public static void close (){
        dialog_loading.dismiss();
    };
}
