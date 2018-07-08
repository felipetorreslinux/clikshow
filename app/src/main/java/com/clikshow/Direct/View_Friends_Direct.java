package com.clikshow.Direct;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.clikshow.API.APIServer;
import com.clikshow.Direct.Models.Amigos_Model;
import com.clikshow.Direct.Service.Service_Direct;
import com.clikshow.FireBase.DirectFirebase;
import com.clikshow.R;

import java.util.ArrayList;
import java.util.List;

public class View_Friends_Direct extends Activity implements View.OnClickListener {

    ViewStub loading_friends_direct;

    List<Amigos_Model> list_friends = new ArrayList<>();
    RecyclerView recyclerview_friends_direct;

    ImageView imageview_back_friends_direct;
    ImageView imageview_search_friends_direct;

    DirectFirebase directFirebase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_friends_direct);

        directFirebase = new DirectFirebase(this);

        imageview_back_friends_direct = (ImageView) findViewById(R.id.imageview_back_friends_direct);
        imageview_back_friends_direct.setOnClickListener(this);
        imageview_search_friends_direct = (ImageView) findViewById(R.id.imageview_search_friends_direct);
        imageview_search_friends_direct.setOnClickListener(this);

        loading_friends_direct = (ViewStub) findViewById(R.id.loading_friends_direct);
        loading_friends_direct.setVisibility(View.VISIBLE);

        recyclerview_friends_direct = (RecyclerView) findViewById(R.id.recyclerview_friends_direct);
        recyclerview_friends_direct.setHasFixedSize(true);
        recyclerview_friends_direct.setNestedScrollingEnabled(false);
        recyclerview_friends_direct.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(APIServer.conexao(this) == true){
            Service_Direct.lista_amigos(this, list_friends, recyclerview_friends_direct, loading_friends_direct);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_back_friends_direct:
                finish();
                break;

            case R.id.imageview_search_friends_direct:
                break;
        }
    }
}

