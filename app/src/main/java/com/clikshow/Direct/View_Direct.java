package com.clikshow.Direct;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clikshow.Direct.Adapter.Usuarios_Online_Adapter;
import com.clikshow.Direct.Models.Rooms_Model;
import com.clikshow.Direct.Models.Usuarios_Online_Model;
import com.clikshow.Direct.Service.Service_Direct;
import com.clikshow.FireBase.NotificationFireBase;
import com.clikshow.R;
import com.clikshow.Utils.Keyboard;
import com.clikshow.Utils.Progress_Alert;

import java.util.ArrayList;
import java.util.List;

public class View_Direct extends Activity implements View.OnClickListener {

    ImageView imageview_back_direct;
    FloatingActionButton floatbutton_friends_direct;

    RecyclerView recyclerView_usuarios_online;
    RecyclerView recylclerview_direct_conversas;

    Service_Direct service_direct;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_direct);

        service_direct = new Service_Direct(this);

        NotificationFireBase.count = 0;

        imageview_back_direct = (ImageView) findViewById(R.id.imageview_back_direct);
        imageview_back_direct.setOnClickListener(this);

        floatbutton_friends_direct = (FloatingActionButton) findViewById(R.id.floatbutton_friends_direct);
        floatbutton_friends_direct.setOnClickListener(this);

        recyclerView_usuarios_online = (RecyclerView) findViewById(R.id.recyclerView_usuarios_online);
        recyclerView_usuarios_online.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView_usuarios_online.setNestedScrollingEnabled(false);
        recyclerView_usuarios_online.setHasFixedSize(true);

        recylclerview_direct_conversas = (RecyclerView) findViewById(R.id.recylclerview_direct_conversas);
        recylclerview_direct_conversas.setHasFixedSize(true);
        recylclerview_direct_conversas.setNestedScrollingEnabled(false);
        recylclerview_direct_conversas.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        service_direct.lista_usuarios_online(recyclerView_usuarios_online);
        service_direct.lista_salas_conversas(recylclerview_direct_conversas);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_back_direct:
                onBackPressed();
                break;

            case R.id.floatbutton_friends_direct:
                startActivity(new Intent(View_Direct.this, View_Friends_Direct.class));
                break;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
