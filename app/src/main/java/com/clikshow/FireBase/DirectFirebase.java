package com.clikshow.FireBase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.Direct.Adapter.Adapter_Rooms_Direct;
import com.clikshow.Direct.Adapter.Amigos_Lista_Adapter;
import com.clikshow.Direct.Models.Amigos_Model;
import com.clikshow.Direct.Models.Rooms_Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectFirebase {
    SharedPreferences sharedPreferences;
    static DatabaseReference firebaseDatabase;

    public DirectFirebase(Activity context){
        this.sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    public void infoUsers (final Activity activity, String user, final List<Rooms_Model> lista_rooms, final RecyclerView recyclerView){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("users").child(user);
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map) dataSnapshot.getValue();
                try{
                    Rooms_Model rooms_model = new Rooms_Model(
                            Integer.parseInt(map.get("id")),
                            map.get("name"),
                            map.get("username"),
                            "",
                            map.get("thumb"));
                    lista_rooms.add(rooms_model);
                    Adapter_Rooms_Direct adapter_rooms_direct = new Adapter_Rooms_Direct(activity, lista_rooms);
                    recyclerView.setAdapter(adapter_rooms_direct);
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
