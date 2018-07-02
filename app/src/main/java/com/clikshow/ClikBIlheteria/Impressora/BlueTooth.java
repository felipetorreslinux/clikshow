package com.clikshow.ClikBIlheteria.Impressora;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelUuid;

import com.clikshow.API.APIServer;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Relogio;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import java.util.logging.Handler;

public class BlueTooth {

    public static BluetoothAdapter bluetoothAdapter = null;
    public static BluetoothDevice bluetoothDevice = null;
    public static BluetoothSocket bluetoothSocket = null;
    public static boolean connection_BlueTooth = false;
    public static OutputStream outputStream = null;

    public static void open (final Activity activity){

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.enable();

        if(bluetoothAdapter.isEnabled()){
            if(connection_BlueTooth == true){
                try{
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(Lista_Impressoras_BlueTooth.MAC_PRINTER);
                    String applicationUUID = "00001101-0000-1000-8000-00805F9B34FB";
                    bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(ParcelUuid.fromString(applicationUUID).getUuid());
                    bluetoothSocket.connect();
                    if(bluetoothSocket.isConnected()){
                        outputStream = bluetoothSocket.getOutputStream();
                        connection_BlueTooth = true;
                    }else{
                        outputStream = null;
                        connection_BlueTooth = false;
                    }
                }catch (IOException e){

                }
            }else{
                Intent intent = new Intent(activity, Lista_Impressoras_BlueTooth.class);
                activity.startActivity(intent);
            };
        }else{
          ToastClass.curto(activity, "Ligue seu bluetooth");
        };
    };

    public static void print_ingresso_cartao_credito (final Activity activity, String nome_evento, int data_evento, int id_ingresso, String nome_cliente, String cpf_cliente, String telefone_cliente,
                                              String tipo_ingresso, double preco_ingresso , String jsonObject){

    };

    public static void print_ingresso_cartao_debito (final Activity activity, String nome_evento, int data_evento, int id_ingresso, String nome_cliente, String cpf_cliente, String telefone_cliente,
                                                      String tipo_ingresso, double preco_ingresso , String jsonObject){
        try {
            String BILL = "";
            String BILL2 = "";

            BILL =  "EVENTO\n"+nome_evento+"\n" +
                    "DATA DO EVENTO\n"+ Datas.data_impressora(data_evento)+"\n" +
                    "INGRESSO: "+id_ingresso+"\n";
            BILL2 = "CLIENTE\n"+nome_cliente+"\n" +
                    "CPF: "+cpf_cliente+"\n" +
                    "TELEFONE: "+telefone_cliente+"\n\n" +
                    "TIPO: "+tipo_ingresso+"\n" +
                    "FORMA PAGAMENTO: CARTAO DEBITO\n" +
                    "VALOR: "+APIServer.preco(preco_ingresso)+"\n\n"+
                    "www.clikshow.com.br\n"+
                    ""+Relogio.hora()+"\n\n\n";

            Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_logo_ingresso);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.POS_PrintBMP(bmp, 200, activity.getRequestedOrientation()));
            outputStream.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
            outputStream.write(BILL.getBytes());
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            outputStream.write(PrinterCommands.POS_PrintQr(PrinterCommands.printQRCode(jsonObject), 400, activity.getChangingConfigurations()));
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(BILL2.getBytes());
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);

        } catch (Exception e) {}
    };


}
