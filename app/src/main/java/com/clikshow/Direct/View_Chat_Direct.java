package com.clikshow.Direct;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.Direct.Models.Conversa_Model;
import com.clikshow.Direct.Service.Service_Direct;
import com.clikshow.R;
import com.clikshow.Splash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class View_Chat_Direct extends Activity implements View.OnClickListener {

    String image_amigo;
    ImageView imageview_amigo_direct;
    ImageView imageview_back_conversa_direct;
    ImageView imageview_menu_conversa_direct;
    TextView textview_name_direct;
    public static TextView status_chat_direct;

    SharedPreferences sharedPreferences;

    List<Conversa_Model> list_chat_room = new ArrayList<>();
    RecyclerView recyclerview_conversa_direct;

    LinearLayoutManager mLinearLayoutManager;
    EditText edittexct_message_direct;
    ImageView imageview_envia_direct;

    Service_Direct service_direct;

    public static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_conversa_direct);

        DatabaseReference databaseReference;

        service_direct = new Service_Direct(this);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        image_amigo = getIntent().getExtras().getString("image_amigo");

        imageview_amigo_direct = (ImageView) findViewById(R.id.imageview_amigo_direct);
        textview_name_direct = (TextView) findViewById(R.id.textview_name_direct);
        status_chat_direct = (TextView) findViewById(R.id.status_chat_direct);

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
        edittexct_message_direct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    String receiver = getIntent().getExtras().getString("id_amigo");
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("rooms").child(receiver).child(String.valueOf(sharedPreferences.getInt("id", 0)));
                    Map<String, Object> me = new HashMap<>();
                    me.put("last_message", "Digitando...");
                    databaseReference.updateChildren(me);

                    databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("rooms").child(String.valueOf(sharedPreferences.getInt("id", 0))).child(receiver);
                    Map<String, Object> friend = new HashMap<>();
                    friend.put("last_message", "Digitando...");
                    databaseReference.updateChildren(friend);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageview_envia_direct = (ImageView) findViewById(R.id.imageview_envia_direct);
        imageview_envia_direct.setOnClickListener(this);

        service_direct.chat(getIntent().getExtras().getString("id_amigo"), list_chat_room, recyclerview_conversa_direct);

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
    };

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
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
            service_direct.send_message(receiver, message, type,
                    getIntent().getExtras().getString("id_amigo"),
                    getIntent().getExtras().getString("name_amigo"),
                    getIntent().getExtras().getString("username_amigo"),
                    image_amigo);
            edittexct_message_direct.setText(null);
            recyclerview_conversa_direct.scrollToPosition(recyclerview_conversa_direct.getAdapter().getItemCount() - 1);
        }
    };

}
