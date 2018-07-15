package com.clikshow.Portaria.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Camera;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.ClikPortaria.Views.View_Valida_Ingresso_Portaria;
import com.clikshow.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Portaria_Service {

    Activity activity;
    BottomSheetDialog bottomSheetDialog;
    AlertDialog.Builder builder;
    View view;

    public Portaria_Service (Activity activity){
        this.activity = activity;
        bottomSheetDialog = new BottomSheetDialog(activity);
        builder = new AlertDialog.Builder(activity);
    }

    public void checar_ingresso (int pass_id, final String cpf, int event_id){
        AndroidNetworking.post(APIServer.URL+"api/checkpass")
        .addHeaders("Authorization", APIServer.token(activity))
        .addBodyParameter("my_pass_id", String.valueOf(pass_id))
        .addBodyParameter("payment_type", String.valueOf(0))
        .addBodyParameter("cpf", cpf)
        .addBodyParameter("event_id", String.valueOf(event_id))
        .addBodyParameter("qrcode", String.valueOf(0))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            JSONObject pass_info = response.getJSONObject("content").getJSONObject("pass_info");
                            JSONObject user_info = response.getJSONObject("content").getJSONObject("user_info");

//                            Intent intent = new Intent(activity, View_Valida_Ingresso_Portaria.class);
//                            intent.putExtra("status", pass_info.getInt("status"));
//                            intent.putExtra("my_pass_id",pass_info.getInt("my_pass_id"));
//                            intent.putExtra("pass_name",pass_info.getString("pass_name"));
//                            intent.putExtra("pass_description",pass_info.getString("pass_description"));
//                            intent.putExtra("starts", pass_info.getInt("starts"));
//                            intent.putExtra("ends",pass_info.getInt("ends"));
//                            intent.putExtra("cpf",user_info.getString("cpf"));
//                            intent.putExtra("name", user_info.getString("name"));
//                            intent.putExtra("cellphone", user_info.getString("cellphone"));
//                            intent.putExtra("email", user_info.getString("email"));
//                            intent.putExtra("thumb",user_info.getString("thumb"));
//                            intent.putExtra("is_registered", user_info.getBoolean("is_registered"));
//                            activity.startActivityForResult(intent, 1);1

                            modal_valida_portaria(
                                    user_info.getString("thumb"),
                                    user_info.getString("name"),
                                    pass_info.getString("pass_name"),
                                    pass_info.getInt("my_pass_id"),
                                    pass_info.getInt("status"));

                            break;

                        case 71:
                            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle(R.string.app_name);
                            builder.setMessage("Este ingresso já foi realizado check-in");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", null);
                            builder.create().show();

                            break;

                        case 73:
                            final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                            builder1.setTitle(R.string.app_name);
                            builder1.setMessage("Chame a coordenação portaria inválida");
                            builder1.setCancelable(false);
                            builder1.setPositiveButton("Ok", null);
                            builder1.create().show();

                            break;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    public void modal_valida_portaria (String image, String name, String ingresso, int id, int status){
        view = activity.getLayoutInflater().inflate(R.layout.bottom_sweet_valida_portaria, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

        ImageView imageview_profile_valida_portaria = view.findViewById(R.id.imageview_profile_valida_portaria);
        TextView textview_name_valida_portaria = view.findViewById(R.id.textview_name_valida_portaria);
        TextView textview_ingresso_valida_portaria = view.findViewById(R.id.textview_ingresso_valida_portaria);
        TextView textview_status_valida_portaria = view.findViewById(R.id.textview_status_valida_portaria);
        Button button_valida_portaria = view.findViewById(R.id.button_valida_portaria);

        Picasso.get()
                .load(image)
                .transform(new CropCircleTransformation())
                .into(imageview_profile_valida_portaria);

        String liberado = null;
        switch (status){
            case 1:
                liberado = "Liberado";
            break;

            default:
                liberado = "negado";
        }
        liberado.toUpperCase();
        textview_name_valida_portaria.setText("Nome: "+name);
        textview_ingresso_valida_portaria.setText("Ingresso: "+ingresso);
        textview_status_valida_portaria.setText("Status: "+liberado);
        button_valida_portaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });




    }

}
