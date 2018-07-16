package com.clikshow.FireBase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class BancoFire {

    public static void offline_user (final Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .getRoot().child("users").child(String.valueOf(sharedPreferences.getInt("id", 0)));
        Map<String, Object> map = new HashMap<>();
        map.put("online", false);
        databaseReference.updateChildren(map);
    }
}
