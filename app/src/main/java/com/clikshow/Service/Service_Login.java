package com.clikshow.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.R;
import com.clikshow.SMS.View_Digita_Cellphone;
import com.clikshow.Service.Toast.ToastClass;

import org.json.JSONException;
import org.json.JSONObject;

public class Service_Login {

    public static void logar (final Activity activity, final Editable login, final Editable senha){

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        final View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_valida_login, null);
        mBuilder.setView(dialogView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog_valida_login = mBuilder.create();
        dialog_valida_login.show();

        final SharedPreferences.Editor profile = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE).edit();

        AndroidNetworking.post(APIServer.URL+"api/authenticate")
        .addBodyParameter("login", login.toString())
        .addBodyParameter("password", senha.toString())
        .setPriority(Priority.LOW)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            dialog_valida_login.dismiss();
                            JSONObject user_info = new JSONObject(response.getJSONObject("content").getJSONObject("user_info").toString());

                                profile.putInt("id", user_info.getInt("id"));
                                profile.putString("name", user_info.getString("name"));
                                profile.putString("status", user_info.getString("status"));
                                profile.putString("username", user_info.getString("username"));
                                profile.putString("email", user_info.getString("email"));
                                profile.putString("cellphone", user_info.getString("cellphone"));
                                profile.putString("profile_pic", user_info.getString("profile_pic"));
                                profile.putString("genre", user_info.getString("genre"));
                                profile.putString("cpf", user_info.getString("cpf"));
                                profile.putString("token", user_info.getString("token"));
                                profile.putBoolean("is_login", false);
                                profile.commit();

                                Intent open = new Intent(activity, View_Digita_Cellphone.class);
                                activity.startActivity(open);
                                activity.finish();

                        break;
                        default:
                            dialog_valida_login.dismiss();
                            ToastClass.curto(activity, "Credenciais inválidas");
                    };
                }catch (JSONException e){};
            };
            @Override
            public void onError(ANError error) {
                APIServer.error_server(activity, error.getErrorCode());
            };
        });
    }
}
