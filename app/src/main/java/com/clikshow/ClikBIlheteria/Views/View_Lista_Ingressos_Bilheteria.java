package com.clikshow.ClikBIlheteria.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.ClikBIlheteria.Impressora.BlueTooth;
import com.clikshow.ClikBIlheteria.Models.Bilheteria_Model;
import com.clikshow.ClikBIlheteria.Services.Bilheteria_Service;
import com.clikshow.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class View_Lista_Ingressos_Bilheteria extends Activity implements View.OnClickListener {

    FrameLayout btn_exit_bilheteria;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    List<Bilheteria_Model> lista_ingressos_bilheteria = new ArrayList<>();

    ImageView menu_user_bilheteria;
    TextView titulo_name_evento_bilheteria;
    ViewStub view_sem_internet;
    NestedScrollView view_lista_ingressos_bilheteria;
    ViewStub loading_ingressos_bilheteria;

    int event_id;
    public static int type_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_lista_ingressos_bilheteria);

        loading_ingressos_bilheteria = (ViewStub) findViewById(R.id.loading_ingressos_bilheteria);
        view_lista_ingressos_bilheteria = (NestedScrollView) findViewById(R.id.view_lista_ingressos_bilheteria);
        view_sem_internet = (ViewStub) findViewById(R.id.view_sem_internet);

        menu_user_bilheteria = (ImageView) findViewById(R.id.menu_user_bilheteria);
        titulo_name_evento_bilheteria = (TextView) findViewById(R.id.titulo_name_evento_bilheteria);

        event_id = getIntent().getExtras().getInt("event_id");
        titulo_name_evento_bilheteria.setText(getIntent().getExtras().getString("event_name"));
        type_service = getIntent().getExtras().getInt("type_service");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_lista_ingressos_bilheteria);
        mLayoutManager = new LinearLayoutManager(View_Lista_Ingressos_Bilheteria.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        btn_exit_bilheteria = (FrameLayout) findViewById(R.id.btn_exit_bilheteria);
        btn_exit_bilheteria.setOnClickListener(this);
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit_bilheteria:
                onBackPressed();
                break;
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        if(APIServer.conexao(this) == true){
            view_sem_internet.setVisibility(View.GONE);
            Bilheteria_Service.listar_ingressos(this, event_id, lista_ingressos_bilheteria, mRecyclerView, loading_ingressos_bilheteria);
        }else{
            view_sem_internet.setVisibility(View.VISIBLE);
        }
    };


    @Override
    public void onBackPressed(){
        if(BlueTooth.connection_BlueTooth == true){
            try {
                BlueTooth.bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        finish();
    }
}
