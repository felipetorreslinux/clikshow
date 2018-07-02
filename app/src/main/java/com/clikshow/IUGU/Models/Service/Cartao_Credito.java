package com.clikshow.IUGU.Models.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.IUGU.Models.Adapter.ListaCartaoCarrinhoAdapter;
import com.clikshow.IUGU.Models.Adapter.ListaCartaoCreditoAdapter;
import com.clikshow.IUGU.Models.CartaoCreditoModel;
import com.clikshow.R;
import com.clikshow.Service.Snackbar.SnackbarClass;
import com.clikshow.Service.Toast.ToastClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Cartao_Credito {

    public static void checar_cpf (final Activity activity, final String cpf, final TextView nome_titular_novo_cartao, final EditText numero_cartao_novo_cartao, final LinearLayout box_dados_novo_cartao){

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        final View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_valida_cpf, null);
        mBuilder.setView(dialogView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog_valida_login = mBuilder.create();
        dialog_valida_login.show();

        AndroidNetworking.get(APIServer.CLIKSOCIAL+"api/check_cpf/"+cpf)
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
                            dialog_valida_login.dismiss();
                            box_dados_novo_cartao.setVisibility(View.VISIBLE);
                            JSONObject cpf = response.getJSONObject("content").getJSONObject("cpf");
                            nome_titular_novo_cartao.setText(cpf.getString("name"));
                            numero_cartao_novo_cartao.requestFocus();
                            break;
                        case 35:
                            dialog_valida_login.dismiss();
                            box_dados_novo_cartao.setVisibility(View.VISIBLE);
                            JSONObject jsonObject = response.getJSONObject("content").getJSONObject("user_info");
                            nome_titular_novo_cartao.setText(jsonObject.getString("name"));
                            numero_cartao_novo_cartao.requestFocus();
                            break;
                        default:
                            dialog_valida_login.dismiss();
                           box_dados_novo_cartao.setVisibility(View.GONE);
                           nome_titular_novo_cartao.setText(null);
                           ToastClass.curto(activity, "CPF inválido ou não existe");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog_valida_login.dismiss();
                }
            }

            @Override
            public void onError(ANError anError) {
                System.out.println(anError.toString());
                dialog_valida_login.dismiss();
                ToastClass.curto(activity, "Aparelho sem conexão com a internet");
            }
        });
    }

    public static void lista (final Activity activity, final View view, final List<CartaoCreditoModel> lista_cartao, final RecyclerView recyclerView, final TextView text_not_cartao_profile, final LottieAnimationView load_cartao){

        AndroidNetworking.get(APIServer.URL+"api/listcards")
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            lista_cartao.clear();
                            JSONArray cartaos = response.getJSONObject("content").getJSONArray("cards");
                            if(cartaos.length() > 0){
                                load_cartao.setVisibility(View.GONE);
                                text_not_cartao_profile.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                lista_cartao.clear();
                                for(int i = 0; i < cartaos.length(); i++){
                                    JSONObject jsonObject = cartaos.getJSONObject(i).getJSONObject("card");

                                    CartaoCreditoModel cartaoCreditoModel = new CartaoCreditoModel(
                                            jsonObject.getInt("id"),
                                            jsonObject.getString("method"),
                                            jsonObject.getString("brand"),
                                            jsonObject.getString("holder_name"),
                                            jsonObject.getString("display_number"),
                                            jsonObject.getString("bin"),
                                            jsonObject.getString("month"),
                                            jsonObject.getString("year"),
                                            jsonObject.getBoolean("test"),
                                            jsonObject.getBoolean("starts"),
                                            jsonObject.getInt("created_at"));
                                    lista_cartao.add(cartaoCreditoModel);
                                }
                                ListaCartaoCreditoAdapter listaCartaoCreditoAdapter = new ListaCartaoCreditoAdapter(activity, view, lista_cartao, recyclerView, text_not_cartao_profile);
                                recyclerView.setAdapter(listaCartaoCreditoAdapter);

                            }else{
                                text_not_cartao_profile.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                load_cartao.setVisibility(View.GONE);
                            };
                            break;
                        default:
                            text_not_cartao_profile.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            load_cartao.setVisibility(View.GONE);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    recyclerView.setVisibility(View.GONE);
                    text_not_cartao_profile.setVisibility(View.VISIBLE);
                    load_cartao.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(ANError anError) {
                load_cartao.setVisibility(View.GONE);
            }
        });
    }

    public static void lista_carrinho (final Activity activity, final List<CartaoCreditoModel> lista_cartao, final RecyclerView recyclerView){
        AndroidNetworking.get(APIServer.URL+"api/listcards")
                .addHeaders("Authorization", APIServer.token(activity))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            int code = response.getInt("code");
                            switch (code){
                                case 0:
                                    lista_cartao.clear();
                                    JSONArray cartaos = response.getJSONObject("content").getJSONArray("cards");
                                    if(cartaos.length() > 0){
                                        for(int i = 0; i < cartaos.length(); i++){
                                            JSONObject jsonObject = cartaos.getJSONObject(i).getJSONObject("card");
                                            CartaoCreditoModel cartaoCreditoModel = new CartaoCreditoModel(
                                                    jsonObject.getInt("id"),
                                                    jsonObject.getString("method"),
                                                    jsonObject.getString("brand"),
                                                    jsonObject.getString("holder_name"),
                                                    jsonObject.getString("display_number"),
                                                    jsonObject.getString("bin"),
                                                    jsonObject.getString("month"),
                                                    jsonObject.getString("year"),
                                                    jsonObject.getBoolean("test"),
                                                    jsonObject.getBoolean("starts"),
                                                    jsonObject.getInt("created_at"));
                                            lista_cartao.add(cartaoCreditoModel);
                                        }
                                        ListaCartaoCarrinhoAdapter listaCartaoCarrinhoAdapter = new ListaCartaoCarrinhoAdapter(activity, lista_cartao, recyclerView);
                                        recyclerView.setAdapter(listaCartaoCarrinhoAdapter);
                                    }
                                    break;
                                default:
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

    public static void cadastrar (final Activity activity, String number, String ccv, String firstName, String lastName, String validateMonth, String vaidateYear){

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        final View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_valida_novo_cartao, null);
        mBuilder.setView(dialogView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog_valida = mBuilder.create();
        dialog_valida.show();

        System.out.println(lastName);

        AndroidNetworking.post(APIServer.URL+"api/addcard")
            .addHeaders("Authorization", APIServer.token(activity))
            .addBodyParameter("number", number)
            .addBodyParameter("ccv", ccv)
            .addBodyParameter("first_name", firstName)
            .addBodyParameter("last_name", lastName)
            .addBodyParameter("validate_month", validateMonth)
            .addBodyParameter("validate_year", vaidateYear)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try{
                    int code = response.getInt("code");
                    System.out.println(code);
                    switch (code){
                        case 0:
                            dialog_valida.dismiss();
                            ToastClass.curto(activity, "Cartão cadastrado com sucesso");
                            Intent intent = new Intent();
                            activity.setResult(Activity.RESULT_OK, intent);
                            activity.onBackPressed();
                            break;
                        case 40:
                            dialog_valida.dismiss();
                            ToastClass.curto(activity, "Cartão de crédito inválido. Tente novamente");
                            break;
                        default:
                            dialog_valida.dismiss();
                            Intent i = new Intent();
                            activity.setResult(Activity.RESULT_CANCELED, i);
                            activity.onBackPressed();

                    }
                }catch (JSONException e){
                    dialog_valida.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                dialog_valida.dismiss();
                Intent i = new Intent();
                activity.setResult(Activity.RESULT_CANCELED, i);
                activity.onBackPressed();
            }
        });
    }

    public static void remover (final Activity activity, final View view, int id_cartao){

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        final View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_remove_cartao_credito, null);
        mBuilder.setView(dialogView);
        mBuilder.setCancelable(false);
        final AlertDialog remove_cartao = mBuilder.create();
        remove_cartao.show();

        AndroidNetworking.delete(APIServer.URL+"api/removecard/"+id_cartao)
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            remove_cartao.dismiss();
                            SnackbarClass.curto(view, "Cartão removido com sucesso");
                            break;
                        default:
                            remove_cartao.dismiss();
                            ToastClass.curto(activity, "Não foi possível remover este cartão no momento.\nTente mais tarde");
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    remove_cartao.dismiss();
                }
            }

            @Override
            public void onError(ANError anError) {
                remove_cartao.dismiss();
                ToastClass.curto(activity, "Aparelho sem conexão com a internet");
            }
        });
    }

};
