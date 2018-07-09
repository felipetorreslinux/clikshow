package com.clikshow.Comments.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikshow.Comments.Model.Comment_Model;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Adapter_Comments extends RecyclerView.Adapter<Adapter_Comments.CommentHolder> {

    Activity activity;
    List<Comment_Model> list_comments;

    public Adapter_Comments(Activity activity, List<Comment_Model> list_comments){
        this.activity = activity;
        this.list_comments = list_comments;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false);
        return new Adapter_Comments.CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentHolder holder, int position) {
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

        holder.textview_username_comment.setText(comment_model.getUsername());
        holder.textview_comment.setText(comment_model.getComment());
        holder.textview_create_at_comment.setText(comment_model.getCreate_at());

        holder.texview_curti_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment_model.getIs_like().equals("false")){
                    holder.texview_curti_comments.setText("Descurtir");
                    comment_model.setIs_like("true");
                }else{
                    holder.texview_curti_comments.setText("Curtir");
                    comment_model.setIs_like("false");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_comments != null ? list_comments.size() : 0;
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        ImageView imageview_user_comment;

        TextView textview_username_comment;
        TextView textview_comment;
        TextView textview_create_at_comment;
        TextView texview_curti_comments;

        public CommentHolder(View itemView) {
            super(itemView);

            imageview_user_comment = itemView.findViewById(R.id.imageview_user_comment);
            textview_username_comment = itemView.findViewById(R.id.textview_username_comment);
            textview_comment = itemView.findViewById(R.id.textview_comment);
            textview_create_at_comment = itemView.findViewById(R.id.textview_create_at_comment);
            texview_curti_comments = itemView.findViewById(R.id.texview_curti_comments);
        }
    }
}
