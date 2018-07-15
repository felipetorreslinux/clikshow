package com.clikshow.Bilheteria.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.clikshow.API.APIServer;
import com.clikshow.Bilheteria.Services.Coordenador_Service;
import com.clikshow.R;
import com.clikshow.Utils.Keyboard;
import com.clikshow.Utils.Progress_Alert;
import com.clikshow.Views.View_Lista_Amigos_ClikSocial;

public class Cortesia_Fragment extends Fragment implements View.OnClickListener {

    static int pass_id = 0;
    View rootView;
    LinearLayout search_profiles_cortesia;
    EditText cpf;
    Coordenador_Service coordenador_service;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cortesia_bilheteria, container, false);

        coordenador_service = new Coordenador_Service(getActivity());
        pass_id = getActivity().getIntent().getExtras().getInt("id");
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cpf.requestFocus();
                Keyboard.open(getActivity(), cpf);
            }
        }, 100);

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
