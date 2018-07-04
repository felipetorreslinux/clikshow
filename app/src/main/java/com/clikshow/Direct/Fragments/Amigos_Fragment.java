package com.clikshow.Direct.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clikshow.API.APIServer;
import com.clikshow.Direct.Adapter.Amigos_Lista_Adapter;
import com.clikshow.Direct.Models.Amigos_Model;
import com.clikshow.Direct.Service.Service_Direct;
import com.clikshow.R;
import com.clikshow.Utils.Loading;

import java.util.ArrayList;
import java.util.List;

public class Amigos_Fragment extends Fragment{

    View rootView;
    RecyclerView recyclerview_amigos_direct;
    RecyclerView.LayoutManager layoutManager;
    List<Amigos_Model> lista_amigos = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup viewGroup, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_amigos_direct, viewGroup, false);

        recyclerview_amigos_direct = (RecyclerView) rootView.findViewById(R.id.recyclerview_amigos_direct);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerview_amigos_direct.setLayoutManager(layoutManager);
        recyclerview_amigos_direct.setNestedScrollingEnabled(false);
        recyclerview_amigos_direct.setHasFixedSize(true);

        Loading.open(getActivity());

        if(APIServer.conexao(getActivity()) == true){
            Service_Direct.lista_amigos(getActivity(), lista_amigos, recyclerview_amigos_direct);
        }else{

        }

        return rootView;
    }
}
