package com.clikshow.Views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.clikshow.R;
import com.github.barteksc.pdfviewer.PDFView;

public class View_Termos_Condicoes extends Activity implements View.OnClickListener {

    ImageView back_termos;
    PDFView pdf_termos;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_termos_privacidade);

        pdf_termos = (PDFView) findViewById(R.id.pdf_termos);
        pdf_termos.fromAsset("termos.pdf").load();

        back_termos = (ImageView) findViewById(R.id.back_termos);
        back_termos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_termos:
                finish();
            break;
        }
    }
}
