package com.clikshow.Bilheteria.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.Bilheteria.Impressora.Lista_Impressoras_BlueTooth;
import com.clikshow.Bilheteria.Services.Bilheteria_Service;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Keyboard;
import com.clikshow.Utils.Relogio;
import com.clikshow.Validation.CPF;

import org.json.JSONException;
import org.json.JSONObject;


public class Dinheiro_Fragment extends Fragment implements View.OnClickListener {

    View rootView;

    TextView name_pagar;
    TextView evento_pagar;
    TextView validade_pagar;
    TextView preco_pagar;

    EditText edittext_valor_recebido_pagar;
    EditText edittext_troco_pagar;
    Button button_pagar_dinheiro;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_dinheiro_bilheteria, container, false);

        name_pagar = (TextView) rootView.findViewById(R.id.name_pagar);
        evento_pagar = (TextView) rootView.findViewById(R.id.evento_pagar);
        validade_pagar = (TextView) rootView.findViewById(R.id.validade_pagar);
        preco_pagar = (TextView) rootView.findViewById(R.id.preco_pagar);

        name_pagar.setText(getActivity().getIntent().getExtras().getString("name"));
        evento_pagar.setText(getActivity().getIntent().getExtras().getString("event_name"));
        validade_pagar.setText("Válido até "+ Datas.data_bilheteria(getActivity().getIntent().getExtras().getInt("ends")));
        preco_pagar.setText("R$ "+ APIServer.preco(getActivity().getIntent().getExtras().getDouble("price")));

        edittext_valor_recebido_pagar = (EditText) rootView.findViewById(R.id.edittext_valor_recebido_pagar);
        edittext_troco_pagar = (EditText) rootView.findViewById(R.id.edittext_troco_pagar);
        edittext_troco_pagar.setText("R$ 0.00");

        edittext_valor_recebido_pagar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() > 0){
                    Bilheteria_Service.troco(getActivity(),
                            getActivity().getIntent().getExtras().getDouble("price"),
                            edittext_valor_recebido_pagar,
                            edittext_troco_pagar);
                }else{
                    edittext_troco_pagar.setText("R$ 0.00");
                };
            };
        });

        button_pagar_dinheiro = (Button) rootView.findViewById(R.id.button_pagar_dinheiro);
        button_pagar_dinheiro.setOnClickListener(this);

        return rootView;
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_pagar_dinheiro:

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());

                if(edittext_valor_recebido_pagar.getText().toString().isEmpty()){
                    builder.setTitle(R.string.app_name);
                    builder.setMessage("Informe o valor recebido");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.create().dismiss();
                            edittext_valor_recebido_pagar.requestFocus();
                            Keyboard.open(getActivity(), edittext_valor_recebido_pagar);
                        }
                    });
                    builder.create().show();
                }else{
                    v = getActivity().getLayoutInflater().inflate(R.layout.dialog_checkin_revenda, null);
                    builder.setView(v);
                    builder.setCancelable(false);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();

                    Animation animation = new ScaleAnimation((float) 0.5,(float) 1.0,(float) 0.5,(float) 1.0);
                    animation.setDuration(200);
                    animation.setFillAfter(false);
                    animation.start();

                    final LinearLayout box_checkin_revenda = v.findViewById(R.id.box_checkin_revenda);
                    box_checkin_revenda.setAnimation(animation);

                    final TextView textview_tipo_checkin = (TextView) v.findViewById(R.id.textview_tipo_checkin);
                    textview_tipo_checkin.setText("Pagar e realizar check-in");

                    final EditText edittext_cpf_checkin_pagar = v.findViewById(R.id.edittext_cpf_checkin_pagar);
                    final EditText edittext_name_checkin_pagar = v.findViewById(R.id.edittext_name_checkin_pagar);
                    edittext_name_checkin_pagar.setVisibility(View.GONE);
                    edittext_name_checkin_pagar.setEnabled(false);
                    final EditText edittext_telefone_checkin_pagar = v.findViewById(R.id.edittext_telefone_checkin_pagar);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Keyboard.open(getActivity(), edittext_cpf_checkin_pagar);
                        }
                    }, 100);

                    final View finalV = v;
                    edittext_cpf_checkin_pagar.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override
                        public void afterTextChanged(Editable s) {}
                        @Override
                        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                           if(charSequence.length() == 11){
                               if(APIServer.conexao(getActivity()) == true){
                                   Bilheteria_Service.checar_cpf(getActivity(), edittext_cpf_checkin_pagar, edittext_name_checkin_pagar, edittext_telefone_checkin_pagar);
                                   new Handler().postDelayed(new Runnable() {
                                       @Override
                                       public void run() {
                                           Keyboard.close(getActivity(), finalV);
                                       }
                                   }, 100);
                               }
                           }else{
                               edittext_name_checkin_pagar.setVisibility(View.GONE);
                               edittext_name_checkin_pagar.setText(null);
                           }
                        };
                    });

                    final Button button_voltar = v.findViewById(R.id.button_voltar);
                    button_voltar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            edittext_valor_recebido_pagar.setText(null);
                        }
                    });

                    final Button button_pagar = v.findViewById(R.id.button_pagar);
                    button_pagar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(edittext_cpf_checkin_pagar.getText().toString().isEmpty()){
                                ToastClass.curto(getActivity(), "Informe o CPF");
                                edittext_cpf_checkin_pagar.requestFocus();
                                Keyboard.open(getActivity(), edittext_cpf_checkin_pagar);
                            }else if(edittext_cpf_checkin_pagar.getText().length() < 11){
                                ToastClass.curto(getActivity(), "CPF inválido ou não existe");
                                edittext_cpf_checkin_pagar.setText(null);
                                edittext_cpf_checkin_pagar.requestFocus();
                                Keyboard.open(getActivity(), edittext_cpf_checkin_pagar);
                            }else if(edittext_telefone_checkin_pagar.getText().toString().isEmpty()){
                                ToastClass.curto(getActivity(), "Informe o telefone");
                                edittext_telefone_checkin_pagar.requestFocus();
                                Keyboard.open(getActivity(), edittext_telefone_checkin_pagar);
                            }else if(edittext_telefone_checkin_pagar.getText().length() < 11){
                                ToastClass.curto(getActivity(), "Telefone inválido ou nào existe");
                                edittext_telefone_checkin_pagar.setText(null);
                                edittext_telefone_checkin_pagar.requestFocus();
                                Keyboard.open(getActivity(), edittext_telefone_checkin_pagar);
                            }else{
                                Keyboard.close(getActivity(), v);
                                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                builder1.setTitle("Confirmar pagamento");
                                builder1.setMessage("Você confirma a venda deste ingresso?\n\n" +
                                        "Ingresso\n"+
                                        ""+name_pagar.getText().toString()+"\n" +
                                        "R$ "+preco_pagar.getText().toString()+"\n\n" +
                                        "Cliente\n"+edittext_name_checkin_pagar.getText().toString()+"\n\n" +
                                        "Data e Hora\n"+ Relogio.hora());

                                builder1.setCancelable(false);

                                builder1.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                        bluetoothAdapter.enable();

                                        AndroidNetworking.post(APIServer.URL+"api/buywithdealer")
                                            .addHeaders("Authorization", APIServer.token(getActivity()))
                                            .addBodyParameter("pass_id", String.valueOf(getActivity().getIntent().getExtras().getInt("id")))
                                            .addBodyParameter("cpf", CPF.MaskCpf(edittext_cpf_checkin_pagar.getText().toString()))
                                            .addBodyParameter("payment_type", "1")
                                            .addBodyParameter("force", "0")
                                            .build()
                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                @Override
                                                public void onResponse(final JSONObject response) {
                                                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                                                    try{
                                                        int code = response.getInt("code");
                                                        switch (code){
                                                            case 0:

                                                                alertDialog.dismiss();
                                                                progressDialog.dismiss();

                                                                final AlertDialog.Builder alert_buy_success = new AlertDialog.Builder(getActivity());
                                                                alert_buy_success.setTitle(R.string.app_name);
                                                                alert_buy_success.setMessage("Venda realizada com sucesso");
                                                                alert_buy_success.setCancelable(false);

                                                                alert_buy_success.setPositiveButton("Imprimir", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        try {
                                                                            Intent intent = new Intent(getActivity(), Lista_Impressoras_BlueTooth.class);
                                                                            intent.putExtra("tipo_ingresso", 1);
                                                                            intent.putExtra("nome_evento", evento_pagar.getText().toString());
                                                                            intent.putExtra("data_evento", getActivity().getIntent().getExtras().getInt("starts"));
                                                                            intent.putExtra("id_evento", getActivity().getIntent().getExtras().getInt("id"));
                                                                            intent.putExtra("nome_cliente", edittext_name_checkin_pagar.getText().toString());
                                                                            intent.putExtra("cpf_cliente", edittext_cpf_checkin_pagar.getText().toString());
                                                                            intent.putExtra("telefone_cliente", edittext_telefone_checkin_pagar.getText().toString());
                                                                            intent.putExtra("nome_ingresso", name_pagar.getText().toString());
                                                                            intent.putExtra("preco_ingresso", getActivity().getIntent().getExtras().getDouble("price"));
                                                                            intent.putExtra("valor_recebido", edittext_valor_recebido_pagar.getText().toString());
                                                                            intent.putExtra("troco_cliente", edittext_troco_pagar.getText().toString());
                                                                            intent.putExtra("qrcode_cliente", response.getJSONObject("content").toString());
                                                                            getActivity().startActivityForResult(intent, 9001);
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                    }
                                                                });
                                                                alert_buy_success.setNegativeButton("Nova venda", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent());
                                                                        getActivity().finish();
                                                                    }
                                                                });
                                                                alert_buy_success.create().show();
                                                            break;

                                                            case 70:

                                                                builder2.setTitle(R.string.app_name);
                                                                builder2.setMessage(evento_pagar.getText().toString()+" está esgotado.");
                                                                builder2.setCancelable(false);
                                                                builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        progressDialog.dismiss();
                                                                        getActivity().onBackPressed();
                                                                        edittext_valor_recebido_pagar.setText(null);
                                                                    }
                                                                });
                                                                builder2.create().show();

                                                            break;

                                                            case 76:

                                                                builder2.setTitle(R.string.app_name);
                                                                builder2.setMessage(edittext_name_checkin_pagar.getText().toString()+" já possui este ingresso.");
                                                                builder2.setCancelable(false);
                                                                builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        progressDialog.dismiss();
                                                                        getActivity().onBackPressed();
                                                                        edittext_valor_recebido_pagar.setText(null);
                                                                    }
                                                                });
                                                                builder2.create().show();

                                                            break;

                                                            case 77:

                                                                builder2.setTitle(R.string.app_name);
                                                                builder2.setMessage(edittext_name_checkin_pagar.getText().toString()+" já tem um ingresso para esse evento.\nVocê gostaria de realizar o check-in mesmo assim?");
                                                                builder2.setCancelable(false);
                                                                builder2.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        alertDialog.dismiss();
                                                                        progressDialog.dismiss();

                                                                        AndroidNetworking.post(APIServer.URL+"api/buywithdealer")
                                                                                .addHeaders("Authorization", APIServer.token(getActivity()))
                                                                                .addBodyParameter("pass_id", String.valueOf(getActivity().getIntent().getExtras().getInt("id")))
                                                                                .addBodyParameter("cpf", CPF.MaskCpf(edittext_cpf_checkin_pagar.getText().toString()))
                                                                                .addBodyParameter("payment_type", String.valueOf(1))
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

                                                                                                    final AlertDialog.Builder alert_buy_success = new AlertDialog.Builder(getActivity());
                                                                                                    alert_buy_success.setTitle(R.string.app_name);
                                                                                                    alert_buy_success.setMessage("Venda realizada com sucesso");
                                                                                                    alert_buy_success.setCancelable(false);

                                                                                                    alert_buy_success.setPositiveButton("Imprimir", new DialogInterface.OnClickListener() {
                                                                                                        @Override
                                                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                                                            JSONObject jsonObject = new JSONObject();
                                                                                                            try {
                                                                                                                jsonObject.put("cpf", edittext_cpf_checkin_pagar.getText().toString());Intent intent = new Intent(getActivity(), Lista_Impressoras_BlueTooth.class);
                                                                                                                intent.putExtra("tipo_ingresso", 1);
                                                                                                                intent.putExtra("nome_evento", evento_pagar.getText().toString());
                                                                                                                intent.putExtra("data_evento", getActivity().getIntent().getExtras().getInt("starts"));
                                                                                                                intent.putExtra("id_evento", getActivity().getIntent().getExtras().getInt("id"));
                                                                                                                intent.putExtra("nome_cliente", edittext_name_checkin_pagar.getText().toString());
                                                                                                                intent.putExtra("cpf_cliente", edittext_cpf_checkin_pagar.getText().toString());
                                                                                                                intent.putExtra("telefone_cliente", edittext_telefone_checkin_pagar.getText().toString());
                                                                                                                intent.putExtra("nome_ingresso", name_pagar.getText().toString());
                                                                                                                intent.putExtra("preco_ingresso", getActivity().getIntent().getExtras().getDouble("price"));
                                                                                                                intent.putExtra("valor_recebido", edittext_valor_recebido_pagar.getText().toString());
                                                                                                                intent.putExtra("troco_cliente", edittext_troco_pagar.getText().toString());
                                                                                                                intent.putExtra("qrcode_cliente", jsonObject.toString());
                                                                                                                getActivity().startActivityForResult(intent, 9001);
                                                                                                            } catch (JSONException e) {
                                                                                                                e.printStackTrace();
                                                                                                            }

                                                                                                        }
                                                                                                    });
                                                                                                    alert_buy_success.setNegativeButton("Nova venda", new DialogInterface.OnClickListener() {
                                                                                                        @Override
                                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                                            getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent());
                                                                                                            getActivity().finish();
                                                                                                        }
                                                                                                    });
                                                                                                    alert_buy_success.create().show();
                                                                                                    break;
                                                                                            }
                                                                                        }catch (JSONException e){}
                                                                                    }

                                                                                    @Override
                                                                                    public void onError(ANError anError) {
                                                                                        APIServer.error_server(getActivity(), anError.getErrorCode());
                                                                                    }
                                                                                });
                                                                    };
                                                                });
                                                                builder2.setNegativeButton("Nova venda", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        progressDialog.dismiss();
                                                                        getActivity().onBackPressed();
                                                                        edittext_valor_recebido_pagar.setText(null);
                                                                    }
                                                                });
                                                                builder2.create().show();

                                                            break;
                                                        }
                                                    }catch (JSONException e){}
                                                }

                                                @Override
                                                public void onError(ANError anError) {
                                                    APIServer.error_server(getActivity(), anError.getErrorCode());
                                                }
                                            });
                                    }
                                });
                                builder1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                        builder.create().dismiss();
                                        edittext_valor_recebido_pagar.setText(null);
                                        getActivity().finish();
                                    }
                                });
                                builder1.create().show();
                            };
                        };
                    });

                }
                break;
        };
    };

}
