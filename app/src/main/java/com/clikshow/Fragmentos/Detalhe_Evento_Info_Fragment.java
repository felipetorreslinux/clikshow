package com.clikshow.Fragmentos;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.clikshow.R;
import com.clikshow.Views.View_Detalhe_Evento;

import static com.clikshow.Views.View_Detalhe_Evento.Description_Evento;

public class Detalhe_Evento_Info_Fragment extends Fragment {

    WebView webview_info_evento;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detalhe_evento_info_ingressos, container, false);

        webview_info_evento = (WebView) rootView.findViewById(R.id.webview_info_evento);
        webview_info_evento.getSettings().setJavaScriptEnabled(true);
        webview_info_evento.setBackgroundColor(Color.TRANSPARENT);
        webview_info_evento.loadDataWithBaseURL("", Description_Evento, "text/html", "UTF-8", "");

        return  rootView;
    };
}
