package com.clikshow.Fragmentos;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clikshow.Fragmentos.Adapter.Ingressos_Detalhes_Adapter;
import com.clikshow.Fragmentos.Models.IngressosModel;
import com.clikshow.R;
import com.clikshow.Service.Service_Eventos;
import com.clikshow.Views.View_Detalhe_Evento;

import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class Detalhe_Evento_Ingressos_Fragment extends Fragment {

    private ShimmerLayout shimmerLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<IngressosModel> lista_ingressos = new ArrayList<>();
    private View rootView;
    private Ingressos_Detalhes_Adapter ingressos_detalhes_adapter;
    private IngressosModel ingressosModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        try {

            rootView = inflater.inflate(R.layout.fragment_detalhe_ingressos, container, false);
            shimmerLayout = (ShimmerLayout) rootView.findViewById(R.id.load_shimmer_lista_ingressos_detalhes);
            mRecyclerView = rootView.findViewById(R.id.rv_detalhe_ingressos);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setNestedScrollingEnabled(false);

            Service_Eventos.listar_ingressos(getActivity(), lista_ingressos, View_Detalhe_Evento.id_event, mRecyclerView, shimmerLayout);

        }catch (Exception e){
            shimmerLayout.startShimmerAnimation();
            shimmerLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
        return rootView;
    };

    @Override
    public void onResume(){
        super.onResume();
        shimmerLayout.startShimmerAnimation();
    }

    @Override
    public void onPause(){
        super.onPause();
        shimmerLayout.stopShimmerAnimation();
    }


}
