package com.clikshow.SMS;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.clikshow.API.APIServer;
import com.clikshow.R;
import com.clikshow.Service.Service_CellPhone;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Keyboard;

public class View_Digita_Cellphone extends Activity implements View.OnClickListener {

    private EditText input_digita_phone;
    private Button btn_digite_phone;
    private FrameLayout back_button_digite_phone;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_digita_cellphone);

        back_button_digite_phone = (FrameLayout) findViewById(R.id.back_button_digite_phone);
        back_button_digite_phone.setOnClickListener(this);

        input_digita_phone = (EditText) findViewById(R.id.input_digita_phone);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Keyboard.open(View_Digita_Cellphone.this, input_digita_phone);
            }
        }, 300);

        input_digita_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 11){
                    btn_digite_phone.setVisibility(View.VISIBLE);
                }else{
                    btn_digite_phone.setVisibility(View.GONE);
                };
            };
        });

        btn_digite_phone = (Button) findViewById(R.id.btn_digite_phone);
        btn_digite_phone.setVisibility(View.GONE);
        btn_digite_phone.setOnClickListener(this);
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button_digite_phone:
                onBackPressed();
                break;
            case R.id.btn_digite_phone:

                if(input_digita_phone.getText().toString().isEmpty()){
                    input_digita_phone.setHint("Informe seu telefone com DDD");
                    input_digita_phone.requestFocus();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Keyboard.open(View_Digita_Cellphone.this, input_digita_phone);
                        }
                    }, 100);
                }else if(input_digita_phone.getText().length() < 11){
                    input_digita_phone.setText(null);
                    input_digita_phone.setHint("Telefone inválido ou não existe");
                    input_digita_phone.setHintTextColor(getResources().getColor(R.color.red_of_problem));
                    input_digita_phone.requestFocus();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Keyboard.open(View_Digita_Cellphone.this, input_digita_phone);
                        }
                    }, 100);
                }else{
                    if(APIServer.conexao(this) == true){
                        Keyboard.close(this, getWindow().getDecorView());
                        Service_CellPhone.check(View_Digita_Cellphone.this, input_digita_phone);
                    }else{
                        ToastClass.curto(this, "Aparelho offline. Verifique sua conexão");
                    }
                };
                break;
        }
    };

    @Override
    public void onBackPressed(){
        finish();
    };
};
