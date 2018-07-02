package com.clikshow.Views;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;

import java.util.ArrayList;
import java.util.Locale;

public class View_Search extends Activity implements View.OnClickListener{

    ImageView back_search;
    ImageView icon_mic_search;
    EditText text_search;
    Button btn_buscar_evento;
    InputMethodManager inputMethodManager;
    SharedPreferences sharedPreferences;

    String[] nome_user;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    Handler handler = new Handler();

    RelativeLayout box_searh;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_search);

        box_searh = (RelativeLayout) findViewById(R.id.box_searh);

        animation();

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        nome_user = sharedPreferences.getString("name", null).split(" ");

        icon_mic_search = (ImageView) findViewById(R.id.icon_mic_search);
        icon_mic_search.setOnClickListener(this);

        btn_buscar_evento = (Button) findViewById(R.id.btn_buscar_evento);
        btn_buscar_evento.setEnabled(false);
        btn_buscar_evento.setVisibility(View.GONE);
        btn_buscar_evento.setOnClickListener(this);

        text_search = (EditText) findViewById(R.id.text_search);
        text_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    btn_buscar_evento.setEnabled(true);
                    btn_buscar_evento.setVisibility(View.VISIBLE);
                }else{
                    btn_buscar_evento.setEnabled(false);
                    btn_buscar_evento.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        back_search = (ImageView) findViewById(R.id.back_search);
        back_search.setOnClickListener(this);

    }

    @Override
    public void onResume(){
        super.onResume();
        animation();
    }

    private void animation(){
        Animation animation = new TranslateAnimation(0, 0,2000, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        box_searh.startAnimation(animation);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_search:
                onBackPressed();
            break;

            case R.id.icon_mic_search:
                falaBuscarEvento();
            break;

            case R.id.btn_buscar_evento:
                if(text_search.getText().toString().isEmpty()){
                    ToastClass.curto(this, "Informe algo para pesquisar...");
                }else{
                    Intent intent = new Intent(this, View_Resultado_Search.class);
                    intent.putExtra("resultado", text_search.getText().toString());
                    startActivityForResult(intent, 101);
                }
            break;
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }


    private void falaBuscarEvento() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Olá "+nome_user[0]+"" +
                        "\nDiga o nome do evento");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text_search.setText(result.get(0));
                }
                break;
            }

            case 101:
                if(resultCode == RESULT_OK){
                    text_search.setText(null);
                    btn_buscar_evento.setVisibility(View.GONE);
                }
                if(resultCode == RESULT_CANCELED){
                    finish();
                }
                break;

        }
    }
}
