package com.clikshow.Comments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;

import com.clikshow.API.APIServer;
import com.clikshow.Comments.Adapter.Adapter_Comments;
import com.clikshow.Comments.Model.Comment_Model;
import com.clikshow.FireBase.NotificationFireBase;
import com.clikshow.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class View_Comments extends Activity implements View.OnClickListener {

    ImageView imageview_back_comments;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    List<Comment_Model> list_comments = new ArrayList<>();
    RecyclerView recyvlerview_comments;
    EditText edittexct_comments;
    ImageView imageview_send_comments;
    ViewStub viewstub_not_comments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_comments);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        edittexct_comments = (EditText) findViewById(R.id.edittexct_comments);
        imageview_back_comments = (ImageView) findViewById(R.id.imageview_back_comments);
        imageview_back_comments.setOnClickListener(this);
        imageview_send_comments = (ImageView) findViewById(R.id.imageview_send_comments);
        imageview_send_comments.setOnClickListener(this);

        recyvlerview_comments = (RecyclerView) findViewById(R.id.recyvlerview_comments);
        recyvlerview_comments.setLayoutManager(new LinearLayoutManager(this));
        recyvlerview_comments.setNestedScrollingEnabled(false);
        recyvlerview_comments.setHasFixedSize(true);

        viewstub_not_comments = (ViewStub) findViewById(R.id.viewstub_not_comments);

    }

    @Override
    public void onResume() {
        super.onResume();
        listComments();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_send_comments:
                sendComments();
                break;

            case R.id.imageview_back_comments:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    };

    @Override
    protected void onPause() {
        super.onPause();
    };

    private void sendComments (){
        if(!edittexct_comments.getText().toString().isEmpty()){
            String id = String.valueOf(getIntent().getExtras().getInt("event_id"));
            String id_comment = String.valueOf(new Date().getTime());
            databaseReference = FirebaseDatabase.getInstance().getReference().getRoot()
                    .child("comments").child(id).child(id_comment);
            Map<String, Object> map = new HashMap<>();
            map.put("event_id", String.valueOf(getIntent().getExtras().getInt("event_id")));
            map.put("user_id", String.valueOf(sharedPreferences.getInt("id", 0)));
            map.put("name", sharedPreferences.getString("name", null));
            map.put("username", sharedPreferences.getString("username", null));
            map.put("thumb", sharedPreferences.getString("profile_pic", null));
            map.put("comment", edittexct_comments.getText().toString().trim());
            map.put("type", "text");
            map.put("create_at", id_comment);
            map.put("is_like", "false");
            map.put("is_count_like", "0");
            databaseReference.setValue(map);
            try{
                JSONObject data = new JSONObject();
                data.put("event_id", getIntent().getExtras().getInt("event_id"));
                data.put("type", "comments");
                edittexct_comments.setText(null);
                edittexct_comments.setHint(R.string.text_commets);
                recyvlerview_comments.scrollToPosition(recyvlerview_comments.getAdapter().getItemCount() - 1);
            }catch (JSONException e){

            }catch (NullPointerException e){

            }

        }else{
            edittexct_comments.setHint(R.string.text_empty_commets);
            edittexct_comments.requestFocus();
        }
    };

    private void listComments (){
        String id = String.valueOf(getIntent().getExtras().getInt("event_id"));
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("comments").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    viewstub_not_comments.setVisibility(View.GONE);
                    list_comments.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Map<String, String> map = (Map)snapshot.getValue();
                        Comment_Model comment_model = new Comment_Model(
                                snapshot.getKey(),
                                map.get("event_id"),
                                map.get("user_id"),
                                map.get("name"),
                                map.get("username"),
                                map.get("thumb"),
                                map.get("comment"),
                                map.get("type"),
                                map.get("create_at"),
                                map.get("is_like"),
                                String.valueOf(map.get("is_count_like")));
                        list_comments.add(comment_model);
                    }
                    Adapter_Comments adapter_comments = new Adapter_Comments(View_Comments.this, list_comments, recyvlerview_comments);
                    recyvlerview_comments.setAdapter(adapter_comments);
                }else{
                    viewstub_not_comments.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                APIServer.error_server(View_Comments.this, databaseError.getCode());
            }
        });
    };
}
