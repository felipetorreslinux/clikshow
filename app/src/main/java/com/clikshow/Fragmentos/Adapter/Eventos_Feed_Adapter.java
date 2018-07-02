package com.clikshow.Fragmentos.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.Fragmentos.Models.Feed_Eventos;
import com.clikshow.Service.Service_Favoritos;
import com.clikshow.Views.View_Detalhe_Evento;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Eventos_Feed_Adapter extends RecyclerView.Adapter<Eventos_Feed_Adapter.AllEventsHolder> {

    List<Feed_Eventos> feedEvents;
    Activity activity;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    View view;


    public Eventos_Feed_Adapter(final Activity activity, List<Feed_Eventos> feedEvents) {
        this.activity = activity;
        this.feedEvents = feedEvents;
    }

    @NonNull
    @Override
    public AllEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feed_events, parent, false);
        return new AllEventsHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AllEventsHolder holder, int position) {
        final Transformation transformation = new RoundedCornersTransformation(6, 0);
        final Feed_Eventos feed_eventos = feedEvents.get(position);

        holder.eventTitulo.setText(feed_eventos.getName());
        holder.eventData.setText(Datas.data_hora_evento_feed(feed_eventos.getStarts()));

        if(!feed_eventos.getBanner().equals(null)){
            Picasso.get()
            .load(feed_eventos.getBanner())
            .transform(transformation)
            .into(holder.eventImage);
        }else{
            Picasso.get()
            .load(R.drawable.ic_profile)
            .transform(transformation)
            .into(holder.eventImage);
        };


        holder.eventImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                builder = new AlertDialog.Builder(activity);
                view = activity.getLayoutInflater().inflate(R.layout.dialog_double_image_feed_eventos, null);
                builder.setCancelable(true);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();

                final TextView name_evento_double_press_feed_eventos = (TextView) view.findViewById(R.id.name_evento_double_press_feed_eventos);
                name_evento_double_press_feed_eventos.setText(feed_eventos.getName());

                final TextView data_evento_double_press_feed_eventos = (TextView) view.findViewById(R.id.data_evento_double_press_feed_eventos);
                data_evento_double_press_feed_eventos.setText(Datas.data_hora_evento_feed(feed_eventos.getStarts()));
                final ImageView banner = (ImageView) view.findViewById(R.id.img_double_press_feed_eventos);

                Picasso.get()
                        .load(feed_eventos.getBanner())
                        .into(banner);

                banner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent open = new Intent(activity, View_Detalhe_Evento.class);
                        open.putExtra("id", feed_eventos.getId());
                        open.putExtra("name", feed_eventos.getName());
                        open.putExtra("description", feed_eventos.getDescription());
                        open.putExtra("type", feed_eventos.getType());
                        open.putExtra("starts", feed_eventos.getStarts());
                        open.putExtra("ends", feed_eventos.getEnds());
                        open.putExtra("banner", feed_eventos.getBanner());
                        open.putExtra("lat", feed_eventos.getLat());
                        open.putExtra("lng", feed_eventos.getLng());
                        open.putExtra("is_favorite", feed_eventos.isIs_favorite());
                        activity.startActivity(open);
                        dialog.dismiss();
                    }
                });

                final ImageView btn_favorite_double_press = (ImageView) view.findViewById(R.id.btn_favorite_double_press);

                if(feed_eventos.isIs_favorite() == true){
                    btn_favorite_double_press.setImageResource(R.drawable.ic_favorites_orange);
                }else{
                    btn_favorite_double_press.setImageResource(R.drawable.ic_heart);
                }

                btn_favorite_double_press.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(feed_eventos.isIs_favorite() == true){
                            btn_favorite_double_press.setImageResource(R.drawable.ic_heart);
                            Service_Favoritos.deletar(activity, String.valueOf(feed_eventos.getId()));
                            feed_eventos.setIs_favorite(false);
                        }else{
                            btn_favorite_double_press.setImageResource(R.drawable.ic_favorites_orange);
                            Service_Favoritos.cadastrar(activity, String.valueOf(feed_eventos.getId()));
                            feed_eventos.setIs_favorite(true);
                        }
                        return false;
                    }
                });

                final ImageView btn_share_double_press = (ImageView) view.findViewById(R.id.btn_share_double_press);
                btn_share_double_press.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareItem(feed_eventos.getBanner());
                        dialog.dismiss();
                    }
                });
                return false;
            }
        });

        holder.eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open = new Intent(activity, View_Detalhe_Evento.class);
                open.putExtra("id", feed_eventos.getId());
                open.putExtra("name", feed_eventos.getName());
                open.putExtra("type", feed_eventos.getType());
                open.putExtra("starts", feed_eventos.getStarts());
                open.putExtra("description", feed_eventos.getDescription());
                open.putExtra("ends", feed_eventos.getEnds());
                open.putExtra("banner", feed_eventos.getBanner());
                open.putExtra("lat", feed_eventos.getLat());
                open.putExtra("lng", feed_eventos.getLng());
                open.putExtra("is_favorite", feed_eventos.isIs_favorite());
                activity.startActivityForResult(open, 3000);
            }
        });
    };

    @Override
    public int getItemCount() {
        return feedEvents != null ? feedEvents.size() : 0;
    }

    public class AllEventsHolder extends  RecyclerView.ViewHolder{
        ImageView eventImage;
        TextView eventData;
        TextView eventTitulo;
        public AllEventsHolder(View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.imagem_evento);
            eventData = itemView.findViewById(R.id.data_evento);
            eventTitulo = itemView.findViewById(R.id.titulo_evento);
        }

    }

    public void shareItem(String url) {
        Picasso.get().load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                activity.startActivity(Intent.createChooser(i, "Share Image"));

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
