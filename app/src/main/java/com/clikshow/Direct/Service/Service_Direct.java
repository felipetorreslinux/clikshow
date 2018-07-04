package com.clikshow.Direct.Service;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.Direct.Adapter.Amigos_Lista_Adapter;
import com.clikshow.Direct.Models.Amigos_Model;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Loading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Service_Direct {

    public static void lista_amigos (final Activity activity, final List<Amigos_Model> lista_amigos, final RecyclerView recyclerView){
        AndroidNetworking.get(APIServer.CLIKSOCIALPROD+"api/followers")
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try {
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            JSONArray amigos = response.getJSONObject("content").getJSONArray("followers");
                            if(activity != null){
                                lista_amigos.clear();
                                for (int i = 0; i < amigos.length(); i++){
                                    JSONObject jsonObject = amigos.getJSONObject(i);
                                    Amigos_Model amigos_model = new Amigos_Model(
                                            jsonObject.getInt("user_id"),
                                            jsonObject.getString("profile_pic_thumb"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("username"));
                                    lista_amigos.add(amigos_model);
                                }
                                Amigos_Lista_Adapter amigos_lista_adapter = new Amigos_Lista_Adapter(activity, lista_amigos);
                                recyclerView.setAdapter(amigos_lista_adapter);
                                Loading.close();
                            }
                            break;
                        default:

                    }
                }catch (JSONException e){

                }catch (NullPointerException e){

                }
            }

            @Override
            public void onError(ANError anError) {
                APIServer.error_server(activity, anError.getErrorCode());
                Loading.close();
            }
        });
    }

    public static void coutn_user_message (final Activity activity, int sender, final TextView textView){
        textView.setVisibility(View.GONE);
        AndroidNetworking.post(APIServer.LOCAL_CHAT+"count_message.php")
                .addBodyParameter("sender", String.valueOf(sender))
                .addBodyParameter("token", APIServer.token(activity))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            int code = response.getInt("code");
                            switch (code){
                                case 0:
                                    int count = response.getInt("count");
                                    textView.setVisibility(View.VISIBLE);
                                    textView.setText(String.valueOf(count));
                                    break;
                                default:
                                    textView.setVisibility(View.GONE);
                                    textView.setText("0");
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
