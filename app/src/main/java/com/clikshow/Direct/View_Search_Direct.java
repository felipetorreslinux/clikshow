package com.clikshow.Direct;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.clikshow.R;

public class View_Search_Direct extends Activity implements View.OnClickListener{

    ImageView imageview_back_search_direct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_search_direct);

        imageview_back_search_direct = (ImageView) findViewById(R.id.imageview_back_search_direct);
        imageview_back_search_direct.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_back_search_direct:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
