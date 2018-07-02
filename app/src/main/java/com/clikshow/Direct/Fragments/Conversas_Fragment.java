package com.clikshow.Direct.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clikshow.R;

public class Conversas_Fragment extends Fragment {

    View rootView;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup viewGroup, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_conversas_direct, viewGroup, false);

        return rootView;
    }
}
