package com.clikshow.IUGU.Models.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clikshow.Carrinho.View_Carrinho;
import com.clikshow.IUGU.Models.CartaoCreditoModel;
import com.clikshow.IUGU.Models.Service.Cartao_Credito;
import com.clikshow.R;

import java.util.List;

public class ListaCartaoCreditoAdapter extends RecyclerView.Adapter<ListaCartaoCreditoAdapter.CartaoHolder> {

    private List<CartaoCreditoModel> lista_cartao;
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private String token;
    private RecyclerView recyclerView;
    private TextView text_not_cartao_profile;
    private View view;

    public ListaCartaoCreditoAdapter(final Activity activity, final View view, List<CartaoCreditoModel> lista_cartao, final RecyclerView recyclerView, final TextView text_not_cartao_profile){
        this.activity = activity;
        this.lista_cartao = lista_cartao;
        this.recyclerView = recyclerView;
        this.view = view;
        this.text_not_cartao_profile = text_not_cartao_profile;
        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
    }

    @NonNull
    @Override
    public CartaoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_cartao_credito, parent, false);
        return new ListaCartaoCreditoAdapter.CartaoHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartaoHolder holder, final int position) {
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


        holder.remove_lista_cartao_credito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("ClikShow");
                builder.setMessage("Deseja realmente remover este cartão de crédito?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItemList(position);
                        Cartao_Credito.remover(activity, view, cartaoCreditoModel.getId());

                    }
                });
                builder.setNegativeButton("Não", null);
                builder.create().show();
            }
        });

        holder.item_cartao_credito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                View_Carrinho.ID_CARTAO_CREDITO = cartaoCreditoModel.getId();
                intent.putExtra("id_cartao", cartaoCreditoModel.getId());
                intent.putExtra("badeira_cartao", cartaoCreditoModel.getBrand_cartao());
                intent.putExtra("numero_cartao", cartaoCreditoModel.getDisplay_number_cartao());
                intent.putExtra("nome_cartao", cartaoCreditoModel.getHolder_name_cartao());
                intent.putExtra("validade_mes_cartao", cartaoCreditoModel.getMonth_cartao());
                intent.putExtra("validade_ano_cartao", cartaoCreditoModel.getYear_cartao());
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
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
        if(lista_cartao.size() == 0){
            recyclerView.setVisibility(View.GONE);
            text_not_cartao_profile.setVisibility(View.VISIBLE);
        }else{
            text_not_cartao_profile.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        return lista_cartao != null ? lista_cartao.size() : 0;
    }

    public class CartaoHolder extends RecyclerView.ViewHolder {

        RelativeLayout item_cartao_credito;
        ImageView img_lista_cartao_credito;
        TextView number_lista_cartao_credito;
        ImageView remove_lista_cartao_credito;

        public CartaoHolder(View itemView) {
            super(itemView);

            item_cartao_credito = itemView.findViewById(R.id.item_cartao_credito);
            img_lista_cartao_credito = itemView.findViewById(R.id.img_lista_cartao_credito);
            number_lista_cartao_credito = itemView.findViewById(R.id.number_lista_cartao_credito);
            remove_lista_cartao_credito = itemView.findViewById(R.id.remove_lista_cartao_credito);
        }
    }

}
