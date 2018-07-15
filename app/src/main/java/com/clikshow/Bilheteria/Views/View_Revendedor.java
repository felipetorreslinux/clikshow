package com.clikshow.Bilheteria.Views;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.clikshow.Bilheteria.Fragments.Credito_Fragment;
import com.clikshow.Bilheteria.Fragments.Debito_Fragment;
import com.clikshow.Bilheteria.Fragments.Dinheiro_Fragment;
import com.clikshow.R;
import com.clikshow.Utils.Keyboard;

import static com.clikshow.Bilheteria.Impressora.BlueTooth.bluetoothAdapter;


public class View_Revendedor extends Activity implements View.OnClickListener{

    ImageView back_bilheteria_finalizar;
    TabLayout tablayout_pagar_bilheteria;
    FrameLayout container_pagar_bilheteria;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_revendedor_bilheteria);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.enable();

        container_pagar_bilheteria = (FrameLayout) findViewById(R.id.container_pagar_bilheteria);

        tablayout_pagar_bilheteria = (TabLayout) findViewById(R.id.tablayout_pagar_bilheteria);
        tablayout_pagar_bilheteria.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getFragmentManager().beginTransaction().replace(R.id.container_pagar_bilheteria,
                                new Dinheiro_Fragment()).commit();
                        Keyboard.close(View_Revendedor.this, getWindow().getDecorView());
                        break;
                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.container_pagar_bilheteria,
                                new Credito_Fragment()).commit();
                        Keyboard.close(View_Revendedor.this, getWindow().getDecorView());
                        break;
                    case 2:
                        getFragmentManager().beginTransaction().replace(R.id.container_pagar_bilheteria,
                                new Debito_Fragment()).commit();
                        Keyboard.close(View_Revendedor.this, getWindow().getDecorView());
                        break;
                }
            };
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        getFragmentManager().beginTransaction().replace(R.id.container_pagar_bilheteria,
                new Dinheiro_Fragment()).commit();
        Keyboard.close(View_Revendedor.this, getWindow().getDecorView());

        back_bilheteria_finalizar = (ImageView) findViewById(R.id.back_bilheteria_finalizar);
        back_bilheteria_finalizar.setOnClickListener(this);
    };

    @Override
    public void onResume(){
        super.onResume();
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
    };

    @Override
    public void onPause(){
        super.onPause();

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_bilheteria_finalizar:
                Intent intent = getIntent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 9001:
                if(resultCode == Activity.RESULT_OK) {
                    finish();
                }else{

                }
                break;
        }
    }
}
