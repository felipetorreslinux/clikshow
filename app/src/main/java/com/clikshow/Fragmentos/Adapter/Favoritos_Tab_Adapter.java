package com.clikshow.Fragmentos.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.Fragmentos.Favorites_Fragment;
import com.clikshow.Fragmentos.Models.FavoritosModel;
import com.clikshow.Views.View_Detalhe_Evento;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.clikshow.Service.Service_Favoritos;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Favoritos_Tab_Adapter extends RecyclerView.Adapter<Favoritos_Tab_Adapter.FavoritosHolder>{
    List<FavoritosModel> lista_favoritos;
    Activity activity;
    View view;

    public Favoritos_Tab_Adapter(final Activity activity, List<FavoritosModel> lista_favoritos){
        this.activity = activity;
        this.lista_favoritos = lista_favoritos;
    }

    @NonNull
    @Override
    public FavoritosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favoritos, parent, false);
        return new Favoritos_Tab_Adapter.FavoritosHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FavoritosHolder holder, final int position) {
        final FavoritosModel favoritosModel = lista_favoritos.get(position);
        final Transformation transformation = new RoundedCornersTransformation(4, 0);

       if(!favoritosModel.getBanner().isEmpty() || !favoritosModel.getBanner().equals(null)){
           Picasso.get()
           .load(favoritosModel.getBanner())
           .transform(transformation)
           .into(holder.image_favoritos);
       }else{
           Picasso.get()
           .load(R.drawable.ef_image_placeholder)
           .transform(transformation)
           .into(holder.image_favoritos);
       };

       holder.nome_evento.setText(favoritosModel.getName());
       holder.data_evento.setText(Datas.data_extenso(favoritosModel.getStarts()));

       holder.image_favoritos.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent open = new Intent(activity, View_Detalhe_Evento.class);
               open.putExtra("id", favoritosModel.getEvent_id());
               open.putExtra("event_id", favoritosModel.getEvent_id());
               open.putExtra("name", favoritosModel.getName());
               open.putExtra("type", favoritosModel.getType());
               open.putExtra("starts", favoritosModel.getStarts());
               open.putExtra("ends", favoritosModel.getEnds());
               open.putExtra("banner", favoritosModel.getBanner());
               open.putExtra("lat", favoritosModel.getLat());
               open.putExtra("lng", favoritosModel.getLng());
               open.putExtra("is_favorite", true);
               activity.startActivityForResult(open, 2000);

           }
       });

       holder.btn_remove_favorites.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
           lista_favoritos.remove(position);
           notifyItemRemoved(position);
           notifyItemRangeChanged(position, lista_favoritos.size());
                Service_Favoritos.deletar(activity, String.valueOf(favoritosModel.getEvent_id()));
           }
       });

    }

    @Override
    public int getItemCount() {
        if(lista_favoritos.size() == 0){
            activity.getFragmentManager().beginTransaction().replace(R.id.container_principal,
                    new Favorites_Fragment()).commit();
        }else{
            return lista_favoritos.size();
        }
        return lista_favoritos != null ? lista_favoritos.size() : 0;
    };

    public class FavoritosHolder extends RecyclerView.ViewHolder {

        ImageView btn_remove_favorites;
        ImageView image_favoritos;
        TextView nome_evento;
        TextView data_evento;


        public FavoritosHolder(View itemView) {
            super(itemView);

            btn_remove_favorites = itemView.findViewById(R.id.btn_remove_favorites);
            image_favoritos = itemView.findViewById(R.id.img_favoritos);
            nome_evento = itemView.findViewById(R.id.nome_evento_favoritos);
            data_evento = itemView.findViewById(R.id.data_evento_favoritos);
        }
    }
}
