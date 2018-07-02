package com.clikshow.Profile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.clikshow.Carrinho.View_Carrinho;
import com.clikshow.IUGU.Models.Adapter.ListaCartaoCreditoAdapter;
import com.clikshow.IUGU.Models.CartaoCreditoModel;
import com.clikshow.IUGU.Models.Service.Cartao_Credito;
import com.clikshow.IUGU.Models.Views.CadastrarCartaoClass;
import com.clikshow.R;

import java.util.ArrayList;
import java.util.List;

public class View_Pagamento_Profile extends Activity implements View.OnClickListener {

    ImageView back_pagamento_profile;

    private FrameLayout btn_back_profile_pagamento;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<CartaoCreditoModel> lista_cartao = new ArrayList<>();
    private ListaCartaoCreditoAdapter listaCartaoCreditoAdapter;
    private TextView text_not_cartao_profile;
    final Handler handle = new Handler();
    private SharedPreferences sharedPreferences;
    private String token;
    private LinearLayout btn_open_editar_perfil;
    private View view;

    private LottieAnimationView load_cartao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pagamento_profile);

        view = findViewById(R.id.profile_pagemento);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        load_cartao = (LottieAnimationView) findViewById(R.id.load_cartao);

        mRecyclerView = (RecyclerView) findViewById(R.id.lista_cartao_credito_profile);
        mLayoutManager = new LinearLayoutManager(View_Pagamento_Profile.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        text_not_cartao_profile =  (TextView) findViewById(R.id.text_not_cartao_profile);
        text_not_cartao_profile.setVisibility(View.GONE);

        carregarCartao();

        // Abre cadastro de novo Cartão de Crédito
        btn_open_editar_perfil = (LinearLayout) findViewById(R.id.btn_open_editar_perfil);
        btn_open_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open = new Intent(View_Pagamento_Profile.this, CadastrarCartaoClass.class);
                startActivity(open);
            }
        });


        // Botao voltar ActionBar
        back_pagamento_profile = (ImageView) findViewById(R.id.back_pagamento_profile);
        back_pagamento_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    };

    public void carregarCartao(){
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                Cartao_Credito.lista(View_Pagamento_Profile.this, view, lista_cartao, mRecyclerView, text_not_cartao_profile, load_cartao);
            }
        }, 50);
    }

    @Override
    public void onResume(){
        super.onResume();
        carregarCartao();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        View_Carrinho.ID_CARTAO_CREDITO=0;
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_pagamento_profile:
                onBackPressed();
                break;
        }
    }
}
