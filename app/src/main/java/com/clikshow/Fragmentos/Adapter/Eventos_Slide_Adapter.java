package com.clikshow.Fragmentos.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Models.FeedSlide;
import com.clikshow.Service.Datas;
import com.clikshow.Service.Service_Favoritos;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Views.View_Detalhe_Evento;
import com.clikshow.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Eventos_Slide_Adapter extends PagerAdapter {

    Activity activity;
    View view;
    List<FeedSlide> lista_eventos = new ArrayList<>();

    public Eventos_Slide_Adapter(final Activity activity, List<FeedSlide> feedSlides){
        this.activity = activity;
        this.lista_eventos = feedSlides;
    }

    @Override
    public int getCount() {
        return lista_eventos != null ? lista_eventos.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        view = LayoutInflater.from(activity).inflate(R.layout.item_image_slide, container, false);
        final FeedSlide itens = lista_eventos.get(position);
        Transformation transformation = new RoundedCornersTransformation(6, 0);
        ImageView img_slide_eventos = view.findViewById(R.id.img_slide_eventos);
        TextView name_slide_eventos = view.findViewById(R.id.name_slide_eventos);
        TextView descricao_slide_eventos = view.findViewById(R.id.descricao_slide_eventos);

        Picasso.get()
            .load(itens.getBanner())
            .transform(transformation)
            .into(img_slide_eventos);

        name_slide_eventos.setText(itens.getName());
        descricao_slide_eventos.setText(Datas.data_hora_evento_feed(itens.getStarts()));
        descricao_slide_eventos.setAlpha((float) 0.7);

        container.addView(view, 0);

        img_slide_eventos.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent open = new Intent(activity, View_Detalhe_Evento.class);
                open.putExtra("id", itens.getId());
                open.putExtra("name", itens.getName());
                open.putExtra("type", itens.getType());
                open.putExtra("description", itens.getDescription());
                open.putExtra("starts", itens.getStarts());
                open.putExtra("ends", itens.getEnds());
                open.putExtra("banner", itens.getBanner());
                open.putExtra("lat", itens.getLat());
                open.putExtra("lng", itens.getLng());
                open.putExtra("is_favorite", itens.isIs_favorite());
                activity.startActivityForResult(open, 3000);
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);
    }

}
