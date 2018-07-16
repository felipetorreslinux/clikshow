package com.clikshow.Direct.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.Direct.Models.Rooms_Model;
import com.clikshow.Direct.View_Chat_Direct;
import com.clikshow.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Adapter_Rooms_Direct extends RecyclerView.Adapter<Adapter_Rooms_Direct.RoomsHolder>{

    Activity activity;
    List<Rooms_Model> list_rooms;
    SharedPreferences sharedPreferences;

    public Adapter_Rooms_Direct(final Activity activity, final List<Rooms_Model> list_rooms){
        this.activity = activity;
        this.list_rooms = list_rooms;
        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Adapter_Rooms_Direct.RoomsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_rooms_direct, parent, false);
        return new Adapter_Rooms_Direct.RoomsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_Rooms_Direct.RoomsHolder holder, int position) {
        final Rooms_Model rooms_model = list_rooms.get(position);

        holder.item_lista_rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, View_Chat_Direct.class);
                intent.putExtra("id_amigo", rooms_model.getId());
                intent.putExtra("image_amigo", rooms_model.getThumb());
                intent.putExtra("name_amigo", rooms_model.getName());
                intent.putExtra("username_amigo", rooms_model.getUsername());
                activity.startActivity(intent);
            }
        });

        if(!rooms_model.getThumb().isEmpty()){
            Picasso.get()
                .load(rooms_model.getThumb())
                .resize(100,100)
                .transform(new CropCircleTransformation())
                .into(holder.imageview_profile_rooms);
        }else{
            Picasso.get()
                .load(R.drawable.ic_profile)
                .resize(100,100)
                .transform(new CropCircleTransformation())
                .into(holder.imageview_profile_rooms);
        }

        holder.textview_name_rooms.setText(rooms_model.getName());
        holder.textview_message_rooms.setText(rooms_model.getLast_message());

    }

    @Override
    public int getItemCount() {
        return list_rooms != null ? list_rooms.size() : 0;
    }

    public class RoomsHolder extends RecyclerView.ViewHolder{
        LinearLayout item_lista_rooms;
        ImageView imageview_profile_rooms;
        TextView textview_name_rooms;
        TextView textview_message_rooms;
        public RoomsHolder(View itemView) {
            super(itemView);
            item_lista_rooms = itemView.findViewById(R.id.item_lista_rooms);
            imageview_profile_rooms = itemView.findViewById(R.id.imageview_profile_rooms);
            textview_message_rooms = itemView.findViewById(R.id.textview_message_rooms);
            textview_name_rooms = itemView.findViewById(R.id.textview_name_rooms);
        }
    }
}
