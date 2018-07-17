package com.clikshow.Direct.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.Direct.Models.Usuarios_Online_Model;
import com.clikshow.Direct.View_Chat_Direct;
import com.clikshow.R;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Usuarios_Online_Adapter extends RecyclerView.Adapter<Usuarios_Online_Adapter.UsuariosOnlineHolder> {

    Activity activity;
    List<Usuarios_Online_Model> lista_usuarios;

    public Usuarios_Online_Adapter(Activity activity, final List<Usuarios_Online_Model> lista_usuarios){
        this.activity = activity;
        this.lista_usuarios = lista_usuarios;
        Collections.reverse(lista_usuarios);
    }

    @NonNull
    @Override
    public UsuariosOnlineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuarios_online, parent, false);
        return new Usuarios_Online_Adapter.UsuariosOnlineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosOnlineHolder holder, int position) {
        Usuarios_Online_Model usuarios_online_model = lista_usuarios.get(position);

        profileOnline(usuarios_online_model, holder);

    }

    @Override
    public int getItemCount() {
        return lista_usuarios != null ? lista_usuarios.size() : 0;
    }

    public class UsuariosOnlineHolder extends RecyclerView.ViewHolder {

        ImageView imageview_usuario_online;
        TextView textview_username_online;

        public UsuariosOnlineHolder(View itemView) {
            super(itemView);
            imageview_usuario_online = itemView.findViewById(R.id.imageview_usuario_online);
            textview_username_online = itemView.findViewById(R.id.textview_username_online);
        }
    }

    private void profileOnline(final Usuarios_Online_Model usuarios_online_model, UsuariosOnlineHolder holder){

        if(usuarios_online_model.getThumb().isEmpty()){
            Picasso.get()
                    .load(R.drawable.ic_profile)
                    .resize(150,150)
                    .transform(new CropCircleTransformation())
                    .into(holder.imageview_usuario_online);
        }else{
            Picasso.get()
                    .load(usuarios_online_model.getThumb())
                    .resize(150,150)
                    .transform(new CropCircleTransformation())
                    .into(holder.imageview_usuario_online);
        };

        holder.textview_username_online.setText(usuarios_online_model.getUsername().toLowerCase());

        holder.imageview_usuario_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, View_Chat_Direct.class);
                intent.putExtra("id_amigo", usuarios_online_model.getId());
                intent.putExtra("image_amigo", usuarios_online_model.getThumb());
                intent.putExtra("name_amigo", usuarios_online_model.getName());
                intent.putExtra("username_amigo", usuarios_online_model.getUsername());
                activity.startActivity(intent);
            }
        });
    }
}
