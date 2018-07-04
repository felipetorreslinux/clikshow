package com.clikshow.Views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.clikshow.Fragmentos.Favorites_Fragment;
import com.clikshow.Fragmentos.Feed_Fragment;
import com.clikshow.Fragmentos.Profile_Fragment;
import com.clikshow.Fragmentos.Meus_Ingressos_Fragment;
import com.clikshow.R;
import com.clikshow.SQLite.Banco;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class View_Principal extends Activity implements View.OnClickListener {
    private FrameLayout container_principal;
    private int tabindex;
    private ImageView img_tab_feed;
    private ImageView img_tab_profile;
    private ImageView img_tab_favorites;
    private ImageView img_tab_ticket;
    private SharedPreferences profile;
    private ImageView btn_open_cliksocial;

    public static int codeBuy = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_principal);

        profile = getSharedPreferences("user_info", MODE_PRIVATE);
        container_principal = (FrameLayout) findViewById(R.id.container_principal);

        img_tab_feed = (ImageView) findViewById(R.id.img_tab_feed);
        img_tab_feed.setOnClickListener(this);

        img_tab_favorites = (ImageView) findViewById(R.id.img_tab_favorites);
        img_tab_favorites.setOnClickListener(this);

        btn_open_cliksocial = (ImageView) findViewById(R.id.btn_open_cliksocial);
        btn_open_cliksocial.setOnClickListener(this);

        img_tab_ticket = (ImageView) findViewById(R.id.img_tab_ticket);
        img_tab_ticket.setOnClickListener(this);

        img_tab_profile = (ImageView) findViewById(R.id.img_tab_profile);
        img_tab_profile.setOnClickListener(this);

        tabindex = 0;
        img_tab_feed.setImageResource(R.drawable.ic_feed_orange);
        img_tab_favorites.setImageResource(R.drawable.ic_favorites);
        img_tab_ticket.setImageResource(R.drawable.ic_bilhete);
        getFragmentManager().beginTransaction().replace(R.id.container_principal,
                new Feed_Fragment()).commit();

    };

    public void imageProfilePic(){
        if(!profile.getString("profile_pic",null).equals("null")){
            Picasso.get()
            .load(Uri.parse(profile.getString("profile_pic", null)))
            .resize(100,100)
            .centerCrop()
            .placeholder(R.drawable.ic_profile)
            .transform(new CropCircleTransformation())
            .into(img_tab_profile);
        }else{
            Picasso.get()
            .load(R.drawable.ic_profile)
            .resize(100,100)
            .centerCrop()
            .placeholder(R.drawable.ic_profile)
            .transform(new CropCircleTransformation())
            .into(img_tab_profile);
        };
    };

    @Override
    public void onResume(){
        super.onResume();
        imageProfilePic();

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        Banco banco = new Banco(this);
        banco.count_carrinho();

        switch (requestCode){
            case 1000:
                if(resultCode == RESULT_OK){
                        tabindex = 2;
                        img_tab_feed.setImageResource(R.drawable.ic_feed);
                        img_tab_favorites.setImageResource(R.drawable.ic_favorites);
                        img_tab_ticket.setImageResource(R.drawable.ic_bilhete_orange);
                        getFragmentManager().beginTransaction().replace(R.id.container_principal,
                                new Meus_Ingressos_Fragment()).commit();
                };
                break;

            case 2000:
                if(resultCode == RESULT_OK){
                        tabindex = 1;
                        img_tab_feed.setImageResource(R.drawable.ic_feed);
                        img_tab_favorites.setImageResource(R.drawable.ic_favorites_orange);
                        img_tab_ticket.setImageResource(R.drawable.ic_bilhete);
                        getFragmentManager().beginTransaction().replace(R.id.container_principal,
                                new Favorites_Fragment()).commit();
                };
                break;

            case 3000:
                if(resultCode == RESULT_OK) {
                    switch (codeBuy){
                        case 0:
                            tabindex = 2;
                            img_tab_feed.setImageResource(R.drawable.ic_feed);
                            img_tab_favorites.setImageResource(R.drawable.ic_favorites);
                            img_tab_ticket.setImageResource(R.drawable.ic_bilhete_orange);
                            getFragmentManager().beginTransaction().replace(R.id.container_principal,
                                    new Meus_Ingressos_Fragment()).commit();

                            break;
                        default:
                    }

                }
                if(resultCode == RESULT_CANCELED){

                }
                break;

            case 4000:
                if(resultCode == RESULT_OK){

                }
                if(resultCode == RESULT_CANCELED){
                    tabindex = 0;
                    img_tab_feed.setImageResource(R.drawable.ic_feed_orange);
                    img_tab_favorites.setImageResource(R.drawable.ic_favorites);
                    img_tab_ticket.setImageResource(R.drawable.ic_bilhete);
                    getFragmentManager().beginTransaction().replace(R.id.container_principal,
                            new Feed_Fragment()).commit();
                }
                break;
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_open_cliksocial:
                Intent cliksocial = getPackageManager().getLaunchIntentForPackage("com.cliksocial");
                if (cliksocial != null) {
                    cliksocial.putExtra("type_app", 1);
                    startActivity(cliksocial);
                }else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.cliksocial")));
                };
            break;

            case R.id.img_tab_feed:
                if(tabindex != 0){
                    tabindex = 0;
                    img_tab_feed.setImageResource(R.drawable.ic_feed_orange);
                    img_tab_favorites.setImageResource(R.drawable.ic_favorites);
                    img_tab_ticket.setImageResource(R.drawable.ic_bilhete);
                    getFragmentManager().beginTransaction().replace(R.id.container_principal,
                            new Feed_Fragment()).commit();
                }
            break;

            case R.id.img_tab_favorites:
                if(tabindex != 1){
                    tabindex = 1;
                    img_tab_feed.setImageResource(R.drawable.ic_feed);
                    img_tab_favorites.setImageResource(R.drawable.ic_favorites_orange);
                    img_tab_ticket.setImageResource(R.drawable.ic_bilhete);
                    getFragmentManager().beginTransaction().replace(R.id.container_principal,
                            new Favorites_Fragment()).commit();
                }
            break;

            case R.id.img_tab_ticket:
                if(tabindex != 2){
                    tabindex = 2;
                    img_tab_feed.setImageResource(R.drawable.ic_feed);
                    img_tab_favorites.setImageResource(R.drawable.ic_favorites);
                    img_tab_ticket.setImageResource(R.drawable.ic_bilhete_orange);
                    getFragmentManager().beginTransaction().replace(R.id.container_principal,
                            new Meus_Ingressos_Fragment()).commit();
                }
            break;

            case R.id.img_tab_profile:
                if(tabindex != 4){
                    tabindex = 4;
                    img_tab_feed.setImageResource(R.drawable.ic_feed);
                    img_tab_favorites.setImageResource(R.drawable.ic_favorites);
                    img_tab_ticket.setImageResource(R.drawable.ic_bilhete);
                    getFragmentManager().beginTransaction().replace(R.id.container_principal,
                            new Profile_Fragment()).commit();
                }
            break;
        }
    }


    @Override
    public void onBackPressed(){
        if(tabindex != 0){
            tabindex = 0;
            img_tab_feed.setImageResource(R.drawable.ic_feed_orange);
            img_tab_favorites.setImageResource(R.drawable.ic_favorites);
            img_tab_ticket.setImageResource(R.drawable.ic_bilhete);
            getFragmentManager().beginTransaction().replace(R.id.container_principal,
                    new Feed_Fragment()).commit();
        }else{
            finish();
        };
    };
}
