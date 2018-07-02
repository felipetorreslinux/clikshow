package com.clikshow.Login;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.Profile.Service.Service_Profile;
import com.clikshow.R;
import com.clikshow.Utils.Keyboard;
import com.clikshow.Validation.Email;

import android.app.Activity;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class View_Recovery_Password extends Activity implements View.OnClickListener {

    ViewStub viewstub_conexao_recovery_password;
    ImageView imageview_back_recovery_password;
    EditText email_recovery_password;
    Button button_recovery_password;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.view_recovery_password);

        viewstub_conexao_recovery_password = (ViewStub) findViewById(R.id.viewstub_conexao_recovery_password);

        imageview_back_recovery_password = (ImageView) findViewById(R.id.imageview_back_recovery_password);
        imageview_back_recovery_password.setOnClickListener(this);

        email_recovery_password = (EditText) findViewById(R.id.email_recovery_password);
        button_recovery_password = (Button) findViewById(R.id.button_recovery_password);
        button_recovery_password.setOnClickListener(this);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Keyboard.open(View_Recovery_Password.this, email_recovery_password);
            }
        }, 100);

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_back_recovery_password:
                onBackPressed();
                break;

            case R.id.button_recovery_password:

                if(email_recovery_password.getText().toString().isEmpty()){
                    email_recovery_password.setHint("Informe seu email aqui...");
                    email_recovery_password.requestFocus();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Keyboard.open(View_Recovery_Password.this, email_recovery_password);
                        }
                    }, 100);
                }else if(Email.valid(this, email_recovery_password) == false){
                    email_recovery_password.setText(null);
                    email_recovery_password.setHint("Email inválido ou não existe");
                    email_recovery_password.setHintTextColor(getResources().getColor(R.color.red_of_problem));
                    email_recovery_password.requestFocus();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Keyboard.open(View_Recovery_Password.this, email_recovery_password);
                        }
                    }, 100);
                }else{
                    Keyboard.close(this, getWindow().getDecorView());
                    if(APIServer.conexao(this) == true){
                        viewstub_conexao_recovery_password.setVisibility(View.GONE);
                        Service_Profile.recovery_pass(this, email_recovery_password);
                    }else{
                        viewstub_conexao_recovery_password.inflate();
                    }
                }
                break;
        }
    };

    @Override
    public void onBackPressed(){
        finish();
    };
}
