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
import com.clikshow.Login.View_Login;
import com.clikshow.Profile.View_Editar_Usuario;
import com.clikshow.Views.View_Principal;
import com.clikshow.Profile.View_Cadastro_Novo_Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.internal.natives.jni.CallClient;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Splash extends Activity implements View.OnClickListener {

    private LinearLayout button_open_login;
    private LinearLayout button_open_novo_usuario;
    SharedPreferences sharedPreferences;
    Call call;
    public static SinchClient sinchClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(120,TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId("106954")
                .applicationKey("cb65bade-5c6a-4f78-aa15-5a01813893dd")
                .applicationSecret("lzzV38Y9VEqZau0koW4stQ==")
                .environmentHost("clientapi.sinch.com")
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.start();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                1);



        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        button_open_login = (LinearLayout) findViewById(R.id.button_open_login);
        button_open_login.setOnClickListener(this);

        button_open_novo_usuario = (LinearLayout) findViewById(R.id.button_open_novo_usuario);
        button_open_novo_usuario.setOnClickListener(this);

        verificarLogados();
    };

    public void verificarLogados(){
        if(sharedPreferences.getBoolean("is_login", false) == true){
            startActivity(new Intent(Splash.this, View_Principal.class));
            finish();
        }else{
            return;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1001:
                if(resultCode == RESULT_OK){

                }
                if(resultCode == RESULT_CANCELED){

                }
                break;
        }
    }

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
