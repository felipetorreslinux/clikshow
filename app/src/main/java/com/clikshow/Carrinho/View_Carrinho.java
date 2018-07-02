package com.clikshow.Carrinho;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Carrinho.Models.ListaCarrinhoModel;
import com.clikshow.Profile.View_Pagamento_Profile;
import com.clikshow.R;
import com.clikshow.SQLite.Banco;
import com.clikshow.Service.Toast.ToastClass;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class View_Carrinho extends Activity implements View.OnClickListener {

    private List<ListaCarrinhoModel> lista_carrinho = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewCard;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView btn_back_cart;
    private TextView btn_voltar_comprar;
    private LinearLayout btn_pagar_carrinho;
    private TextView btn_fale_conosco_carrinho;
    private ImageView img_faq_whatsapp;


    private TextView btn_forma_pagamento_carrinho;

    public static NestedScrollView box_central_carrinho;

    public static ViewStub box_sem_carrinho;
    public static TextView total_carrinho_view;
    public static LinearLayout bottom_bar_carrinho;

    public static int ID_CARTAO_CREDITO;

    private LinearLayout box_forma_pagamento_carrinho;
    private ImageView imagem_bandeira_cartao_carrinho;
    private TextView numero_cartao_credito_carrinho;
    private TextView titular_cartao_credito_carrinho;
    private TextView validade_cartao_credito_carrinho;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_carrinho);

        ID_CARTAO_CREDITO = 0;

        try{
            Banco banco = new Banco(this);
            mLayoutManager = new LinearLayoutManager(this);

            box_central_carrinho = (NestedScrollView) findViewById(R.id.box_central_carrinho);
            box_sem_carrinho = (ViewStub) findViewById(R.id.box_sem_carrinho);
            bottom_bar_carrinho = (LinearLayout) findViewById(R.id.bottom_bar_carrinho);
            total_carrinho_view = (TextView) findViewById(R.id.total_carrinho);
            total_carrinho_view.setText("R$ " + APIServer.preco(Banco.total_carrinho()));

            itensCart();

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_carrinho);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setNestedScrollingEnabled(false);
            lista_carrinho.clear();

            banco.lista_carrinho(this, lista_carrinho, mRecyclerView, total_carrinho_view, bottom_bar_carrinho);

            btn_pagar_carrinho = (LinearLayout) findViewById(R.id.btn_pagar_carrinho);
            btn_pagar_carrinho.setOnClickListener(this);

            btn_fale_conosco_carrinho = (TextView) findViewById(R.id.btn_fale_conosco_carrinho);
            btn_fale_conosco_carrinho.setOnClickListener(this);

            img_faq_whatsapp = (ImageView) findViewById(R.id.img_faq_whatsapp);
            img_faq_whatsapp.setOnClickListener(this);

            btn_voltar_comprar = (TextView) findViewById(R.id.btn_voltar_comprar);
            btn_voltar_comprar.setOnClickListener(this);

            btn_back_cart = (ImageView) findViewById(R.id.back_carrinho);
            btn_back_cart.setOnClickListener(this);

            btn_forma_pagamento_carrinho = (TextView) findViewById(R.id.btn_forma_pagamento_carrinho);
            btn_forma_pagamento_carrinho.setOnClickListener(this);

            box_forma_pagamento_carrinho = (LinearLayout) findViewById(R.id.box_forma_pagamento_carrinho);
            box_forma_pagamento_carrinho.setVisibility(View.GONE);

            imagem_bandeira_cartao_carrinho = (ImageView) findViewById(R.id.imagem_bandeira_cartao_carrinho);
            numero_cartao_credito_carrinho = (TextView) findViewById(R.id.numero_cartao_credito_carrinho);
            titular_cartao_credito_carrinho = (TextView) findViewById(R.id.titular_cartao_credito_carrinho);
            validade_cartao_credito_carrinho = (TextView) findViewById(R.id.validade_cartao_credito_carrinho);


        }catch (Exception e){
            e.printStackTrace();
        }
    };

    public static void itensCart(){
        if(Banco.total_carrinho() > 0){
            box_central_carrinho.setVisibility(View.VISIBLE);
            bottom_bar_carrinho.setVisibility(View.VISIBLE);
            box_sem_carrinho.setVisibility(View.GONE);
        }else{
            box_central_carrinho.setVisibility(View.GONE);
            bottom_bar_carrinho.setVisibility(View.GONE);
            box_sem_carrinho.inflate();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_fale_conosco_carrinho:
                APIServer.getWhatsapp(this);
                break;

            case R.id.img_faq_whatsapp:
                APIServer.getWhatsapp(this);
                break;

            case R.id.btn_forma_pagamento_carrinho:

                startActivityForResult(new Intent(this, View_Pagamento_Profile.class), 1000);

                break;


            case R.id.btn_pagar_carrinho:

                try {
                    switch (ID_CARTAO_CREDITO){
                        case 0:

                            ToastClass.curto(this, "Escolha a forma de pagamento");

                            break;
                        default:

                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);

                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            View view = getLayoutInflater().inflate(R.layout.dialog_valida_carrinho, null);
                            builder.setView(view);
                            builder.setCancelable(false);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            Banco.pagarCarrinho(this, ID_CARTAO_CREDITO, alertDialog);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_voltar_comprar:

                onBackPressed();

                break;
            case R.id.back_carrinho:

                onBackPressed();

                break;
        }
    };

    @Override
    public void onBackPressed(){
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1000:
                if (resultCode == RESULT_OK) {
                    ID_CARTAO_CREDITO = data.getExtras().getInt("id_cartao");
                    String mes_cartao = null;
                    String ano_cartao = null;

                    switch (data.getExtras().getString("badeira_cartao")) {
                        case "VISA":
                            imagem_bandeira_cartao_carrinho.setImageResource(R.drawable.ic_visa_cartao);
                            break;
                        case "MasterCard":
                            imagem_bandeira_cartao_carrinho.setImageResource(R.drawable.ic_cartao_master);
                            break;
                        case "HiperCard":
                            imagem_bandeira_cartao_carrinho.setImageResource(R.drawable.ic_hiper_cartao);
                            break;
                        default:
                            imagem_bandeira_cartao_carrinho.setImageResource(R.drawable.ic_card);
                    }
                    ;

                    int mes = Integer.parseInt(data.getExtras().getString("validade_mes_cartao"));
                    if (mes < 10) {
                        mes_cartao = "0" + data.getExtras().getString("validade_mes_cartao");
                        ano_cartao = data.getExtras().getString("validade_ano_cartao");
                    } else {
                        mes_cartao = data.getExtras().getString("validade_mes_cartao");
                        ano_cartao = data.getExtras().getString("validade_ano_cartao");
                    }

                    numero_cartao_credito_carrinho.setText("Número: " + data.getExtras().getString("numero_cartao"));
                    titular_cartao_credito_carrinho.setText("Nome: " + data.getExtras().getString("nome_cartao"));
                    validade_cartao_credito_carrinho.setText("Validade: " + mes_cartao + "/" + ano_cartao);
                    btn_forma_pagamento_carrinho.setText("Alterar forma de pagamento");
                    box_forma_pagamento_carrinho.setVisibility(View.VISIBLE);
                }
                ;

                if (resultCode == RESULT_CANCELED) {
                    if (ID_CARTAO_CREDITO == 0) {
                        btn_forma_pagamento_carrinho.setText("Escolha forma de pagamento");
                        box_forma_pagamento_carrinho.setVisibility(View.GONE);
                    } else {
                        box_forma_pagamento_carrinho.setVisibility(View.VISIBLE);
                        btn_forma_pagamento_carrinho.setText("Alterar forma de pagamento");
                    }

                }
                ;
                break;
        };
    };
};
