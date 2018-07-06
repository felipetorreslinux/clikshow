package com.clikshow.Portaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class View_Portaria extends Activity implements View.OnClickListener{

    ImageView btn_menu_portaria;
    ImageView btn_exit_portaria;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_portaria);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        btn_exit_portaria = (ImageView) findViewById(R.id.btn_exit_portaria);
        btn_exit_portaria.setOnClickListener(this);
        btn_menu_portaria = (ImageView) findViewById(R.id.btn_menu_portaria);
        btn_menu_portaria.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_menu_portaria:
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
                bottomSheetDialog.setCancelable(true);
                View view = getLayoutInflater().inflate(R.layout.bottomsweet_menu_portaria, null);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();

                ImageView imageview_profile_portaria = view.findViewById(R.id.imageview_profile_portaria);
                TextView textview_name_prolife_portaria = view.findViewById(R.id.textview_name_prolife_portaria);
                TextView textview_email_prolife_portaria = view.findViewById(R.id.textview_email_prolife_portaria);
                TextView textview_hora_portaria = view.findViewById(R.id.textview_hora_portaria);

                textview_name_prolife_portaria.setText(sharedPreferences.getString("name", null));
                textview_email_prolife_portaria.setText(sharedPreferences.getString("email", null));
                textview_hora_portaria.setText(Datas.data_hora_aparelho());

                Picasso.get()
                        .load(sharedPreferences.getString("profile_pic", null))
                        .resize(100, 100)
                        .transform(new CropCircleTransformation())
                        .into(imageview_profile_portaria);

                break;

            case R.id.btn_exit_portaria:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Deseja realmente sair da portaria?");
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.negative_button_dialog, null);
                builder.create().show();
                break;

        }
    }
}
