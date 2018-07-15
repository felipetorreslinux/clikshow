package com.clikshow.Bilheteria.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.clikshow.Bilheteria.Fragments.Cancelamento_Fragment;
import com.clikshow.Bilheteria.Fragments.Cortesia_Fragment;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.squareup.picasso.Picasso;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class View_Coordenador extends Activity implements View.OnClickListener{

    FrameLayout container_coordenador_bilheteria;
    ImageView back_coordenador_bilheteria;
    TabLayout tablayout_coordenador;

    ImageView imageview_image_event_coordenador;
    TextView textview_name_ingresso_coordenador;
    TextView textview_name_event_coordenador;
    TextView textview_validade_event_coordenador;

    public static String NAME_EVENT = null;
    public static int VALIDATE_EVENT = 0;
    public static String EVENT_THUMB = null;
    public static String TIPO_INGRESSO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_coordenador_bilheteria);

        NAME_EVENT = getIntent().getExtras().getString("event_name");
        EVENT_THUMB = getIntent().getExtras().getString("event_thumb");
        TIPO_INGRESSO = getIntent().getExtras().getString("name");
        VALIDATE_EVENT = getIntent().getExtras().getInt("ends");

        container_coordenador_bilheteria = (FrameLayout) findViewById(R.id.container_coordenador_bilheteria);
        imageview_image_event_coordenador = (ImageView) findViewById(R.id.imageview_image_event_coordenador);
        textview_name_ingresso_coordenador = (TextView) findViewById(R.id.textview_name_ingresso_coordenador);
        textview_name_event_coordenador = (TextView) findViewById(R.id.textview_name_event_coordenador);
        textview_validade_event_coordenador = (TextView) findViewById(R.id.textview_validade_event_coordenador);


        tablayout_coordenador = (TabLayout) findViewById(R.id.tablayout_coordenador);
        tablayout_coordenador.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getFragmentManager().beginTransaction().replace(R.id.container_coordenador_bilheteria,
                                new Cortesia_Fragment()).commit();
                        break;

                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.container_coordenador_bilheteria,
                                new Cancelamento_Fragment()).commit();

                        break;
                };
            };

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        back_coordenador_bilheteria = (ImageView) findViewById(R.id.back_coordenador_bilheteria);
        back_coordenador_bilheteria.setOnClickListener(this);

        getFragmentManager().beginTransaction().replace(R.id.container_coordenador_bilheteria,
                new Cortesia_Fragment()).commit();

    };

    @Override
    public void onResume(){
        super.onResume();
        info_evento();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back_coordenador_bilheteria:
                onBackPressed();
                break;

        }
    };

    private void info_evento(){
        Picasso.get()
                .load(EVENT_THUMB)
                .resize(150, 150)
                .transform(new CropCircleTransformation())
                .into(imageview_image_event_coordenador);
        textview_name_ingresso_coordenador.setText(TIPO_INGRESSO);
        textview_name_event_coordenador.setText(NAME_EVENT);
        textview_validade_event_coordenador.setText("Validade até "+Datas.data_bilheteria(VALIDATE_EVENT));
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    };
}
