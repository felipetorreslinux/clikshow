package com.clikshow.ClikBIlheteria.Services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.ClikBIlheteria.Adapter.Bilheteria_Lista_Ingressos_Adapter;
import com.clikshow.ClikBIlheteria.Models.Bilheteria_Model;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Keyboard;
import com.clikshow.Utils.Loading;
import com.clikshow.Validation.CPF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.List;
import java.util.logging.Handler;

public class Bilheteria_Service {

    public static void troco (final Activity activity, final Double preco, final EditText valor, final EditText troco){
        double valor_recebido = Integer.parseInt(valor.getText().toString());
        double total = valor_recebido - preco;
        if(total < 0){
            troco.setText("R$ 0.00");
        }else{
            troco.setText("R$ "+APIServer.preco(total));
        };
    };

    public static void checar_cpf (final Activity activity, final EditText cpf, final EditText nome, final EditText telefone){
        Loading.open(activity);
        AndroidNetworking.get(APIServer.CLIKSOCIAL+"api/check_cpf/"+cpf.getText().toString().trim())
            .addHeaders("Authorization", APIServer.token(activity))
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                    try{
                        int code = response.getInt("code");
                        String name = null;
                        String cellphone = null;
                        switch (code){
                            case 0:
                                name = response.getJSONObject("content").getJSONObject("cpf").getString("name");
                                nome.setText(name);
                                nome.setVisibility(View.VISIBLE);
                                Loading.close();
                                break;
                            case 35:
                                name = response.getJSONObject("content").getJSONObject("user_info").getString("name");
                                cellphone = response.getJSONObject("content").getJSONObject("user_info").getString("cellphone");
                                nome.setText(name);
                                nome.setVisibility(View.VISIBLE);
                                if(cellphone.length() > 5){
                                    telefone.setText(cellphone.substring(2));
                                }else{
                                    telefone.setText(null);
                                };
                                Loading.close();
                                break;
                            case 9000:
                                nome.setVisibility(View.GONE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("CPF inválido");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        cpf.setText(null);
                                        cpf.requestFocus();
                                        Keyboard.open(activity, cpf);
                                    }
                                });
                                builder.create().show();
                                Loading.close();
                                break;
                        }
                        Keyboard.close(activity, activity.getWindow().getDecorView());
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    Loading.close();
                    APIServer.error_server(activity, anError.getErrorCode());
                }
            });
    }

    public static void listar_ingressos (final Activity activity, int id, final List<Bilheteria_Model> lista_ingressos, final RecyclerView recyclerView, final ViewStub loading_ingressos_bilheteria){
        loading_ingressos_bilheteria.setVisibility(View.VISIBLE);
        AndroidNetworking.get(APIServer.URL+"api/listpass/"+id)
        .addHeaders("Authorization", APIServer.token(activity))
        .setPriority(Priority.LOW)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            if(activity != null){
                                JSONArray ingressos = response.getJSONObject("content").getJSONArray("passes");
                                lista_ingressos.clear();
                                for(int i = 0; i < ingressos.length(); i++){
                                    JSONObject jsonObject = ingressos.getJSONObject(i);
                                    Bilheteria_Model bilheteria_model = new Bilheteria_Model(
                                            jsonObject.getInt("id"),
                                            jsonObject.getInt("event_id"),
                                            jsonObject.getString("event_name"),
                                            jsonObject.getString("event_thumb"),
                                            jsonObject.getString("type"),
                                            jsonObject.getInt("status"),
                                            jsonObject.getDouble("price"),
                                            jsonObject.getString("description"),
                                            jsonObject.getString("name"),
                                            jsonObject.getInt("starts"),
                                            jsonObject.getInt("ends"));
                                    lista_ingressos.add(bilheteria_model);
                                };

                                Bilheteria_Lista_Ingressos_Adapter bilheteria_lista_ingressos_adapter = new Bilheteria_Lista_Ingressos_Adapter(activity, lista_ingressos, recyclerView);
                                recyclerView.setAdapter(bilheteria_lista_ingressos_adapter);
                                recyclerView.setVisibility(View.VISIBLE);
                                loading_ingressos_bilheteria.destroyDrawingCache();
                                loading_ingressos_bilheteria.setVisibility(View.GONE);
                            }
                            break;
                        default:
                            recyclerView.setVisibility(View.GONE);
                            loading_ingressos_bilheteria.inflate();
                    }
                }catch (JSONException e){}
            }
            @Override
            public void onError(ANError anError) {
                APIServer.error_server(activity, anError.getErrorCode());
            }
        });
    };

    public static void force_checkin (final Activity activity, int pass_id, String cpf, int payment_type, int force){

        AndroidNetworking.post(APIServer.URL+"api/buywithdealer")
            .addHeaders("Authorization", APIServer.token(activity))
            .addBodyParameter("pass_id", String.valueOf(pass_id))
            .addBodyParameter("cpf", CPF.MaskCpf(cpf))
            .addBodyParameter("payment_type", String.valueOf(payment_type))
            .addBodyParameter("force", String.valueOf(force))
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                    try{
                        int code = response.getInt("code");
                        switch (code){
                            case 0:
                                    activity.setResult(Activity.RESULT_OK, activity.getIntent());
                                    activity.finish();
                                break;
                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    APIServer.error_server(activity, anError.getErrorCode());
                }
            });
    }

    public static void cancelar_ingresso (final Activity activity, int pass_id, String cpf){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.app_name);
        builder.setCancelable(false);
        AndroidNetworking.post(APIServer.URL+"api/cancelpass")
        .addHeaders("Authorization", APIServer.token(activity))
        .addBodyParameter("pass_id", String.valueOf(pass_id))
        .addBodyParameter("cpf", CPF.MaskCpf(cpf))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            builder.setMessage("Cancelamento realizado com sucesso");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                }
                            });
                            builder.create().show();
                            break;
                        case 60:
                            builder.setMessage("Ingresso não existe ou já está cancelado");
                            builder.setPositiveButton("voltar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                }
                            });
                            builder.create().show();
                            break;
                    }
                }catch (JSONException e){}
            }

            @Override
            public void onError(ANError anError) {
                APIServer.error_server(activity, anError.getErrorCode());
            }
        });
    }

}
