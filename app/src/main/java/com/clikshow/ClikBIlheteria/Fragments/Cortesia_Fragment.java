package com.clikshow.ClikBIlheteria.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.IconMarginSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.ClikBIlheteria.Services.Bilheteria_Service;
import com.clikshow.ClikBIlheteria.Views.View_Revendedor;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Validation.CPF;

import org.json.JSONException;
import org.json.JSONObject;

public class Cortesia_Fragment extends Fragment implements View.OnClickListener {
    View rootView;

    EditText edittext_cpf_cortesia_bilheteria_coordenador;
    EditText edittext_name_cortesia_bilheteria_coordenador;
    EditText edittext_telefone_cortesia_bilheteria_coordenador;
    Button button_cortesia_ingresso_coordenador;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cortesia_bilheteria, container, false);

        edittext_cpf_cortesia_bilheteria_coordenador = (EditText) rootView.findViewById(R.id.edittext_cpf_cortesia_bilheteria_coordenador);
        edittext_name_cortesia_bilheteria_coordenador = (EditText) rootView.findViewById(R.id.edittext_name_cortesia_bilheteria_coordenador);
        edittext_name_cortesia_bilheteria_coordenador.setEnabled(false);
        edittext_telefone_cortesia_bilheteria_coordenador = (EditText) rootView.findViewById(R.id.edittext_telefone_cortesia_bilheteria_coordenador);
        edittext_cpf_cortesia_bilheteria_coordenador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() == 11){
                    if(APIServer.conexao(getActivity()) == true){
                        Bilheteria_Service.checar_cpf(getActivity(), edittext_cpf_cortesia_bilheteria_coordenador, edittext_name_cortesia_bilheteria_coordenador, edittext_telefone_cortesia_bilheteria_coordenador);
                    }else{

                    }
                }else{
                    edittext_name_cortesia_bilheteria_coordenador.setText(null);
                    edittext_name_cortesia_bilheteria_coordenador.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button_cortesia_ingresso_coordenador = (Button) rootView.findViewById(R.id.button_cortesia_ingresso_coordenador);
        button_cortesia_ingresso_coordenador.setOnClickListener(this);

        return rootView;
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_cortesia_ingresso_coordenador:
                if(edittext_cpf_cortesia_bilheteria_coordenador.getText().toString().isEmpty()){

                }else if(edittext_cpf_cortesia_bilheteria_coordenador.getText().length()< 11){

                }else{

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.app_name);
                    builder.setCancelable(false);

                    AndroidNetworking.post(APIServer.URL+"api/buywithdealer")
                            .addHeaders("Authorization", APIServer.token(getActivity()))
                            .addBodyParameter("pass_id", String.valueOf(getActivity().getIntent().getExtras().getInt("id")))
                            .addBodyParameter("cpf", CPF.MaskCpf(edittext_cpf_cortesia_bilheteria_coordenador.getText().toString()))
                            .addBodyParameter("payment_type", String.valueOf(4))
                            .addBodyParameter("force", String.valueOf(0))
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try{
                                        int code = response.getInt("code");
                                        System.out.println(response.toString());
                                        switch (code){
                                            case 0:
                                                builder.setMessage("Cortesia realizada com sucesso");
                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        getActivity().finish();
                                                    }
                                                });
                                                builder.create().show();
                                                break;

                                            case 70:
                                                builder.setMessage("Cortesia esgotada para este ingresso");
                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        getActivity().finish();
                                                    }
                                                });
                                                builder.create().show();
                                            break;

                                            case 76:
                                                builder.setMessage(edittext_name_cortesia_bilheteria_coordenador.getText().toString()+" já possui cortesia");
                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        getActivity().finish();
                                                    }
                                                });
                                                builder.create().show();
                                            break;

                                            case 77:

                                                builder.setMessage(edittext_name_cortesia_bilheteria_coordenador.getText().toString()+" ja tem cortesia para este evento. Deseja ceder uma cortesia?");
                                                builder.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Bilheteria_Service.force_checkin(getActivity(),
                                                                getActivity().getIntent().getExtras().getInt("id"),
                                                                edittext_cpf_cortesia_bilheteria_coordenador.getText().toString(),
                                                                4, 1);
                                                    }
                                                });
                                                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        getActivity().finish();
                                                    }
                                                });
                                                builder.create().show();

                                            break;

                                        }
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    APIServer.error_server(getActivity(), anError.getErrorCode());
                                }
                            });
                }
                break;
        }
    }
}
