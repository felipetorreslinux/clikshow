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
import com.clikshow.Direct.Adapter.Conversa_Room_Adapter;
import com.clikshow.Direct.Adapter.Usuarios_Online_Adapter;
import com.clikshow.Direct.Models.Amigos_Model;
import com.clikshow.Direct.Models.Conversa_Model;
import com.clikshow.Direct.Models.Rooms_Model;
import com.clikshow.Direct.Models.Usuarios_Online_Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service_Direct {

    static Activity activity;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;

    public Service_Direct(Activity activity){
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    public void profile_add_online (int id){
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("users").child(String.valueOf(id));
        Map<String, Object> map = new HashMap<>();
        map.put("id", sharedPreferences.getInt("id", 0));
        map.put("token_firebase", FirebaseInstanceId.getInstance().getToken());
        map.put("name", sharedPreferences.getString("name", null));
        map.put("online", false);
        map.put("username", sharedPreferences.getString("username", null));
        map.put("thumb", sharedPreferences.getString("profile_pic", ""));
        map.put("online", true);
        databaseReference.updateChildren(map);
    }

    public void profile_remove_online (int id){
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("users").child(String.valueOf(id));
        Map<String, Object> map = new HashMap<>();
        map.put("online", false);
        databaseReference.updateChildren(map);
    }

    public void lista_usuarios_online(final List<Usuarios_Online_Model> lista_usuarios_online, final RecyclerView recyclerView){
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_usuarios_online.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getChildrenCount() > 0){
                        if(snapshot.child("online").exists()){
                            boolean online = (boolean) snapshot.child("online").getValue();
                            if(online == true){
                                Usuarios_Online_Model usuarios_online_model = new Usuarios_Online_Model(
                                        snapshot.child("id").getValue().toString(),
                                        snapshot.child("name").getValue().toString(),
                                        snapshot.child("username").getValue().toString(),
                                        snapshot.child("thumb").getValue().toString());
                                lista_usuarios_online.add(usuarios_online_model);
                            }
                        }
                    }
                }
                Usuarios_Online_Adapter usuarios_online_adapter = new Usuarios_Online_Adapter(activity, lista_usuarios_online);
                recyclerView.setAdapter(usuarios_online_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void lista_salas_conversas (final List<Rooms_Model> lista_salas_conversas, final RecyclerView recyclerView){
        final int id = sharedPreferences.getInt("id", 0);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("messages").child(String.valueOf(id));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("users").child(snapshot.getKey());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Rooms_Model rooms_model = new Rooms_Model(
                                    dataSnapshot.child("id").getValue().toString(),
                                    dataSnapshot.child("name").getValue().toString(),
                                    dataSnapshot.child("username").getValue().toString(),
                                    dataSnapshot.child("thumb").getValue().toString());
                            lista_salas_conversas.add(rooms_model);
                            Adapter_Rooms_Direct adapter = new Adapter_Rooms_Direct(activity, lista_salas_conversas);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                                            String.valueOf(jsonObject.getInt("user_id")),
                                            jsonObject.getString("profile_pic_thumb"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("username"));
                                    lista_amigos.add(amigos_model);
                                    firebaseDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("users").child(String.valueOf(jsonObject.getInt("user_id")));
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id", String.valueOf(jsonObject.getInt("user_id")));
                                    map.put("token_firebase", FirebaseInstanceId.getInstance().getToken());
                                    map.put("name", jsonObject.getString("name"));
                                    map.put("username", jsonObject.getString("username"));
                                    map.put("thumb", jsonObject.getString("profile_pic_thumb"));
                                    firebaseDatabase.updateChildren(map);
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

    public void send_message (String receiver, String message, String type){
        int id = sharedPreferences.getInt("id", 0);
        long time = new Date().getTime();
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("messages").child(String.valueOf(id)).child(receiver).push();
        Map<String, Object> send = new HashMap<>();
        send.put("sender", id);
        send.put("receiver", receiver);
        send.put("message", message);
        send.put("create_at", time);
        send.put("type", type);
        databaseReference.setValue(send);

        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("messages").child(receiver).child(String.valueOf(id)).push();
        Map<String, Object> receive = new HashMap<>();
        receive.put("sender", id);
        receive.put("receiver", receiver);
        receive.put("message", message);
        receive.put("create_at", time);
        receive.put("type", type);
        databaseReference.setValue(receive);
    }

    public void chat (String receiver, final List<Conversa_Model> lista_conversa, final RecyclerView recyclerView){
        String me = String.valueOf(sharedPreferences.getInt("id", 0));
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("messages").child(me).child(receiver);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_conversa.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map) snapshot.getValue();
                    Conversa_Model conversa_model = new Conversa_Model(
                            String.valueOf(map.get("create_at")),
                            map.get("message"),
                            map.get("receiver"),
                            String.valueOf(map.get("sender")));
                    lista_conversa.add(conversa_model);
                }
                Conversa_Room_Adapter adapter = new Conversa_Room_Adapter(activity, lista_conversa);
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
