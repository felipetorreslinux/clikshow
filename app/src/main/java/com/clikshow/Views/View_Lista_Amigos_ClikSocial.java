package com.clikshow.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;

import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Models.AmigosClikSocialModel;
import com.clikshow.R;
import com.clikshow.Service.Service_ClikSocial;
import com.clikshow.Service.Toast.ToastClass;

import java.util.ArrayList;
import java.util.List;

public class View_Lista_Amigos_ClikSocial extends Activity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<AmigosClikSocialModel> lista_amigos = new ArrayList<>();
    private ImageView back_lista_amigos_cliksocial;

    private EditText texto_buscar_amigos_cliksocial;

    private ViewStub loading_amigos_cliksocial;
    private ViewStub box_sem_conexao_amigos_cliksocial;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_lista_amigos_cliksocial);

        box_sem_conexao_amigos_cliksocial = (ViewStub) findViewById(R.id.box_sem_conexao_amigos_cliksocial);
        loading_amigos_cliksocial = (ViewStub) findViewById(R.id.loading_amigos_cliksocial);

        back_lista_amigos_cliksocial = (ImageView) findViewById(R.id.back_lista_amigos_cliksocial);
        back_lista_amigos_cliksocial.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_lista_amigos_cliksocial);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        texto_buscar_amigos_cliksocial = (EditText) findViewById(R.id.texto_buscar_amigos_cliksocial);
        texto_buscar_amigos_cliksocial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() > 0){
                    if(APIServer.conexao(View_Lista_Amigos_ClikSocial.this) == true){
                        Service_ClikSocial.buscar_users(View_Lista_Amigos_ClikSocial.this, charSequence, lista_amigos, mRecyclerView);
                    }
                }else{
                    if(APIServer.conexao(View_Lista_Amigos_ClikSocial.this) == true){
                        Service_ClikSocial.lista_amigos(View_Lista_Amigos_ClikSocial.this, lista_amigos, mRecyclerView);
                    }
                }
            }
        });
    };

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(APIServer.conexao(View_Lista_Amigos_ClikSocial.this) == true){
            Service_ClikSocial.lista_amigos(View_Lista_Amigos_ClikSocial.this, lista_amigos, mRecyclerView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_lista_amigos_cliksocial:
                finish();
            break;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
