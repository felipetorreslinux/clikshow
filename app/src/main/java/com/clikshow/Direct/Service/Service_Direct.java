package com.clikshow.Direct.Service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;

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

import static com.clikshow.Direct.View_Direct.recylclerview_direct_conversas;

public class Service_Direct {

    static Activity activity;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    List<Rooms_Model> lis_rooms = new ArrayList<>();

    public Service_Direct(Activity activity){
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    public void lista_amigos(final List<Amigos_Model> lista_amigos, final RecyclerView recyclerView, final ViewStub loading_friends_direct){
        AndroidNetworking.get(APIServer.CLIKSOCIALPROD+"api/followers")
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try {
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray amigos = response.getJSONObject("content").getJSONArray("followers");
                            if(activity != null){
                                lista_amigos.clear();
                                DatabaseReference firebaseDatabase;
                                for (int i = 0; i < amigos.length(); i++){
                                    JSONObject jsonObject = amigos.getJSONObject(i);
                                    Amigos_Model amigos_model = new Amigos_Model(
                                            jsonObject.getInt("user_id"),
                                            jsonObject.getString("profile_pic_thumb"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("username"));
                                    lista_amigos.add(amigos_model);
                                    firebaseDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("users").child(String.valueOf(jsonObject.getInt("user_id")));
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id", String.valueOf(jsonObject.getInt("user_id")));
                                    map.put("isOnline", false);
                                    map.put("isDigiting", false);
                                    map.put("name", jsonObject.getString("name"));
                                    map.put("username", jsonObject.getString("username"));
                                    map.put("thumb", jsonObject.getString("profile_pic_thumb"));
                                    firebaseDatabase.setValue(map);
                                }
                                Amigos_Lista_Adapter amigos_lista_adapter = new Amigos_Lista_Adapter(activity, lista_amigos);
                                recyclerView.setAdapter(amigos_lista_adapter);
                                loading_friends_direct.setVisibility(View.GONE);
                            }
                            break;
                        default:
                            loading_friends_direct.setVisibility(View.GONE);
                    }
                }catch (JSONException e){

                }catch (NullPointerException e){

                }
            }

            @Override
            public void onError(ANError anError) {
                APIServer.error_server(activity, anError.getErrorCode());
                loading_friends_direct.setVisibility(View.VISIBLE);
            }
        });
    }

    public void get_rooms (final int id){
        lis_rooms.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("rooms");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String[] room = snapshot.getKey().split("_");
                    String me = String.valueOf(id);
                    if(room[0].equals(me)){
                        String users = room[1];
                        get_users_rooms(users);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void get_users_rooms (final String id){
        lis_rooms.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getChildrenCount() > 0){
                        if(snapshot.getKey().equals(id)){
                            Map<String, String> map = (Map) snapshot.getValue();
                            Rooms_Model rooms_model = new Rooms_Model(
                                    Integer.parseInt(map.get("id")),
                                    map.get("name"),
                                    map.get("username"),
                                    null,
                                    map.get("thumb"));
                            lis_rooms.add(rooms_model);
                        }
                    }
                }
                Adapter_Rooms_Direct adapter_rooms_direct = new Adapter_Rooms_Direct(activity, lis_rooms);
                recylclerview_direct_conversas.setAdapter(adapter_rooms_direct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void send_message (int sender, int receiver, String message, String type){
        if(sharedPreferences != null){
            int id = sharedPreferences.getInt("id", 0);
            if(id != 0){
                long time = new Date().getTime();
                databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("rooms").child(id+"_"+receiver).push();
                Map<String, Object> send = new HashMap<>();
                send.put("sender", id);
                send.put("receiver", receiver);
                send.put("message", message);
                send.put("create_at", time);
                databaseReference.setValue(send);

                databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("direct").child("rooms").child(receiver+"_"+id).push();
                Map<String, Object> receive = new HashMap<>();
                receive.put("sender", id);
                receive.put("receiver", receiver);
                receive.put("message", message);
                receive.put("create_at", time);
                databaseReference.setValue(receive);
            }
        }
    }

}
