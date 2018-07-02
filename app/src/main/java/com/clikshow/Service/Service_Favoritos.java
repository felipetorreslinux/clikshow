package com.clikshow.Service;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Adapter.Favoritos_Tab_Adapter;
import com.clikshow.Fragmentos.Models.FavoritosModel;
import com.clikshow.Service.Toast.ToastClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Service_Favoritos {

    public static void cadastrar (final Activity activity, String event_id){
        AndroidNetworking.post(APIServer.URL+"api/addfavorite")
        .addHeaders("Authorization", APIServer.token(activity))
        .addBodyParameter("event_id", event_id)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            break;
                        default:
                            ToastClass.curto(activity, "Você já favoritou este evento");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    ToastClass.curto(activity, "Aparelho sem conexão com a internet");
                }
            }

            @Override
            public void onError(ANError anError) {
                ToastClass.curto(activity, "Aparelho sem conexão com a internet");
            }
        });
    };

    public static void deletar (final Activity activity, String event_id){
        AndroidNetworking.delete(APIServer.URL+"api/removefavorite/"+event_id)
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int code = response.getInt("code");
                    switch (code){
                        case 0:

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

    public static void listar (final Activity activity, final List<FavoritosModel> lista_favoritos, final RecyclerView mRecyclerView, final ViewStub box_sem_itens, final ViewStub loading_tab_favorites){
        AndroidNetworking.get(APIServer.URL+"api/listfavorites")
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
                            if(activity != null){
                                JSONArray favoritos = response.getJSONObject("content").getJSONArray("favorites");
                                if(favoritos.length() > 0){
                                    lista_favoritos.clear();
                                    for(int i = 0; i < favoritos.length(); i++){
                                        JSONObject jsonObject = favoritos.getJSONObject(i);
                                        FavoritosModel favoritosModel = new FavoritosModel(
                                                jsonObject.getInt("id"),
                                                jsonObject.getInt("event_id"),
                                                jsonObject.getString("name"),
                                                jsonObject.getString("type"),
                                                jsonObject.getInt("starts"),
                                                jsonObject.getInt("ends"),
                                                jsonObject.getString("banner"),
                                                jsonObject.getString("thumb"),
                                                jsonObject.getString("city"),
                                                jsonObject.getString("state"),
                                                jsonObject.getString("lat"),
                                                jsonObject.getString("lng"),
                                                jsonObject.getString("classification"),
                                                jsonObject.getString("description"),
                                                jsonObject.getBoolean("is_private"));
                                        lista_favoritos.add(favoritosModel);
                                    };
                                    Favoritos_Tab_Adapter favoritos_tab_adapter = new Favoritos_Tab_Adapter(activity, lista_favoritos);
                                    mRecyclerView.setAdapter(favoritos_tab_adapter);
                                    loading_tab_favorites.setVisibility(View.GONE);
                                }else{
                                    lista_favoritos.clear();
                                    box_sem_itens.inflate();
                                    loading_tab_favorites.setVisibility(View.GONE);
                                }
                            }
                            break;
                        default:
                        lista_favoritos.clear();
                        box_sem_itens.inflate();
                        loading_tab_favorites.setVisibility(View.GONE);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    lista_favoritos.clear();
                    box_sem_itens.inflate();
                    loading_tab_favorites.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(ANError anError) {

            }
        });
    };
}
