package com.clikshow.Direct.Service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
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
import com.clikshow.Direct.View_Direct;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Progress_Alert;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.clikshow.FireBase.NotificationFireBase.sendNotifi;

public class Service_Direct {

    static Activity activity;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;

    public Service_Direct(Activity activity){
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    public void profile_add_online (int id){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("users").child(String.valueOf(id));
        Map<String, Object> map = new HashMap<>();
        map.put("id", String.valueOf(sharedPreferences.getInt("id", 0)));
        map.put("token_firebase", FirebaseInstanceId.getInstance().getToken());
        map.put("name", sharedPreferences.getString("name", null));
        map.put("online", String.valueOf(false));
        map.put("username", sharedPreferences.getString("username", null));
        map.put("thumb", sharedPreferences.getString("profile_pic", ""));
        map.put("online", String.valueOf(true));
        map.put("timestamp", String.valueOf(new Date().getTime()));
        databaseReference.updateChildren(map);
    }

    public void profile_remove_online (int id){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("users").child(String.valueOf(id));
        Map<String, Object> map = new HashMap<>();
        map.put("online", String.valueOf(false));
        databaseReference.updateChildren(map);
    }

    public void lista_usuarios_online(final RecyclerView recyclerView){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("users");
        databaseReference.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Usuarios_Online_Model> lista_usuarios_online = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getChildrenCount() > 0){
                        if(snapshot.child("online").exists()){
                            String online = (String) snapshot.child("online").getValue();
                            if(online.equals("true")){
                                Usuarios_Online_Model model = new Usuarios_Online_Model();
                                model = snapshot.getValue(Usuarios_Online_Model.class);
                                lista_usuarios_online.add(model);
                            }
                        }
                    }
                }
                Usuarios_Online_Adapter adapter = new Usuarios_Online_Adapter(activity, lista_usuarios_online);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void lista_salas_conversas (final RecyclerView recyclerView){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("rooms").child(String.valueOf(sharedPreferences.getInt("id", 0)));
        databaseReference.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Rooms_Model> lista_salas_conversas = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Rooms_Model model = new Rooms_Model();
                    model = snapshot.getValue(Rooms_Model.class);
                    lista_salas_conversas.add(model);
                }
                Adapter_Rooms_Direct adapter_rooms_direct = new Adapter_Rooms_Direct(activity, lista_salas_conversas);
                recyclerView.setAdapter(adapter_rooms_direct);
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
                                for (int i = 0; i < amigos.length(); i++){
                                    JSONObject jsonObject = amigos.getJSONObject(i);
                                    Amigos_Model amigos_model = new Amigos_Model(
                                            String.valueOf(jsonObject.getInt("user_id")),
                                            jsonObject.getString("profile_pic_thumb"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("username"));
                                    lista_amigos.add(amigos_model);
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("users").child(String.valueOf(jsonObject.getInt("user_id")));
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id", String.valueOf(jsonObject.getInt("user_id")));
                                    map.put("token_firebase", FirebaseInstanceId.getInstance().getToken());
                                    map.put("name", jsonObject.getString("name"));
                                    map.put("username", jsonObject.getString("username"));
                                    map.put("thumb", jsonObject.getString("profile_pic_thumb"));
                                    databaseReference.updateChildren(map);
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

    public void send_message (String receiver, String message, String type, String friend_id, String friend_name, String friend_username, String friend_thumb){
        String time = String.valueOf(new Date().getTime());

        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("rooms").child(receiver).child(String.valueOf(sharedPreferences.getInt("id", 0)));
        Map<String, Object> me = new HashMap<>();
        me.put("id", String.valueOf(sharedPreferences.getInt("id", 0)));
        me.put("thumb", String.valueOf(sharedPreferences.getString("profile_pic", "")));
        me.put("name", String.valueOf(sharedPreferences.getString("name", "")));
        me.put("username", sharedPreferences.getString("profile_pic", ""));
        me.put("last_message", message);
        me.put("timestamp", String.valueOf(time));
        databaseReference.updateChildren(me);

        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("rooms").child(String.valueOf(sharedPreferences.getInt("id", 0))).child(receiver);
        Map<String, Object> friend = new HashMap<>();
        friend.put("id", friend_id);
        friend.put("thumb", friend_thumb);
        friend.put("name", friend_name);
        friend.put("username", friend_username);
        friend.put("last_message", message);
        friend.put("timestamp", String.valueOf(time));
        databaseReference.updateChildren(friend);

        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("messages").child(String.valueOf(sharedPreferences.getInt("id", 0))).child(receiver).push();
        Map<String, String> send = new HashMap<>();
        send.put("sender", String.valueOf(sharedPreferences.getInt("id", 0)));
        send.put("receiver", receiver);
        send.put("message", message);
        send.put("read", String.valueOf(false));
        send.put("create_at", time);
        send.put("type", type);
        databaseReference.setValue(send);

        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("messages").child(receiver).child(String.valueOf(sharedPreferences.getInt("id", 0))).push();
        Map<String, String> receive = new HashMap<>();
        receive.put("sender", String.valueOf(sharedPreferences.getInt("id", 0)));
        receive.put("receiver", receiver);
        receive.put("message", message);
        receive.put("read", String.valueOf(false));
        receive.put("create_at", time);
        receive.put("type", type);
        databaseReference.setValue(receive);


        send_notifications(receiver, String.valueOf(sharedPreferences.getString("name", "")), message );

    }

    public void chat (String receiver, final List<Conversa_Model> lista_conversa, final RecyclerView recyclerView){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("messages").child(String.valueOf(sharedPreferences.getInt("id", 0))).child(receiver);
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

    public void send_all_direct (final String message, final String type){
        DatabaseReference users = FirebaseDatabase.getInstance().getReference().getRoot().child("users");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                        long time = new Date().getTime();
                        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("rooms").child(snapshot.child("id").getValue().toString()).child(String.valueOf(sharedPreferences.getInt("id", 0)));
                        Map<String, Object> me = new HashMap<>();
                        me.put("id", String.valueOf(sharedPreferences.getInt("id", 0)));
                        me.put("thumb", String.valueOf(sharedPreferences.getString("profile_pic", "")));
                        me.put("name", String.valueOf(sharedPreferences.getString("name", "")));
                        me.put("username", sharedPreferences.getString("profile_pic", ""));
                        me.put("last_message", message);
                        me.put("timestamp", String.valueOf(time));
                        databaseReference.updateChildren(me);

                        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("rooms").child(String.valueOf(sharedPreferences.getInt("id", 0))).child(snapshot.child("id").getValue().toString());
                        Map<String, Object> friend = new HashMap<>();
                        friend.put("id", snapshot.child("id").getValue().toString());
                        friend.put("thumb", snapshot.child("thumb").getValue().toString());
                        friend.put("name", snapshot.child("name").getValue().toString());
                        friend.put("username", snapshot.child("username").getValue().toString());
                        friend.put("last_message", message);
                        friend.put("timestamp", String.valueOf(time));
                        databaseReference.updateChildren(friend);

                        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("messages").child(snapshot.child("id").getValue().toString()).child(String.valueOf(sharedPreferences.getInt("id", 0))).push();
                        Map<String, Object> send = new HashMap<>();
                        send.put("sender", String.valueOf(sharedPreferences.getInt("id", 0)));
                        send.put("receiver", snapshot.child("id").getValue().toString());
                        send.put("message", message);
                        send.put("read", String.valueOf(false));
                        send.put("create_at", String.valueOf(new Date().getTime()));
                        send.put("type", type);
                        databaseReference.setValue(send);

                        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("messages").child(String.valueOf(sharedPreferences.getInt("id", 0))).child(snapshot.child("id").getValue().toString()).push();
                        Map<String, Object> send1 = new HashMap<>();
                        send1.put("sender", String.valueOf(sharedPreferences.getInt("id", 0)));
                        send1.put("receiver", snapshot.child("id").getValue().toString());
                        send1.put("message", message);
                        send1.put("read", String.valueOf(false));
                        send1.put("create_at", String.valueOf(new Date().getTime()));
                        send1.put("type", type);
                        databaseReference.setValue(send1);
                    }
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ToastClass.curto(activity, databaseError.getMessage());
            }
        });
    }

    public void send_notifications (final String id, final String title, final String message){
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("users").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map) dataSnapshot.getValue();
                try{
                    JSONObject notifi = new JSONObject();
                    JSONObject dados = new JSONObject();
                    JSONObject data = new JSONObject();
                    data.put("type", "direct");
                    data.put("id", id);
                    dados.put("title", title);
                    dados.put("body", message);
                    notifi.put("to", map.get("token_firebase"));
                    notifi.put("notification", dados);
                    notifi.put("data", data);
                    sendNotifi(notifi);
                }catch (JSONException e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
