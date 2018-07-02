package com.clikshow.Fragmentos.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clikshow.Fragmentos.Models.Ajuda_Model;
import com.clikshow.R;

import java.util.List;

public class Ajuda_Adapter extends RecyclerView.Adapter<Ajuda_Adapter.AjudaHolder> {

    private List<Ajuda_Model> lista_ajuda;
    private Activity activity;

    public Ajuda_Adapter(final Activity activity, List<Ajuda_Model> lista_ajuda){
        this.activity = activity;
        this.lista_ajuda = lista_ajuda;
    }

    @NonNull
    @Override
    public Ajuda_Adapter.AjudaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_ajuda, parent, false);
        return new Ajuda_Adapter.AjudaHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final Ajuda_Adapter.AjudaHolder holder, int position) {
        final Ajuda_Model ajuda_model = lista_ajuda.get(position);



        holder.titulo_ajuda.setText(ajuda_model.getTitulo());
        holder.texto_ajuda.setText(ajuda_model.getTexto());
        holder.texto_ajuda.setVisibility(View.GONE);

        holder.titulo_ajuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ajuda_model.getOpen_tab() == 0){
                    ajuda_model.setOpen_tab(1);
                    holder.texto_ajuda.setVisibility(View.VISIBLE);
                }else{
                    ajuda_model.setOpen_tab(0);
                    holder.texto_ajuda.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista_ajuda != null ? lista_ajuda.size() : 0;
    }

    public class AjudaHolder extends RecyclerView.ViewHolder{

        TextView titulo_ajuda;
        TextView texto_ajuda;

        public AjudaHolder(View itemView) {
            super(itemView);
            titulo_ajuda= itemView.findViewById(R.id.titulo_ajuda);
            texto_ajuda = itemView.findViewById(R.id.texto_ajuda);
        }
    }
}
