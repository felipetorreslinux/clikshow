package com.clikshow.Direct;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.clikshow.Direct.Models.Amigos_Model;
import com.clikshow.Direct.Models.Rooms_Model;
import com.clikshow.Direct.Service.Service_Direct;
import com.clikshow.FireBase.DirectFirebase;
import com.clikshow.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class View_Direct extends Activity implements View.OnClickListener {

    ImageView imageview_back_direct;
    FloatingActionButton floatbutton_friends_direct;

    List<Rooms_Model> list_rooms = new ArrayList<>();
    RecyclerView recylclerview_direct_conversas;

    SharedPreferences sharedPreferences;
    DirectFirebase directFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_direct);

        imageview_back_direct = (ImageView) findViewById(R.id.imageview_back_direct);
        imageview_back_direct.setOnClickListener(this);

        floatbutton_friends_direct = (FloatingActionButton) findViewById(R.id.floatbutton_friends_direct);
        floatbutton_friends_direct.setOnClickListener(this);

        recylclerview_direct_conversas = (RecyclerView) findViewById(R.id.recylclerview_direct_conversas);
        recylclerview_direct_conversas.setHasFixedSize(true);
        recylclerview_direct_conversas.setNestedScrollingEnabled(false);
        recylclerview_direct_conversas.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        directFirebase = new DirectFirebase(this);
        directFirebase.list_chat_direct(this, list_rooms, recylclerview_direct_conversas);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_back_direct:
                onBackPressed();
                break;

            case R.id.floatbutton_friends_direct:
                startActivityForResult(new Intent(View_Direct.this, View_Friends_Direct.class), 1);
                break;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 1:
                if(requestCode == Activity.RESULT_OK){

                }else{

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
