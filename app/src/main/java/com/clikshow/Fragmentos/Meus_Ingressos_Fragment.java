package com.clikshow.Fragmentos;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Models.MeusIngressosModel;
import com.clikshow.QRCode.QRCodeClass;
import com.clikshow.R;
import com.clikshow.Service.Service_Meus_Ingressos;
import com.clikshow.Service.Toast.ToastClass;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Meus_Ingressos_Fragment extends Fragment {

    private ViewStub loading_tab_ingressos;
    private ViewStub box_sem_ingressos;
    private SwipeRefreshLayout pull_refresh_ingressos;

    LinearLayout box_qrcode_sem_net;
    ImageView imageview_qrdcode_sem_net;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MeusIngressosModel> lista_ingressos = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_meus_ingressos, container, false);

        loading_tab_ingressos = (ViewStub) rootView.findViewById(R.id.loading_tab_ingressos);
        box_sem_ingressos = (ViewStub) rootView.findViewById(R.id.box_sem_ingressos);

        box_qrcode_sem_net = (LinearLayout) rootView.findViewById(R.id.box_qrcode_sem_net);
        imageview_qrdcode_sem_net = (ImageView) rootView.findViewById(R.id.imageview_qrdcode_sem_net);

        pull_refresh_ingressos = (SwipeRefreshLayout) rootView.findViewById(R.id.pull_refresh_ingressos);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_meus_ingressos);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        if(APIServer.conexao(getActivity()) == true){
            box_qrcode_sem_net.setVisibility(View.GONE);
            loading_tab_ingressos.setVisibility(View.VISIBLE);
            lista_ingressos.clear();
            Service_Meus_Ingressos.listar_novos_ingressos(getActivity(), lista_ingressos, mRecyclerView, loading_tab_ingressos, box_sem_ingressos);

        }else{
            loading_tab_ingressos.setVisibility(View.GONE);
            box_qrcode_sem_net.setVisibility(View.VISIBLE);
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            try {
                MultiFormatWriter multiFormatWriter= new MultiFormatWriter();
                BitMatrix bitMatrix = null;
                bitMatrix = multiFormatWriter.encode(QRCodeClass.qrcode_profile(getActivity()), BarcodeFormat.QR_CODE, 450, 450);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                imageview_qrdcode_sem_net.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        };

        pull_refresh_ingressos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(APIServer.conexao(getActivity()) == true){
                    Service_Meus_Ingressos.listar_novos_ingressos(getActivity(), lista_ingressos, mRecyclerView, loading_tab_ingressos, box_sem_ingressos);
                    pull_refresh_ingressos.setRefreshing(false);
                }else{
                    pull_refresh_ingressos.setRefreshing(false);
                };
            }
        });

        return  rootView;
    };

    @Override
    public void onResume(){
        super.onResume();
    };

    @Override
    public void onPause(){
        super.onPause();
    };

}
