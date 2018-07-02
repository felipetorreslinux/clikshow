package com.clikshow.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Models.Search_Model;
import com.clikshow.R;
import com.clikshow.Service.Service_Eventos;

import java.util.ArrayList;
import java.util.List;

public class View_Resultado_Search extends Activity implements View.OnClickListener {

    public static TextView qtd_info_resultado;
    ImageView back_resultados_search;

    List<ItemsResultdos> lista_items_buscar = new ArrayList<>();
    RecyclerView recycler_resultado_busca;
    LinearLayoutManager layoutManagerItensResultado;
    String[] resultado;

    public class ItemsResultdos {
        private String nome;
        public ItemsResultdos(String nome) {
            this.nome = nome;
        }
        public String getNome() {
            return nome;
        }
        public void setNome(String nome) {
            this.nome = nome;
        }
    };

    RecyclerView recycler_lista_search;
    List<Search_Model> list_search = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_resultado_search);

        resultado = getIntent().getExtras().getString("resultado").split(" ");

        qtd_info_resultado = (TextView) findViewById(R.id.qtd_info_resultado);

        recycler_resultado_busca = (RecyclerView) findViewById(R.id.recycler_resultado_busca);
        layoutManagerItensResultado = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_resultado_busca.setLayoutManager(layoutManagerItensResultado);
        recycler_resultado_busca.setHasFixedSize(true);
        recycler_resultado_busca.setNestedScrollingEnabled(false);

        lista_items_buscar.clear();
        for(int i = 0; i < resultado.length; i++){
            ItemsResultdos itemsResultdos = new ItemsResultdos(resultado[i]);
            lista_items_buscar.add(itemsResultdos);
        };

        recycler_lista_search = (RecyclerView) findViewById(R.id.lista_searh);
        recycler_lista_search.setLayoutManager(new LinearLayoutManager(this));
        recycler_lista_search.setHasFixedSize(true);
        recycler_lista_search.setNestedScrollingEnabled(false);

        ItensResultadoAdapter itensResultadoAdapter = new ItensResultadoAdapter(this, lista_items_buscar);
        recycler_resultado_busca.setAdapter(itensResultadoAdapter);


        back_resultados_search = (ImageView) findViewById(R.id.back_resultados_search);
        back_resultados_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_resultados_search:
                setResult(Activity.RESULT_OK, getIntent());
                finish();
            break;
        }
    }

    @Override
    public void onBackPressed(){
        setResult(Activity.RESULT_CANCELED, getIntent());
        finish();
    }

    public void itensBuscar(String buscar, TextView qtd_info_resultado){
        list_search.clear();
        Service_Eventos.listar_search(this, buscar, list_search, recycler_lista_search);
    }


    private class ItensResultadoAdapter extends RecyclerView.Adapter<ItensResultadoAdapter.ItensHolder>{

        List<ItemsResultdos> lista_items_buscar;

        public ItensResultadoAdapter(final Activity activity, List<ItemsResultdos> lista_items_buscar){
            this.lista_items_buscar = lista_items_buscar;
        }

        @NonNull
        @Override
        public ItensHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_lista_resultado_search, parent, false);
            return new ItensResultadoAdapter.ItensHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItensHolder holder, final int position) {
            final ItemsResultdos itemsResultdos = lista_items_buscar.get(position);

            itensBuscar(itemsResultdos.getNome(), qtd_info_resultado);

            holder.name_resultado_lista_search.setText(itemsResultdos.getNome());
            holder.remove_name_resultado_lista_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItemLista(position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return lista_items_buscar != null ? lista_items_buscar.size() : 0;
        }

        private void removeItemLista(int position){
            lista_items_buscar.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, lista_items_buscar.size());


            if(lista_items_buscar.size() < 1){
                setResult(Activity.RESULT_OK, getIntent());
                finish();
            }
        }


        public class ItensHolder extends RecyclerView.ViewHolder{

            LinearLayout remove_name_resultado_lista_search;
            TextView name_resultado_lista_search;

            public ItensHolder(View itemView) {
                super(itemView);

                remove_name_resultado_lista_search = (LinearLayout) itemView.findViewById(R.id.remove_name_resultado_lista_search);
                name_resultado_lista_search = (TextView) itemView.findViewById(R.id.name_resultado_lista_search);
            }

        }

    }
}
