package com.clikshow.Views;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Adapter.Ajuda_Adapter;
import com.clikshow.Fragmentos.Models.Ajuda_Model;
import com.clikshow.R;

import java.util.ArrayList;
import java.util.List;

public class View_Ajuda extends Activity implements View.OnClickListener {

    private ImageView back_ajuda;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Ajuda_Model> lista_ajuda = new ArrayList<>();
    private TextView btn_fale_conosco_ajuda;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_ajuda);

        try{

            btn_fale_conosco_ajuda = (TextView) findViewById(R.id.btn_fale_conosco_ajuda);
            btn_fale_conosco_ajuda.setOnClickListener(this);

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_ajuda);
            mLayoutManager = new LinearLayoutManager(View_Ajuda.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setNestedScrollingEnabled(false);

                Ajuda_Model ajuda_model = new Ajuda_Model(
                        "PORQUE MINHA COMPRA ESTÁ DEMORANDO A AUTORIZAR OU FOI RECUSADA?",
                        "Todas as compras realizadas via cartão de crédito, passam por um processo de análise pela operadora do seu cartão.\nEm alguns casos pode demorar até 48horas para a validação da compra.\n" +
                                "Verique se a forma de pagamento, seus dados e os dados do cartão foram preenchidos corretamente.\n" +
                                "Por se tornar um processo sigiloso entre a operadorado do cartão e o titular, não conseguimos informar ao certo qual a causa especifica para a não aprovação.\n" +
                                "Pedimos por gentileza que você entre em contato imediatamente com a operadora do seu cartão.\n" +
                                "Caso a compra de imediato não seja autorizada, verifique a disponibilidade do seu limite junto a sua operadora ou tente fazer uma nova compra com outra forma de pagamento.",
                        0);
                lista_ajuda.add(ajuda_model);
            Ajuda_Model ajuda_model1 = new Ajuda_Model(
                    "QUERO COMPRAR UM INGRESSO COM CARTÃO DE CRÉDITO, MAS NÃO SOU O TITULAR, O QUE FAZER?",
                    "Se você quer comprar um ingresso mas o cartão de crédito não está no seu nome, recomendamos que o titular do cartão faça todo o processo de compra e depois mude a titularidade do check-in para você com seus dados.\n" +
                            "Entre no APP CLIK SHOW na aba MEUS INGRESSOS / EFETUAR CHECK-IN e preencha os dados do check-in com os seus dados.",
                    0);
            lista_ajuda.add(ajuda_model1);

            Ajuda_Model ajuda_model2 = new Ajuda_Model(
                    "COMO PEDIR REEMBOLSO?",
                    "Se o cliente desitir do evento após a compra do ingresso, ele poderá pedir o reembolso através do APP CLIK SHOW na aba MEUS INGRESSOS / CANCELAR COMPRA.\n" +
                            "A desistência pode ser feita até 5 dias úteis antes do evento ser realizado.\n" +
                            "Será descontado a taxa de serviço do APP CLIK SHOW se o cliente realizou a compra via cartão de crédito sem parcelamento ou boleto bancário.\nCaso o cliente tenha feito a compra parcelada via cartão de crédito, será descontado as taxas dos juros do parcelamento mais a taxa de serviço CLIK SHOW.",
                    0);
            lista_ajuda.add(ajuda_model2);

            Ajuda_Model ajuda_model3 = new Ajuda_Model(
                    "QUERO MUDAR A TITULARIDADE DO MEU INGRESSO. COMO FAZER?",
                    "Se você deseja repassar o ingresso para outra pessoa, entre no APP CLIK SHOW na aba MEUS INGRESSOS / EFETUAR CHECK-IN e preencha os dados do novo titular.",
                    0);
            lista_ajuda.add(ajuda_model3);


            Ajuda_Model ajuda_model4 = new Ajuda_Model(
                    "COMO VALIDAR CORTESIA?",
                    "Se você ganhou a cortesia de um evento, baixe o APP CLIK SHOW, preencha seus dados corretamente, incluindo seu CPF e entre na aba MEUS INGRESSOS.\n" +
                            "A cortesia estará disponivel.\nCaso você não possa comparecer ao evento, comunique ao realizador do evento pois a cortesia é intransferivel.",
                    0);
            lista_ajuda.add(ajuda_model4);


            Ajuda_Model ajuda_model5 = new Ajuda_Model(
                    "QUANTOS INGRESSOS POSSO COMPRAR COM MEU NÚMERO DE CPF?",
                    "É permitida a compra de 10 unidades de ingressos por CPF.\n" +
                            "Após a compra o usuário deverá preencher os dados dos titulares de cada ingresso no APP CLIK SHOW na aba MEUS INGRESSOS / EFETUAR CHECK-IN. Cada titular terá que baixar o app para validar o ingresso no seu nome.",
            0);

            lista_ajuda.add(ajuda_model5);

            Ajuda_Model ajuda_model6 = new Ajuda_Model(
                    "CANCELAMENTO. COMO PROCEDER?",
                    "Caso você se engane e compre o ingresso do evento errado entre no APP CLIK SHOW e faça imediatamente o cancelamento para uma nova compra do evento certo.\n" +
                            "Entre na aba MEUS INGRESSOS / CANCELAR COMPRA.\n" +
                            "O cancelamento só poderá ser feito até 5 dias úteis antes do evento ser realizado.\n" +
                            "Será descontado a taxa de serviço do APP CLIK SHOW se o cliente realizou a compra via cartão de crédito sem parcelamento ou boleto bancário.\n" +
                            "Caso o cliente tenha feito a compra parcelada via cartão de crédito, será descontado as taxas dos juros do parcelamento mais a taxa de serviço CLIK SHOW.\n" +
                            "Caso falte poucos dias para o evento certo, entre em contato imediatamente conosco via e-mail ou telefone.",
                    0);

            lista_ajuda.add(ajuda_model6);

            Ajuda_Adapter ajuda_adapter = new Ajuda_Adapter(View_Ajuda.this, lista_ajuda);
            mRecyclerView.setAdapter(ajuda_adapter);
        }catch (Exception e){ }


        back_ajuda = (ImageView) findViewById(R.id.back_ajuda);
        back_ajuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fale_conosco_ajuda:
                APIServer.getWhatsapp(this);
                break;
        }
    }
}
