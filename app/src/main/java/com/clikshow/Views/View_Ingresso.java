package com.clikshow.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.clikshow.API.APIServer;
import com.clikshow.Carrinho.View_Carrinho;
import com.clikshow.Fragmentos.Adapter.Meus_Ingressos_Adapter;
import com.clikshow.Fragmentos.Meus_Ingressos_Fragment;
import com.clikshow.QRCode.QRCodeClass;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.clikshow.Service.SegurancaPrint.ViewSecurity;
import com.clikshow.Service.Service_ClikSocial;
import com.clikshow.Service.Service_Meus_Ingressos;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Utils.Relogio;
import com.clikshow.Validation.CPF;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Date;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class View_Ingresso extends Activity implements View.OnClickListener {

    private static int MY_PASS_ID;
    private static String THUMB_INGRESSO;
    private static int VALIDADE_INGRESSO;
    private static String NAME_INGRESSO;
    private static String DESCRIPTION_INGRESSO;
    private static String CPF_INGRESSO;
    private static int PAYMENT_TYPE;

    private ImageView back_ingresso;

    private LinearLayout box_checkin_ingresso;
    Transformation transformation;
    InputMethodManager keyboard;

    private ImageView imagem_ingresso;
    private TextView data_ingresso;
    private TextView name_ingrresso;
    private TextView description_ingresso;
    private TextView name_user_ingresso;
    private TextView cpf_user_ingresso;
    private ImageView imagem_logo_qrcode_ingresso;
    private TextView texto_info_valida_checkin_ingresso;
    private TextView check_ticket;

    SharedPreferences sharedPreferences;

    private ImageView qrcode_ingresso;
    private TextView btn_cancelar_ingresso;

    EditText cpf_validar_checkin_ingresso;
    TextView name_validar_checkin_ingresso;

    CheckBox check_box_user_checkin;
    CheckBox check_box_amigos_checkin;

    public static String cpf_validar_checkin;

    public static TextView relogio_ingresso;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_ingresso);

        ViewSecurity.qrcode(this);

        relogio_ingresso = (TextView) findViewById(R.id.relogio_ingresso);

        final Handler atualizador = new Handler();
        atualizador.post(new Runnable() {
            @Override
            public void run() {
                Relogio.start(relogio_ingresso);
                atualizador.postDelayed(this, 100);
            }
        });

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        box_checkin_ingresso = (LinearLayout) findViewById(R.id.box_checkin_ingresso);

        imagem_ingresso = (ImageView) findViewById(R.id.imagem_ingresso);
        data_ingresso = (TextView) findViewById(R.id.data_ingresso);
        name_ingrresso = (TextView) findViewById(R.id.name_ingresso);
        description_ingresso = (TextView) findViewById(R.id.description_ingresso);
        name_user_ingresso = (TextView) findViewById(R.id.name_user_ingresso);
        cpf_user_ingresso = (TextView) findViewById(R.id.cpf_user_ingresso);
        imagem_logo_qrcode_ingresso = (ImageView) findViewById(R.id.imagem_logo_qrcode_ingresso);
        texto_info_valida_checkin_ingresso = (TextView) findViewById(R.id.texto_info_valida_checkin_ingresso);
        check_ticket = (TextView) findViewById(R.id.check_ticket);
        check_ticket.setOnClickListener(this);
        qrcode_ingresso = (ImageView) findViewById(R.id.qrcode_ingresso);
        btn_cancelar_ingresso = (TextView) findViewById(R.id.btn_cancelar_ingresso);
        btn_cancelar_ingresso.setOnClickListener(this);

        MY_PASS_ID = getIntent().getExtras().getInt("my_pass_id");
        THUMB_INGRESSO = getIntent().getExtras().getString("thumb");
        VALIDADE_INGRESSO = getIntent().getExtras().getInt("ends");
        NAME_INGRESSO = getIntent().getExtras().getString("name");
        DESCRIPTION_INGRESSO = getIntent().getExtras().getString("description");
        CPF_INGRESSO = getIntent().getExtras().getString("cpf");
        PAYMENT_TYPE = getIntent().getExtras().getInt("payment_type");

        transformation = new RoundedCornersTransformation(4, 0);

        Picasso.get()
        .load(THUMB_INGRESSO)
        .transform(transformation)
        .resize(118, 118)
        .centerCrop()
        .error(R.drawable.ic_place_doodle)
        .placeholder(R.drawable.ic_place_doodle)
        .into(imagem_ingresso);

        data_ingresso.setText("Válido até "+Datas.data_impressora(VALIDADE_INGRESSO));
        name_ingrresso.setText(NAME_INGRESSO);
        description_ingresso.setText(DESCRIPTION_INGRESSO);

        switch (PAYMENT_TYPE){
            case 4:
                switch (CPF_INGRESSO){
                    case "null":
                        imagem_logo_qrcode_ingresso.setVisibility(View.GONE);
                        texto_info_valida_checkin_ingresso.setVisibility(View.VISIBLE);
                        qrcode_ingresso.setAlpha((float) 0.1);
                        box_checkin_ingresso.setVisibility(View.GONE);
                        name_user_ingresso.setText(null);
                        cpf_user_ingresso.setText(null);
                        check_ticket.setText("Cortesia");
                        check_ticket.setBackground(getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_cortesia));
                        btn_cancelar_ingresso.setVisibility(View.VISIBLE);
                        relogio_ingresso.setVisibility(View.INVISIBLE);

                        qrcodeInvalido();

                        break;


                    default:

                        qrcode_ingresso.setOnClickListener(this);
                        if(CPF.MaskCpf(sharedPreferences.getString("cpf", null)) == CPF_INGRESSO){
                            name_user_ingresso.setText(sharedPreferences.getString("name", null));
                        }else{
                            name_user_ingresso.setText(sharedPreferences.getString("name", ""));
                        }
                        imagem_logo_qrcode_ingresso.setVisibility(View.VISIBLE);
                        texto_info_valida_checkin_ingresso.setVisibility(View.GONE);
                        qrcode_ingresso.setAlpha((float) 1);
                        box_checkin_ingresso.setVisibility(View.VISIBLE);
                        check_ticket.setText("Cortesia");
                        check_ticket.setBackground(getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_cortesia));
                        cpf_user_ingresso.setText("CPF: "+CPF_INGRESSO);
                        btn_cancelar_ingresso.setVisibility(View.INVISIBLE);

                        qrcodeValido();

                }

                break;
        }

        switch (CPF_INGRESSO){
            case "null":
                switch (PAYMENT_TYPE){
                    case 4:
                        imagem_logo_qrcode_ingresso.setVisibility(View.GONE);
                        texto_info_valida_checkin_ingresso.setVisibility(View.VISIBLE);
                        qrcode_ingresso.setAlpha((float) 0.1);
                        box_checkin_ingresso.setVisibility(View.GONE);
                        name_user_ingresso.setText(null);
                        cpf_user_ingresso.setText(null);
                        check_ticket.setText("Cortesia");
                        check_ticket.setBackground(getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_cortesia));
                        btn_cancelar_ingresso.setVisibility(View.GONE);
                        relogio_ingresso.setVisibility(View.VISIBLE);

                        qrcodeInvalido();
                        break;
                    default:
                        imagem_logo_qrcode_ingresso.setVisibility(View.GONE);
                        texto_info_valida_checkin_ingresso.setVisibility(View.VISIBLE);
                        qrcode_ingresso.setAlpha((float) 0.1);
                        box_checkin_ingresso.setVisibility(View.GONE);
                        name_user_ingresso.setText(null);
                        cpf_user_ingresso.setText(null);
                        check_ticket.setText("Efetuar check-in");
                        check_ticket.setBackground(getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_laranja));
                        btn_cancelar_ingresso.setVisibility(View.VISIBLE);
                        relogio_ingresso.setVisibility(View.GONE);

                        qrcodeInvalido();
                }


                break;


            default:

                switch (PAYMENT_TYPE) {
                    case 4:
                        qrcode_ingresso.setOnClickListener(this);
                        if(CPF.MaskCpf(sharedPreferences.getString("cpf", null)) == CPF_INGRESSO){
                            name_user_ingresso.setText(sharedPreferences.getString("name", null));
                        }else{
                            name_user_ingresso.setText(sharedPreferences.getString("name", ""));
                        }
                        imagem_logo_qrcode_ingresso.setVisibility(View.VISIBLE);
                        texto_info_valida_checkin_ingresso.setVisibility(View.GONE);
                        qrcode_ingresso.setAlpha((float) 1);
                        box_checkin_ingresso.setVisibility(View.VISIBLE);
                        check_ticket.setText("Cortesia");
                        check_ticket.setBackground(getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_cortesia));
                        cpf_user_ingresso.setText("CPF: "+CPF_INGRESSO);
                        btn_cancelar_ingresso.setVisibility(View.INVISIBLE);
                        relogio_ingresso.setVisibility(View.VISIBLE);
                        break;
                    default:
                        qrcode_ingresso.setOnClickListener(this);
                        if(CPF.MaskCpf(sharedPreferences.getString("cpf", null)) == CPF_INGRESSO){
                            name_user_ingresso.setText(sharedPreferences.getString("name", null));
                        }else{
                            name_user_ingresso.setText(sharedPreferences.getString("name", ""));
                        }
                        imagem_logo_qrcode_ingresso.setVisibility(View.VISIBLE);
                        texto_info_valida_checkin_ingresso.setVisibility(View.GONE);
                        qrcode_ingresso.setAlpha((float) 1);
                        box_checkin_ingresso.setVisibility(View.VISIBLE);
                        check_ticket.setText("Check-in realizado");
                        check_ticket.setBackground(getResources().getDrawable(R.drawable.btn_lista_meus_ingressos_verde));
                        cpf_user_ingresso.setText("CPF: "+CPF_INGRESSO);
                        btn_cancelar_ingresso.setVisibility(View.INVISIBLE);
                        relogio_ingresso.setVisibility(View.VISIBLE);


                }

                qrcodeValido();
        }

        back_ingresso = (ImageView) findViewById(R.id.back_ingresso);
        back_ingresso.setOnClickListener(this);
    }

    public void openQRCode(){

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_qrcode_profile, null);
        dialogView.setBackgroundResource(R.color.white);
        mBuilder.setView(dialogView);
        mBuilder.setCancelable(true);
        final AlertDialog dialog_qrcode = mBuilder.create();
        dialog_qrcode.show();
        final ImageView imgqrcode = (ImageView) dialogView.findViewById(R.id.img_profile_qrcode_max);
        final TextView btn_share_qrcode = (TextView) dialog_qrcode.findViewById(R.id.btn_share_qrcode_profile);

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", APIServer.token(this));
            jsonObject.put("my_pass_id", MY_PASS_ID);
            jsonObject.put("cpf", CPF_INGRESSO);
            jsonObject.put("payment_type", PAYMENT_TYPE);

            MultiFormatWriter multiFormatWriter= new MultiFormatWriter();
            BitMatrix bitMatrix = null;
            bitMatrix = multiFormatWriter.encode(jsonObject.toString(), BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            final Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imgqrcode.setImageBitmap(bitmap);
            final String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"ClikShow QRCode", "ClikShow QRCode");

            btn_share_qrcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_qrcode.dismiss();
                    Uri bitmapUri = Uri.parse(bitmapPath);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri );
                    startActivity(Intent.createChooser(intent , "ClikShow QRCode"));
                }
            });

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final LinearLayout close_img_qrcode_profile = dialog_qrcode.findViewById(R.id.close_img_qrcode_profile);
        close_img_qrcode_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_qrcode.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qrcode_ingresso:
                openQRCode();
                break;

            case R.id.check_ticket:
                    efetuarCheckInIngresso();
                break;

            case R.id.back_ingresso:
                    Intent intent = getIntent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                break;

            case R.id.btn_cancelar_ingresso:
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.app_name);
                    builder.setMessage("Deseja realmente cancelar este ingresso?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(APIServer.conexao(View_Ingresso.this) == true){
                                Service_Meus_Ingressos.cancelar_ingresso(View_Ingresso.this, MY_PASS_ID);
                            }else{
                                ToastClass.curto(View_Ingresso.this, "Aparelho offline\nVerifique sua conexão");
                            };
                        }
                    });
                    builder.setNegativeButton("Não",null);
                    builder.create();
                    builder.show();
                break;
        }
    }

    public void qrcodeInvalido(){
        try {
            MultiFormatWriter multiFormatWriter= new MultiFormatWriter();
            BitMatrix bitMatrix = null;
            bitMatrix = multiFormatWriter.encode("{'token':'null', 'cpf':'null', token: 'null', 'payment_type': 'null'}", BarcodeFormat.QR_CODE, 250, 250);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrcode_ingresso.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void qrcodeValido(){
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", APIServer.token(this));
            jsonObject.put("my_pass_id", MY_PASS_ID);
            jsonObject.put("cpf", CPF_INGRESSO);
            jsonObject.put("payment_type", PAYMENT_TYPE);

            MultiFormatWriter multiFormatWriter= new MultiFormatWriter();
            BitMatrix bitMatrix = null;
            bitMatrix = multiFormatWriter.encode(jsonObject.toString(), BarcodeFormat.QR_CODE, 250, 250);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrcode_ingresso.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void efetuarCheckInIngresso(){

        switch (CPF_INGRESSO){
            case "null":
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_efetuar_checkin, null);
                builder.setCancelable(false);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final ImageView imagem_ingresso_efetuar_checkin = (ImageView) view.findViewById(R.id.imagem_ingresso_efetuar_checkin);
                final TextView name_ingresso_efetuar_checkin = (TextView) view.findViewById(R.id.name_ingresso_efetuar_checkin);
                final TextView validade_ingresso_efetuar_checkin = (TextView) view.findViewById(R.id.validade_ingresso_efetuar_checkin);
                cpf_validar_checkin_ingresso = (EditText) view.findViewById(R.id.cpf_validar_checkin_ingresso);
                name_validar_checkin_ingresso = (TextView) view.findViewById(R.id.name_validar_checkin_ingresso);
                final TextView btn_efetuar_checkin_ingresso = (TextView) view.findViewById(R.id.btn_efetuar_checkin_ingresso);
                final TextView btn_voltar_checkin_ingresso = (TextView) view.findViewById(R.id.btn_voltar_checkin_ingresso);

                check_box_user_checkin = (CheckBox) view.findViewById(R.id.check_box_user_checkin);
                check_box_amigos_checkin = (CheckBox) view.findViewById(R.id.check_box_amigos_checkin);

                check_box_user_checkin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check_box_user_checkin.setChecked(true);
                        check_box_amigos_checkin.setChecked(false);
                        String cpf = sharedPreferences.getString("cpf", null);
                        String cpf_transformado = cpf.replace(".", "").replace("-","");
                        cpf_validar_checkin = cpf_transformado;
                        name_validar_checkin_ingresso.setText(sharedPreferences.getString("name", null));
                        cpf_validar_checkin_ingresso.setText(cpf_validar_checkin);
                        cpf_validar_checkin_ingresso.setCursorVisible(false);
                        AndroidNetworking.forceCancel("CHECAR_CPF_CHECKIN");
                    }
                });

                check_box_amigos_checkin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check_box_amigos_checkin.setChecked(true);
                        check_box_user_checkin.setChecked(false);
                        name_validar_checkin_ingresso.setText(null);
                        cpf_validar_checkin_ingresso.setText(null);
                        cpf_validar_checkin_ingresso.setCursorVisible(false);
                        Intent intent = new Intent(View_Ingresso.this, View_Lista_Amigos_ClikSocial.class);
                        startActivityForResult(intent, 9000);
                    }
                });

                Picasso.get()
                .load(THUMB_INGRESSO)
                .placeholder(R.drawable.ic_place_doodle)
                .transform(transformation)
                .resize(100,100)
                .error(R.drawable.ic_place_doodle)
                .into(imagem_ingresso_efetuar_checkin);

                name_ingresso_efetuar_checkin.setText(NAME_INGRESSO);
                validade_ingresso_efetuar_checkin.setText("Válido até "+Datas.data_impressora(VALIDADE_INGRESSO));


                cpf_validar_checkin_ingresso.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s.length() == 11){
                            cpf_validar_checkin = s.toString();
                            if(APIServer.conexao(View_Ingresso.this) == true){
                                Service_Meus_Ingressos.checar_cpf_checkin(
                                        View_Ingresso.this,
                                        name_validar_checkin_ingresso,
                                        cpf_validar_checkin_ingresso,
                                        keyboard);
                            }else{
                                ToastClass.curto(View_Ingresso.this, "Aparelho offline. Verifique sua conexão e tente novamente");
                                name_validar_checkin_ingresso.setText(null);
                                cpf_validar_checkin_ingresso.setText(null);
                                cpf_validar_checkin_ingresso.requestFocus();
                            }
                        }else{
                            name_validar_checkin_ingresso.setText(null);
                        }
                    }
                });


                btn_voltar_checkin_ingresso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cpf_validar_checkin_ingresso.setText(null);
                        name_validar_checkin_ingresso.setText(null);
                        alertDialog.dismiss();
                    }
                });

                btn_efetuar_checkin_ingresso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cpf_validar_checkin_ingresso.getText().toString().isEmpty()){
                            ToastClass.curto(View_Ingresso.this, "Informe o CPF");
                            cpf_validar_checkin_ingresso.requestFocus();
                        }else{
                            if(APIServer.conexao(View_Ingresso.this) == true){
                                Service_Meus_Ingressos.realizar_checkin(View_Ingresso.this, MY_PASS_ID, cpf_validar_checkin_ingresso, name_validar_checkin_ingresso);
                            }else{
                                alertDialog.dismiss();
                                ToastClass.longo(View_Ingresso.this, "Aparelho offline. Verifique sua conexão e tente novamente");
                            }
                        }
                    }
                });

            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 9000:
                if (resultCode == RESULT_OK) {
                    if(APIServer.conexao(this) == true){
                        int user_id = data.getExtras().getInt("user_id");
                        Service_ClikSocial.buscar_cpf_id(this, user_id, name_validar_checkin_ingresso,cpf_validar_checkin_ingresso);
                    }else{
                        name_validar_checkin_ingresso.setText(null);
                        cpf_validar_checkin_ingresso.setText(null);
                        cpf_validar_checkin_ingresso.requestFocus();
                    }
                }
                if(resultCode == RESULT_CANCELED){
                    name_validar_checkin_ingresso.setText(null);
                    cpf_validar_checkin_ingresso.setText(null);
                    check_box_user_checkin.setChecked(false);
                    check_box_amigos_checkin.setChecked(false);
                }
                break;
        };
    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


}
