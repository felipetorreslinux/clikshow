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

    public void sendMessage (int receiver, String editText){
        if(sharedPreferences != null){
            int id = sharedPreferences.getInt("id", 0);
            if(id != 0){
                long time = new Date().getTime();
                firebaseDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("rooms").child(String.valueOf(id)).child(String.valueOf(receiver)).child(String.valueOf(time));
                Map<String, Object> send = new HashMap<>();
                send.put("sender", id);
                send.put("receiver", receiver);
                send.put("message", editText);
                send.put("create_at", time);
                firebaseDatabase.setValue(send);

                firebaseDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("rooms").child(String.valueOf(receiver)).child(String.valueOf(id)).child(String.valueOf(time));
                Map<String, Object> receive = new HashMap<>();
                receive.put("sender", id);
                receive.put("receiver", receiver);
                receive.put("message", editText);
                receive.put("create_at", time);
                firebaseDatabase.setValue(receive);
            }
        }
    }

    public void list_chat_direct (final Activity activity, final List<Rooms_Model> lista_rooms, final RecyclerView recyclerView){
        int id = sharedPreferences.getInt("id", 0);
        lista_rooms.clear();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("rooms").child(String.valueOf(id));
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        infoUsers(activity, snapshot.getKey(),lista_rooms, recyclerView);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                APIServer.error_server(activity, databaseError.getCode());
            }
        });
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

    public void chat_room (final Activity activity, final String sender, final String receiver){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("rooms");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> arrayList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.child(receiver).child(sender).getChildren()){
                    Map<String, String> map = (Map) snapshot.getValue();
                    String id = map.get("sender");
                    if(id.equals(sender)){
                        System.out.println(map.get("message"));
                    }else{
                        System.out.println(map.get("message"));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
