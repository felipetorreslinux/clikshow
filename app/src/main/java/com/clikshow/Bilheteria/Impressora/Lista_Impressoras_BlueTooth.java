package com.clikshow.Bilheteria.Impressora;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Bilheteria.Models.ImpressorasModel;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Relogio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.clikshow.Bilheteria.Impressora.BlueTooth.bluetoothDevice;
import static com.clikshow.Bilheteria.Impressora.BlueTooth.connection_BlueTooth;

public class Lista_Impressoras_BlueTooth extends Activity {

    private BluetoothAdapter bluetoothAdapter = null;
    public static String MAC_PRINTER = null;
    int tipo_ingresso;

    RecyclerView recycler_impressoras;
    List<ImpressorasModel> lista_impressoras = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_lista_impressoras_bilheteria);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.enable();

        tipo_ingresso = getIntent().getExtras().getInt("tipo_ingresso");

        recycler_impressoras = (RecyclerView) findViewById(R.id.recycler_impressoras);
        recycler_impressoras.setLayoutManager(new LinearLayoutManager(this));
        recycler_impressoras.setNestedScrollingEnabled(false);
        recycler_impressoras.setHasFixedSize(true);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();
        ImpressorasModel impressorasModel;
        if(bluetoothDevices.size() > 0){
            for (BluetoothDevice dipositivos : bluetoothDevices){
                String name_bluetooth = dipositivos.getName();
                String mac_bluetooth = dipositivos.getAddress();
                impressorasModel = new ImpressorasModel(name_bluetooth, mac_bluetooth);
                lista_impressoras.add(impressorasModel);
            }
            ImpressorasAdapter impressorasAdapter = new ImpressorasAdapter(this, lista_impressoras);
            recycler_impressoras.setAdapter(impressorasAdapter);
        }

    };

    public class ImpressorasAdapter extends RecyclerView.Adapter<ImpressorasAdapter.ImpressorasHolder>{

        List<ImpressorasModel> lista_printer;
        Activity activity;

        public ImpressorasAdapter(final Activity activity, final List<ImpressorasModel> lista_printer) {
            this.lista_printer = lista_printer;
            this.activity = activity;
        }

        @NonNull
        @Override
        public ImpressorasAdapter.ImpressorasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_printer, parent, false);
            return new ImpressorasAdapter.ImpressorasHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImpressorasAdapter.ImpressorasHolder holder, int position) {
            final ImpressorasModel impressorasModel = lista_impressoras.get(position);

            holder.item_name_lista_impressoras.setText(impressorasModel.getName());
            holder.item_address_lista_impressoras.setText(impressorasModel.getMac());

            holder.item_list_impressora_bilhteria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{

                        final ProgressDialog progressDialog = new ProgressDialog(activity);
                        progressDialog.setTitle(R.string.app_name);
                        progressDialog.setMessage("Conectando impressora...");
                        progressDialog.show();

                        MAC_PRINTER = impressorasModel.getMac();
                        bluetoothDevice = bluetoothAdapter.getRemoteDevice(Lista_Impressoras_BlueTooth.MAC_PRINTER);
                        String applicationUUID = "00001101-0000-1000-8000-00805F9B34FB";
                        BlueTooth.bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(ParcelUuid.fromString(applicationUUID).getUuid());
                        BlueTooth.bluetoothSocket.connect();
                        BlueTooth.outputStream = BlueTooth.bluetoothSocket.getOutputStream();

                        if(BlueTooth.bluetoothSocket.isConnected()){
                            connection_BlueTooth = true;

                            progressDialog.setMessage("Imprimindo...");

                            switch (tipo_ingresso){
                                case 1:

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {

                                                String BILL = "";
                                                String BILL2 = "";

                                                BILL = "EVENTO\n" + getIntent().getExtras().getString("nome_evento") + "\n" +
                                                        "DATA DO EVENTO\n" + Datas.data_impressora(getIntent().getExtras().getInt("starts")) + "\n" +
                                                        "INGRESSO: " + getIntent().getExtras().getInt("id_evento") + "\n";
                                                BILL2 = "CLIENTE\n" + getIntent().getExtras().getString("nome_cliente") + "\n" +
                                                        "CPF: " + getIntent().getExtras().getString("cpf_cliente") + "\n" +
                                                        "TELEFONE: " + getIntent().getExtras().getString("telefone_cliente") + "\n\n" +
                                                        "TIPO: " + getIntent().getExtras().getString("nome_ingresso") + "\n" +
                                                        "FORMA PAGAMENTO: A VISTA\n" +
                                                        "VALOR: " + APIServer.preco(getIntent().getExtras().getDouble("preco_ingresso")) + "\n" +
                                                        "VALOR PAGO: " + APIServer.preco(Double.parseDouble(getIntent().getExtras().getString("valor_recebido"))) + "\n" +
                                                        "TROCO: " + getIntent().getExtras().getString("troco_cliente") + "\n\n" +
                                                        "www.clikshow.com.br\n" +
                                                        "" + Relogio.hora() + "\n\n\n";

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



                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 2000);


                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            activity.onBackPressed();
                                        }
                                    }, 4000);

                                break;

                                case 2:

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {

                                                String BILL = "";
                                                String BILL2 = "";

                                                BILL = "EVENTO\n" + getIntent().getExtras().getString("nome_evento") + "\n" +
                                                        "DATA DO EVENTO\n" + Datas.data_impressora(getIntent().getExtras().getInt("starts")) + "\n" +
                                                        "INGRESSO: " + getIntent().getExtras().getInt("id_evento") + "\n";
                                                BILL2 = "CLIENTE\n" + getIntent().getExtras().getString("nome_cliente") + "\n" +
                                                        "CPF: " + getIntent().getExtras().getString("cpf_cliente") + "\n" +
                                                        "TELEFONE: " + getIntent().getExtras().getString("telefone_cliente") + "\n\n" +
                                                        "TIPO: " + getIntent().getExtras().getString("nome_ingresso") + "\n" +
                                                        "FORMA PAGAMENTO: CARTAO CREDITO\n" +
                                                        "VALOR: " + APIServer.preco(getIntent().getExtras().getDouble("preco_ingresso")) + "\n\n" +
                                                        "www.clikshow.com.br\n" +
                                                        "" + Relogio.hora() + "\n\n\n";

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


                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 2000);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            activity.onBackPressed();
                                        }
                                    }, 4000);


                                    break;

                                case 3:

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {

                                                String BILL = "";
                                                String BILL2 = "";

                                                BILL = "EVENTO\n" + getIntent().getExtras().getString("nome_evento") + "\n" +
                                                        "DATA DO EVENTO\n" + Datas.data_impressora(getIntent().getExtras().getInt("starts")) + "\n" +
                                                        "INGRESSO: " + getIntent().getExtras().getInt("id_evento") + "\n";
                                                BILL2 = "CLIENTE\n" + getIntent().getExtras().getString("nome_cliente") + "\n" +
                                                        "CPF: " + getIntent().getExtras().getString("cpf_cliente") + "\n" +
                                                        "TELEFONE: " + getIntent().getExtras().getString("telefone_cliente") + "\n\n" +
                                                        "TIPO: " + getIntent().getExtras().getString("nome_ingresso") + "\n" +
                                                        "FORMA PAGAMENTO: CARTAO DEBITO\n" +
                                                        "VALOR: " + APIServer.preco(getIntent().getExtras().getDouble("preco_ingresso")) + "\n\n" +
                                                        "www.clikshow.com.br\n" +
                                                        "" + Relogio.hora() + "\n\n\n";

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

                                                BlueTooth.outputStream.flush();

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 2000);



                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            activity.onBackPressed();
                                        }
                                    }, 4000);
                                    break;
                            }

                        }else{
                            connection_BlueTooth = false;
                            ToastClass.curto(activity, "Não conectado");
                        }
                    }catch (IOException e){

                    };
                };
            });
        }

        @Override
        public int getItemCount() {
            return lista_printer != null ? lista_printer.size() : 0;
        }

        public class ImpressorasHolder extends RecyclerView.ViewHolder{

            LinearLayout item_list_impressora_bilhteria;
            TextView item_name_lista_impressoras;
            TextView item_address_lista_impressoras;

            public ImpressorasHolder(View itemView) {
                super(itemView);

                item_list_impressora_bilhteria = itemView.findViewById(R.id.item_list_impressora_bilhteria);
                item_name_lista_impressoras = itemView.findViewById(R.id.item_name_lista_impressoras);
                item_address_lista_impressoras = itemView.findViewById(R.id.item_address_lista_impressoras);

            }


        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
