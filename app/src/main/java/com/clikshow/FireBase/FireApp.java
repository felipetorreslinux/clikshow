package com.clikshow.FireBase;

import android.app.Application;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.FirebaseApp;

import java.util.Map;

public class FireApp extends Application {

    private static Firebase firebase;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

    public static Firebase getFirebase(){
        if( firebase == null ){
            firebase = new Firebase("https://clikshow-3aa9a.firebaseio.com/");
        }
        return( firebase );
    }
}
