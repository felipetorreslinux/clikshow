package com.clikshow.Comments.Service;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.clikshow.FireBase.NotificationFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Service_Comments {

    static DatabaseReference databaseReference;

    public static void count_likes (int event_id, final TextView textView){
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("likes").child(String.valueOf(event_id));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 1){
                    textView.setText(String.valueOf(dataSnapshot.getChildrenCount())+" clik");
                }else if(dataSnapshot.getChildrenCount() > 1){
                    textView.setText(String.valueOf(dataSnapshot.getChildrenCount())+" cliks");
                }else{
                    textView.setText(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                textView.setText(null);
            }
        });
    }

    public static void count_comments(int event_id, final TextView textView){
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("comments").child(String.valueOf(event_id));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 1){
                    textView.setText(String.valueOf(dataSnapshot.getChildrenCount())+" comentário");
                }else if(dataSnapshot.getChildrenCount() > 1){
                    textView.setText(String.valueOf(dataSnapshot.getChildrenCount())+" comentários");
                }else{
                    textView.setText(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                textView.setText(null);
            }
        });
    }

    public static void add_like_event (int event_id, int user_id){
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("likes").child(String.valueOf(event_id)).child(String.valueOf(user_id));
        Map<String, Object> map = new HashMap<>();
        map.put("like", true);
        databaseReference.setValue(map);
    }

    public static void remove_like_event (int event_id, int user_id){
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("likes").child(String.valueOf(event_id)).child(String.valueOf(user_id));
        databaseReference.removeValue();
    }
}
