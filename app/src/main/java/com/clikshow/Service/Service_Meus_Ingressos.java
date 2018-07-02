package com.clikshow.Service;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Adapter.Meus_Ingressos_Adapter;
import com.clikshow.Fragmentos.Models.MeusIngressosModel;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Validation.CPF;
import com.clikshow.Views.View_Ingresso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.clikshow.Views.View_Ingresso.cpf_validar_checkin;

public class Service_Meus_Ingressos {

    public static void checar_cpf_checkin (final Activity activity, final TextView textView, final EditText editText, final InputMethodManager inputMethodManager){
        AndroidNetworking.get(APIServer.CLIKSOCIAL+"api/check_cpf/"+cpf_validar_checkin)
            .addHeaders("Authorization", APIServer.token(activity))
            .setTag("CHECAR_CPF_CHECKIN")
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        int code = response.getInt("code");
                        switch (code){
                            case 0:
                                JSONObject jsonCpf = response.getJSONObject("content").getJSONObject("cpf");
                                textView.setText(jsonCpf.getString("name"));
                                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
                                break;
                            case 35:
                                JSONObject jsonUserInfo = response.getJSONObject("content").getJSONObject("user_info");
                                textView.setText(jsonUserInfo.getString("name"));
                                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
                                break;
                            default:
                                ToastClass.curto(activity, "CPF inválido ou inexistente");
                                textView.setText(null);
                                editText.setText(null);
                                editText.requestFocus();
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

    public static void realizar_checkin (final Activity activity, final int MY_PASS_ID, final EditText editText, final TextView name_ingresso_efetuar_checkin){

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_loading_valida_checkin, null);
        builder.setCancelable(false);
        builder.setView(view);
        final AlertDialog alertDialog  = builder.create();
        alertDialog.show();

        final Handler handler = new Handler();

        AndroidNetworking.post(APIServer.URL+"api/pass_checkin")
            .addHeaders("Authorization", APIServer.token(activity))
            .addBodyParameter("my_pass_id", String.valueOf(MY_PASS_ID))
            .addBodyParameter("cpf", CPF.MaskCpf(cpf_validar_checkin))
            .addBodyParameter("force", "0")
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int code = response.getInt("code");
                        switch (code){
                            case 0:
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        alertDialog.dismiss();
                                        activity.onBackPressed();
                                        ToastClass.curto(activity, "Check-In realizado com sucesso");
                                    }
                                }, 2000);
                                break;

