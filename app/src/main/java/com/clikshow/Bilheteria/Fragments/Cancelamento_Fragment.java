package com.clikshow.Bilheteria.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.clikshow.API.APIServer;
import com.clikshow.Bilheteria.Services.Coordenador_Service;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Keyboard;

public class Cancelamento_Fragment extends Fragment implements View.OnClickListener {
    View rootView;

    EditText edittext_cpf_cancelamento_bilheteria_coordenador;
    Button button_cancelar_ingresso_coordenador;
    Coordenador_Service coordenador_service;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cancelamento_bilheteria, container, false);

        coordenador_service = new Coordenador_Service(getActivity());

        edittext_cpf_cancelamento_bilheteria_coordenador = (EditText) rootView.findViewById(R.id.edittext_cpf_cancelamento_bilheteria_coordenador);
        button_cancelar_ingresso_coordenador = (Button) rootView.findViewById(R.id.button_cancelar_ingresso_coordenador);
        button_cancelar_ingresso_coordenador.setOnClickListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                edittext_cpf_cancelamento_bilheteria_coordenador.requestFocus();
                Keyboard.open(getActivity(), edittext_cpf_cancelamento_bilheteria_coordenador);
            }
        }, 100);

        return rootView;
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_cancelar_ingresso_coordenador:
                if(edittext_cpf_cancelamento_bilheteria_coordenador.getText().toString().isEmpty()){
                    edittext_cpf_cancelamento_bilheteria_coordenador.setHint("Informe o CPF aqui...");
                    edittext_cpf_cancelamento_bilheteria_coordenador.requestFocus();
                }else{
                    if(APIServer.conexao(getActivity()) == true){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.app_name);
                        builder.setMessage("Deseja realmente cancelar este ingresso?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                coordenador_service.cancelar_ingresso(getActivity(),
                                        getActivity().getIntent().getExtras().getInt("id"),
                                        edittext_cpf_cancelamento_bilheteria_coordenador.getText().toString());
                            }
                        });
                        builder.setNegativeButton("Não", null);
                        builder.create().show();
                    }else{
                        ToastClass.curto(getActivity(), "Aparelho sem conexão com a internet. Tente mais tarde");
                        edittext_cpf_cancelamento_bilheteria_coordenador.setText(null);
                    }
                }
            break;
        }
    }
};
