package com.clikshow.Bilheteria.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.clikshow.Bilheteria.Impressora.BlueTooth;
import com.clikshow.Bilheteria.Models.Bilheteria_Model;
import com.clikshow.Bilheteria.Services.Bilheteria_Service;
import com.clikshow.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class View_Lista_Ingressos_Bilheteria extends Activity implements View.OnClickListener {

    ImageView imageview_back_list_ingressos;
    RecyclerView mRecyclerView;
    List<Bilheteria_Model> lista_ingressos_bilheteria = new ArrayList<>();
    NestedScrollView view_lista_ingressos_bilheteria;

    int event_id;
    public static int type_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_lista_ingressos_bilheteria);

        view_lista_ingressos_bilheteria = (NestedScrollView) findViewById(R.id.view_lista_ingressos_bilheteria);
        imageview_back_list_ingressos = (ImageView) findViewById(R.id.imageview_back_list_ingressos);
        imageview_back_list_ingressos.setOnClickListener(this);

        event_id = getIntent().getExtras().getInt("event_id");
        type_service = getIntent().getExtras().getInt("type_service");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_lista_ingressos_bilheteria);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(View_Lista_Ingressos_Bilheteria.this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_back_list_ingressos:
                onBackPressed();
                break;
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        Bilheteria_Service.listar_ingressos(this, event_id, lista_ingressos_bilheteria, mRecyclerView);
    };


    @Override
    public void onBackPressed(){
        if(BlueTooth.connection_BlueTooth == true){
            try {
                BlueTooth.bluetoothSocket.close();
            } catch (IOException e) {}
        }
        finish();
    }
}
