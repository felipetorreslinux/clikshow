package com.clikshow.Fragmentos;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.androidnetworking.AndroidNetworking;
import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Adapter.Favoritos_Tab_Adapter;
import com.clikshow.Fragmentos.Models.FavoritosModel;
import com.clikshow.R;
import com.clikshow.Service.Service_Favoritos;

import java.util.ArrayList;
import java.util.List;

public class Favorites_Fragment extends Fragment{
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Favoritos_Tab_Adapter favoritos_tab_adapter;
    private List<FavoritosModel> lista_favoritos = new ArrayList<>();

    private ViewStub box_sem_itens_favoritos;
    private ViewStub box_sem_conexao_favoritos;
    private ViewStub loading_tab_favorites;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        box_sem_conexao_favoritos = (ViewStub) rootView.findViewById(R.id.box_sem_conexao_favoritos);
        box_sem_itens_favoritos = (ViewStub) rootView.findViewById(R.id.box_not_favoritados);
        loading_tab_favorites = (ViewStub) rootView.findViewById(R.id.loading_tab_favorites);

        if(APIServer.conexao(getActivity()) == true){
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_favoritos);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            loading_tab_favorites.inflate();
            Service_Favoritos.listar(getActivity(), lista_favoritos, mRecyclerView, box_sem_itens_favoritos, loading_tab_favorites);
        }else{
            box_sem_conexao_favoritos.inflate();
            lista_favoritos.clear();
        }
        return  rootView;
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(APIServer.conexao(getActivity()) == false){
            AndroidNetworking.forceCancelAll();
        }

    }
}
