package com.clikshow.Carrinho.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Carrinho.Models.ListaCarrinhoModel;
import com.clikshow.R;
import com.clikshow.SQLite.Banco;

import org.w3c.dom.Text;

import java.util.List;

import static com.clikshow.Carrinho.View_Carrinho.itensCart;

public class Carrinho_Adapter extends RecyclerView.Adapter<Carrinho_Adapter.CarrinhoHolder> {

    private Activity activity;
    private List<ListaCarrinhoModel> lista_carrinho;
    private TextView total_carrinho_view;
    private LinearLayout bottom_bar_carrinho;
    Banco banco;

    public Carrinho_Adapter(final Activity activity, List<ListaCarrinhoModel> lista_carrinho, final TextView total_carrinho_view, final LinearLayout bottom_bar_carrinho){
        this.activity = activity;
        this.lista_carrinho = lista_carrinho;
        this.banco = new Banco(activity);
        this.total_carrinho_view = total_carrinho_view;
        this.bottom_bar_carrinho = bottom_bar_carrinho;
    }

    @NonNull
    @Override
    public Carrinho_Adapter.CarrinhoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrinho, parent, false);
        return new Carrinho_Adapter.CarrinhoHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final Carrinho_Adapter.CarrinhoHolder holder, final int position) {
        final ListaCarrinhoModel listaCarrinhoModel = lista_carrinho.get(position);
        holder.qtd_carrinho.setText(String.valueOf(listaCarrinhoModel.getQtd()));
        holder.nome_ingresso_carrinho.setText(listaCarrinhoModel.getEvent_name());
        holder.nome_evento_carrinho.setText(listaCarrinhoModel.getDescription());
        holder.preco_ingresso_carrinho.setText("R$ "+ APIServer.preco(listaCarrinhoModel.getPrice()));

        holder.btn_add_qtd_carrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int q = Integer.parseInt(String.valueOf(holder.qtd_carrinho.getText()));
            int soma = q+=1;
            if(soma <= 10){
                holder.qtd_carrinho.setText(String.valueOf(soma));
                banco.addQtd(activity, listaCarrinhoModel.getId());
            }else{
                soma = 10;
                holder.qtd_carrinho.setText(String.valueOf(soma));
            }
            total_carrinho_view.setText("R$ " +APIServer.preco(Banco.total_carrinho()));
            banco.count_carrinho();
            itensCart();
            }
        });

        holder.btn_remove_qtd_carrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int q = Integer.parseInt(String.valueOf(holder.qtd_carrinho.getText()));
            int soma = q=q-1;
            if(soma < 1){
                soma=0;
                holder.qtd_carrinho.setText(String.valueOf(soma));
                removeLista(position);
                banco.removeQtd(listaCarrinhoModel.getId());
            }
            banco.removeQtd(listaCarrinhoModel.getId());
            holder.qtd_carrinho.setText(String.valueOf(soma));
            total_carrinho_view.setText("R$ " +APIServer.preco(Banco.total_carrinho()));
            banco.count_carrinho();

            itensCart();
            }
        });
    }

    public void removeLista(int position){
        lista_carrinho.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return lista_carrinho != null ? lista_carrinho.size() : 0;
    }

    public class CarrinhoHolder extends RecyclerView.ViewHolder{

        TextView qtd_carrinho;
        TextView nome_ingresso_carrinho;
        TextView nome_evento_carrinho;
        TextView preco_ingresso_carrinho;

        ImageView btn_add_qtd_carrinho;
        ImageView btn_remove_qtd_carrinho;

        public CarrinhoHolder(View itemView) {
            super(itemView);

            qtd_carrinho = itemView.findViewById(R.id.qtd_carrinho);
            nome_ingresso_carrinho = itemView.findViewById(R.id.nome_ingresso_carrinho);
            nome_evento_carrinho = itemView.findViewById(R.id.nome_evento_carrinho);
            preco_ingresso_carrinho = itemView.findViewById(R.id.preco_ingresso_carrinho);

            btn_add_qtd_carrinho = itemView.findViewById(R.id.btn_add_qtd_carrinho);
            btn_remove_qtd_carrinho = itemView.findViewById(R.id.btn_remove_qtd_carrinho);

        }

    }
}
