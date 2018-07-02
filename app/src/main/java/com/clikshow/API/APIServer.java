package com.clikshow.API;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.clikshow.Service.Toast.ToastClass;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

public class APIServer {
//    public static String URL = "http://34.200.90.213/";
    public static String URL = "http://18.233.50.81/";
    public static String CLIKSOCIAL = "http://52.90.166.201/";
    public static String CLIKSOCIALPROD = "http://api.cliksocial.com/";
    public static String LOCAL = "http://192.168.0.20:8000/";
    public static String SOCKET = "http://socket.cliksocial.com:3000";

    public static String LINK_GERENCIADOR = "http://clikshow.cliksocial.com";

    public static String token (Activity activity){
        String token = null;
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        return token;
    };

    public static boolean conexao (Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        else
            return false;
    }

    public static String preco (double price){
        String preco = null;
        final NumberFormat formatter = new DecimalFormat("#0.00");
        preco = formatter.format(price);
        return preco;
    }

    public static void Alerta (final Activity activity, String titulo, String mensagem, String btn_positive, String btn_negative, Boolean cancelar){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(titulo);
        builder.setMessage(mensagem);
        builder.setCancelable(cancelar);
        builder.setPositiveButton(btn_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(btn_negative, null);
        builder.create().show();
    }

    public static void getWhatsapp(final Activity activity) {
        try{
            String number = "558173305579";
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Estou com problemas em pagamento");
            sendIntent.putExtra("jid", number + "@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(sendIntent);
        }
        catch(Exception e) {
        }
    }

    public static void error_server (final Activity activity, int code){
        switch (code){
            case 500:
                ToastClass.curto(activity, "Desculpa o transtorno. Nosso servidor se encotra em manutenção. Assim que retornar ao normal avisaremoa para você. Obrigado");
                break;
            default:
                System.out.println(code);
        }
    }
}
