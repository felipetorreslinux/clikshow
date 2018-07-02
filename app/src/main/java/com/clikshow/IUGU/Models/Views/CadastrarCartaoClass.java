package com.clikshow.IUGU.Models.Views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.IUGU.Models.Service.Cartao_Credito;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;

import java.util.ArrayList;
import java.util.List;

public class CadastrarCartaoClass extends Activity {

    private LinearLayout box_dados_novo_cartao;
    private ImageView back_button_novo_cartao;


    private EditText cpf_titular_novo_cartao;
    private TextView nome_titular_novo_cartao;
    private EditText numero_cartao_novo_cartao;

    private Spinner validade_mes_novo_cartao;
    private Spinner validade_ano_novo_cartao;

    private EditText ccv_novo_cartao;

    private Button btn_add_novo_cartao;

    private SharedPreferences sharedPreferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_novo_cartao_credito);
        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        box_dados_novo_cartao = (LinearLayout)  findViewById(R.id.box_dados_novo_cartao);
        box_dados_novo_cartao.setVisibility(View.GONE);

        carregaValidades();

        cpf_titular_novo_cartao = (EditText) findViewById(R.id.cpf_titular_novo_cartao);

        nome_titular_novo_cartao = (TextView) findViewById(R.id.nome_titular_novo_cartao);
        numero_cartao_novo_cartao = (EditText) findViewById(R.id.numero_cartao_novo_cartao);

        ccv_novo_cartao = (EditText) findViewById(R.id.ccv_novo_cartao);

        cpf_titular_novo_cartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 11){
                    Cartao_Credito.checar_cpf(CadastrarCartaoClass.this,
                            cpf_titular_novo_cartao.getText().toString(), nome_titular_novo_cartao,
                            numero_cartao_novo_cartao, box_dados_novo_cartao);
                }else{
                    box_dados_novo_cartao.setVisibility(View.GONE);
                    nome_titular_novo_cartao.setText(null);
                }
            }
        });

        btn_add_novo_cartao = (Button) findViewById(R.id.btn_add_novo_cartao);
        btn_add_novo_cartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(APIServer.conexao(CadastrarCartaoClass.this) == true){
                String[] nome = nome_titular_novo_cartao.getText().toString().split(" ");
                String fisrtName = nome[0];
                String lastName = nome[1];
                Cartao_Credito.cadastrar(
                    CadastrarCartaoClass.this,
                    numero_cartao_novo_cartao.getText().toString(),
                    ccv_novo_cartao.getText().toString(),
                    fisrtName,
                    lastName,
                    String.valueOf(validade_mes_novo_cartao.getSelectedItem()),
                    String.valueOf(validade_ano_novo_cartao.getSelectedItem()));
            }else{
                ToastClass.curto(CadastrarCartaoClass.this, "Aparelho offline\nVerifique sua conexão");
            }
            }
        });

        back_button_novo_cartao = (ImageView) findViewById(R.id.back_button_novo_cartao);
        back_button_novo_cartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void carregaValidades() {

        validade_mes_novo_cartao = (Spinner) findViewById(R.id.validade_mes_novo_cartao);
        List<String> meses = new ArrayList<String>();
        String a = null;
        meses.clear();
        for (int i = 01; i <= 12; i++){
            if(i < 10){
                 a = "0"+String.valueOf(i);
            }else{
                 a = String.valueOf(i);
            }
            meses.add(a);
        }
        ArrayAdapter<String> meses_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, meses);
        meses_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        validade_mes_novo_cartao.setAdapter(meses_adapter);


        validade_ano_novo_cartao = (Spinner) findViewById(R.id.validade_ano_novo_cartao);
        List<String> anos = new ArrayList<String>();
        anos.clear();
        for (int i = 2018; i <= 2050; i++){
            anos.add(String.valueOf(i));
        }
        ArrayAdapter<String> anos_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, anos);
        anos_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        validade_ano_novo_cartao.setAdapter(anos_adapter);
    };
}
