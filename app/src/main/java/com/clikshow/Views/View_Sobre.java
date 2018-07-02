package com.clikshow.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clikshow.R;
import com.github.barteksc.pdfviewer.PDFView;

public class View_Sobre extends Activity implements View.OnClickListener {

    private ImageView back_sobre;
    private LinearLayout btn_open_termos_condicoes;
    private LinearLayout btn_open_politica_privacidade;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sobre);


        btn_open_termos_condicoes = (LinearLayout) findViewById(R.id.btn_open_termos_condicoes);
        btn_open_termos_condicoes.setOnClickListener(this);

        btn_open_politica_privacidade = (LinearLayout) findViewById(R.id.btn_open_politica_privacidade);
        btn_open_politica_privacidade.setOnClickListener(this);

        back_sobre = (ImageView) findViewById(R.id.back_sobre);
        back_sobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_open_termos_condicoes:
                Intent open_termos = new Intent(this, View_Termos_Condicoes.class);
                startActivity(open_termos);
                break;
            case R.id.btn_open_politica_privacidade:
                Intent open_politica = new Intent(this, View_Politica_Privacidade.class);
                startActivity(open_politica);
                break;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
