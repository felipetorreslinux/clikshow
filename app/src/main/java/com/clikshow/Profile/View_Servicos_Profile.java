package com.clikshow.Profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.clikshow.API.APIServer;
import com.clikshow.Profile.Models.ServicosProfileModel;
import com.clikshow.Profile.Service.Service_Profile;
import com.clikshow.R;

import java.util.ArrayList;
import java.util.List;

public class View_Servicos_Profile extends Activity implements View.OnClickListener{

    ImageView back_servicos_profile;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    List<ServicosProfileModel> lista_servicos_profile = new ArrayList<>();
    ViewStub box_sem_net_servicos_profile;
    ViewStub loading_servicos_profile;
    ViewStub view_not_service_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_servicos_profile);

        box_sem_net_servicos_profile = (ViewStub) findViewById(R.id.box_sem_net_servicos_profile);
        loading_servicos_profile = (ViewStub) findViewById(R.id.loading_servicos_profile);
        view_not_service_profile = (ViewStub) findViewById(R.id.view_not_service_profile);
        mRecyclerView = (RecyclerView) findViewById(R.id.lista_servicos_profile);
        mLayoutManager = new LinearLayoutManager(View_Servicos_Profile.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        if(APIServer.conexao(this) == true){
            Service_Profile.lista_jobs(this, lista_servicos_profile, mRecyclerView, loading_servicos_profile, view_not_service_profile);
            box_sem_net_servicos_profile.setVisibility(View.GONE);
        }else{
            box_sem_net_servicos_profile.inflate();
        };

        back_servicos_profile = (ImageView) findViewById(R.id.back_servicos_profile);
        back_servicos_profile.setOnClickListener(this);
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_servicos_profile:
                onBackPressed();
                break;
        }
    };

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1000:
                if(resultCode == RESULT_OK){

                }
                if(resultCode == RESULT_CANCELED){

                }
                break;
        }
    }
}
