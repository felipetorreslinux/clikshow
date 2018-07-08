package com.clikshow.Direct;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.clikshow.FireBase.DirectFirebase;
import com.clikshow.R;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class View_Chat_Direct extends Activity implements View.OnClickListener {

    String image_amigo;
    ImageView imageview_amigo_direct;
    ImageView imageview_back_conversa_direct;
    ImageView imageview_menu_conversa_direct;
    TextView textview_name_direct;
    TextView textview_username_direct;

    SharedPreferences sharedPreferences;

    RecyclerView recyclerview_conversa_direct;

    EditText edittexct_message_direct;
    ImageView imageview_record_direct;
    ImageView imageview_envia_direct;

    DirectFirebase directFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_conversa_direct);

        directFirebase = new DirectFirebase(this);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        directFirebase.chat_room(this,
                String.valueOf(sharedPreferences.getInt("id", 0)),
                String.valueOf(getIntent().getExtras().getInt("id_amigo")));


        image_amigo = getIntent().getExtras().getString("image_amigo");
        imageview_amigo_direct = (ImageView) findViewById(R.id.imageview_amigo_direct);
        textview_name_direct = (TextView) findViewById(R.id.textview_name_direct);
        textview_username_direct = (TextView) findViewById(R.id.textview_username_direct);

        recyclerview_conversa_direct = (RecyclerView) findViewById(R.id.recyclerview_conversa_direct);
        recyclerview_conversa_direct.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_conversa_direct.setNestedScrollingEnabled(false);
        recyclerview_conversa_direct.setHasFixedSize(true);

        imageview_menu_conversa_direct = (ImageView) findViewById(R.id.imageview_menu_conversa_direct);
        imageview_menu_conversa_direct.setOnClickListener(this);

        imageview_back_conversa_direct = (ImageView) findViewById(R.id.imageview_back_conversa_direct);
        imageview_back_conversa_direct.setOnClickListener(this);

        edittexct_message_direct = (EditText) findViewById(R.id.edittexct_message_direct);

        edittexct_message_direct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    directFirebase.userDigitingOn(View_Chat_Direct.this);

                }else{
                    directFirebase.userDigitingOff(View_Chat_Direct.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        imageview_record_direct = (ImageView) findViewById(R.id.imageview_record_direct);
        imageview_record_direct.setOnClickListener(this);

        imageview_envia_direct = (ImageView) findViewById(R.id.imageview_envia_direct);
        imageview_envia_direct.setOnClickListener(this);
    };

    @Override
    public void onResume(){
        super.onResume();
        amigoDirectDados();
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
        finish();
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
            directFirebase.sendMessage(getIntent().getExtras().getInt("id_amigo"), edittexct_message_direct.getText().toString().trim());
            edittexct_message_direct.setText(null);
        }
    };
}
