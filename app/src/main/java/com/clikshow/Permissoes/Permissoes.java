package com.clikshow.Permissoes;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

public class Permissoes {
    public static void accept (Activity activity){
        ActivityCompat.requestPermissions(activity,
                new String[]{
                        Manifest.permission.CAMERA
                }, 1);
    };
}
