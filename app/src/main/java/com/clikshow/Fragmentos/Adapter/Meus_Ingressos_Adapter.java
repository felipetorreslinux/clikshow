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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Models.MeusIngressosModel;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.clikshow.Service.Service_Meus_Ingressos;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Views.View_Ingresso;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class Meus_Ingressos_Adapter extends RecyclerView.Adapter<Meus_Ingressos_Adapter.IngressosNovosHolder> {

    private Activity activity;
    private List<MeusIngressosModel> lista_meus_ingressos;
    private static int TYPE_INGRESSO;


    public Meus_Ingressos_Adapter(final Activity activity, List<MeusIngressosModel> lista_meus_ingressos) {
        this.activity = activity;
        this.lista_meus_ingressos = lista_meus_ingressos;
    }

    @NonNull
    @Override
    public IngressosNovosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meus_ingressos, parent, false);
        return new IngressosNovosHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final IngressosNovosHolder holder, int position) {

        Transformation transformation = new RoundedCornersTransformation(6, 0);
        final MeusIngressosModel meus_ingressos_model = lista_meus_ingressos.get(position);

        Picasso.get()
            .load(meus_ingressos_model.getEvent_thumb())
            .resize(118, 118)
            .centerCrop()
            .transform(transformation)
            .error(R.drawable.ic_place_doodle)
            .placeholder(R.drawable.ic_place_doodle)
            .into(holder.imagemTicket);

        holder.dataTicket.setText("Válido até "+Datas.data_impressora(meus_ingressos_model.getStarts()));
        holder.tituloTicket.setText(meus_ingressos_model.getPass_name());
        holder.description_ticket.setText(meus_ingressos_model.getPass_description());


        switch (meus_ingressos_model.getStatus()){
            case 0:
                holder.checkTicket.setText("Negado");
                holder.checkTicket.setBackground(activity.getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_vermelho));
                break;
            case 1:
                if(meus_ingressos_model.getPayment_type() == 4){
                    holder.checkTicket.setText("Cortesia");
                    holder.checkTicket.setBackground(activity.getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_cortesia));
                }else if(meus_ingressos_model.getCpf().equals("null")){
                    holder.checkTicket.setText("Efetuar check-in");
                    holder.checkTicket.setBackground(activity.getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_laranja));
                }else{
                    holder.checkTicket.setText("Check-in realizado");
                    holder.checkTicket.setBackground(activity.getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_verde));
                };
                break;
            case 2:
                holder.checkTicket.setText("Pendente");
                holder.checkTicket.setBackground(activity.getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_vermelho));
                break;
            case 3:
                holder.checkTicket.setText("Extornado");
                holder.checkTicket.setBackground(activity.getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_preto));
                break;
            case 4:
                holder.checkTicket.setText("Utilizado");
                holder.checkTicket.setBackground(activity.getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_preto));
                break;
            case 5:
                holder.checkTicket.setText("Cancelado");
                holder.checkTicket.setBackground(activity.getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_vermelho));
                break;
            case 6:
                holder.checkTicket.setText("Rejeitado");
                holder.checkTicket.setBackground(activity.getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_vermelho));
                break;
        }

        holder.checkTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (meus_ingressos_model.getStatus()){
                    case 1:
                        if(APIServer.conexao(activity) == true){
                            Service_Meus_Ingressos.checar_ingresso(activity, meus_ingressos_model.getMy_pass_id());
                            Intent intent = new Intent(activity, View_Ingresso.class);
                            intent.putExtra("my_pass_id", meus_ingressos_model.getMy_pass_id());
                            intent.putExtra("cpf", meus_ingressos_model.getCpf());
                            intent.putExtra("status", meus_ingressos_model.getStatus());
                            intent.putExtra("thumb", meus_ingressos_model.getEvent_thumb());
                            intent.putExtra("name", meus_ingressos_model.getPass_name());
                            intent.putExtra("description", meus_ingressos_model.getPass_description());
                            intent.putExtra("payment_type", meus_ingressos_model.getPayment_type());
                            intent.putExtra("price", meus_ingressos_model.getPrice());
                            intent.putExtra("ends", meus_ingressos_model.getEnds());
                            activity.startActivityForResult(intent, 1000);
                        }else{
                            ToastClass.curto(activity, "Apareho offline\nVerifique sua conexão");
                        }
                    break;
                }
            }
        });
    };

    @Override
    public int getItemCount() {
        return lista_meus_ingressos != null ? lista_meus_ingressos.size() : 0;
    }

    public class IngressosNovosHolder extends RecyclerView.ViewHolder {

         ImageView imagemTicket;
         TextView tituloTicket;
         TextView dataTicket;
         TextView description_ticket;
         Button checkTicket;

        public IngressosNovosHolder(View itemView) {
            super(itemView);

            imagemTicket = itemView.findViewById(R.id.imagem_ticket);
            tituloTicket = itemView.findViewById(R.id.titulo_ticket);
            dataTicket = itemView.findViewById(R.id.data_ticket);
            description_ticket = itemView.findViewById(R.id.description_ticket);
            checkTicket = itemView.findViewById(R.id.check_ticket);
        };
    };
}
