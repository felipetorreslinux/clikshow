package com.clikshow.Bilheteria.Views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;

import com.clikshow.API.APIServer;
import com.clikshow.Bilheteria.Impressora.BlueTooth;
import com.clikshow.Bilheteria.Impressora.PrinterCommands;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.clikshow.Utils.Relogio;


public class View_Ingresso_Impressora extends Activity {

    int ingresso;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ingresso = getIntent().getExtras().getInt("tipo_ingresso");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (ingresso){
                        case 1:
                            progressDialog = new ProgressDialog(View_Ingresso_Impressora.this);
                            progressDialog.setMessage("Imprimindo ingresso");
                            progressDialog.setCancelable(true);
                            progressDialog.show();

                            try {
                                String BILL = "";
                                String BILL2 = "";

                                BILL =  "EVENTO\n"+getIntent().getExtras().getString("nome_evento")+"\n" +
                                        "DATA DO EVENTO\n"+ Datas.data_impressora(getIntent().getExtras().getInt("starts"))+"\n" +
                                        "INGRESSO: "+getIntent().getExtras().getInt("id_evento")+"\n";
                                BILL2 = "CLIENTE\n"+getIntent().getExtras().getString("nome_cliente")+"\n" +
                                        "CPF: "+getIntent().getExtras().getString("cpf_cliente")+"\n" +
                                        "TELEFONE: "+getIntent().getExtras().getString("telefone_cliente")+"\n\n" +
                                        "TIPO: "+getIntent().getExtras().getString("nome_ingresso")+"\n" +
                                        "FORMA PAGAMENTO: A VISTA\n" +
                                        "VALOR: "+ APIServer.preco(getIntent().getExtras().getDouble("preco_ingresso"))+"\n"+
                                        "VALOR PAGO: "+APIServer.preco(Double.parseDouble(getIntent().getExtras().getString("valor_recebido")))+"\n"+
                                        "TROCO: "+getIntent().getExtras().getString("troco_cliente")+"\n\n"+
                                        "www.clikshow.com.br\n"+
                                        ""+ Relogio.hora()+"\n\n\n";

                                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo_ingresso);
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                                BlueTooth.outputStream.write(PrinterCommands.POS_PrintBMP(bmp, 200, getRequestedOrientation()));
                                BlueTooth.outputStream.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
                                BlueTooth.outputStream.write(BILL.getBytes());
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                                BlueTooth.outputStream.write(PrinterCommands.POS_PrintQr(PrinterCommands.printQRCode(getIntent().getExtras().getString("qrcode_cliente")), 400, getChangingConfigurations()));
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                                BlueTooth.outputStream.write(BILL2.getBytes());
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);

                                BlueTooth.outputStream.flush();

                            } catch (Exception e) {}

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    finish();
                                }
                            }, 3000);

                            break;


                        case 2:

                            progressDialog = new ProgressDialog(View_Ingresso_Impressora.this);
                            progressDialog.setMessage("Imprimindo ingresso");
                            progressDialog.setCancelable(true);
                            progressDialog.show();

                            try {
                                String BILL = "";
                                String BILL2 = "";

                                BILL =  "EVENTO\n"+getIntent().getExtras().getString("nome_evento")+"\n" +
                                        "DATA DO EVENTO\n"+ Datas.data_impressora(getIntent().getExtras().getInt("starts"))+"\n" +
                                        "INGRESSO: "+getIntent().getExtras().getInt("id_evento")+"\n";
                                BILL2 = "CLIENTE\n"+getIntent().getExtras().getString("nome_cliente")+"\n" +
                                        "CPF: "+getIntent().getExtras().getString("cpf_cliente")+"\n" +
                                        "TELEFONE: "+getIntent().getExtras().getString("telefone_cliente")+"\n\n" +
                                        "TIPO: "+getIntent().getExtras().getString("nome_ingresso")+"\n" +
                                        "FORMA PAGAMENTO: CARTAO CREDITO\n" +
                                        "VALOR: "+ APIServer.preco(getIntent().getExtras().getDouble("preco_ingresso"))+"\n"+
                                        ""+ Relogio.hora()+"\n\n\n";

                                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo_ingresso);
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                                BlueTooth.outputStream.write(PrinterCommands.POS_PrintBMP(bmp, 200, getRequestedOrientation()));
                                BlueTooth.outputStream.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
                                BlueTooth.outputStream.write(BILL.getBytes());
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                                BlueTooth.outputStream.write(PrinterCommands.POS_PrintQr(PrinterCommands.printQRCode(getIntent().getExtras().getString("qrcode_cliente")), 400, getChangingConfigurations()));
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                                BlueTooth.outputStream.write(BILL2.getBytes());
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);

                                BlueTooth.outputStream.flush();

                            } catch (Exception e) {}

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    finish();
                                }
                            }, 3000);


                            break;

                        case 3:

                            progressDialog = new ProgressDialog(View_Ingresso_Impressora.this);
                            progressDialog.setMessage("Imprimindo ingresso");
                            progressDialog.setCancelable(true);
                            progressDialog.show();

                            try {
                                String BILL = "";
                                String BILL2 = "";

                                BILL =  "EVENTO\n"+getIntent().getExtras().getString("nome_evento")+"\n" +
                                        "DATA DO EVENTO\n"+ Datas.data_impressora(getIntent().getExtras().getInt("starts"))+"\n" +
                                        "INGRESSO: "+getIntent().getExtras().getInt("id_evento")+"\n";
                                BILL2 = "CLIENTE\n"+getIntent().getExtras().getString("nome_cliente")+"\n" +
                                        "CPF: "+getIntent().getExtras().getString("cpf_cliente")+"\n" +
                                        "TELEFONE: "+getIntent().getExtras().getString("telefone_cliente")+"\n\n" +
                                        "TIPO: "+getIntent().getExtras().getString("nome_ingresso")+"\n" +
                                        "FORMA PAGAMENTO: CARTAO DEBITO\n" +
                                        "VALOR: "+ APIServer.preco(getIntent().getExtras().getDouble("preco_ingresso"))+"\n"+
                                        ""+ Relogio.hora()+"\n\n\n";

                                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo_ingresso);
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                                BlueTooth.outputStream.write(PrinterCommands.POS_PrintBMP(bmp, 200, getRequestedOrientation()));
                                BlueTooth.outputStream.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
                                BlueTooth.outputStream.write(BILL.getBytes());
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                                BlueTooth.outputStream.write(PrinterCommands.POS_PrintQr(PrinterCommands.printQRCode(getIntent().getExtras().getString("qrcode_cliente")), 400, getChangingConfigurations()));
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                                BlueTooth.outputStream.write(BILL2.getBytes());
                                BlueTooth.outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);

                                BlueTooth.outputStream.flush();

                            } catch (Exception e) {}

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    finish();
                                }
                            }, 3000);


                            break;
                    }
                }
            }, 3000);


    };
}
