package com.clikshow.IUGU.Models.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clikshow.IUGU.Models.CartaoCreditoModel;
import com.clikshow.IUGU.Models.Service.Cartao_Credito;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;

import java.util.List;

import static com.clikshow.Carrinho.View_Carrinho.ID_CARTAO_CREDITO;

public class ListaCartaoCarrinhoAdapter extends RecyclerView.Adapter<ListaCartaoCarrinhoAdapter.CartaoCarrinhoHolder>{

    List<CartaoCreditoModel> lista_cartao;
    Activity activity;
    private RecyclerView mRecyclerView;


    public ListaCartaoCarrinhoAdapter(final Activity activity, List<CartaoCreditoModel> lista_cartao, final RecyclerView mRecyclerView){
        this.activity = activity;
        this.lista_cartao = lista_cartao;
        this.mRecyclerView = mRecyclerView;
    }

    @NonNull
    @Override
    public ListaCartaoCarrinhoAdapter.CartaoCarrinhoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_cartao_credito, parent, false);
        return new ListaCartaoCarrinhoAdapter.CartaoCarrinhoHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListaCartaoCarrinhoAdapter.CartaoCarrinhoHolder holder, final int position) {
        final CartaoCreditoModel cartaoCreditoModel = lista_cartao.get(position);

        switch (cartaoCreditoModel.getBrand_cartao()){
            case "VISA":
                holder.img_lista_cartao_credito.setImageResource(R.drawable.ic_visa_cartao);
                break;
            case "MasterCard":
                holder.img_lista_cartao_credito.setImageResource(R.drawable.ic_cartao_master);
                break;
            case "HiperCard":
                holder.img_lista_cartao_credito.setImageResource(R.drawable.ic_hiper_cartao);
                break;
            default:
                holder.img_lista_cartao_credito.setImageResource(R.drawable.ic_card);
        };

        String a = cartaoCreditoModel.getDisplay_number_cartao().replace("X", "*").replace("-", " ");
        holder.number_lista_cartao_credito.setText(a);

        holder.remove_lista_cartao_credito.setImageResource(R.drawable.ic_check_cartao);
        holder.remove_lista_cartao_credito.setVisibility(View.GONE);

        holder.item_cartao_credito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.remove_lista_cartao_credito.getVisibility() == View.GONE){
                    holder.remove_lista_cartao_credito.setVisibility(View.VISIBLE);
                    ID_CARTAO_CREDITO = cartaoCreditoModel.getId();
                }else{
                    holder.remove_lista_cartao_credito.setVisibility(View.GONE);
                    ID_CARTAO_CREDITO = 0;
                }
            }
        });
    }

    public void removeItemList(int position){
        lista_cartao.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, lista_cartao.size());
    }

    @Override
    public int getItemCount() {
        return lista_cartao != null ? lista_cartao.size() : 0;
    }


    public class CartaoCarrinhoHolder extends RecyclerView.ViewHolder{

        RelativeLayout item_cartao_credito;

        ImageView img_lista_cartao_credito;
        TextView number_lista_cartao_credito;
        ImageView remove_lista_cartao_credito;

        public CartaoCarrinhoHolder(View itemView) {
            super(itemView);

            item_cartao_credito = itemView.findViewById(R.id.item_cartao_credito);
            img_lista_cartao_credito = itemView.findViewById(R.id.img_lista_cartao_credito);
            number_lista_cartao_credito = itemView.findViewById(R.id.number_lista_cartao_credito);
            remove_lista_cartao_credito = itemView.findViewById(R.id.remove_lista_cartao_credito);
        }
    }
}
