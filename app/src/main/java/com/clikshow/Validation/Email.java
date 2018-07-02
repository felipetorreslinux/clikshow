package com.clikshow.Validation;

import android.app.Activity;
import android.widget.EditText;

public class Email {
    public static boolean valid (final Activity activity, EditText email){
        String email_check = email.getText().toString().trim();
        if(email_check.indexOf("@") == -1 || email_check.indexOf(".") == -1){
            return false;
        }else{
            return true;
        }
    }
}
