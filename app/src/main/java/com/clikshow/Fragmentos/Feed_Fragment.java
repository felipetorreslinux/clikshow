package com.clikshow.Fragmentos;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Carrinho.View_Carrinho;
import com.clikshow.Direct.Service.Service_Direct;
import com.clikshow.Direct.View_Direct;
import com.clikshow.FireBase.NotificationFireBase;
import com.clikshow.Fragmentos.Models.Search_Model;
import com.clikshow.R;
import com.clikshow.SQLite.Banco;
import com.clikshow.Service.Service_Eventos;
import com.clikshow.Views.View_Search;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Feed_Fragment extends Fragment {

    View rootView;
    View view;

    public static ImageView btn_open_cart;
    public static TextView count_carrinho_feed;
    public static TextView count_direct_feed;

    RelativeLayout buscar_eventos_feed;
    FrameLayout container_tabs_feed;
    ImageView btn_open_direct;

    List<Search_Model> lista_search = new ArrayList<>();
    RecyclerView mRecyclerView;
    SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);

        container_tabs_feed = (FrameLayout) rootView.findViewById(R.id.container_tabs_feed);
        count_carrinho_feed = (TextView) rootView.findViewById(R.id.count_carrinho_feed);

        count_direct_feed = (TextView) rootView.findViewById(R.id.count_direct_feed);

        buscar_eventos_feed = (RelativeLayout) rootView.findViewById(R.id.buscar_eventos_feed);
        buscar_eventos_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), View_Search.class);
                getActivity().startActivityForResult(intent, 4000);
            }
        });

        Banco banco = new Banco(getActivity());
        banco.count_carrinho();


            // Abre Direct

            btn_open_direct = (ImageView) rootView.findViewById(R.id.btn_open_direct);
            btn_open_direct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), View_Direct.class));
                }
            });

            // Abre Carrinho
            btn_open_cart = (ImageView) rootView.findViewById(R.id.btn_open_cart);
            btn_open_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent open = new Intent(getActivity(), View_Carrinho.class);
                    startActivity(open);
                }
            });

            getFragmentManager().beginTransaction().replace(R.id.container_tabs_feed, new Feed_Lista_Fragment()).commit();

            count_notitications();


        return  rootView;
    };

    public void listaSearchFeed (String search) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_search);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        if(APIServer.conexao(getActivity()) == true){
            mRecyclerView.setVisibility(View.VISIBLE);
            Service_Eventos.listar_search(getActivity(), search, lista_search, mRecyclerView);
        }else{
            mRecyclerView.setVisibility(View.GONE);
        };
    }

    @Override
    public void onResume(){
        super.onResume();
        Banco banco = new Banco(getActivity());
        banco.count_carrinho();
        count_notitications();
    };


    @Override
    public void onPause(){
        super.onPause();
        Banco banco = new Banco(getActivity());
        banco.count_carrinho();
    };

    public void count_notitications(){
        final Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(NotificationFireBase.count == 0){
                    count_direct_feed.setVisibility(View.GONE);
                }else{
                    count_direct_feed.setVisibility(View.VISIBLE);
                    count_direct_feed.setText(String.valueOf(NotificationFireBase.count));
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);

    }
};
