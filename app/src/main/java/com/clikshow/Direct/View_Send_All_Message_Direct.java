package com.clikshow.Direct;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.clikshow.Direct.Service.Service_Direct;
import com.clikshow.R;
import com.clikshow.Utils.Keyboard;

public class View_Send_All_Message_Direct extends Activity implements View.OnClickListener {

    EditText message_send_all_direct;
    ImageView image_send_all_direct;
    Service_Direct service_direct;

    LinearLayout loading_send_message;
    LottieAnimationView image_loading_send_message;
    TextView text_loading_send_message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_send_all_message_direct);

        service_direct = new Service_Direct(this);

        loading_send_message = (LinearLayout) findViewById(R.id.loading_send_message);
        image_loading_send_message = (LottieAnimationView) findViewById(R.id.image_loading_send_message);
        text_loading_send_message = (TextView) findViewById(R.id.text_loading_send_message);

        loading_send_message.setVisibility(View.GONE);

        message_send_all_direct = (EditText) findViewById(R.id.message_send_all_direct);
        image_send_all_direct = (ImageView) findViewById(R.id.image_send_all_direct);
        image_send_all_direct.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_send_all_direct:
                sendAllDirect();
                break;
        }
    }

    private void sendAllDirect() {
        String message = message_send_all_direct.getText().toString().trim();
        if(message.isEmpty()){
            message_send_all_direct.setHint("Escreva antes de enviar...");
        }else{
            Keyboard.close(this, getWindow().getDecorView());
            animationSendAll();
            message_send_all_direct.setHint("Escreva aqui...");
            service_direct.send_all_direct(message, "text");
            message_send_all_direct.setText(null);

        }
    }

    private void animationSendAll() {

        loading_send_message.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                image_loading_send_message.setAnimation(R.raw.confirm);
                text_loading_send_message.setText("Mensagens enviadas com sucesso");
            }
        }, 5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loading_send_message.setVisibility(View.GONE);
                image_loading_send_message.setAnimation(R.raw.envia_sms);
                text_loading_send_message.setText("Enviando mensagens");
                finish();
            }
        }, 8000);

    }
}
