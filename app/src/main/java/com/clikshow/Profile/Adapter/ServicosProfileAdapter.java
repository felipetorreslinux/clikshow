package com.clikshow.Profile.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.ClikBIlheteria.Impressora.BlueTooth;
import com.clikshow.ClikBIlheteria.Services.Bilheteria_Service;
import com.clikshow.ClikBIlheteria.Views.View_Lista_Ingressos_Bilheteria;
import com.clikshow.ClikPortaria.Views.View_Principal_Portaria;
import com.clikshow.Portaria.View_Portaria;
import com.clikshow.Profile.Models.ServicosProfileModel;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.clikshow.Service.Toast.ToastClass;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ServicosProfileAdapter extends RecyclerView.Adapter<ServicosProfileAdapter.ServicosHolder>{

    private List<ServicosProfileModel> lista_servicos;
    private Activity activity;

    public ServicosProfileAdapter(final Activity activity, List<ServicosProfileModel> lista_servicos){
        this.activity = activity;
        this.lista_servicos = lista_servicos;

    }
    @NonNull
    @Override
    public ServicosProfileAdapter.ServicosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_servicos_profile, parent, false);
        return new ServicosProfileAdapter.ServicosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicosProfileAdapter.ServicosHolder holder, int position) {
        final ServicosProfileModel servicosProfileModel = lista_servicos.get(position);
        final Transformation transformation = new RoundedCornersTransformation(10, 0);

        if(servicosProfileModel.getThumb().equals(null)){
            Picasso.get()
                    .load(R.drawable.ef_image_placeholder)
                    .transform(transformation)
                    .into(holder.img_evento_servico_profile);

        }else{
            Picasso.get()
                    .load(servicosProfileModel.getThumb())
                    .transform(transformation)
                    .into(holder.img_evento_servico_profile);

        };

        holder.name_evento_servico_profile.setText(servicosProfileModel.getName());
        holder.tipo_servico_profile.setText(servicosProfileModel.getRole_name());
        holder.data_servico_profile.setText(Datas.data_extenso(servicosProfileModel.getStarts()));

        holder.open_servicos_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (servicosProfileModel.getRole_id()){

                    // Coordenador
                    case 3:
                        Intent open_coordenador = new Intent(activity, View_Lista_Ingressos_Bilheteria.class);
                        open_coordenador.putExtra("event_id", servicosProfileModel.getEvent_id());
                        open_coordenador.putExtra("event_name", servicosProfileModel.getName());
                        open_coordenador.putExtra("type_service", 3);
                        activity.startActivityForResult(open_coordenador, 1000);
                        break;

                    // Portaria
                    case 7:
                        Intent open_portaria = new Intent(activity, View_Portaria.class);
                        open_portaria.putExtra("event_id", servicosProfileModel.getEvent_id());
                        open_portaria.putExtra("type_service", 7);
                        activity.startActivity(open_portaria);
                        break;

                    // Revendedor
                    case 8:
                        Intent open_revendedor = new Intent(activity, View_Lista_Ingressos_Bilheteria.class);
                        open_revendedor.putExtra("event_id", servicosProfileModel.getEvent_id());
                        open_revendedor.putExtra("event_name", servicosProfileModel.getName());
                        open_revendedor.putExtra("type_service", 8);
                        activity.startActivityForResult(open_revendedor, 1000);
                        break;

                };
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista_servicos != null ? lista_servicos.size() : 0;
    }

    public class ServicosHolder extends RecyclerView.ViewHolder{

        LinearLayout box_item_servico_profile;

        ImageView img_evento_servico_profile;
        TextView name_evento_servico_profile;
        TextView tipo_servico_profile;
        TextView data_servico_profile;

        Button open_servicos_profile;

        public ServicosHolder(View itemView) {
            super(itemView);

            box_item_servico_profile = itemView.findViewById(R.id.box_item_servico_profile);

            img_evento_servico_profile = itemView.findViewById(R.id.img_evento_servico_profile);
            name_evento_servico_profile = itemView.findViewById(R.id.name_evento_servico_profile);
            tipo_servico_profile = itemView.findViewById(R.id.tipo_servico_profile);
            data_servico_profile = itemView.findViewById(R.id.data_servico_profile);

            open_servicos_profile = itemView.findViewById(R.id.open_servicos_profile);

        }
    };
}
