package com.clikshow.ClikPortaria.Views.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Parcelable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.ClikPortaria.Views.View_Valida_Ingresso_Portaria;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.google.gson.JsonObject;
import com.google.zxing.MultiFormatWriter;

import org.json.JSONException;
import org.json.JSONObject;

public class Service_Portaria {

    public static void checar_ingresso (final Activity activity, String MY_PASS_ID, int PAYMENT_TYPE, final String cpf, int EVENT_ID, int QRCODE){
        AndroidNetworking.post(APIServer.URL+"api/checkpass")
        .addHeaders("Authorization", APIServer.token(activity))
        .addBodyParameter("my_pass_id", MY_PASS_ID)
        .addBodyParameter("payment_type", String.valueOf(PAYMENT_TYPE))
        .addBodyParameter("cpf", cpf)
        .addBodyParameter("event_id", String.valueOf(EVENT_ID))
        .addBodyParameter("qrcode", String.valueOf(QRCODE))
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

                            Intent intent = new Intent(activity, View_Valida_Ingresso_Portaria.class);
                            intent.putExtra("status", pass_info.getInt("status"));
                            intent.putExtra("my_pass_id",pass_info.getInt("my_pass_id"));
                            intent.putExtra("pass_name",pass_info.getString("pass_name"));
                            intent.putExtra("pass_description",pass_info.getString("pass_description"));
                            intent.putExtra("starts", pass_info.getInt("starts"));
                            intent.putExtra("ends",pass_info.getInt("ends"));
                            intent.putExtra("cpf",user_info.getString("cpf"));
                            intent.putExtra("name", user_info.getString("name"));
                            intent.putExtra("cellphone", user_info.getString("cellphone"));
                            intent.putExtra("email", user_info.getString("email"));
                            intent.putExtra("thumb",user_info.getString("thumb"));
                            intent.putExtra("is_registered", user_info.getBoolean("is_registered"));
                            activity.startActivityForResult(intent, 1);

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

    public static void validar_ingresso (final Activity activity, String MY_PASS_ID){
        AndroidNetworking.put(APIServer.URL+"api/invalidatepass/"+MY_PASS_ID)
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            ToastClass.curto(activity, "Ingresso validado com sucesso");
                            Intent intent = new Intent();
                            activity.setResult(Activity.RESULT_OK, intent);
                            activity.finish();
                            break;
                        default:
                            ToastClass.curto(activity, "Não foi possível validar este ingresso no momento");
                            activity.finish();
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

}
