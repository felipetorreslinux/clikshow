package com.clikshow.ClikBIlheteria.Views;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.clikshow.ClikBIlheteria.Fragments.Cancelamento_Fragment;
import com.clikshow.ClikBIlheteria.Fragments.Cortesia_Fragment;
import com.clikshow.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class View_Coordenador extends Activity implements View.OnClickListener{

    FrameLayout container_coordenador_bilheteria;
    ImageView back_coordenador_bilheteria;
    TabLayout tablayout_coordenador;

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

        tablayout_coordenador = (TabLayout) findViewById(R.id.tablayout_coordenador);
        tablayout_coordenador.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getFragmentManager().beginTransaction().replace(R.id.container_coordenador_bilheteria,
                                new Cancelamento_Fragment()).commit();
                        break;

                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.container_coordenador_bilheteria,
                                new Cortesia_Fragment()).commit();
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
                new Cancelamento_Fragment()).commit();

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back_coordenador_bilheteria:
                onBackPressed();
                break;

        }
    };

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    };
}
