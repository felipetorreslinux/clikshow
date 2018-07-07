package com.clikshow.FireBase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DirectFirebase {
    static SharedPreferences sharedPreferences;
    static DatabaseReference firebaseDatabase;

    public static void userOnline(final Activity activity){
        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if(sharedPreferences != null){
            int id = sharedPreferences.getInt("id", 0);
            if(id != 0){
                firebaseDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("users").child(String.valueOf(id));
                Map<String, Object> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                map.put("isOnline", true);
                map.put("isDigiting", false);
                map.put("username", sharedPreferences.getString("username", null));
                map.put("thumb", sharedPreferences.getString("profile_pic", null));
                firebaseDatabase.setValue(map);

            }
        }
    }

    public static void userOffline(final Activity activity){
        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if(sharedPreferences != null){
            int id = sharedPreferences.getInt("id", 0);
            if(id != 0){
                firebaseDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("users").child(String.valueOf(id));
                Map<String, Object> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                map.put("isOnline", false);
                map.put("isDigiting", false);
                map.put("username", sharedPreferences.getString("username", null));
                map.put("thumb", sharedPreferences.getString("profile_pic", null));
                firebaseDatabase.updateChildren(map);

            }
        }
    }

    public static void userDigitingOn(final Activity activity){
        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if(sharedPreferences != null){
            int id = sharedPreferences.getInt("id", 0);
            if(id != 0){
                firebaseDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("users").child(String.valueOf(id));
                Map<String, Object> map = new HashMap<>();
                map.put("isDigiting", true);
                firebaseDatabase.updateChildren(map);
            }
        }
    }

    public static void userDigitingOff(final Activity activity){
        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if(sharedPreferences != null){
            int id = sharedPreferences.getInt("id", 0);
            if(id != 0){
                firebaseDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("users").child(String.valueOf(id));
                Map<String, Object> map = new HashMap<>();
                map.put("isDigiting", false);
                firebaseDatabase.updateChildren(map);
            }
        }
    }
}
