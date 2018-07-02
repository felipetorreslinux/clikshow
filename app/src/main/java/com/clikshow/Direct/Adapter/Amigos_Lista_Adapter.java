package com.clikshow.Direct.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.Direct.Models.Amigos_Model;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Amigos_Lista_Adapter extends RecyclerView.Adapter<Amigos_Lista_Adapter.AmigosHolder> {

    Activity activity;
    List<Amigos_Model> lista_amigos;

    public Amigos_Lista_Adapter(final Activity activity, final List<Amigos_Model> lista_amigos){
        this.activity = activity;
        this.lista_amigos = lista_amigos;
    }

    @NonNull
    @Override
    public AmigosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_amigos_direct, parent, false);
        return new Amigos_Lista_Adapter.AmigosHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AmigosHolder holder, int position) {
        final Amigos_Model amigos_model = lista_amigos.get(position);

        if(amigos_model.getThumb().isEmpty() || amigos_model.getThumb().equals("null")){
            Picasso.get()
                    .load(R.drawable.ic_profile)
                    .resize(100, 100)
                    .transform(new CropCircleTransformation())
                    .into(holder.imageview_amigos_lista_direct);
        }else{
            Picasso.get()
                    .load(amigos_model.getThumb())
                    .resize(100, 100)
                    .transform(new CropCircleTransformation())
                    .into(holder.imageview_amigos_lista_direct);
        }

        holder.name_amigos_lista_direct.setText(amigos_model.getName());
        holder.username_amigos_lista_direct.setText(amigos_model.getUsername());

        holder.item_lista_amigos_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastClass.curto(activity, amigos_model.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista_amigos != null ? lista_amigos.size() : 0;
    }

    public class AmigosHolder extends RecyclerView.ViewHolder{

        LinearLayout item_lista_amigos_direct;

        ImageView imageview_amigos_lista_direct;
        TextView name_amigos_lista_direct;
        TextView username_amigos_lista_direct;

        public AmigosHolder(View itemView) {
            super(itemView);

            item_lista_amigos_direct = itemView.findViewById(R.id.item_lista_amigos_direct);
            imageview_amigos_lista_direct = itemView.findViewById(R.id.imageview_amigos_lista_direct);
            name_amigos_lista_direct = itemView.findViewById(R.id.name_amigos_lista_direct);
            username_amigos_lista_direct = itemView.findViewById(R.id.username_amigos_lista_direct);
        }
    }
}
