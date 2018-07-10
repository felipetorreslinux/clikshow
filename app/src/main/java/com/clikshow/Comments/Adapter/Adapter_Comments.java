package com.clikshow.Comments.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Comments.Model.Comment_Model;
import com.clikshow.Comments.View_Comments;
import com.clikshow.FireBase.NotificationFireBase;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.clikshow.Service.Toast.ToastClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Adapter_Comments extends RecyclerView.Adapter<Adapter_Comments.CommentHolder> {

    Activity activity;
    List<Comment_Model> list_comments;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;

    public Adapter_Comments(Activity activity, List<Comment_Model> list_comments, RecyclerView recyclerView){
        this.activity = activity;
        this.list_comments = list_comments;
        this.recyclerView = recyclerView;
        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false);
        return new Adapter_Comments.CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentHolder holder, final int position) {
        final Comment_Model comment_model = list_comments.get(position);

        if(!comment_model.getThumb().equals("null")){
            Picasso.get()
                    .load(comment_model.getThumb())
                    .resize(100,100)
                    .transform(new CropCircleTransformation())
                    .into(holder.imageview_user_comment);
        }else{
            Picasso.get()
                    .load(comment_model.getThumb())
                    .resize(100,100)
                    .transform(new CropCircleTransformation())
                    .into(holder.imageview_user_comment);
        }

        holder.textview_username_comment.setText(comment_model.getUsername().toLowerCase());
        holder.textview_comment.setText(comment_model.getComment());
        holder.textview_create_at_comment.setText(Datas.getTimeAgo(Long.parseLong(comment_model.getCreate_at())));

        holder.imageview_like_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(comment_model.getIs_like().equals("false")){
                    comment_model.setIs_like("true");
                    holder.imageview_like_comments.setImageResource(R.drawable.ic_favorites_orange);
                    databaseReference = FirebaseDatabase.getInstance().getReference().getRoot()
                            .child("comments").child(comment_model.getEvent_id()).child(comment_model.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("is_like", "true");
                    databaseReference.updateChildren(map);
                    try{
                        JSONObject data = new JSONObject();
                        data.put("event_id", comment_model.getEvent_id());
                        data.put("user_id", comment_model.getUser_id());
                        data.put("type", "likes_comments");
                        NotificationFireBase.send_push_like_comments(
                                 comment_model.getComment(),
                                sharedPreferences.getString("username", null)
                                        .toLowerCase()+" curtiu seu comentário",
                                 data);
                    }catch (JSONException e){}catch (NullPointerException e){};
                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemViewType(position));
                }else{
                    comment_model.setIs_like("false");
                    holder.imageview_like_comments.setImageResource(R.drawable.ic_heart);
                    databaseReference = FirebaseDatabase.getInstance().getReference().getRoot()
                            .child("comments").child(comment_model.getEvent_id()).child(comment_model.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("is_like", "false");
                    databaseReference.updateChildren(map);
                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemViewType(position));
                }

            }
        });

        if(String.valueOf(sharedPreferences.getInt("id", 0)).equals(comment_model.getUser_id())){
            holder.imageview_like_comments.setVisibility(View.GONE);
            holder.imageview_response_comments.setVisibility(View.GONE);
        }else{
            holder.imageview_like_comments.setVisibility(View.VISIBLE);
            holder.imageview_response_comments.setVisibility(View.VISIBLE);
            if(comment_model.getIs_like().equals("false")){
                holder.imageview_like_comments.setImageResource(R.drawable.ic_heart);
            }else{
                holder.imageview_like_comments.setImageResource(R.drawable.ic_favorites_orange);
            }
        };

        holder.imageview_response_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.item_comments_list.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View view = v;
                if(String.valueOf(sharedPreferences.getInt("id", 0)).equals(comment_model.getUser_id())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(R.string.app_name);
                    builder.setCancelable(true);
                    builder.setMessage("Deseja excluir este seu comentário?");
                    builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeItemListComments(comment_model, position);
                            Snackbar.make(view, "Comentário removido com sucesso", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Não", null);
                    builder.create().show();
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_comments != null ? list_comments.size() : 0;
    }

    public void removeItemListComments(Comment_Model comment_model, int position){
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("comments")
                .child(comment_model.getEvent_id()).child(comment_model.getId());
        databaseReference.removeValue();
        list_comments.remove(comment_model);
        notifyItemRemoved(position);
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        LinearLayout item_comments_list;
        ImageView imageview_user_comment;
        TextView textview_username_comment;
        TextView textview_comment;
        TextView textview_create_at_comment;
        ImageView imageview_like_comments;
        ImageView imageview_response_comments;

        public CommentHolder(View itemView) {
            super(itemView);

            item_comments_list = itemView.findViewById(R.id.item_comments_list);
            imageview_user_comment = itemView.findViewById(R.id.imageview_user_comment);
            textview_username_comment = itemView.findViewById(R.id.textview_username_comment);
            textview_comment = itemView.findViewById(R.id.textview_comment);
            textview_create_at_comment = itemView.findViewById(R.id.textview_create_at_comment);
            imageview_like_comments = itemView.findViewById(R.id.imageview_like_comments);
            imageview_response_comments = itemView.findViewById(R.id.imageview_response_comments);
        }
    }

}
