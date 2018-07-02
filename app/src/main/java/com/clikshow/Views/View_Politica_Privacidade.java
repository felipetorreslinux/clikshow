package com.clikshow.Views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.clikshow.R;
import com.github.barteksc.pdfviewer.PDFView;

public class View_Politica_Privacidade extends Activity implements View.OnClickListener {

    ImageView back_politica;
    PDFView pdf_politica;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_politica_privacidade);

        back_politica = (ImageView) findViewById(R.id.back_politica);
        back_politica.setOnClickListener(this);

        pdf_politica = (PDFView) findViewById(R.id.pdf_politica);
        pdf_politica.fromAsset("politica.pdf").load();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_politica:
                finish();
                break;
        }

    }
}
