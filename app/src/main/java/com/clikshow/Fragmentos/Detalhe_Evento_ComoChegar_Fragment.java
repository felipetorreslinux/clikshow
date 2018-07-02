package com.clikshow.Fragmentos;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.clikshow.Views.View_Detalhe_Evento;
import com.clikshow.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Detalhe_Evento_ComoChegar_Fragment extends Fragment {

    MapView mMapView;
    Button btn_open_mapa_evento;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detalhe_como_chegar_ingressos, container, false);

        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                googleMap = mMap;
                LatLng latLng = new LatLng(Double.parseDouble(View_Detalhe_Evento.lat), Double.parseDouble(View_Detalhe_Evento.lng));
                final MarkerOptions marker = new MarkerOptions().position(latLng)
                        .flat(false)
                        .draggable(false)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_point_map_orange));

                googleMap.setMinZoomPreference(16);
                googleMap.addMarker(marker);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });

        btn_open_mapa_evento = (Button) rootView.findViewById(R.id.btn_open_mapa_evento);
        btn_open_mapa_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("google.navigation:q="+View_Detalhe_Evento.lat+","+View_Detalhe_Evento.lng+"");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        return rootView;
    };

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
