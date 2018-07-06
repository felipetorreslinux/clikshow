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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.Portaria.Fragments.Inicio_Portaria_Fragment;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class View_Portaria extends Activity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    ImageView btn_menu_portaria;
    ImageView btn_search_portaria;

    SharedPreferences sharedPreferences;
    DrawerLayout drawerlayout_portaria;
    NavigationView nav_drawer_portaria;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_portaria);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        drawerlayout_portaria = (DrawerLayout) findViewById(R.id.drawerlayout_portaria);
        drawerlayout_portaria.closeDrawer(Gravity.START);

        nav_drawer_portaria = (NavigationView) findViewById(R.id.nav_drawer_portaria);
        nav_drawer_portaria.setNavigationItemSelectedListener(this);
        itensMenuDrawerPortaria();

        btn_menu_portaria = (ImageView) findViewById(R.id.btn_menu_portaria);
        btn_menu_portaria.setOnClickListener(this);

        btn_search_portaria = (ImageView) findViewById(R.id.btn_search_portaria);
        btn_search_portaria.setOnClickListener(this);

    };

    @Override
    protected void onResume() {
        super.onResume();
        getFragmentManager().beginTransaction().replace(R.id.container_portaria, new Inicio_Portaria_Fragment()).commit();
    };


    private void itensMenuDrawerPortaria(){
        View header = nav_drawer_portaria.getHeaderView(0);
        ImageView imageview_profile_portaria = header.findViewById(R.id.imageview_profile_portaria);
        TextView textview_name_prolife_portaria = header.findViewById(R.id.textview_name_prolife_portaria);
        TextView textview_email_prolife_portaria = header.findViewById(R.id.textview_email_prolife_portaria);

        textview_name_prolife_portaria.setText(sharedPreferences.getString("name", null));
        textview_email_prolife_portaria.setText(sharedPreferences.getString("email", null));

        Picasso.get()
                .load(sharedPreferences.getString("profile_pic", null))
                .resize(100, 100)
                .transform(new CropCircleTransformation())
                .into(imageview_profile_portaria);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_menu_portaria:
                drawerlayout_portaria.openDrawer(Gravity.LEFT);
                break;

            case R.id.btn_search_portaria:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if(drawerlayout_portaria.isDrawerOpen(Gravity.LEFT)){
            drawerlayout_portaria.closeDrawer(Gravity.START);
        }else{
            encerrarPortaria();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.encerrar_portaria:
                encerrarPortaria();
                break;
        }
        drawerlayout_portaria.closeDrawer(Gravity.START);
        return true;
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
