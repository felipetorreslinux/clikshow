package com.clikshow.Service;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Adapter.Amigos_Lista_ClikSocial_Adapter;
import com.clikshow.Fragmentos.Models.AmigosClikSocialModel;
import com.clikshow.Service.Toast.ToastClass;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.clikshow.Views.View_Ingresso.cpf_validar_checkin;

public class Service_ClikSocial {

    public static void lista_amigos (final Activity activity, final List<AmigosClikSocialModel> lista_amigos, final RecyclerView recyclerView){
        AndroidNetworking.get(APIServer.CLIKSOCIALPROD+"api/following")
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray amigos = response.getJSONArray("content");
                            lista_amigos.clear();
                            if(amigos.length() > 0){
                                for(int i = 0; i < amigos.length(); i++){
                                    JSONObject jsonObject = amigos.getJSONObject(i);
                                    AmigosClikSocialModel amigosClikSocialModel = new AmigosClikSocialModel(
                                            jsonObject.getInt("user_id"),
                                            jsonObject.getString("profile_pic_thumb"),
                                            jsonObject.getString("username"),
                                            jsonObject.getString("name"),
                                            jsonObject.getBoolean("im_following"));
                                    lista_amigos.add(amigosClikSocialModel);
                                }
                                Amigos_Lista_ClikSocial_Adapter amigos_lista_clikSocial_adapter = new Amigos_Lista_ClikSocial_Adapter(activity, lista_amigos);
                                recyclerView.setAdapter(amigos_lista_clikSocial_adapter);
                            }else{
                                lista_amigos.clear();
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

    public static void buscar_cpf_id (final Activity activity, int user_id, final TextView textView, final EditText editText){
        AndroidNetworking.get(APIServer.CLIKSOCIALPROD+"api/profile/"+user_id)
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            JSONObject jsonObject = response.getJSONObject("content").getJSONObject("profile_info");
                            if(jsonObject.getString("cpf").equals("")){
                                textView.setText(null);
                                editText.setText(null);
                                editText.setCursorVisible(true);
                                editText.requestFocus();
                                ToastClass.curto(activity, "Seu amigo não tem CPF cadastrado no momento. Informe manualmente o CPF do mesmo no campo acima");
                            }else{
                                String cpf = jsonObject.getString("cpf");
                                textView.setText(jsonObject.getString("name"));
                                editText.setText(cpf.replaceAll("[0-9]", "*").replace("-", "").replace(".", ""));
                                cpf_validar_checkin=cpf.replace(".", "").replace("-", "");
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

    public static void buscar_users (final Activity activity, Editable busca, final List<AmigosClikSocialModel> lista_amigos, final RecyclerView recyclerView){
        AndroidNetworking.post(APIServer.CLIKSOCIALPROD+"api/search")
        .addHeaders("Authorization", APIServer.token(activity))
        .addBodyParameter("term", busca.toString())
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray amigos = response.getJSONArray("content");
                            if(amigos != null){
                                lista_amigos.clear();
                                if(amigos.length() > 0){
                                    for(int i = 0; i < amigos.length(); i++){
                                        JSONObject jsonObject = amigos.getJSONObject(i);
                                        AmigosClikSocialModel amigosClikSocialModel = new AmigosClikSocialModel(
                                                jsonObject.getInt("user_id"),
                                                jsonObject.getString("profile_pic_thumb") != null ? jsonObject.getString("profile_pic_thumb") : null,
                                                jsonObject.getString("username"),
                                                jsonObject.getString("name"),
                                                jsonObject.getBoolean("is_following"));
                                        lista_amigos.add(amigosClikSocialModel);
                                    }
                                    Amigos_Lista_ClikSocial_Adapter amigos_lista_clikSocial_adapter = new Amigos_Lista_ClikSocial_Adapter(activity, lista_amigos);
                                    recyclerView.setAdapter(amigos_lista_clikSocial_adapter);
                                }else{
                                    lista_amigos.clear();
                                }
                            }
                            break;
                        default:
                            lista_amigos.clear();
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

    public static void seguir_user (final Activity activity, int user_id, final Button button){
        AndroidNetworking.post(APIServer.CLIKSOCIALPROD+"api/follow")
        .addHeaders("Authorization", APIServer.token(activity))
        .addBodyParameter("followed_id", String.valueOf(user_id))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            button.setVisibility(View.GONE);
                            break;
                        default:
                            button.setVisibility(View.VISIBLE);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                System.out.println(anError.getMessage());
            }
        });
    }

    public static void enviar_direct (final Activity activity, String sender_id, String receiver_id, String username, String message){
        AndroidNetworking.post(APIServer.CLIKSOCIALPROD+"api/send_message")
        .addHeaders("Authorization", APIServer.token(activity))
        .addBodyParameter("sender_id", sender_id)
        .addBodyParameter("receiver_id", receiver_id)
        .addBodyParameter("room_id", sender_id+"_"+receiver_id)
        .addBodyParameter("username", username)
        .addBodyParameter("message", message)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            break;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                System.out.println(anError.getErrorCode());
                System.out.println(anError.getMessage());
                System.out.println(anError.getResponse());
                System.out.println(anError.getErrorBody());
            }
        });
    }
}
