package com.clikshow;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.clikshow.API.APIServer;
import com.clikshow.FireBase.FireApp;
import com.clikshow.Login.View_Login;
import com.clikshow.Profile.View_Editar_Usuario;
import com.clikshow.Views.View_Principal;
import com.clikshow.Profile.View_Cadastro_Novo_Usuario;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Splash extends Activity implements View.OnClickListener {

    private LinearLayout button_open_login;
    private LinearLayout button_open_novo_usuario;
    SharedPreferences sharedPreferences;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(120,TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);

        button_open_login = (LinearLayout) findViewById(R.id.button_open_login);
        button_open_login.setOnClickListener(this);

        button_open_novo_usuario = (LinearLayout) findViewById(R.id.button_open_novo_usuario);
        button_open_novo_usuario.setOnClickListener(this);

        verificarLogados();

        Firebase firebase = FireApp.getFirebase().child("direct").child("usuarios");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "felipe torres");
        firebase.updateChildren(map);
    }

    public void verificarLogados(){
        if(sharedPreferences.getBoolean("is_login", false) == true){
            startActivity(new Intent(Splash.this, View_Principal.class));
            finish();
        }else{
            return;
        }
    };

    @Override
    public void onBackPressed(){
        finish();
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_open_login:
                startActivity(new Intent(Splash.this, View_Login.class));
                break;

            case R.id.button_open_novo_usuario:
                startActivityForResult(new Intent(Splash.this, View_Cadastro_Novo_Usuario.class), 1001);
                break;
        }
    }

};
