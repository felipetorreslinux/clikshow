package com.clikshow.SQLite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.service.carrier.CarrierService;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.Carrinho.Adapter.Carrinho_Adapter;
import com.clikshow.Carrinho.Models.CarrinhoModel;
import com.clikshow.Carrinho.Models.ListaCarrinhoModel;
import com.clikshow.Carrinho.View_Carrinho;
import com.clikshow.Fragmentos.Meus_Ingressos_Fragment;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Views.View_Principal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.logging.Handler;

import static com.clikshow.Carrinho.View_Carrinho.total_carrinho_view;
import static com.clikshow.Fragmentos.Feed_Fragment.count_carrinho_feed;

public class Banco {

    public static SQLiteDatabase banco;
    BancoCore bancoCore;

    public Banco(Context context){
        bancoCore = new BancoCore(context);
        banco = bancoCore.getWritableDatabase();
    }

    public void add (CarrinhoModel carrinhoModel){

        ContentValues values = new ContentValues();
        values.put("id_ingresso", carrinhoModel.getId());
        values.put("event_id", carrinhoModel.getEvent_id());
        values.put("event_name", carrinhoModel.getEvent_name());
        values.put("description", carrinhoModel.getDescription());
        values.put("event_thumb", carrinhoModel.getEvent_thumb());
        values.put("qtd", carrinhoModel.getQtd());
        values.put("price", carrinhoModel.getPrice());
        values.put("total", carrinhoModel.getPrice()*carrinhoModel.getQtd());

        String id = String.valueOf(carrinhoModel.getId());

        Cursor cursor = banco.rawQuery("select * from carrinho where id_ingresso = ?", new String[]{id});
        if(cursor != null){
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                int qtd = cursor.getInt(cursor.getColumnIndex("qtd"));
                if(qtd < 10){
                    ContentValues val = new ContentValues();
                    int price = cursor.getInt(cursor.getColumnIndex("price"));
                    int soma = qtd + 1;
                    int total = soma * price;
                    val.put("qtd", soma);
                    val.put("total", total);
                    banco.update("carrinho", val, "id_ingresso = ?", new String[]{id});
                }
            }else{
                banco.insert("carrinho", null, values);
            }
        }
        cursor.close();
    }

    public void addQtd (final Activity activity, int id_ingresso){
        String id = String.valueOf(id_ingresso);
        Cursor cursor = banco.rawQuery("select * from carrinho where id_ingresso = ?", new String[]{id});
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            int qtd = cursor.getInt(cursor.getColumnIndex("qtd"));
            if(qtd < 10){
                int price = cursor.getInt(cursor.getColumnIndex("price"));
                int soma = qtd + 1;
                int total = soma * price;
                ContentValues val = new ContentValues();
                val.put("qtd", soma);
                val.put("total", total);
                banco.update("carrinho", val, "id_ingresso = ?", new String[]{id});
            }else{
                ToastClass.curto(activity, "Limite máximo de ingressos atingido");
            }
        }
        cursor.close();
    }

    public void removeQtd (int id_ingresso){
        String id = String.valueOf(id_ingresso);
        Cursor cursor = banco.rawQuery("select * from carrinho where id_ingresso = ?", new String[]{id});
        if(cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int qtd = cursor.getInt(cursor.getColumnIndex("qtd"));
                int price = cursor.getInt(cursor.getColumnIndex("price"));
                if (qtd >= 1) {
                    int subtracao = qtd - 1;
                    int total = subtracao * price;
                    ContentValues val = new ContentValues();
                    val.put("qtd", subtracao);
                    val.put("total", total);
                    banco.update("carrinho", val, "id_ingresso = ?", new String[]{id});
                } else {
                    banco.delete("carrinho", "id_ingresso = ?", new String[]{id});
                    banco.rawQuery("delete from carrinho where qtd = 0", null);
                }
            }
        }
        cursor.close();
    }

    public static void count_carrinho(){
        int count_cart = 0;
        Cursor cursor = banco.rawQuery("select * from carrinho where qtd != 0", null);
        if(cursor != null){
            count_cart = cursor.getCount();
            if(count_cart > 0){
                count_carrinho_feed.setVisibility(View.VISIBLE);
            }else {
                count_carrinho_feed.setVisibility(View.GONE);
            }
        }else{
            count_cart = 0;
        }
        count_carrinho_feed.setText(String.valueOf(count_cart));
    };

    public static double total_carrinho(){
        double total = 0;
        Cursor cursor = banco.rawQuery("select sum(total) as total from carrinho", null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    total += cursor.getInt(0);
                }while (cursor.moveToNext());
            }
        }else{
            total = 0;
        }
        cursor.close();
        return total;
    };


    public static void lista_carrinho (final Activity activity, List<ListaCarrinhoModel> lista_carrinho, RecyclerView recyclerView, TextView total_carrinho_view, LinearLayout bottom_bar_carrinho){
        Cursor cursor = banco.rawQuery("select * from carrinho where qtd != 0", null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    ListaCarrinhoModel listaCarrinhoModel = new ListaCarrinhoModel(
                            cursor.getInt(cursor.getColumnIndex("id_ingresso")),
                            cursor.getInt(cursor.getColumnIndex("event_id")),
                            cursor.getString(cursor.getColumnIndex("event_name")),
                            cursor.getString(cursor.getColumnIndex("description")),
                            cursor.getString(cursor.getColumnIndex("event_thumb")),
                            cursor.getInt(cursor.getColumnIndex("qtd")),
                            cursor.getInt(cursor.getColumnIndex("price")),
                            cursor.getInt(cursor.getColumnIndex("total")));
                    lista_carrinho.add(listaCarrinhoModel);

                }while (cursor.moveToNext());
                Carrinho_Adapter carrinho_adapter = new Carrinho_Adapter(activity, lista_carrinho, total_carrinho_view, bottom_bar_carrinho);
                recyclerView.setAdapter(carrinho_adapter);
            };
        }
        cursor.close();
    };


    public static void pagarCarrinho (final Activity activity, final int card, final AlertDialog final_alert_dialog) throws JSONException {

            final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
            final SharedPreferences sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
            final String name_user = sharedPreferences.getString("name", "Olá");

            final JSONObject carrinho = new JSONObject();
            final JSONArray linha = new JSONArray();

            carrinho.put("card_id", String.valueOf(card));
            carrinho.put("plots", String.valueOf(1));

            Cursor cursor = banco.rawQuery("select * from carrinho", null);
            if(cursor != null){
                cursor.moveToFirst();

                do{
                    JSONObject itens = new JSONObject();
                    itens.put("pass_id",  cursor.getInt(cursor.getColumnIndex("id_ingresso")));
                    itens.put("amount", cursor.getInt(cursor.getColumnIndex("qtd")));
                    linha.put(itens);
                }while (cursor.moveToNext());

            }
            cursor.close();
            carrinho.put("items", linha);

            AndroidNetworking.post(APIServer.URL+"api/buycart")
            .addHeaders("Authorization", APIServer.token(activity))
            .addHeaders("Content-Type", "application/json")
            .addJSONObjectBody(carrinho)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(final JSONObject response) {

                    try{

                        int code = response.getInt("code");

                        switch (code){

                            case 0:
                                View_Principal.codeBuy = 0;

                                final_alert_dialog.dismiss();

                                View view = activity.getLayoutInflater().inflate(R.layout.dialog_finaliza_carrinho_venda, null);
                                builder1.setView(view);
                                builder1.setCancelable(false);
                                final AlertDialog alertDialog = builder1.create();
                                alertDialog.show();

                                final LinearLayout box_final_carrinho = view.findViewById(R.id.box_final_carrinho);
                                box_final_carrinho.setBackgroundColor(activity.getResources().getColor(R.color.green_cliksocial));

                                final ImageView imagem_info_final_carrinho = view.findViewById(R.id.imagem_info_final_carrinho);
                                imagem_info_final_carrinho.setImageResource(R.drawable.ic_check_white);

                                final TextView texto_info_final_carrinho = view.findViewById(R.id.texto_info_final_carrinho);
                                texto_info_final_carrinho.setText("Compra realizada com sucesso");

                                final TextView info_pagamento_final_carrinho = view.findViewById(R.id.info_pagamento_final_carrinho);
                                info_pagamento_final_carrinho.setBackgroundColor(activity.getResources().getColor(R.color.red_of_problem));
                                info_pagamento_final_carrinho.setText("Não esqueça de realizar o check-in");

                                final TextView btn_close_final_carrinho = view.findViewById(R.id.btn_close_final_carrinho);

                                btn_close_final_carrinho.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        banco.delete("carrinho",null, null);
                                        alertDialog.dismiss();
                                        activity.finish();
                                    }
                                });

                            break;

                            case 70:

                                View_Principal.codeBuy = 70;

                                final int qtd = response.getJSONObject("content").getInt("remaining");
                                final int id = response.getJSONObject("content").getInt("pass_id");
                                final int price = response.getJSONObject("content").getInt("pass_price");
                                final String name = response.getJSONObject("content").getString("pass_name");

                                if(qtd > 0){

                                    builder1.setCancelable(false);
                                    builder1.setTitle(R.string.app_name);
                                    builder1.setMessage("O ingresso "+
                                            name+
                                            " só tem "+qtd+" disponível no momento.\n" +
                                            "Deseja recalcular seu carrinho e continuar com esta compra?");


                                    builder1.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("qtd", qtd);
                                            contentValues.put("total", qtd * price);
                                            banco.update("carrinho", contentValues, "id_ingresso = ?", new String[]{String.valueOf(id)});
                                            activity.finish();
                                        }
                                    });
                                    builder1.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.finish();
                                            banco.delete("carrinho", null, null);
                                        }
                                    });
                                    builder1.create().show();

                                }else{

                                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                                    builder1.setCancelable(false);
                                    builder1.setTitle(R.string.app_name);

                                    builder1.setMessage("O ingresso "+ name + " não está mais disponível para compra.");

                                    builder1.setPositiveButton("Remover do carrinho", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.finish();
                                            banco.delete("carrinho", "id_ingresso = ?", new String[]{String.valueOf(id)});
                                        }
                                    });
                                    builder1.create().show();
                                };

                            break;

                            case 75:

                                View_Principal.codeBuy = 75;

                                int qtd_remaing = response.getJSONObject("content").getInt("remaining_for_user");
                                String name_ingresso = response.getJSONObject("content").getString("pass_name");

                                builder1.setCancelable(false);
                                builder1.setTitle(R.string.app_name);

                                if(qtd_remaing > 1) {
                                    builder1.setMessage(name_user + " resta apenas " + qtd_remaing + " ingresso de " + name_ingresso + " para você");
                                }else if(qtd_remaing > 2){
                                    builder1.setMessage(name_user + " restam apenas " + qtd_remaing + " ingressos de " + name_ingresso + " para você" );
                                }else{
                                    builder1.setMessage(name_user + " não há mais " + name_ingresso + " disponível para você");
                                };

                                builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activity.finish();
                                        int id = 0;
                                        try {
                                            id = response.getJSONObject("content").getInt("pass_id");
                                            banco.delete("carrinho","id_ingresso = ?", new String[]{String.valueOf(id)});
                                            activity.finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                builder1.create().show();

                            break;
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(ANError anError) {
                    ToastClass.curto(activity, anError.toString());
                }
            });

    };
}
