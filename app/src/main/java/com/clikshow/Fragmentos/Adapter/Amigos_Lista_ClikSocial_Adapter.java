package com.clikshow.Fragmentos.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Models.AmigosClikSocialModel;
import com.clikshow.R;
import com.clikshow.Service.Service_ClikSocial;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Keyboard;
import com.clikshow.Views.View_Ingresso;
import com.clikshow.Views.View_Lista_Amigos_ClikSocial;
import com.santalu.aspectratioimageview.AspectRatioImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.security.Key;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Amigos_Lista_ClikSocial_Adapter extends RecyclerView.Adapter<Amigos_Lista_ClikSocial_Adapter.AmigosHolder> {

    private List<AmigosClikSocialModel> lista_amigos;
    private Activity activity;
    InputMethodManager keyboard;
    private SharedPreferences sharedPreferences;

    public Amigos_Lista_ClikSocial_Adapter(final Activity activity, List<AmigosClikSocialModel> lista_amigos){
        this.activity = activity;
        this.lista_amigos = lista_amigos;
        keyboard = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Amigos_Lista_ClikSocial_Adapter.AmigosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_amigos_cliksocial, parent, false);
        return new Amigos_Lista_ClikSocial_Adapter.AmigosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Amigos_Lista_ClikSocial_Adapter.AmigosHolder holder, int position) {
        final Transformation transformation = new RoundedCornersTransformation(60, 0);
        final AmigosClikSocialModel amigosClikSocialModel = lista_amigos.get(position);

        if(amigosClikSocialModel.getProfile_pic_thumb().equals("")){
            Picasso.get()
                .load(R.drawable.ic_profile)
                .resize(100,100)
                .transform(transformation)
                .into(holder.imagem_amigo_lista_cliksocial);
        }else{
            Picasso.get()
                .load(amigosClikSocialModel.getProfile_pic_thumb())
                .error(R.drawable.ic_profile)
                .placeholder(R.drawable.ic_profile)
                .resize(100,100)
                .transform(transformation)
                .into(holder.imagem_amigo_lista_cliksocial);
        }

        holder.name_amigo_lista_cliksocial.setText(amigosClikSocialModel.getName());
        holder.username_amigo_lista_cliksocial.setText(amigosClikSocialModel.getUsername().toLowerCase().replace(" ", ""));

        if(amigosClikSocialModel.isIs_following() == true){
            holder.btn_seguir_amigos_cliksocial.setVisibility(View.GONE);
        }else{
            holder.btn_seguir_amigos_cliksocial.setVisibility(View.VISIBLE);
        };

        holder.item_lista_amigos_cliksocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Keyboard.close(activity, activity.getWindow().getDecorView());
                Intent intent = new Intent();
                intent.putExtra("name", amigosClikSocialModel.getName());
                intent.putExtra("user_id", amigosClikSocialModel.getUser_id());
                activity.setResult(Activity.RESULT_OK, intent);
                activity.onBackPressed();
            }
        });

        holder.btn_seguir_amigos_cliksocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(APIServer.conexao(activity) == true){
                Service_ClikSocial.seguir_user(activity, amigosClikSocialModel.getUser_id(), holder.btn_seguir_amigos_cliksocial);
            }else{
                ToastClass.curto(activity, "Aparelho offline\nVerifique a conexão");
            };
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista_amigos != null ? lista_amigos.size() : 0;
    }

    public class AmigosHolder extends RecyclerView.ViewHolder{

        LinearLayout item_lista_amigos_cliksocial;
        ImageView imagem_amigo_lista_cliksocial;
        TextView name_amigo_lista_cliksocial;
        TextView username_amigo_lista_cliksocial;
        Button btn_seguir_amigos_cliksocial;

        public AmigosHolder(View itemView) {
            super(itemView);

            item_lista_amigos_cliksocial = itemView.findViewById(R.id.item_lista_amigos_cliksocial);
            imagem_amigo_lista_cliksocial = itemView.findViewById(R.id.imagem_amigo_lista_cliksocial);
            name_amigo_lista_cliksocial = itemView.findViewById(R.id.name_amigo_lista_cliksocial);
            username_amigo_lista_cliksocial = itemView.findViewById(R.id.username_amigo_lista_cliksocial);
            btn_seguir_amigos_cliksocial = itemView.findViewById(R.id.btn_seguir_amigos_cliksocial);
        }
    }
}
