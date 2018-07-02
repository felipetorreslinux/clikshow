package com.clikshow.Service.SegurancaPrint;

import android.app.Activity;
import android.view.WindowManager;

public class ViewSecurity {
    public static void qrcode(final Activity activity){
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }
}
