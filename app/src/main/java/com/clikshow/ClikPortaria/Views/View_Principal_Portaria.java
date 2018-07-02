package com.clikshow.ClikPortaria.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.ClikBIlheteria.Views.View_Lista_Ingressos_Bilheteria;

import com.clikshow.ClikPortaria.Views.Service.Service_Portaria;
import com.clikshow.IUGU.Models.Service.Cartao_Credito;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import io.supercharge.shimmerlayout.ShimmerLayout;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class View_Principal_Portaria extends Activity {

    private ImageView img_user_logado_portaria;
    private ImageView btn_exit_portaria;
    private ImageView btn_open_qrcode_portaria;
    private SharedPreferences sharedPreferences;
    private InputMethodManager inputMethodManager;
    EditText cpf_portaria;

    private RelativeLayout box_valida_portaria;

    private TextView btn_validar_ingresso_portaria;

    private Timer timer = new Timer();

    private static int EVENT_ID_PORTARIA;
    String cpf = null;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_principal_portaria);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        EVENT_ID_PORTARIA = getIntent().getExtras().getInt("event_id");

        cpf_portaria = (EditText) findViewById(R.id.cpf_portaria);

        cpf_portaria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 11){
                    if(APIServer.conexao(View_Principal_Portaria.this) == true){
                        Service_Portaria.checar_ingresso(View_Principal_Portaria.this,
                                "",
                                0,
                                cpf_portaria.getText().toString(),
                                EVENT_ID_PORTARIA,
                                0);
                    }else{
                        ToastClass.curto(View_Principal_Portaria.this, "Aparelho offline\nVerifique sua conexão");
                    }
                }
            }
        });

        img_user_logado_portaria = (ImageView) findViewById(R.id.img_user_logado_portaria);
        Picasso.get()
            .load(Uri.fromFile(new File(sharedPreferences.getString("profile_pic", null))))
            .transform(new CropCircleTransformation())
            .resize(100,100)
            .into(img_user_logado_portaria);

        btn_open_qrcode_portaria = (ImageView) findViewById(R.id.btn_open_qrcode_portaria);
        btn_open_qrcode_portaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQRCode();
            }
        });

        btn_exit_portaria = (ImageView) findViewById(R.id.btn_exit_portaria);
        btn_exit_portaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    };

    private void openQRCode(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setPrompt("");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.initiateScan();
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null){
            if(intentResult.getContents() != null){
                String op = intentResult.getContents();
                try {
                    JSONObject jsonObject = new JSONObject(op);
                    cpf = jsonObject.getString("cpf").replace(".", "").replace("-", "");
                    if(cpf.length() == 14){
                        ToastClass.curto(View_Principal_Portaria.this, "Erro de leitura");
                    }else{
                        cpf_portaria.setText(cpf);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                finish();
            }
        } else {

        }

        if(resultCode == RESULT_OK){
            cpf_portaria.setText(null);
        }
        if(resultCode == RESULT_CANCELED){
            cpf_portaria.setText(null);
        }
    };

    @Override
    public void onBackPressed(){
        finish();
    };


    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}
