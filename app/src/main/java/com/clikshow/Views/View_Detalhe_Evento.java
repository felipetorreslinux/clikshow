package com.clikshow.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Carrinho.View_Carrinho;
import com.clikshow.Fragmentos.Detalhe_Evento_ComoChegar_Fragment;
import com.clikshow.Fragmentos.Detalhe_Evento_Info_Fragment;
import com.clikshow.Fragmentos.Detalhe_Evento_Ingressos_Fragment;
import com.clikshow.Fragmentos.Models.FavoritosModel;
import com.clikshow.R;
import com.clikshow.SQLite.Banco;
import com.clikshow.Service.Datas;
import com.clikshow.Service.Localizacao.Local_Service;
import com.clikshow.Service.Service_Favoritos;
import com.clikshow.Service.Toast.ToastClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class View_Detalhe_Evento extends Activity {

    private ImageView img_detalhe_evento;
    private ImageView img_favorite_evento;
    private TextView resultado_favorite_evento;
    private ImageView back_detalhes_evento;

    private TabLayout tab_detalhe_evento;
    private List<FavoritosModel> lista_favoritos = new ArrayList<>();

    private TextView name_evento;
    private TextView data_evento_detalhes;
    private TextView hora_evento_detalhes;
    private TextView local_evento_detalhes;

    private ImageView btn_share_detalhes_evento;

    public static String lat;
    public static String lng;
    public static String nome_evento;
    public static String local_evento;
    public static int id_event;
    public static boolean is_favorite;
    public static TextView valor_total_carrinho;
    public static LinearLayout toolbar_btn_carrinho;
    public static String Description_Evento;

    private LinearLayout btn_open_cart_detallhes;

    Handler handle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detalhe_evento);

        Banco banco = new Banco(this);

        lat = getIntent().getExtras().getString("lat");
        lng = getIntent().getExtras().getString("lng");
        nome_evento = getIntent().getExtras().getString("name");
        local_evento = Local_Service.local_evento(View_Detalhe_Evento.this, Double.parseDouble(lat), Double.parseDouble(lng));
        id_event = getIntent().getExtras().getInt("id");
        is_favorite = getIntent().getExtras().getBoolean("is_favorite");

        Description_Evento = getIntent().getExtras().getString("description");

        toolbar_btn_carrinho = (LinearLayout) findViewById(R.id.toolbar_btn_carrinho);
        valor_total_carrinho = (TextView) findViewById(R.id.valor_total_carrinho);

        if(Banco.total_carrinho() > 0){
            toolbar_btn_carrinho.setVisibility(View.VISIBLE);
            valor_total_carrinho.setText("R$ " + APIServer.preco(Banco.total_carrinho()));
        }else{
            toolbar_btn_carrinho.setVisibility(View.GONE);
            valor_total_carrinho.setText("R$ " + APIServer.preco(Banco.total_carrinho()));
        };

        img_detalhe_evento = (ImageView) findViewById(R.id.img_detalhe_evento);
        img_favorite_evento = (ImageView) findViewById(R.id.img_favorite_evento);
        resultado_favorite_evento = (TextView) findViewById(R.id.resultado_favorite_evento);

        tab_detalhe_evento = (TabLayout) findViewById(R.id.tab_detalhe_evento);

        name_evento = (TextView) findViewById(R.id.name_evento_detalhes);
        name_evento.setText(nome_evento);

        data_evento_detalhes = (TextView) findViewById(R.id.data_evento_detalhes);
        data_evento_detalhes.setText(Datas.data_extenso(getIntent().getExtras().getInt("starts")));

        hora_evento_detalhes = (TextView) findViewById(R.id.hora_evento_detalhes);
        hora_evento_detalhes.setText(Datas.hora_evento(getIntent().getExtras().getInt("starts")));

        local_evento_detalhes = (TextView) findViewById(R.id.local_evento_detalhes);
        local_evento_detalhes.setText(local_evento);

        img_detalhe_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open = new Intent(View_Detalhe_Evento.this, View_Foto_Evento_Zoom.class);
                open.putExtra("banner", getIntent().getExtras().getString("banner"));
                startActivity(open);
            }
        });

        Picasso.get()
        .load(getIntent().getExtras().getString("banner"))
        .error(R.drawable.ic_place_doodle)
        .placeholder(R.drawable.ic_place_doodle)
        .into(img_detalhe_evento);

        if (is_favorite == true) {
            img_favorite_evento.setImageResource(R.drawable.ic_favorites_orange);
            resultado_favorite_evento.setText("Você gostou deste evento...");
        } else {
            img_favorite_evento.setImageResource(R.drawable.ic_heart);
            resultado_favorite_evento.setText(null);
        };

        img_favorite_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (is_favorite == false) {
                Service_Favoritos.cadastrar(View_Detalhe_Evento.this, String.valueOf(id_event));
                img_favorite_evento.setImageResource(R.drawable.ic_favorites_orange);
                resultado_favorite_evento.setText("Você gostou deste evento...");
            }else{
                Service_Favoritos.deletar(View_Detalhe_Evento.this, String.valueOf(id_event));
                img_favorite_evento.setImageResource(R.drawable.ic_heart);
                resultado_favorite_evento.setText(null);
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            }
        });

        tab_detalhe_evento.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getFragmentManager().beginTransaction().replace(R.id.container_detalhe_evento,
                                new Detalhe_Evento_Ingressos_Fragment()).commit();
                        break;
                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.container_detalhe_evento,
                                new Detalhe_Evento_Info_Fragment()).commit();
                        break;
                    case 2:
                        getFragmentManager().beginTransaction().replace(R.id.container_detalhe_evento,
                                new Detalhe_Evento_ComoChegar_Fragment()).commit();
                        break;
                };
            };
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFragmentManager().beginTransaction().replace(R.id.container_detalhe_evento,
                        new Detalhe_Evento_Ingressos_Fragment()).commit();
            }
        }, 100);

        back_detalhes_evento = (ImageView) findViewById(R.id.back_detalhes_evento);
        back_detalhes_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_open_cart_detallhes = (LinearLayout) findViewById(R.id.btn_open_cart_detallhes);
        btn_open_cart_detallhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Detalhe_Evento.this, View_Carrinho.class);
                startActivityForResult(intent, 6000);
            }
        });

    };

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 6000:
                if(resultCode == RESULT_OK){
                    Banco.count_carrinho();
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();
                }
                break;
            default:

        }
    }
};
