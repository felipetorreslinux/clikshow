package com.clikshow.Service.Localizacao;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Local_Service {

    public static String local_evento (final Context context, double LATITUDE, double LONGITUDE) {
        String response = null;
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if(addresses.size() > 0) {
                for (int i = 0; i < addresses.size(); i++) {
                    String logradouro = addresses.get(i).getThoroughfare();
                    String bairro = addresses.get(i).getSubLocality();
                    String cidade = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String numero = addresses.get(0).getFeatureName();
                    if (numero.equals(null))
                        numero = "S/N";
                        response = logradouro + ", " + numero + "\n" + bairro + " - " + cidade;
                }
            }else{
                response = "Local não informado ou não existe";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
