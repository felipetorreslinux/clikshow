package com.clikshow.FireBase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.Comments.View_Comments;
import com.clikshow.Direct.View_Chat_Direct;
import com.clikshow.Direct.View_Direct;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Views.View_Principal;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public class NotificationFireBase extends FirebaseMessagingService  {

    private static String onTokenRefresh() {
        String token_firebase = null;
        token_firebase = FirebaseInstanceId.getInstance().getToken();
        return token_firebase;
    };

    public static void sendNotifi(JSONObject jsonObject){
        AndroidNetworking.post("https://fcm.googleapis.com/fcm/send")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "key=AIzaSyCa4bLEd1qWf7yH8wA99XzB_cVZwQSS35A")
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {}
                    @Override
                    public void onError(ANError anError) {}
                });
    };

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            notification_message(remoteMessage);
        }
    }

    private void notification_message(RemoteMessage remoteMessage){
        switch (remoteMessage.getData().get("type")){

            case "likes":
            Intent intent_likes = new Intent(this, View_Comments.class);
            intent_likes.putExtra("event_id", Integer.parseInt(remoteMessage.getData().get("event_id")));
            intent_likes.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntentlikes = PendingIntent.getActivity(this, 0, intent_likes, PendingIntent.FLAG_ONE_SHOT);
            String channel_likes = getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri_likes = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder_likes = new NotificationCompat.Builder(this, channel_likes)
                    .setSmallIcon(R.drawable.ic_logo_splash)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri_likes)
                    .setContentIntent(pendingIntentlikes);
            NotificationManager notificationManager_likes = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channel_likes,
                        "clikshow_app",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager_likes.createNotificationChannel(channel);
            }
            notificationManager_likes.notify(Integer.parseInt(remoteMessage.getData().get("event_id")), notificationBuilder_likes.build());
            break;


            case "direct":
                if(View_Chat_Direct.active == false){
                    Intent intent = new Intent(this, View_Direct.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    String string = getString(R.string.default_notification_channel_id);
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, string)
                            .setSmallIcon(R.drawable.ic_logo_splash)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification().getBody())
                            .setAutoCancel(true)
                            .setSound(uri)
                            .setContentIntent(pendingIntent);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(string,
                                "clikshow_app",
                                NotificationManager.IMPORTANCE_LOW);
                        notificationManager.createNotificationChannel(channel);
                    }
                    notificationManager.notify(Integer.parseInt(remoteMessage.getData().get("id")), builder.build());
                }
                break;
        }
    };

    public static void send_push_comments(String title, String message, JSONObject data){
        try{
            JSONObject notifi = new JSONObject();
            JSONObject dados = new JSONObject();
            dados.put("title", title);
            dados.put("body", message);
            notifi.put("to", "/topics/comments");
            notifi.put("notification", dados);
            notifi.put("data", data);
            sendNotifi(notifi);
        }catch (JSONException e){}catch (NullPointerException e) {}
    }

    public static void send_push_click (String title, String message, JSONObject data){
        try{
            JSONObject notifi = new JSONObject();
            JSONObject dados = new JSONObject();
            dados.put("title", title);
            dados.put("body", message);
            notifi.put("to", "/topics/likes");
            notifi.put("notification", dados);
            notifi.put("data", data);
            sendNotifi(notifi);
        }catch (JSONException e){}catch (NullPointerException e){};
    }

    public static void send_push_like_comments (final String title, final String message, final JSONObject data){
        try{
            String id = String.valueOf(data.get("user_id"));
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .getRoot().child("users").child(id);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> map = (Map)dataSnapshot.getValue();
                    try{
                        JSONObject notifi = new JSONObject();
                        JSONObject dados = new JSONObject();
                        dados.put("title", title);
                        dados.put("body", message);
                        notifi.put("to", map.get("token_firebase"));
                        notifi.put("notification", dados);
                        notifi.put("data", data);
                        sendNotifi(notifi);
                    }catch (JSONException e){}
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }catch (JSONException e){}catch (NullPointerException e){}
    }
}