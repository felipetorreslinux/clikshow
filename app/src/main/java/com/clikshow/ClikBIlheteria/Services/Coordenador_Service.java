package com.clikshow.ClikBIlheteria.Services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.design.widget.BottomSheetDialog;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.ClikBIlheteria.Fragments.Cortesia_Fragment;
import com.clikshow.ClikBIlheteria.Views.View_Coordenador;
import com.clikshow.FireBase.NotificationFireBase;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Keyboard;
import com.clikshow.Utils.Loading;
import com.clikshow.Utils.Progress_Alert;
import com.clikshow.Validation.CPF;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Coordenador_Service{

    Activity activity;
    static AlertDialog.Builder builder;
    static BottomSheetDialog bottomSheetDialog;
    static View view;

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    static ImageView imageview_image_profile_cortesia;
    static TextView textview_name_profile_cortesia;
    static TextView textview_cpf_profile_cortesia;

    static ImageView imageview_image_event_cortesia;
    static TextView textview_name_event_cortesia;
    static TextView textview_ingresso_event_cortesia;
    static TextView textview_validade_event_cortesia;

    static Button button_back_profile_cortesia;
    static Button button_ok_profile_cortesia;

    public Coordenador_Service(final Activity activity){
        this.activity = activity;

        sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        editor = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE).edit();
        builder = new AlertDialog.Builder(activity);
        bottomSheetDialog = new BottomSheetDialog(activity);
        view = activity.getLayoutInflater().inflate(R.layout.bottom_sweet_info_profile_cortesia, null);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);

        imageview_image_profile_cortesia = view.findViewById(R.id.imageview_image_profile_cortesia);
        textview_name_profile_cortesia = view.findViewById(R.id.textview_name_profile_cortesia);
        textview_cpf_profile_cortesia = view.findViewById(R.id.textview_cpf_profile_cortesia);

        imageview_image_event_cortesia = view.findViewById(R.id.imageview_image_event_cortesia);
        textview_name_event_cortesia = view.findViewById(R.id.textview_name_event_cortesia);
        textview_ingresso_event_cortesia = view.findViewById(R.id.textview_ingresso_event_cortesia);
        textview_validade_event_cortesia = view.findViewById(R.id.textview_validade_event_cortesia);

        button_back_profile_cortesia = view.findViewById(R.id.button_back_profile_cortesia);
        button_ok_profile_cortesia = view.findViewById(R.id.button_ok_profile_cortesia);
    }

    public static String cpf_inibe (String cpf){
        cpf = cpf.replace(".", "").replace("-", "");
        return "*********"+cpf.charAt(9)+""+cpf.charAt(10);
    }

    public void check_cpf_cortesia(final Activity activity, final String cpf, final int pass_id){
        Progress_Alert.open(activity, null, "Colentando informações\nAguarde...");
        AndroidNetworking.get(APIServer.CLIKSOCIALPROD+"api/check_cpf/"+cpf.trim())
            .addHeaders("Authorization", APIServer.token(activity))
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                    try{
                        int code = response.getInt("code");
                        String name = null;
                        int id = 0;
                        switch (code){
                            case 0:
                                name = response.getJSONObject("content").getJSONObject("cpf").getString("name");
                                modal_info(name, cpf, pass_id);
                                Progress_Alert.close();
                                break;
                            case 35:
                                id = response.getJSONObject("content").getJSONObject("user_info").getInt("id");
                                info_profile_cliksocial(activity, id, pass_id);
                                break;
                            case 9000:
                                Progress_Alert.close();
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("CPF inválido");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.create().show();
                                break;
                        }
                        Keyboard.close(activity, activity.getWindow().getDecorView());
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    APIServer.error_server(activity, anError.getErrorCode());
                    Progress_Alert.close();
                }
            });
    }

    public void modal_info (String name, final String cpf, final int pass_id){
        textview_name_profile_cortesia.setText(name);
        textview_cpf_profile_cortesia.setText("CPF: " + cpf_inibe(cpf));
        Picasso.get()
                .load(R.drawable.ic_profile)
                .resize(150, 150)
                .transform(new CropCircleTransformation())
                .into(imageview_image_profile_cortesia);
        Picasso.get()
                .load(View_Coordenador.EVENT_THUMB)
                .resize(150, 150)
                .transform(new CropCircleTransformation())
                .into(imageview_image_event_cortesia);
        textview_name_event_cortesia.setText(View_Coordenador.NAME_EVENT);
        textview_ingresso_event_cortesia.setText(View_Coordenador.TIPO_INGRESSO);
        textview_validade_event_cortesia.setText("Valida até "+Datas.data_bilheteria(View_Coordenador.VALIDATE_EVENT));
        bottomSheetDialog.show();
        button_back_profile_cortesia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        button_ok_profile_cortesia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cpr_number = CPF.MaskCpf(cpf);
                validar_cortesia(activity, pass_id, cpr_number);
            }
        });
    }

    public void info_profile_cliksocial(final Activity activity, final int id, final int pass_id){
        AndroidNetworking.get(APIServer.CLIKSOCIALPROD+"api/profile/"+id)
                .addHeaders("Authorization", APIServer.token(activity))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try{
                            int code = response.getInt("code");
                            switch (code){
                                case 0:
                                    JSONObject jsonObject = response.getJSONObject("content").getJSONObject("profile_info");
                                    modal_info_profile(jsonObject.getInt("profile_id"), pass_id, jsonObject);
                                    break;
                                default:
                            }
                        }catch (JSONException e){}catch (NullPointerException e){}
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void modal_info_profile (final int id, final int pass_id, JSONObject jsonObject){
        try{
            if(jsonObject.getString("cpf").equals("")){
                String name = jsonObject.getString("name");
                builder.setTitle(R.string.app_name);
                builder.setMessage(name+" não tem CPF cadastrado.\nRealize a validação da cortesia informando o CPF no campo acima");
                builder.setCancelable(true);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                Keyboard.close(activity, activity.getWindow().getDecorView());
            }else {
                Keyboard.close(activity, activity.getWindow().getDecorView());
                final String cpf_profile = jsonObject.getString("cpf");
                if (!jsonObject.getString("profile_pic_thumb").equals("")) {
                    Picasso.get()
                            .load(jsonObject.getString("profile_pic_thumb"))
                            .transform(new CropCircleTransformation())
                            .into(imageview_image_profile_cortesia);
                } else {
                    Picasso.get()
                            .load(R.drawable.ic_profile)
                            .transform(new CropCircleTransformation())
                            .into(imageview_image_profile_cortesia);
                };
                textview_name_profile_cortesia.setText(jsonObject.getString("name"));
                textview_cpf_profile_cortesia.setText("CPF: " + cpf_inibe(jsonObject.getString("cpf")));

                Picasso.get()
                        .load(View_Coordenador.EVENT_THUMB)
                        .resize(150, 150)
                        .transform(new CropCircleTransformation())
                        .into(imageview_image_event_cortesia);

                textview_name_event_cortesia.setText(View_Coordenador.NAME_EVENT);
                textview_ingresso_event_cortesia.setText(View_Coordenador.TIPO_INGRESSO);
                textview_validade_event_cortesia.setText("Valida até " + Datas.data_bilheteria(View_Coordenador.VALIDATE_EVENT));

                bottomSheetDialog.show();

                button_back_profile_cortesia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                button_ok_profile_cortesia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validar_cortesia(activity, pass_id, cpf_profile);
                    }
                });
            }
            Progress_Alert.close();
        }catch (JSONException e){}catch (NullPointerException e){}
    }

    public void validar_cortesia (final Activity activity, final int pass_id, final String cpf){
        AndroidNetworking.post(APIServer.URL+"api/buywithdealer")
                .addHeaders("Authorization", APIServer.token(activity))
                .addBodyParameter("pass_id", String.valueOf(pass_id))
                .addBodyParameter("cpf", cpf)
                .addBodyParameter("payment_type", String.valueOf(4))
                .addBodyParameter("force", String.valueOf(0))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" - "+cpf);
                        try{
                            int code = response.getInt("code");
                            switch (code){
                                case 0:
                                    builder.setMessage("Cortesia realizada com sucesso");
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.finish();
                                        }
                                    });
                                    builder.create().show();
                                    break;

                                case 70:
                                    builder.setMessage("Cortesia esgotada para este ingresso");
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.finish();
                                        }
                                    });
                                    builder.create().show();
                                    break;

                                case 76:
                                    builder.setMessage("Este usuário já possui esta cortesia");
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.finish();
                                        }
                                    });
                                    builder.create().show();
                                    break;

                                case 77:
                                    builder.setMessage("Este usuário já tem cortesia para este evento.\nDeseja ceder outra cortesia?");
                                    builder.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            cortesia_forcada(activity, pass_id, cpf);
                                        }
                                    });
                                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.finish();
                                        }
                                    });
                                    builder.create().show();
                                    break;
                            }
                            bottomSheetDialog.dismiss();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        APIServer.error_server(activity, anError.getErrorCode());
                    }
                });
    }

    public void cortesia_forcada (final Activity activity, final int pass_id, final String cpf){
        Progress_Alert.open(activity, null, "Autorizando cortesia...");
        AndroidNetworking.post(APIServer.URL+"api/buywithdealer")
        .addHeaders("Authorization", APIServer.token(activity))
        .addBodyParameter("pass_id", String.valueOf(pass_id))
        .addBodyParameter("cpf", cpf)
        .addBodyParameter("payment_type", String.valueOf(4))
        .addBodyParameter("force", String.valueOf(1))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            Progress_Alert.close();
                            builder.setMessage("Cortesia realizada com sucesso");
                            builder.setPositiveButton("Ok", null);
                            builder.setNegativeButton(null, null);
                            builder.create().show();
                            break;
                        default:
                            Progress_Alert.close();
                            builder.setMessage("Houver um erro na autorização.\nTente mais tarde ou procure a gerência.");
                            builder.setPositiveButton("Ok", null);
                            builder.setNegativeButton(null, null);
                            builder.create().show();
                    }
                }catch (JSONException e){}
            }

            @Override
            public void onError(ANError anError) {
                Progress_Alert.close();
                APIServer.error_server(activity, anError.getErrorCode());
            }
        });
    }


    public void cancelar_ingresso (final Activity activity, int pass_id, String cpf){
        AndroidNetworking.post(APIServer.URL+"api/cancelpass")
        .addHeaders("Authorization", APIServer.token(activity))
        .addBodyParameter("pass_id", String.valueOf(pass_id))
        .addBodyParameter("cpf", CPF.MaskCpf(cpf))
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int code = response.getInt("code");
                    switch (code){
                        case 0:
                            builder.setMessage("Ingresso cancelado com sucesso");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                }
                            });
                            builder.setNegativeButton(null, null);
                            builder.create().show();
                            break;
                        case 60:
                            builder.setMessage("Não existe ingresso para cancelar neste CPF");
                            builder.setPositiveButton("voltar", null);
                            builder.setNegativeButton(null, null);
                            builder.create().show();
                            break;
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
