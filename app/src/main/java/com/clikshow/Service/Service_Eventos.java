package com.clikshow.Service;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Adapter.Eventos_Feed_Adapter;
import com.clikshow.Fragmentos.Adapter.Eventos_Slide_Adapter;
import com.clikshow.Fragmentos.Adapter.Ingressos_Detalhes_Adapter;
import com.clikshow.Fragmentos.Adapter.Search_Lista_Adapter;
import com.clikshow.Fragmentos.Models.Feed_Eventos;
import com.clikshow.Fragmentos.Models.FeedSlide;
import com.clikshow.Fragmentos.Models.IngressosModel;
import com.clikshow.Fragmentos.Models.Search_Model;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Views.View_Resultado_Search;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;
import me.relex.circleindicator.CircleIndicator;

public class Service_Eventos {

    public static void listar_search (final Activity activity, final String search, final List<Search_Model> lista_search, final RecyclerView recyclerView){
        AndroidNetworking.post(APIServer.URL+"api/searchevents")
            .addHeaders("Authorization", APIServer.token(activity))
            .addBodyParameter("search", search)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        int code = response.getInt("code");
                        switch (code){
                            case 0:
                                if (activity != null) {
                                    JSONArray search = response.getJSONObject("content").getJSONArray("event");
                                    if(search.length() > 0){
                                        if(search.length() > 1){
                                            View_Resultado_Search.qtd_info_resultado.setText(search.length()+" eventos encontrados");
                                        }else{
                                            View_Resultado_Search.qtd_info_resultado.setText(search.length()+" evento encontrado");
                                        };
                                        for (int i = 0; i < search.length(); i++){
                                            JSONObject jsonObject = search.getJSONObject(i);
                                            Search_Model search_model = new Search_Model(
                                                    jsonObject.getInt("id"),
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
                                                    jsonObject.getString("producer_id"),
                                                    jsonObject.getBoolean("is_private"),
                                                    jsonObject.getBoolean("is_favorite"));
                                            lista_search.add(search_model);
                                        }
                                        Search_Lista_Adapter search_lista_adapter = new Search_Lista_Adapter(activity, lista_search);
                                        recyclerView.setAdapter(search_lista_adapter);
                                    }else{
                                        View_Resultado_Search.qtd_info_resultado.setText("Nenhum evento foi encontrado");
                                    };
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

    public static void lista_eventos (final Activity activity, final List<FeedSlide> slides, final List<Feed_Eventos> eventos, final RecyclerView mRecyclerView, final UltraViewPager mViewPager, final SwipeRefreshLayout swipeRefreshLayout, final ViewStub loading_tab_feed){
        AndroidNetworking.get(APIServer.URL+"api/listevents")
        .addHeaders("Authorization", APIServer.token(activity))
        .setPriority(Priority.LOW)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            try{
                                if(activity != null){
                                    slides.clear();
                                    eventos.clear();
                                    swipeRefreshLayout.setRefreshing(false);

                                    JSONArray eventos_lista = response.getJSONObject("content").getJSONArray("event");

                                    if(eventos_lista.length() > 0){

                                        for (int i = 0; i < 5; i++) {

                                            JSONObject jsonObject = eventos_lista.getJSONObject(i);

                                            FeedSlide feedSlide = new FeedSlide(
                                                    jsonObject.getInt("id"),
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
                                                    jsonObject.getString("producer_name"),
                                                    jsonObject.getBoolean("is_private"),
                                                    jsonObject.getBoolean("is_favorite"));
                                            slides.add(feedSlide);
                                        }

                                        for (int i = 0; i < eventos_lista.length(); i++) {

                                            JSONObject jsonObject = eventos_lista.getJSONObject(i);

                                            Feed_Eventos events = new Feed_Eventos(
                                                    jsonObject.getInt("id"),
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
                                                    jsonObject.getString("producer_name"),
                                                    jsonObject.getBoolean("is_private"),
                                                    jsonObject.getBoolean("is_favorite"));
                                            eventos.add(events);

                                        };

                                        Eventos_Feed_Adapter eventos_feed_adapter = new Eventos_Feed_Adapter(activity, eventos);
                                        Eventos_Slide_Adapter eventos_slide_feed = new Eventos_Slide_Adapter(activity, slides);
                                        mViewPager.setAdapter(eventos_slide_feed);
                                        mViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
                                        mViewPager.initIndicator();
                                        mViewPager.getIndicator()
                                                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                                                .setFocusColor(activity.getResources().getColor(R.color.colorAccent))
                                                .setNormalColor(activity.getResources().getColor(R.color.white ))
                                                .setRadius((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, activity.getResources().getDisplayMetrics()));
                                        mViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                                        mViewPager.getIndicator().setMargin(0,25, 0 ,25);
                                        mViewPager.getIndicator().build();
                                        mViewPager.setInfiniteLoop(true);
                                        mViewPager.setAutoScroll(4000);
                                        mViewPager.setPageTransformer(false, new UltraScaleTransformer());
                                        mRecyclerView.setAdapter(eventos_feed_adapter);

                                        swipeRefreshLayout.setRefreshing(false);
                                        loading_tab_feed.setVisibility(View.GONE);

                                    }else{
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            break;
                        default:
                            swipeRefreshLayout.setRefreshing(false);
                    };
                }catch (JSONException e){
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
            };
            @Override
            public void onError(ANError error) {
                swipeRefreshLayout.setRefreshing(false);
                loading_tab_feed.setVisibility(View.GONE);
            };
        });
    }

    public static void listar_ingressos(final Activity activity, final List<IngressosModel> lista_ingressos, int id, final RecyclerView recyclerView, final ShimmerLayout shimmerLayout){
        shimmerLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        shimmerLayout.startShimmerAnimation();
        AndroidNetworking.get(APIServer.URL+"api/listpass/"+id)
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code =  response.getInt("code");
                    switch (code){
                        case 0:
                            shimmerLayout.stopShimmerAnimation();
                            if(activity != null){
                                try{
                                    lista_ingressos.clear();
                                    JSONArray ingressos = response.getJSONObject("content").getJSONArray("passes");
                                    if(ingressos.length() > 0){
                                        for(int i = 0; i < ingressos.length(); i++){
                                            JSONObject jsonObject = ingressos.getJSONObject(i);
                                            IngressosModel ingressosModel = new IngressosModel(
                                                    jsonObject.getInt("id"),
                                                    jsonObject.getInt("event_id"),
                                                    jsonObject.getString("event_name"),
                                                    jsonObject.getString("event_thumb"),
                                                    jsonObject.getString("name"),
                                                    jsonObject.getString("type"),
                                                    jsonObject.getInt("status"),
                                                    jsonObject.getLong("price"),
                                                    jsonObject.getString("description"),
                                                    jsonObject.getInt("starts"),
                                                    jsonObject.getInt("ends"),
                                                    0);
                                            lista_ingressos.add(ingressosModel);
                                        }
                                        Ingressos_Detalhes_Adapter ingressos_detalhes_adapter = new Ingressos_Detalhes_Adapter(activity, lista_ingressos);
                                        recyclerView.setAdapter(ingressos_detalhes_adapter);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        shimmerLayout.setVisibility(View.GONE);
                                    }else{
                                        lista_ingressos.clear();
                                        recyclerView.setVisibility(View.GONE);
                                        shimmerLayout.setVisibility(View.VISIBLE);
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                            break;
                        default:
                            recyclerView.setVisibility(View.GONE);
                            shimmerLayout.setVisibility(View.VISIBLE);
                            shimmerLayout.startShimmerAnimation();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    recyclerView.setVisibility(View.GONE);
                    shimmerLayout.setVisibility(View.VISIBLE);
                    shimmerLayout.stopShimmerAnimation();
                }
            }

            @Override
            public void onError(ANError anError) {
                recyclerView.setVisibility(View.GONE);
                shimmerLayout.setVisibility(View.VISIBLE);
                shimmerLayout.startShimmerAnimation();
            }
        });
    }

}
