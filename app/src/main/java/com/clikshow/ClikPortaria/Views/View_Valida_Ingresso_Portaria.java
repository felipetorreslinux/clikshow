package com.clikshow.ClikPortaria.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.ClikPortaria.Views.Service.Service_Portaria;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class View_Valida_Ingresso_Portaria extends Activity implements View.OnClickListener {

    private ImageView imagem_user_portaria;
    private TextView name_user_portaria;
    private TextView tipo_ingresso_portaria;
    private TextView btn_validar_ingresso_portaria;

    private String imagem;
    private String name;
    private String tipo;
    private int MYPASSID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_valida_ingresso_portaria);
        overridePendingTransition(R.anim.slide_up, R.anim.fade_out);

        Transformation transformation = new RoundedCornersTransformation(10, 0);

        imagem = getIntent().getExtras().getString("thumb");
        name = getIntent().getExtras().getString("name");
        tipo = getIntent().getExtras().getString("pass_name");
        MYPASSID = getIntent().getExtras().getInt("my_pass_id");


        imagem_user_portaria = (ImageView) findViewById(R.id.imagem_user_portaria);
        name_user_portaria = (TextView) findViewById(R.id.name_user_portaria);
        tipo_ingresso_portaria = (TextView) findViewById(R.id.tipo_ingresso_portaria);
        btn_validar_ingresso_portaria = (TextView) findViewById(R.id.btn_validar_ingresso_portaria);
        btn_validar_ingresso_portaria.setOnClickListener(this);

        if(getIntent().getExtras().getBoolean("is_registered") == false){
            Picasso.get()
            .load(R.drawable.ic_profile)
            .resize(250,250)
            .transform(transformation)
            .into(imagem_user_portaria);
        }else{
            Picasso.get()
            .load(imagem)
            .resize(250,250)
            .transform(transformation)
            .into(imagem_user_portaria);
        };

        name_user_portaria.setText(name.toUpperCase());
        tipo_ingresso_portaria.setText(tipo.toUpperCase());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_validar_ingresso_portaria:
                if(APIServer.conexao(this) == true){
                    Service_Portaria.validar_ingresso(this, String.valueOf(MYPASSID));
                }else{
                    ToastClass.curto(this, "Aparelho ofline\nVerifique sua conexão");
                    Intent intent = getIntent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}

