package com.clikshow.Direct;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.clikshow.Direct.Fragments.Amigos_Fragment;
import com.clikshow.Direct.Fragments.Conversas_Fragment;
import com.clikshow.R;

public class View_Direct extends Activity implements View.OnClickListener {

    ImageView imageview_back_direct;
    TabLayout tablayout_direct;
    FrameLayout container_direct;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_direct);

        imageview_back_direct = (ImageView) findViewById(R.id.imageview_back_direct);
        imageview_back_direct.setOnClickListener(this);

        container_direct = (FrameLayout) findViewById(R.id.container_direct);

        tablayout_direct = (TabLayout) findViewById(R.id.tablayout_direct);
        tablayout_direct.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getFragmentManager().beginTransaction().replace(R.id.container_direct,
                                new Conversas_Fragment()).commit();
                        break;
                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.container_direct,
                                new Amigos_Fragment()).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_back_direct:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
