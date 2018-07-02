package com.clikshow.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.Views.View_Principal;
import com.clikshow.R;
import com.clikshow.SMS.View_SMS_Code;
import com.clikshow.Service.Toast.ToastClass;

import org.json.JSONException;
import org.json.JSONObject;

public class Service_CellPhone {

    public static void check (final Activity activity, final EditText cellphone){

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        final View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_envia_sms, null);
        mBuilder.setView(dialogView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog_envia_sms = mBuilder.create();

        dialog_envia_sms.show();

        AndroidNetworking.post(APIServer.CLIKSOCIALPROD+"api/check_cellphone")
        .addHeaders("Authorization", APIServer.token(activity))
        .addBodyParameter("cellphone", cellphone.getText().toString().trim())
        .setPriority(Priority.LOW)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String code = response.getString("code").toString();
                    switch (code){
                        case "0":
                            dialog_envia_sms.dismiss();
                            Intent open = new Intent(activity, View_SMS_Code.class);
                            open.putExtra("cellphone_extra", cellphone.getText().toString());
                            activity.startActivity(open);
                            cellphone.setText(null);
                            break;
                        default:
                            dialog_envia_sms.dismiss();
                            APIServer.error_server(activity, response.getInt("code"));
                    };
                }catch (JSONException e){}
            };
            @Override
            public void onError(ANError error) {
                dialog_envia_sms.dismiss();
                APIServer.error_server(activity, error.getErrorCode());
            };
        });
    };

    public static void validarCodeSMS(final Activity activity, String code, final String cellphone){

        final SharedPreferences.Editor editor = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE).edit();

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        final View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_valida_code_sms, null);
        mBuilder.setView(dialogView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog_valida_code_sms = mBuilder.create();
        dialog_valida_code_sms.show();

        AndroidNetworking.post(APIServer.CLIKSOCIALPROD+"api/check_digits")
        .addHeaders("Authorization", APIServer.token(activity))
        .addBodyParameter("digits", code.trim())
        .setPriority(Priority.LOW)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String code = response.getString("code").toString();
                    switch (code){
                        case "0":
                            editor.putBoolean("is_login", true);
                            editor.putString("cellphone", cellphone);
                            editor.commit();
                            dialog_valida_code_sms.dismiss();
                            Intent open = new Intent(activity, View_Principal.class);
                            activity.startActivity(open);
                            activity.finishAffinity();
                            break;

                        default:
                            editor.putBoolean("is_login", false);
                            editor.commit();
                            dialog_valida_code_sms.dismiss();
                            ToastClass.curto(activity, "Código inválido");

                    };
                }catch (JSONException e){}
            };
            @Override
            public void onError(ANError error) {
                dialog_valida_code_sms.dismiss();
                editor.putBoolean("is_login", false);
                editor.commit();
                APIServer.error_server(activity, error.getErrorCode());
            };
        });
    }

}
