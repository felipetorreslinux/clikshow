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

    public static void add_user (final Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .getRoot().child("users").child(String.valueOf(sharedPreferences.getInt("id", 0)));
        Map<String, Object> map = new HashMap<>();
        map.put("id", sharedPreferences.getInt("id", 0));
        map.put("token_firebase", FirebaseInstanceId.getInstance().getToken());
        map.put("name", sharedPreferences.getString("name", null));
        map.put("username", sharedPreferences.getString("username", null));
        map.put("email", sharedPreferences.getString("email", null));
        map.put("cellphone", sharedPreferences.getString("cellphone", null));
        map.put("profile_pic", sharedPreferences.getString("profile_pic", null));
        map.put("isOnline", true);
        databaseReference.setValue(map);
    };

    public static void offline_user (final Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .getRoot().child("users").child(String.valueOf(sharedPreferences.getInt("id", 0)));
        Map<String, Object> map = new HashMap<>();
        map.put("isOnline", false);
        databaseReference.updateChildren(map);
    }
}
