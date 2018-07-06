package com.clikshow.Portaria.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clikshow.R;

public class Inicio_Portaria_Fragment extends Fragment implements View.OnClickListener{

    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_inicio_portaria, viewGroup, false);

        return rootView;
    }

    @Override
    public void onClick(View v) {

    }
}
