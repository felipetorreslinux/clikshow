package com.clikshow.Fragmentos.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Carrinho.Models.CarrinhoModel;
import com.clikshow.Fragmentos.Models.IngressosModel;
import com.clikshow.R;
import com.clikshow.SQLite.Banco;
import com.clikshow.Service.Datas;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Views.View_Detalhe_Evento;

import java.util.List;

import static com.clikshow.Views.View_Detalhe_Evento.valor_total_carrinho;


public class Ingressos_Detalhes_Adapter extends RecyclerView.Adapter<Ingressos_Detalhes_Adapter.ListTicketsViewHolder> {

    List<IngressosModel> lista_evento_ingressos;
    Activity activity;

    public Ingressos_Detalhes_Adapter(final Activity activity, List<IngressosModel> lista_evento_ingressos){
        this.lista_evento_ingressos = lista_evento_ingressos;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListTicketsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingressos_lista_detalhes, parent, false);
        return new ListTicketsViewHolder(itemLista);
    }

        @Override
        public void onBindViewHolder(@NonNull final ListTicketsViewHolder holder, final int position) {
            final IngressosModel ingressosModel = lista_evento_ingressos.get(position);

            holder.qtd_detalhe_ingresso.setText(String.valueOf(ingressosModel.getQtdIngressos()));
            holder.nome_evento_detalhe_ingresso.setText(ingressosModel.getTicket_name());
            holder.validade_evento_detalhe_ingresso.setText(Datas.data_extenso(ingressosModel.getEnds()));
            holder.preco_evento_detalhe_ingresso.setText("R$ " + APIServer.preco(ingressosModel.getPrice()));

            holder.btn_add_ingresso_detalhe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Banco banco = new Banco(activity);
                    int qtd = Integer.parseInt(holder.qtd_detalhe_ingresso.getText().toString());
                    if(qtd < 10){
                        qtd = qtd + 1;
                        holder.qtd_detalhe_ingresso.setBackgroundResource(R.color.colorAccent);
                        holder.qtd_detalhe_ingresso.setTextColor(activity.getResources().getColor(R.color.white));
                        holder.qtd_detalhe_ingresso.setText(String.valueOf(qtd));
                        ingressosModel.setQtdIngressos(qtd);
                        CarrinhoModel carrinhoModel = new CarrinhoModel(
                                ingressosModel.getId(),
                                ingressosModel.getEvent_id(),
                                ingressosModel.getEvent_name(),
                                ingressosModel.getDescription(),
                                ingressosModel.getEvent_thumb(),
                                ingressosModel.getQtdIngressos(),
                                ingressosModel.getPrice(),
                                ingressosModel.getQtdIngressos()*ingressosModel.getPrice());

                        banco.add(carrinhoModel);
                    }else if(qtd == 10){
                        ToastClass.curto(activity, "Limite máximo de ingressos atingido");
                        holder.qtd_detalhe_ingresso.setText(String.valueOf(10));
                        ingressosModel.setQtdIngressos(10);
                    }
                    if(Banco.total_carrinho() > 0){
                        View_Detalhe_Evento.toolbar_btn_carrinho.setVisibility(View.VISIBLE);
                    }else{
                        View_Detalhe_Evento.toolbar_btn_carrinho.setVisibility(View.GONE);
                    }
                    View_Detalhe_Evento.valor_total_carrinho.setText("R$ " + APIServer.preco(Banco.total_carrinho()));
                }
            });


            holder.btn_remove_ingresso_detalhe.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    Banco banco = new Banco(activity);
                    int q = Integer.parseInt(holder.qtd_detalhe_ingresso.getText().toString());
                    int sub = q - 1;
                    if(sub < 1){
                        holder.qtd_detalhe_ingresso.setBackgroundResource(R.color.gray);
                        holder.qtd_detalhe_ingresso.setTextColor(activity.getResources().getColor(R.color.black));
                        holder.qtd_detalhe_ingresso.setText("0");
                        ingressosModel.setQtdIngressos(0);
                        banco.removeQtd(ingressosModel.getId());
                    }else{
                        banco.removeQtd(ingressosModel.getId());
                        ingressosModel.setQtdIngressos(sub);
                        holder.qtd_detalhe_ingresso.setText(String.valueOf(sub));
                    }
                    if(Banco.total_carrinho() > 0){
                        View_Detalhe_Evento.toolbar_btn_carrinho.setVisibility(View.VISIBLE);
                    }else{
                        View_Detalhe_Evento.toolbar_btn_carrinho.setVisibility(View.GONE);
                    }
                    View_Detalhe_Evento.valor_total_carrinho.setText("R$ " + APIServer.preco(Banco.total_carrinho()));
                }
            });
        }

        @Override
        public int getItemCount() {

            return lista_evento_ingressos != null ? lista_evento_ingressos.size() : 0;
        }


        public class ListTicketsViewHolder extends RecyclerView.ViewHolder {

            TextView qtd_detalhe_ingresso;
            TextView nome_evento_detalhe_ingresso;
            TextView validade_evento_detalhe_ingresso;
            TextView preco_evento_detalhe_ingresso;
            ImageView btn_add_ingresso_detalhe;
            ImageView btn_remove_ingresso_detalhe;

            public ListTicketsViewHolder(View itemView) {
                super(itemView);
                qtd_detalhe_ingresso = itemView.findViewById(R.id.qtd_detalhe_ingresso);
                nome_evento_detalhe_ingresso = itemView.findViewById(R.id.nome_evento_detalhe_ingresso);
                validade_evento_detalhe_ingresso = itemView.findViewById(R.id.validade_evento_detalhe_ingresso);
                preco_evento_detalhe_ingresso = itemView.findViewById(R.id.preco_evento_detalhe_ingresso);
                btn_add_ingresso_detalhe = itemView.findViewById(R.id.btn_add_ingresso_detalhe);
                btn_remove_ingresso_detalhe = itemView.findViewById(R.id.btn_remove_ingresso_detalhe);
            }
        }
}
