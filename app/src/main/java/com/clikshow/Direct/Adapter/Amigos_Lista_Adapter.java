package com.clikshow.Direct.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.StyleableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clikshow.Direct.Models.Amigos_Model;
import com.clikshow.Direct.View_Conversa_Direct;
import com.clikshow.FireBase.FireApp;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Amigos_Lista_Adapter extends RecyclerView.Adapter<Amigos_Lista_Adapter.AmigosHolder> {

    Activity activity;
    List<Amigos_Model> lista_amigos;
    SharedPreferences sharedPreferences;

    public Amigos_Lista_Adapter(final Activity activity, final List<Amigos_Model> lista_amigos){
        this.activity = activity;
        this.lista_amigos = lista_amigos;
        this.sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public AmigosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_amigos_direct, parent, false);
        return new Amigos_Lista_Adapter.AmigosHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final AmigosHolder holder, int position) {
        final Amigos_Model amigos_model = lista_amigos.get(position);

        if(amigos_model.getThumb().isEmpty() || amigos_model.getThumb().equals("null")){
            Picasso.get()
                    .load(R.drawable.ic_profile)
                    .resize(100, 100)
                    .transform(new CropCircleTransformation())
                    .into(holder.imageview_amigos_lista_direct);
        }else{
            Picasso.get()
                    .load(amigos_model.getThumb())
                    .resize(100, 100)
                    .transform(new CropCircleTransformation())
                    .into(holder.imageview_amigos_lista_direct);
        }

        holder.name_amigos_lista_direct.setText(amigos_model.getName());

        Firebase firebase = FireApp.getFirebase().child("direct").child("users").child(String.valueOf(amigos_model.getId()));
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("isOnline").getValue() != null){
                    if(dataSnapshot.child("isOnline").getValue().toString().equals("true")){
                        holder.username_amigos_lista_direct.setText("Online");
                        holder.username_amigos_lista_direct.setTextColor(activity.getResources().getColor(R.color.green_cliksocial));
                    }else{
                        holder.username_amigos_lista_direct.setText("Offline");
                        holder.username_amigos_lista_direct.setTextColor(activity.getResources().getColor(R.color.red_of_problem));

                    }
                }else{
                    holder.username_amigos_lista_direct.setText(amigos_model.getUsername());
                    holder.username_amigos_lista_direct.setTextColor(activity.getResources().getColor(R.color.chumbo));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        holder.item_lista_amigos_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, View_Conversa_Direct.class);
                intent.putExtra("id_amigo", amigos_model.getId());
                intent.putExtra("image_amigo", amigos_model.getThumb());
                intent.putExtra("name_amigo", amigos_model.getName());
                intent.putExtra("username_amigo", amigos_model.getUsername());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista_amigos != null ? lista_amigos.size() : 0;
    }

    public class AmigosHolder extends RecyclerView.ViewHolder{

        RelativeLayout item_lista_amigos_direct;

        ImageView imageview_amigos_lista_direct;
        TextView name_amigos_lista_direct;
        TextView username_amigos_lista_direct;

        public AmigosHolder(View itemView) {
            super(itemView);

            item_lista_amigos_direct = itemView.findViewById(R.id.item_lista_amigos_direct);
            imageview_amigos_lista_direct = itemView.findViewById(R.id.imageview_amigos_lista_direct);
            name_amigos_lista_direct = itemView.findViewById(R.id.name_amigos_lista_direct);
            username_amigos_lista_direct = itemView.findViewById(R.id.username_amigos_lista_direct);
        }
    }
}
