package com.clikshow.Direct;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.Direct.Models.Conversa_Model;
import com.clikshow.Direct.Service.Service_Direct;
import com.clikshow.R;
import com.clikshow.Splash;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class View_Chat_Direct extends Activity implements View.OnClickListener {

    String image_amigo;
    ImageView imageview_amigo_direct;
    ImageView imageview_back_conversa_direct;
    ImageView imageview_menu_conversa_direct;
    TextView textview_name_direct;
    TextView textview_username_direct;

    SharedPreferences sharedPreferences;

    List<Conversa_Model> list_chat_room = new ArrayList<>();
    RecyclerView recyclerview_conversa_direct;

    LinearLayoutManager mLinearLayoutManager;
    EditText edittexct_message_direct;
    ImageView imageview_record_direct;
    ImageView imageview_envia_direct;

    Service_Direct service_direct;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_conversa_direct);

        Splash.STATE_VIEW_CHAT = 1;

        service_direct = new Service_Direct(this);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        image_amigo = getIntent().getExtras().getString("image_amigo");
        imageview_amigo_direct = (ImageView) findViewById(R.id.imageview_amigo_direct);
        textview_name_direct = (TextView) findViewById(R.id.textview_name_direct);
        textview_username_direct = (TextView) findViewById(R.id.textview_username_direct);

        recyclerview_conversa_direct = (RecyclerView) findViewById(R.id.recyclerview_conversa_direct);
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerview_conversa_direct.setLayoutManager(mLinearLayoutManager);
        recyclerview_conversa_direct.setNestedScrollingEnabled(false);
        recyclerview_conversa_direct.setHasFixedSize(true);

        imageview_menu_conversa_direct = (ImageView) findViewById(R.id.imageview_menu_conversa_direct);
        imageview_menu_conversa_direct.setOnClickListener(this);

        imageview_back_conversa_direct = (ImageView) findViewById(R.id.imageview_back_conversa_direct);
        imageview_back_conversa_direct.setOnClickListener(this);

        edittexct_message_direct = (EditText) findViewById(R.id.edittexct_message_direct);

        imageview_record_direct = (ImageView) findViewById(R.id.imageview_record_direct);
        imageview_record_direct.setOnClickListener(this);

        imageview_envia_direct = (ImageView) findViewById(R.id.imageview_envia_direct);
        imageview_envia_direct.setOnClickListener(this);

        service_direct.chat(getIntent().getExtras().getString("id_amigo"), list_chat_room, recyclerview_conversa_direct);

    };

    @Override
    public void onResume(){
        super.onResume();
        amigoDirectDados();
        Splash.STATE_VIEW_CHAT = 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_back_conversa_direct:
                onBackPressed();
                break;

            case R.id.imageview_menu_conversa_direct:
                openMenuConversa();
                break;

            case R.id.imageview_envia_direct:
                enviaMessageDirect();
                break;
        }
    };

    @Override
    public void onBackPressed(){
        Splash.STATE_VIEW_CHAT = 0;
        finish();
    };

    @Override
    protected void onPause() {
        Splash.STATE_VIEW_CHAT = 0;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Splash.STATE_VIEW_CHAT = 0;
        super.onDestroy();
    }

    private void amigoDirectDados(){
        if(image_amigo.isEmpty() || image_amigo.equals("null")){
            Picasso.get()
                    .load(R.drawable.ic_profile)
                    .resize(100,100)
                    .transform(new CropCircleTransformation())
                    .into(imageview_amigo_direct);
        }else{
            Picasso.get()
                    .load(image_amigo)
                    .resize(100,100)
                    .transform(new CropCircleTransformation())
                    .into(imageview_amigo_direct);
        }
        textview_name_direct.setText(getIntent().getExtras().getString("name_amigo"));
    };

    private void openMenuConversa (){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_menu_conversa_direct, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();
    };

    private void enviaMessageDirect (){
        if(edittexct_message_direct.getText().toString().isEmpty()){
            edittexct_message_direct.setHint("Escreva algo antes de enviar...");
        }else{
            String receiver = getIntent().getExtras().getString("id_amigo");
            String message = edittexct_message_direct.getText().toString().trim();
            String type = "text";
            service_direct.send_message(receiver, message, type);
            edittexct_message_direct.setText(null);
            recyclerview_conversa_direct.scrollToPosition(recyclerview_conversa_direct.getAdapter().getItemCount() - 1);
        }
    };

}
