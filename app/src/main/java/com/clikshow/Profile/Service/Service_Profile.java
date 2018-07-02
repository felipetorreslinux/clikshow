package com.clikshow.Profile.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.clikshow.API.APIServer;
import com.clikshow.Login.View_Recovery_Password;
import com.clikshow.Login.View_Login;
import com.clikshow.Profile.Adapter.ServicosProfileAdapter;
import com.clikshow.Profile.Models.ServicosProfileModel;
import com.clikshow.Profile.View_Editar_Usuario;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Keyboard;
import com.clikshow.Utils.Loading;
import com.clikshow.Validation.CPF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static com.clikshow.Profile.View_Cadastro_Novo_Usuario.BIRTHDAY_NOVA_CONTA;
import static com.clikshow.Profile.View_Cadastro_Novo_Usuario.GENRE_NOVA_CONTA;

public class Service_Profile {

    public static void check_cpf (final Activity activity, final EditText cpf, final EditText name, final EditText username, final EditText email, final LinearLayout box_dados_nova_conta){
        Loading.open(activity);
        AndroidNetworking.get(APIServer.CLIKSOCIALPROD+"api/check_cpf/"+cpf.getText().toString().trim())
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int code = response.getInt("code");
                        switch (code){
                            case 0:
                                Loading.close();
                                String name_cpf = response.getJSONObject("content").getJSONObject("cpf").getString("name");
                                name.setText(name_cpf);
                                name.setEnabled(false);
                                box_dados_nova_conta.setVisibility(View.VISIBLE);
                                username.requestFocus();
                                GENRE_NOVA_CONTA = response.getJSONObject("content").getJSONObject("cpf").getString("gender");
                                String[] birthday = response.getJSONObject("content").getJSONObject("cpf").getString("birthday").split(" ");
                                BIRTHDAY_NOVA_CONTA = birthday[0];
                                System.out.println(birthday[0]);
                                break;

                            case 35:
                                Loading.close();
                                cpf.setText(null);
                                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("Este CPF já esta sendo utilizado por outro usuário.");
                                builder.setCancelable(false);

                                builder.setNeutralButton("Tentar novamente", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        builder.create().dismiss();
                                        cpf.setText(null);
                                        cpf.requestFocus();
                                        Keyboard.open(activity, cpf);
                                    }
                                });

