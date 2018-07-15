package com.clikshow.Bilheteria.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.Bilheteria.Models.Bilheteria_Model;
import com.clikshow.Bilheteria.Views.View_Coordenador;
import com.clikshow.Bilheteria.Views.View_Lista_Ingressos_Bilheteria;
import com.clikshow.Bilheteria.Views.View_Revendedor;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Bilheteria_Lista_Ingressos_Adapter extends RecyclerView.Adapter<Bilheteria_Lista_Ingressos_Adapter.ItensIngressos> {

    List<Bilheteria_Model> lista_ingressos_bilheteria;
    Activity activity;

    public Bilheteria_Lista_Ingressos_Adapter(Activity activity, List<Bilheteria_Model> lista_ingressos_bilheteria, RecyclerView recyclerView){
        this.activity = activity;
        this.lista_ingressos_bilheteria = lista_ingressos_bilheteria;
    }

    @NonNull
    @Override
    public ItensIngressos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewFeed = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_ingressos_bilheteria, parent, false);
        return new ItensIngressos(viewFeed);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItensIngressos holder, int position) {
        final Bilheteria_Model bilheteria_model = lista_ingressos_bilheteria.get(position);

        Picasso.get()
                .load(bilheteria_model.getEvent_thumb())
                .resize(200,200)
                .into(holder.imageview_image_ingressgo_bilheteria);
        holder.name_ingresso_bilheteria.setText(bilheteria_model.getName());
        holder.evento_ingresso_bilheteria.setText(bilheteria_model.getEvent_name());
        holder.validade_ingresso_bilheteria.setText("Válido até "+ Datas.data_bilheteria(bilheteria_model.getEnds()));


        int type = View_Lista_Ingressos_Bilheteria.type_service;
        switch (type){
            case 3:
                holder.item_bilheteria.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, View_Coordenador.class);
                        intent.putExtra("id", bilheteria_model.getId());
                        intent.putExtra("event_id", bilheteria_model.getEvent_id());
                        intent.putExtra("event_name", bilheteria_model.getEvent_name());
                        intent.putExtra("event_thumb", bilheteria_model.getEvent_thumb());
                        intent.putExtra("type", bilheteria_model.getType());
                        intent.putExtra("status", bilheteria_model.getStatus());
                        intent.putExtra("price", bilheteria_model.getPrice());
                        intent.putExtra("name", bilheteria_model.getName());
                        intent.putExtra("description", bilheteria_model.getDescription());
                        intent.putExtra("starts", bilheteria_model.getStarts());
                        intent.putExtra("ends", bilheteria_model.getEnds());
                        activity.startActivity(intent);
                    }
                });
                break;
            case 8:
                holder.item_bilheteria.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, View_Revendedor.class);
                        intent.putExtra("id", bilheteria_model.getId());
                        intent.putExtra("event_id", bilheteria_model.getEvent_id());
                        intent.putExtra("event_name", bilheteria_model.getEvent_name());
                        intent.putExtra("event_thumb", bilheteria_model.getEvent_thumb());
                        intent.putExtra("type", bilheteria_model.getType());
                        intent.putExtra("status", bilheteria_model.getStatus());
                        intent.putExtra("price", bilheteria_model.getPrice());
                        intent.putExtra("name", bilheteria_model.getName());
                        intent.putExtra("description", bilheteria_model.getDescription());
                        intent.putExtra("starts", bilheteria_model.getStarts());
                        intent.putExtra("ends", bilheteria_model.getEnds());
                        activity.startActivity(intent);
                    }
                });
                break;
        }


    }

    @Override
    public int getItemCount() {
        return lista_ingressos_bilheteria !=  null ? lista_ingressos_bilheteria.size() : 0;
    }

    public class ItensIngressos extends RecyclerView.ViewHolder{

        LinearLayout item_bilheteria;
        ImageView imageview_image_ingressgo_bilheteria;
        TextView name_ingresso_bilheteria;
        TextView evento_ingresso_bilheteria;
        TextView validade_ingresso_bilheteria;

        public ItensIngressos(View itemView) {
            super(itemView);

            item_bilheteria = itemView.findViewById(R.id.item_ingresso_bilheteria);
            imageview_image_ingressgo_bilheteria = itemView.findViewById(R.id.imageview_image_ingressgo_bilheteria);;
            name_ingresso_bilheteria = itemView.findViewById(R.id.name_ingresso_bilheteria);
            evento_ingresso_bilheteria = itemView.findViewById(R.id.evento_ingresso_bilheteria);
            validade_ingresso_bilheteria = itemView.findViewById(R.id.validade_ingresso_bilheteria);

        }

    }

}
