package com.clikshow.Direct.Service;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.Direct.Adapter.Amigos_Lista_Adapter;
import com.clikshow.Direct.Models.Amigos_Model;

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
            }
        });
    }
}