                            case 76:
                                final AlertDialog.Builder builder0 = new AlertDialog.Builder(activity);
                                builder0.setTitle(R.string.app_name);
                                builder0.setMessage("Você já tem um ingresso deste tipo.\nNão é possível realizar o check-in para você.");
                                builder0.setCancelable(false);
                                builder0.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                        activity.onBackPressed();
                                    }
                                });
                                builder0.create().show();

                                break;

                            case 77:
                                final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                                builder1.setTitle(R.string.app_name);
                                builder1.setMessage(name_ingresso_efetuar_checkin.getText().toString()+" já tem um ingresso para esse evento.\nVocê gostaria de realizar o check-in mesmo assim?");
                                builder1.setCancelable(false);
                                builder1.setPositiveButton("Check-in", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AndroidNetworking.post(APIServer.URL+"api/pass_checkin")
                                                .addHeaders("Authorization", APIServer.token(activity))
                                                .addBodyParameter("my_pass_id", String.valueOf(MY_PASS_ID))
                                                .addBodyParameter("cpf", editText.getText().toString())
                                                .addBodyParameter("force", "1")
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try{
                                                            int code = response.getInt("code");
                                                            switch (code){
                                                                case 0:
                                                                    alertDialog.dismiss();
                                                                    activity.onBackPressed();
                                                                    ToastClass.curto(activity, "Check-In realizado com sucesso");
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
                                });
                                builder1.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                        activity.onBackPressed();
                                    }
                                });
                                builder1.create().show();

                                break;
                            default:
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        alertDialog.dismiss();
                                        ToastClass.longo(activity, "Não foi possível fazer o check-in no momento\nTente mais tarde");                                    }
                                }, 2000);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                        alertDialog.show();
                    }
                }

                @Override
                public void onError(final ANError anError) {
                    APIServer.error_server(activity, anError.getErrorCode());
                }
            });
    }

    public static void cancelar_ingresso (final Activity activity,final int MY_PASS_ID){

        AndroidNetworking.get(APIServer.URL+"api/checkrefund/"+MY_PASS_ID)
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                System.out.println(response.toString());
              try {
                  int code = response.getInt("code");
                  switch (code){
                      case 0:
                          AndroidNetworking.put(APIServer.URL+"api/refund/"+MY_PASS_ID)
                                  .addHeaders("Authorization", APIServer.token(activity))
                                  .build()
                                  .getAsJSONObject(new JSONObjectRequestListener() {
                                      @Override
                                      public void onResponse(JSONObject response) {
                                          System.out.println(response.toString());
                                          try {
                                              int code = response.getInt("code");
                                              switch (code){
                                                  case 0:
                                                      builder.setTitle(R.string.app_name);
                                                      builder.setMessage("Ingresso cancelado com sucesso");
                                                      builder.setCancelable(false);
                                                      builder.setPositiveButton("Ok", null);
                                                      builder.create().show();
                                                      activity.onBackPressed();
                                                      break;
                                                  default:
                                                      builder.setTitle(R.string.app_name);
                                                      builder.setMessage("Não foi possível cancelar o ingresso.\nEntre em contato com nosso suporte.");
                                                      builder.setCancelable(false);
                                                      builder.setPositiveButton("Ok", null);
                                                      builder.create().show();
                                                      activity.onBackPressed();
                                              }
                                          }catch (JSONException e){

                                          }
                                      }

                                      @Override
                                      public void onError(ANError anError) {

                                      }
                                  });
                          break;
                      default:
                          ToastClass.curto(activity, "Não foi possível cancelar o ingresso no momento");
                          activity.onBackPressed();
                  }
              }catch (JSONException e){

              }
            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    public static void listar_novos_ingressos (final Activity activity, final List<MeusIngressosModel> lista_meus_ingressos, final RecyclerView recyclerView, final ViewStub loading_novos_ingressos, final ViewStub box_sem_ingressos_novos){

        AndroidNetworking.get(APIServer.URL+"api/mypasses")
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response.toString());
                try {
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            if(activity != null){
                                lista_meus_ingressos.clear();
                                JSONArray novos_ingressos = response.getJSONObject("content").getJSONArray("passes");
                                if(novos_ingressos.length() > 0){
                                    for (int i = 0; i < novos_ingressos.length(); i++){
                                        JSONObject jsonObject = novos_ingressos.getJSONObject(i);
                                        MeusIngressosModel meus_ingressos_model = new MeusIngressosModel(
                                            jsonObject.getInt("my_pass_id"),
                                            jsonObject.getInt("pass_id"),
                                            jsonObject.getString("pass_name"),
                                            jsonObject.getString("pass_description"),
                                            jsonObject.getDouble("price"),
                                            jsonObject.getString("cpf"),
                                            jsonObject.getInt("status"),
                                            jsonObject.getString("event_thumb"),
                                            jsonObject.getInt("payment_type"),
                                            jsonObject.getString("origin"),
                                            jsonObject.getInt("starts"),
                                            jsonObject.getInt("ends"));
                                        lista_meus_ingressos.add(meus_ingressos_model);
                                    };
                                    Meus_Ingressos_Adapter meus_ingressos_adapter = new Meus_Ingressos_Adapter(activity, lista_meus_ingressos);
                                    recyclerView.setAdapter(meus_ingressos_adapter);
                                    loading_novos_ingressos.setVisibility(View.GONE);
                                }else{
                                    loading_novos_ingressos.setVisibility(View.GONE);
                                    box_sem_ingressos_novos.inflate();
                                }
                            }
                            break;
                        default:
                            box_sem_ingressos_novos.setVisibility(View.GONE);
                            loading_novos_ingressos.setVisibility(View.GONE);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    box_sem_ingressos_novos.setVisibility(View.GONE);

                }
            }

            @Override
            public void onError(ANError anError) {
                lista_meus_ingressos.clear();
            }
        });
    }

    public static void checar_ingresso (final Activity activity, int MY_PASS_ID){
        AndroidNetworking.get(APIServer.URL+"api/invoicecheck/"+MY_PASS_ID)
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code = response.getInt("code");
                    Intent intent = new Intent();
                    switch (code){
                        case 0:
                            int code_status = response.getJSONObject("content").getInt("status");
                            switch (code_status){
                                case 2:
                                    ToastClass.curto(activity, "Este ingresso está pendente de pagamento. Atualize a sua lista de ingressos");
                                    activity.setResult(Activity.RESULT_OK, intent);
                                    activity.onBackPressed();
                                    break;
                                case 3:
                                    ToastClass.curto(activity, "Este ingresso foi extornado e não pode ser utilizado");
                                    activity.setResult(Activity.RESULT_OK, intent);
                                    activity.onBackPressed();
                                    break;
                                case 4:
                                    ToastClass.curto(activity, "Este ingresso já foi utilizado");
                                    activity.setResult(Activity.RESULT_OK, intent);
                                    activity.onBackPressed();
                                    break;
                                case 5:
                                    ToastClass.curto(activity, "Este ingresso foi cancelado e não pode ser utilizado");
                                    activity.setResult(Activity.RESULT_OK, intent);
                                    activity.onBackPressed();
                                    break;
                                case 6:
                                    ToastClass.curto(activity, "Erro servidor ClikShow");
                                    activity.setResult(Activity.RESULT_OK, intent);
                                    activity.onBackPressed();
                                    break;
                            }
                            break;
                        case 66:
                            ToastClass.curto(activity, "Problema com seu ingresso entre em contato com nosso suporte");
                            activity.setResult(Activity.RESULT_OK, intent);
                            activity.onBackPressed();
                            break;
                        case 67:

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
}
