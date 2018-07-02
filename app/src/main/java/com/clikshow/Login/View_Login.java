package com.clikshow.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Profile.View_Cadastro_Novo_Usuario;
import com.clikshow.R;
import com.clikshow.Service.Service_Login;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Keyboard;

public class View_Login extends Activity implements View.OnClickListener {

    private EditText email;
    private EditText senha;
    private Button btn_login;
    private TextView open_new_profile_login;
    private TextView open_recovery_password;
    private FrameLayout back_button_login;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login);

        open_new_profile_login = (TextView) findViewById(R.id.open_new_profile_login);
        open_new_profile_login.setOnClickListener(this);

        open_recovery_password = (TextView) findViewById(R.id.open_recovery_password);
        open_recovery_password.setOnClickListener(this);

        email = (EditText) findViewById(R.id.input_email_login);
        senha = (EditText) findViewById(R.id.input_password_login);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Keyboard.open(View_Login.this, email);
            }
        }, 100);

        btn_login = (Button) findViewById(R.id.btn_login_app);
        btn_login.setOnClickListener(this);

        back_button_login = (FrameLayout) findViewById(R.id.back_button_login);
        back_button_login.setOnClickListener(this);
    };

    @Override
    public void onBackPressed(){
        finish();
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back_button_login:
                onBackPressed();
                break;

            case R.id.open_new_profile_login:
                startActivity(new Intent(this, View_Cadastro_Novo_Usuario.class));
                break;

            case R.id.open_recovery_password:
                startActivity(new Intent (this, View_Recovery_Password.class));
                break;

            case R.id.btn_login_app:
                if(email.getText().toString().isEmpty()) {
                    email.setHint("Informe seu email ou CPF");
                    email.requestFocus();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Keyboard.open(View_Login.this, email);
                        }
                    }, 100);
                }else if(senha.getText().toString().isEmpty()){
                    senha.setHint("Informe sua senha");
                    senha.requestFocus();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Keyboard.open(View_Login.this, senha);
                        }
                    }, 100);
                }else if(senha.getText().toString().length() < 6){
                    senha.setText(null);
                    senha.setHint("Senha muito curta");
                    senha.requestFocus();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Keyboard.open(View_Login.this, senha);
                        }
                    }, 100);
                }else{
                    Keyboard.close(this, getWindow().getDecorView());
                    if (APIServer.conexao(this) == true) {
                        Service_Login.logar(this, email.getText(), senha.getText());
                    }else{
                        ToastClass.curto(this, "Aparelho sem conexão com a internet");
                    }
                };
                break;
        }
    }

};
