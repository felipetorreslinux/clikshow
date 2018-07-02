package com.clikshow.Views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.clikshow.R;
import com.squareup.picasso.Picasso;

public class View_Foto_Evento_Zoom extends Activity {

    private ImageView img_photo_evento_zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_evento_zoom);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        img_photo_evento_zoom = (ImageView) findViewById(R.id.img_photo_evento_zoom);

        Picasso.get()
            .load(getIntent().getExtras().getString("banner"))
            .into(img_photo_evento_zoom);
    };

    @Override
    public void onBackPressed(){
        finish();
    };
}
