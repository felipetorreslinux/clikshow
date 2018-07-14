package com.clikshow.ClikBIlheteria.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.clikshow.API.APIServer;
import com.clikshow.ClikBIlheteria.Services.Bilheteria_Service;
import com.clikshow.ClikBIlheteria.Services.Coordenador_Service;
import com.clikshow.ClikBIlheteria.Views.View_Revendedor;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Keyboard;
import com.clikshow.Utils.Progress_Alert;
import com.clikshow.Validation.CPF;
import com.clikshow.Views.View_Lista_Amigos_ClikSocial;

import org.json.JSONException;
import org.json.JSONObject;

public class Cortesia_Fragment extends Fragment implements View.OnClickListener {
    View rootView;

    LinearLayout search_profiles_cortesia;

    EditText cpf;
    Button button_cortesia_ingresso_coordenador;

    Coordenador_Service coordenador_service;

    int pass_id = 0;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cortesia_bilheteria, container, false);

        coordenador_service = new Coordenador_Service(getActivity());
        pass_id = getActivity().getIntent().getExtras().getInt("pass_id");

        search_profiles_cortesia = rootView.findViewById(R.id.search_profiles_cortesia);
        search_profiles_cortesia.setOnClickListener(this);

        cpf = (EditText) rootView.findViewById(R.id.edittext_cpf_cortesia_bilheteria_coordenador);
        cpf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() == 11){
                    Keyboard.close(getActivity(), getActivity().getWindow().getDecorView());
                    String number_cpf = cpf.getText().toString().trim();
                    if(APIServer.conexao(getActivity()) == true){
                        coordenador_service.check_cpf_cortesia(getActivity(), number_cpf, pass_id);
                        cpf.setText(null);
                    }else{

                    };
                };
            }
        });

        return rootView;
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_profiles_cortesia:
                Intent intent = new Intent(getActivity(), View_Lista_Amigos_ClikSocial.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode == Activity.RESULT_OK){
                    int id = data.getExtras().getInt("user_id");
                    if(APIServer.conexao(getActivity()) == true){
                        Keyboard.close(getActivity(), getActivity().getWindow().getDecorView());
                        Progress_Alert.open(getActivity(), null, "Coletando informações\nAguarde...");
                        coordenador_service.info_profile_cliksocial(getActivity(), id, pass_id);
                    }else{

                    };
                }else{

                }
                break;
        }

    }
}
