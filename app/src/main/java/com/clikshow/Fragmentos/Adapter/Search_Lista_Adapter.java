package com.clikshow.Fragmentos.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.Fragmentos.Models.Search_Model;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.clikshow.Views.View_Detalhe_Evento;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Search_Lista_Adapter extends RecyclerView.Adapter<Search_Lista_Adapter.SearchHolder> {

    private List<Search_Model> lista_search;
    private Activity activity;


    public Search_Lista_Adapter(final Activity activity, final List<Search_Model> lista_search){
        this.activity = activity;
        this.lista_search = lista_search;

    }

    @NonNull
    @Override
    public Search_Lista_Adapter.SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_list_event, parent, false);
        return new Search_Lista_Adapter.SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Search_Lista_Adapter.SearchHolder holder, int position) {

        animation(holder.item_lista_search);

        final Transformation transformation = new RoundedCornersTransformation(4, 0);
        final Search_Model search_model = lista_search.get(position);

        if(!search_model.getBanner().equals(null)){
            Picasso.get()
                    .load(search_model.getBanner())
                    .transform(transformation)
                    .into(holder.search_image);
        }else{
            Picasso.get()
                    .load(R.drawable.ef_image_placeholder)
                    .transform(transformation)
                    .into(holder.search_image);
        }


        holder.search_event_name.setText(search_model.getName());
        holder.search_data.setText(Datas.data_extenso(search_model.getStarts()));

        holder.search_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open = new Intent(activity, View_Detalhe_Evento.class);
                open.putExtra("id", search_model.getId());
                open.putExtra("name", search_model.getName());
                open.putExtra("type", search_model.getType());
                open.putExtra("starts", search_model.getStarts());
                open.putExtra("description", search_model.getDescription());
                open.putExtra("ends", search_model.getEnds());
                open.putExtra("banner", search_model.getBanner());
                open.putExtra("lat", search_model.getLat());
                open.putExtra("lng", search_model.getLng());
                open.putExtra("is_favorite", search_model.isIs_favorite());
                activity.startActivity(open);
            }
        });
    }

    private void animation(LinearLayout linearLayout){
        Animation animation = new TranslateAnimation(2000, 0,0, 0);
        animation.setDuration(400);
        animation.setFillAfter(true);
        linearLayout.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return lista_search != null ? lista_search.size() : 0;
    }

    public class SearchHolder extends RecyclerView.ViewHolder{

        LinearLayout item_lista_search;
        ImageView search_image;
        TextView search_data;
        TextView search_event_name;

        public SearchHolder(View itemView) {
            super(itemView);

            item_lista_search = itemView.findViewById(R.id.item_lista_search_box);
            search_image = itemView.findViewById(R.id.imagem_search);
            search_data = itemView.findViewById(R.id.data_search);
            search_event_name = itemView.findViewById(R.id.titulo_search);
        }
    }
}