                                builder.setNegativeButton("Denunciar fraude", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        builder.create().dismiss();
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setType("plain/text");
                                        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"contato@supportclik.com"});
                                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Denunciar Fraude de CPF");
                                        activity.startActivity(Intent.createChooser(intent, "Enviar email por..."));
                                        activity.finishAffinity();
                                    }
                                });

                                builder.setPositiveButton("Recuperar senha", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        builder.create().dismiss();
                                        activity.startActivity(new Intent(activity, View_Recovery_Password.class));
                                        activity.finish();
                                    }
                                });

                                builder.create().show();

                                break;
                            default:
                                Loading.close();
                                cpf.setText(null);
                                cpf.setHint("CPF inválido ou não existe");
                                cpf.setHintTextColor(activity.getResources().getColor(R.color.red_of_problem));
                                cpf.requestFocus();
                                Keyboard.open(activity, cpf);
                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    Loading.close();
                    APIServer.error_server(activity, anError.getErrorCode());
                }
            });
    }

    public static void add (final Activity activity, EditText name, final EditText email, EditText username, EditText password, final EditText cpf){
        Loading.open(activity);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AndroidNetworking.post(APIServer.CLIKSOCIALPROD+"api/user")
            .addBodyParameter("name", name.getText().toString().trim())
            .addBodyParameter("email", email.getText().toString().trim())
            .addBodyParameter("username", username.getText().toString().trim())
            .addBodyParameter("password", password.getText().toString().trim())
            .addBodyParameter("blood_type", "")
            .addBodyParameter("birthday", BIRTHDAY_NOVA_CONTA)
            .addBodyParameter("genre", GENRE_NOVA_CONTA)
            .addBodyParameter("cpf", CPF.MaskCpf(cpf.getText().toString().trim()))
            .addBodyParameter("usertype", "0")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                    try{
                        int code = response.getInt("code");
                        switch (code){
                            case 0:
                                Loading.close();
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("Cadastro realizado com sucesso");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Fazer Login", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activity.finish();
                                        activity.startActivity(new Intent(activity, View_Login.class));
                                    }
                                });
                                builder.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activity.finish();
                                    }
                                });
                                builder.create().show();
                                break;
                            case 3:
                                Loading.close();
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("Este email já esta em uso");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Trocar email", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        builder.create().dismiss();
                                        email.setText(null);
                                        email.requestFocus();
                                        Keyboard.open(activity, email);
                                    }
                                });
                                builder.setNegativeButton("Denunciar fraude", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        builder.create().dismiss();
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setType("plain/text");
                                        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"contato@supportclik.com"});
                                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Denunciar Fraude de Email");
                                        activity.startActivity(Intent.createChooser(intent, "Enviar email por..."));
                                        activity.finish();
                                    }
                                });
                                builder.create().show();
                                break;
                            default:
                                Loading.close();
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("Não foi possível realizar o cadastro no momento");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activity.finish();
                                    }
                                });
                                builder.create().show();

                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    APIServer.error_server(activity, anError.getErrorCode());
                }
            });

    }

    public static void recovery_pass (final Activity activity, final EditText email){
        Loading.open(activity);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AndroidNetworking.post(APIServer.CLIKSOCIALPROD+"api/user/resetpassword")
            .addBodyParameter("email", email.getText().toString().trim())
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int code = response.getInt("code");
                        switch (code){
                            case 0:
                                Loading.close();
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("Enviamos para o email\n"+email.getText().toString().trim().toLowerCase()+"\num link de confirmação. Verifique sua Caixa de Entrada ou de Spam, abra o email que enviamos e confirme sua recuperação de senha");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        builder.create().dismiss();
                                        activity.finish();
                                    }
                                });
                                builder.create().show();
                                break;

                            case 22:
                                Loading.close();
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("Email inválido ou inexistente");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Tentar Novamente", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        builder.create().dismiss();
                                        email.setText(null);
                                        email.requestFocus();
                                        Keyboard.open(activity, email);
                                    }
                                });
                                builder.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        email.setText(null);
                                        email.setHint(null);
                                        activity.finish();
                                    }
                                });
                                builder.create().show();
                                break;

                            default:
                                Loading.close();
                                activity.finish();
                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    Loading.close();
                    APIServer.error_server(activity, anError.getErrorCode());
                }
            });
    }

    public static void editar_dados (final Activity activity, final String email, final SharedPreferences.Editor profile_editor) {

        final Uri image = View_Editar_Usuario.IMAGE_UPLOAD_EDIT_PROFILE;

        final SharedPreferences.Editor editor = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE).edit();

        if(image == null){
            AndroidNetworking.post(APIServer.URL+"api/edit_profile")
            .addHeaders("Authorization", APIServer.token(activity))
            .addBodyParameter("email", email)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response.toString());
                    try{
                        int code = response.getInt("code");
                        switch (code){
                            case 0:
                                JSONObject jsonObject = response.getJSONObject("content").getJSONObject("user_info");
                                editor.putString("email", jsonObject.getString("email"));
                                editor.commit();
                                if(editor.commit()){
                                    Intent intent = activity.getIntent();
                                    intent.putExtra("email", email);
                                    activity.setResult(Activity.RESULT_OK, intent);
                                    activity.finish();
                                };
                                break;
                            default:
                                Intent intent = activity.getIntent();
                                activity.setResult(Activity.RESULT_CANCELED, intent);
                                activity.finish();
                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    APIServer.error_server(activity, anError.getErrorCode());
                }
            });
        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            View view = activity.getLayoutInflater().inflate(R.layout.dialog_upload_foto, null);
            builder.setView(view);
            builder.setCancelable(false);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            final ProgressBar progressBarUploadFoto = view.findViewById(R.id.progressBarUploadFoto);
            progressBarUploadFoto.setScaleY((float) 5);

            AndroidNetworking.upload(APIServer.URL+"api/edit_profile")
                    .addHeaders("Authorization", APIServer.token(activity))
                    .addMultipartFile("profile_pic", new File(image.getPath()))
                    .addQueryParameter("email", email)
                    .setTag("UPLOAD_FILE_PHOTO_PROFILE")
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {
                            progressBarUploadFoto.setProgress((int) bytesUploaded);
                            progressBarUploadFoto.setMax((int) totalBytes);
                        };
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            try{
                                int code = response.getInt("code");
                                switch (code){
                                    case 0:
                                        JSONObject jsonObject = response.getJSONObject("content").getJSONObject("user_info");
                                        editor.putString("profile_pic", jsonObject.getString("profile_pic"));
                                        editor.putString("email", jsonObject.getString("email"));
                                        editor.commit();
                                        if(editor.commit()){
                                            Intent intent = activity.getIntent();
                                            activity.setResult(Activity.RESULT_OK, intent);
                                            activity.finish();
                                            alertDialog.dismiss();
                                        };
                                        break;
                                    default:
                                        Intent intent = activity.getIntent();
                                        activity.setResult(Activity.RESULT_CANCELED, intent);
                                        activity.finish();
                                        alertDialog.dismiss();
                                }
                            }catch (JSONException e){}
                        }

                        @Override
                        public void onError(ANError anError) {
                            APIServer.error_server(activity, anError.getErrorCode());
                        }
                    });
        };
    }

    public static void lista_jobs (final Activity activity, final List<ServicosProfileModel> lista_jobs, final RecyclerView recyclerView, final ViewStub loading_servicos_profile, final ViewStub view_not_service_profile) {
        loading_servicos_profile.inflate();
        final Animation animation = new TranslateAnimation(0,0,2000,0);
        AndroidNetworking.get(APIServer.URL+"api/userjobeventlist")
        .addHeaders("Authorization", APIServer.token(activity))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 45:
                            recyclerView.setVisibility(View.GONE);
                            loading_servicos_profile.setVisibility(View.GONE);
                            view_not_service_profile.inflate();
                            break;
                        case 0:
                            if(activity != null){
                                JSONArray jobevent = response.getJSONObject("content").getJSONArray("jobevent");
                                if(jobevent.length() > 0){
                                    for(int i = 0; i < jobevent.length(); i++){
                                        JSONObject jsonObject = jobevent.getJSONObject(i);
                                        ServicosProfileModel servicosProfileModel = new ServicosProfileModel(
                                                jsonObject.getInt("event_id"),
                                                jsonObject.getString("cpf"),
                                                jsonObject.getInt("role_id"),
                                                jsonObject.getString("role_name"),
                                                jsonObject.getJSONObject("event").getInt("id"),
                                                jsonObject.getJSONObject("event").getString("name"),
                                                jsonObject.getJSONObject("event").getString("type"),
                                                jsonObject.getJSONObject("event").getString("banner"),
                                                jsonObject.getJSONObject("event").getString("thumb"),
                                                jsonObject.getJSONObject("event").getString("city"),
                                                jsonObject.getJSONObject("event").getString("state"),
                                                jsonObject.getJSONObject("event").getString("lat"),
                                                jsonObject.getJSONObject("event").getString("lng"),
                                                jsonObject.getJSONObject("event").getString("classification"),
                                                jsonObject.getJSONObject("event").getString("description"),
                                                jsonObject.getJSONObject("event").getInt("starts"),
                                                jsonObject.getJSONObject("event").getInt("ends"),
                                                jsonObject.getJSONObject("event").getString("producer_id"),
                                                jsonObject.getJSONObject("event").getBoolean("is_private"),
                                                jsonObject.getJSONObject("event").getBoolean("is_favorite"));
                                        lista_jobs.add(servicosProfileModel);
                                    }
                                    recyclerView.setVisibility(View.VISIBLE);
                                    ServicosProfileAdapter servicosProfileAdapter = new ServicosProfileAdapter(activity, lista_jobs);
                                    recyclerView.setAdapter(servicosProfileAdapter);
                                    loading_servicos_profile.setVisibility(View.GONE);
                                    view_not_service_profile.setVisibility(View.GONE);
                                    animation.setDuration(350);
                                    animation.setFillAfter(true);
                                    animation.start();
                                    recyclerView.setAnimation(animation);
                                };
                            };
                            break;
                        default:
                            recyclerView.setVisibility(View.GONE);
                            loading_servicos_profile.setVisibility(View.GONE);
                            view_not_service_profile.inflate();
                    }
                }catch (JSONException e){}
            }

            @Override
            public void onError(ANError anError) {
                APIServer.error_server(activity, anError.getErrorCode());
            }
        });
    }
}
