package com.clikshow.Direct.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.Direct.Models.Conversa_Model;
import com.clikshow.R;
import com.clikshow.Service.Datas;

import java.util.List;

public class Conversa_Room_Adapter extends RecyclerView.Adapter<Conversa_Room_Adapter.ConversaHolder> {

    Activity activity;
    List<Conversa_Model> list_rooms_chat;
    SharedPreferences sharedPreferences;

    public Conversa_Room_Adapter(Activity activity, List<Conversa_Model> list_rooms_chat){
        this.activity = activity;
        this.list_rooms_chat = list_rooms_chat;
        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Conversa_Room_Adapter.ConversaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversa_room, parent, false);
        return new Conversa_Room_Adapter.ConversaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Conversa_Room_Adapter.ConversaHolder holder, int position) {
        Conversa_Model conversa_model = list_rooms_chat.get(position);

        if(Integer.parseInt(conversa_model.getSender()) == sharedPreferences.getInt("id", 0)){
            holder.box_receiver_chat.setVisibility(View.GONE);
            holder.box_sender_chat.setVisibility(View.VISIBLE);
            holder.text_sender_chat.setText(conversa_model.getMessage());
            holder.time_sender_chat.setText(Datas.getTimeAgo(Long.parseLong(conversa_model.getCreate_at())));
        }else{
            holder.box_receiver_chat.setVisibility(View.VISIBLE);
            holder.box_sender_chat.setVisibility(View.GONE);
            holder.text_receiver_chat.setText(conversa_model.getMessage());
            holder.time_receiver_chat.setText(Datas.getTimeAgo(Long.parseLong(conversa_model.getCreate_at())));
        }
    }

    @Override
    public int getItemCount() {
        return list_rooms_chat != null ? list_rooms_chat.size() : 0;
    }

    public class ConversaHolder extends  RecyclerView.ViewHolder{

        LinearLayout box_receiver_chat;
        LinearLayout box_sender_chat;
        TextView text_receiver_chat;
        TextView text_sender_chat;
        TextView time_receiver_chat;
        TextView time_sender_chat;

        public ConversaHolder(View itemView) {
            super(itemView);

            box_receiver_chat = itemView.findViewById(R.id.box_receiver_chat);
            box_sender_chat = itemView.findViewById(R.id.box_sender_chat);
            text_receiver_chat = itemView.findViewById(R.id.text_receiver_chat);
            text_sender_chat = itemView.findViewById(R.id.text_sender_chat);
            time_receiver_chat = itemView.findViewById(R.id.time_receiver_chat);
            time_sender_chat = itemView.findViewById(R.id.time_sender_chat);

        }
    }
}
