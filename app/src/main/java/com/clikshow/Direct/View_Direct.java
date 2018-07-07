package com.clikshow.Direct;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.clikshow.API.APIServer;
import com.clikshow.Direct.Fragments.Amigos_Fragment;
import com.clikshow.Direct.Fragments.Conversas_Fragment;
import com.clikshow.Direct.Service.Service_Direct;
import com.clikshow.R;
import com.clikshow.Views.View_Search;

public class View_Direct extends Activity implements View.OnClickListener {

    ImageView imageview_back_direct;
    ImageView imageview_search_friends_direct;
    FloatingActionButton floatbutton_friends_direct;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_direct);

        imageview_back_direct = (ImageView) findViewById(R.id.imageview_back_direct);
        imageview_back_direct.setOnClickListener(this);

        imageview_search_friends_direct = (ImageView) findViewById(R.id.imageview_search_friends_direct);
        imageview_search_friends_direct.setOnClickListener(this);

        floatbutton_friends_direct = (FloatingActionButton) findViewById(R.id.floatbutton_friends_direct);
        floatbutton_friends_direct.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_back_direct:
                onBackPressed();
                break;

            case R.id.imageview_search_friends_direct:
                Intent intent = new Intent(this, View_Search_Direct.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.floatbutton_friends_direct:
                break;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 1:
                if(requestCode == Activity.RESULT_OK){

                }else{

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
