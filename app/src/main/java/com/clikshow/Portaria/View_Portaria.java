package com.clikshow.Portaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.Portaria.Fragments.Inicio_Portaria_Fragment;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class View_Portaria extends Activity implements View.OnClickListener{

    ImageView imageview_back_porteiro;
    ImageView image_view_open_camera_porteiro;
    SurfaceView surfaceview_camera_porteiro;

    SharedPreferences sharedPreferences;
    DrawerLayout drawerlayout_portaria;
    NavigationView nav_drawer_portaria;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_portaria);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        image_view_open_camera_porteiro = (ImageView) findViewById(R.id.image_view_open_camera_porteiro);
        image_view_open_camera_porteiro.setOnClickListener(this);

        surfaceview_camera_porteiro = (SurfaceView) findViewById(R.id.surfaceview_camera_porteiro);

        imageview_back_porteiro = (ImageView) findViewById(R.id.imageview_back_porteiro);
        imageview_back_porteiro.setOnClickListener(this);

    };

    @Override
    protected void onResume() {
        super.onResume();
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_back_porteiro:
                onBackPressed();
                break;

            case R.id.btn_search_portaria:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        encerrarPortaria();
    }

    private void encerrarPortaria(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Deseja realmente sair da portaria?");
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.negative_button_dialog, null);
        builder.create().show();
    }
}
