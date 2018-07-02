package com.clikshow.Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Profile.Service.Service_Profile;
import com.clikshow.R;
import com.clikshow.Utils.Keyboard;
import com.clikshow.Validation.Email;
import com.clikshow.Views.View_Termos_Condicoes;

public class View_Cadastro_Novo_Usuario extends Activity implements View.OnClickListener {

    public static String GENRE_NOVA_CONTA = null;
    public static String BIRTHDAY_NOVA_CONTA = null;

    Handler handler = new Handler();
    ViewStub viewstub_conexao_nova_conta;
    LinearLayout box_dados_nova_conta;
    ImageView back_new_profile;
    EditText cpf_nova_conta;
    EditText name_nova_conta;
    EditText username_nova_conta;
    EditText email_nova_conta;
    EditText password_nova_conta;
    EditText conf_password_nova_conta;
    TextView textview_ler_termos_condicoes;
    CheckBox checkbox_termos_nova_conta;
    Button button_nova_conta;

    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.view_nova_conta_profile);

        viewstub_conexao_nova_conta = (ViewStub) findViewById(R.id.viewstub_conexao_nova_conta);

        back_new_profile = (ImageView) findViewById(R.id.back_new_profile);
        back_new_profile.setOnClickListener(this);

        box_dados_nova_conta = (LinearLayout) findViewById(R.id.box_dados_nova_conta);
        box_dados_nova_conta.setVisibility(View.GONE);

        cpf_nova_conta = (EditText) findViewById(R.id.cpf_nova_conta);
        name_nova_conta = (EditText) findViewById(R.id.name_nova_conta);
        username_nova_conta = (EditText) findViewById(R.id.username_nova_conta);
        email_nova_conta = (EditText) findViewById(R.id.email_nova_conta);
        password_nova_conta = (EditText) findViewById(R.id.password_nova_conta);
        conf_password_nova_conta = (EditText) findViewById(R.id.conf_password_nova_conta);
        textview_ler_termos_condicoes = (TextView) findViewById(R.id.textview_ler_termos_condicoes);
        textview_ler_termos_condicoes.setOnClickListener(this);
        checkbox_termos_nova_conta = (CheckBox) findViewById(R.id.checkbox_termos_nova_conta);
        button_nova_conta = (Button) findViewById(R.id.button_nova_conta);
        button_nova_conta.setOnClickListener(this);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Keyboard.open(View_Cadastro_Novo_Usuario.this, cpf_nova_conta);
            }
        }, 100);

        cpf_nova_conta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 11){
                    if(APIServer.conexao(View_Cadastro_Novo_Usuario.this) == true){
                        viewstub_conexao_nova_conta.setVisibility(View.GONE);
                        Keyboard.close(View_Cadastro_Novo_Usuario.this, getWindow().getDecorView());
                        Service_Profile.check_cpf(View_Cadastro_Novo_Usuario.this, cpf_nova_conta, name_nova_conta, username_nova_conta, email_nova_conta, box_dados_nova_conta);
                    }else{
                        viewstub_conexao_nova_conta.inflate();
                    }
                }else{
                    name_nova_conta.setText(null);
                    username_nova_conta.setText(null);
                    box_dados_nova_conta.setVisibility(View.GONE);
                }
            }
        });

        email_nova_conta.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    if(email_nova_conta.getText().toString().isEmpty()){
                        email_nova_conta.setText(null);
                        email_nova_conta.setHint("Informe seu email");
                        email_nova_conta.setHintTextColor(getResources().getColor(R.color.red_of_problem));
                    }else if(Email.valid(View_Cadastro_Novo_Usuario.this, email_nova_conta) == false){
                        email_nova_conta.setText(null);
                        email_nova_conta.setHint("Email inválido ou não existe");
                        email_nova_conta.setHintTextColor(getResources().getColor(R.color.red_of_problem));
                        email_nova_conta.requestFocus();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Keyboard.open(View_Cadastro_Novo_Usuario.this, email_nova_conta);
                            }
                        }, 100);
                    };
                };
            };
        });

    };

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (view.getId()){
            case R.id.back_new_profile:
                onBackPressed();
                break;

            case R.id.textview_ler_termos_condicoes:
                startActivity(new Intent(View_Cadastro_Novo_Usuario.this, View_Termos_Condicoes.class));
                break;

            case R.id.button_nova_conta:
                if(APIServer.conexao(this) == true){
                    viewstub_conexao_nova_conta.setVisibility(View.GONE);
                    if(username_nova_conta.getText().toString().isEmpty()){
                        username_nova_conta.setHintTextColor(getResources().getColor(R.color.red_of_problem));
                        username_nova_conta.setHint("Crie seu usuário");
                        username_nova_conta.requestFocus();
                        Keyboard.open(this, username_nova_conta);
                    }else if(email_nova_conta.getText().toString().isEmpty()){
                        email_nova_conta.setHintTextColor(getResources().getColor(R.color.red_of_problem));
                        email_nova_conta.setHint("Informe seu email");
                        email_nova_conta.requestFocus();
                        Keyboard.open(this, email_nova_conta);
                    }else if(password_nova_conta.getText().toString().isEmpty()){
                        password_nova_conta.setHintTextColor(getResources().getColor(R.color.red_of_problem));
                        password_nova_conta.setHint("Informe sua senha");
                        password_nova_conta.requestFocus();
                        Keyboard.open(this, password_nova_conta);
                    }else if(password_nova_conta.getText().length() < 6){
                        password_nova_conta.setText(null);
                        password_nova_conta.setHintTextColor(getResources().getColor(R.color.red_of_problem));
                        password_nova_conta.setHint("Senha muito curta. Mínimo 6 caracteres");
                        password_nova_conta.requestFocus();
                        Keyboard.open(this, password_nova_conta);
                    }else if(!password_nova_conta.getText().toString().equals(conf_password_nova_conta.getText().toString())) {
                        builder.setTitle(R.string.app_name);
                        builder.setMessage("Senhas não conferem");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                password_nova_conta.setText(null);
                                conf_password_nova_conta.setText(null);
                                password_nova_conta.requestFocus();
                                Keyboard.open(View_Cadastro_Novo_Usuario.this, password_nova_conta);
                            }
                        });
                        builder.create().show();
                    }else if(checkbox_termos_nova_conta.isChecked() == false){
                        builder.setTitle(R.string.app_name);
                        builder.setMessage("Você precisa aceitar os Termos e Condições");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Eu aceito", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkbox_termos_nova_conta.setChecked(true);
                            }
                        });
                        builder.setNegativeButton("Não aceito", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkbox_termos_nova_conta.setChecked(false);
                            }
                        });
                        builder.create().show();
                    }else{
                        Keyboard.close(this, getWindow().getDecorView());
                        Service_Profile.add(this, name_nova_conta, email_nova_conta, username_nova_conta, password_nova_conta, cpf_nova_conta);
                    }
                }else{
                    viewstub_conexao_nova_conta.inflate();
                }
                break;
        };
    };

    @Override
    public void onBackPressed(){
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    };
};
