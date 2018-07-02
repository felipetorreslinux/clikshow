package com.clikshow.SMS;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.R;
import com.clikshow.Service.Service_CellPhone;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Keyboard;

public class View_SMS_Code extends Activity implements View.OnClickListener{

    static EditText codeone , codetwo , codetree, codefour;

    TextView text_code_valid_sms;
    TextView wrong_number_phone;
    LinearLayout reenvia_code_phone;
    FrameLayout back_button_valid_code_phone;
    Button btn_valid_code_phone;

    static int valid_number_sms;

    @Override
    protected void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        setContentView(R.layout.view_sms_code);

        valid_number_sms = 0;

        text_code_valid_sms = (TextView) findViewById(R.id.text_code_valid_sms);
        text_code_valid_sms.setText(getResources().getString(R.string.label_text_valid_code_phone)+"\n+55 "+getIntent().getExtras().getString("cellphone_extra").toString());

        reenvia_code_phone = (LinearLayout) findViewById(R.id.reenvia_code_phone);
        reenvia_code_phone.setVisibility(View.GONE);

        new Thread(new Runnable() {
            public void run() {
                for (int progress=60; progress >= 0; progress-=1) {
                    try {
                        Thread.sleep(1000);
                        if(progress < 1)
                            reenvia_code_phone.setVisibility(View.VISIBLE);
                        else
                            reenvia_code_phone.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        back_button_valid_code_phone = (FrameLayout) findViewById(R.id.back_button_valid_code_phone);
        back_button_valid_code_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid_number_sms=1;
                onBackPressed();
            }
        });

        codeone = (EditText) findViewById(R.id.code_phone_one);
        codetwo = (EditText) findViewById(R.id.code_phone_two);
        codetree = (EditText) findViewById(R.id.code_phone_tree);
        codefour = (EditText) findViewById(R.id.code_phone_four);

        codeone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence a, int start, int before, int count) {
                if(a.length() == 1){
                    codetwo.requestFocus();
                }else{
                    codeone.requestFocus();
                }
            };

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        codetwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence b, int start, int before, int count) {
                if(b.length() == 1){
                    codetree.requestFocus();
                }else{
                    codetwo.requestFocus();
                };
            };

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        codetree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(c.length() == 1){
                    codefour.requestFocus();
                }else{
                    codetree.requestFocus();
                };
            };

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        codefour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence d, int start, int before, int count) {
                if(d.length() > 0){
                    btn_valid_code_phone.setVisibility(View.VISIBLE);
                    Keyboard.close(View_SMS_Code.this, getWindow().getDecorView());
                    if(APIServer.conexao(View_SMS_Code.this) == true){
                        String code = codeone.getText().toString()+""+codetwo.getText().toString()+""+codetree.getText().toString()+""+codefour.getText().toString();
                        Service_CellPhone.validarCodeSMS(View_SMS_Code.this, code, getIntent().getExtras().getString("cellphone_extra"));
                    }else{
                        ToastClass.curto(View_SMS_Code.this, "Aparelho offline. Verifique sua conexão");
                    }
                }else{
                    codetree.requestFocus();
                };
            };

            @Override
            public void afterTextChanged(Editable s) {}
        });


        wrong_number_phone = (TextView) findViewById(R.id.wrong_number_phone);
        wrong_number_phone.setOnClickListener(this);

        btn_valid_code_phone = (Button) findViewById(R.id.btn_valid_code_phone);
        btn_valid_code_phone.setOnClickListener(this);

    };

    @Override
    public void onBackPressed(){
        valid_number_sms=1;
        finish();
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.wrong_number_phone:
                onBackPressed();
                break;

            case R.id.btn_valid_code_phone:
                if(APIServer.conexao(this) == true){
                    String code = codeone.getText().toString()+""+codetwo.getText().toString()+""+codetree.getText().toString()+""+codefour.getText().toString();
                    Service_CellPhone.validarCodeSMS(View_SMS_Code.this, code, getIntent().getExtras().getString("cellphone_extra"));
                }else{
                    ToastClass.curto(this, "Aparelho offline. Verifique sua conexão");
                }
            break;
        }
    }
};

