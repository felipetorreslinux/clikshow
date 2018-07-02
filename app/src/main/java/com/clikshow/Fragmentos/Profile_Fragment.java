package com.clikshow.Fragmentos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clikshow.API.APIServer;
import com.clikshow.Profile.Models.ServicosProfileModel;
import com.clikshow.Profile.View_Editar_Usuario;
import com.clikshow.Profile.View_Pagamento_Profile;
import com.clikshow.Profile.View_Servicos_Profile;
import com.clikshow.QRCode.QRCodeClass;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.clikshow.Views.View_Ajuda;
import com.clikshow.Views.View_Sobre;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.content.Context.MODE_PRIVATE;

public class Profile_Fragment extends Fragment {

    private TextView name_profile;
    private TextView cellphone_profile;
    private TextView email_profile;
    private ImageView img_profile;
    private ImageView img_qrcode_profile;
    private LinearLayout btn_open_payment_profile;
    private LinearLayout btn_open_editar_perfil;
    private LinearLayout openEditProfile;
    private FrameLayout btn_settings_profile;
    private LinearLayout btn_open_servicos;
    private List<ServicosProfileModel> lista_servicos_profile = new ArrayList<>();
    private LinearLayout btn_open_ajuda;
    private LinearLayout btn_open_sobre;
    private SharedPreferences profile;
    private SharedPreferences.Editor profile_editor;
    private Dialog dialog;

    public static String QRCODE_INGRESSO;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        try{

            profile = getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
            profile_editor = getActivity().getSharedPreferences("user_info", MODE_PRIVATE).edit();

            QRCODE_INGRESSO = QRCodeClass.qrcode_profile(getActivity());

            img_profile = (ImageView) rootView.findViewById(R.id.img_profile);

            try {
                img_qrcode_profile = (ImageView) rootView.findViewById(R.id.img_qrcode_profile);
                MultiFormatWriter multiFormatWriter= new MultiFormatWriter();
                BitMatrix bitMatrix = null;
                bitMatrix = multiFormatWriter.encode(profile.getString("cpf", ""), BarcodeFormat.QR_CODE, 300, 300);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                img_qrcode_profile.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            img_qrcode_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qrcode_Profile();
                }
            });

            name_profile = (TextView) rootView.findViewById(R.id.name_profile);
            name_profile.setText(profile.getString("name", null));

            cellphone_profile = (TextView) rootView.findViewById(R.id.cellphone_profile);
            cellphone_profile.setVisibility(View.GONE);
//

            email_profile = (TextView) rootView.findViewById(R.id.email_profile);
            email_profile.setText(profile.getString("email", null));

            btn_open_editar_perfil = (LinearLayout) rootView.findViewById(R.id.btn_open_editar_perfil);
            btn_open_editar_perfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent open = new Intent(getActivity(), View_Editar_Usuario.class);
                    startActivityForResult(open, 5000);
                }
            });

            btn_open_payment_profile = (LinearLayout) rootView.findViewById(R.id.btn_open_payment_profile);
            btn_open_payment_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent open = new Intent(getActivity(), View_Pagamento_Profile.class);
                    startActivity(open);
                }
            });

            btn_settings_profile = (FrameLayout) rootView.findViewById(R.id.btn_settings_profile);
            btn_settings_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final BottomSheetDialog BottomSheetDialog = new BottomSheetDialog(getActivity());
                    final View dialog_settings = getActivity().getLayoutInflater().inflate(R.layout.dialog_menu_profile, null);
                    BottomSheetDialog.setContentView(dialog_settings);
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) dialog_settings.getParent());
                    BottomSheetDialog.show();

                    final TextView btn_reclamacoes_sugestoes = (TextView) dialog_settings.findViewById(R.id.btn_reclamacoes_sugestoes);
                    final TextView btn_exit_app = (TextView) dialog_settings.findViewById(R.id.btn_exit_app);


                    btn_reclamacoes_sugestoes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BottomSheetDialog.dismiss();
                            APIServer.getWhatsapp(getActivity());
                        }
                    });

                    btn_exit_app.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BottomSheetDialog.dismiss();
                            dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog_logout);
                            final Button btnSim = (Button) dialog.findViewById(R.id.btn_logout_sim);
                            final Button btnNao = (Button) dialog.findViewById(R.id.btn_logout_nao);
                            btnSim.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    profile_editor.putBoolean("is_login", false);
                                    profile_editor.commit();
                                    getActivity().finish();
                                }
                            });

                            btnNao.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.setCancelable(false);
                            dialog.setTitle(R.string.app_name);
                            dialog.show();
                        }
                    });
                }
            });

            btn_open_servicos = (LinearLayout) rootView.findViewById(R.id.btn_open_servicos);
            btn_open_servicos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent open = new Intent(getActivity(), View_Servicos_Profile.class);
                    open.putExtra("id", profile.getInt("id", 0));
                    startActivity(open);
                }
            });

            btn_open_ajuda = (LinearLayout) rootView.findViewById(R.id.btn_open_ajuda);
            btn_open_ajuda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent open = new Intent(getActivity(), View_Ajuda.class);
                    startActivity(open);
                }
            });

            btn_open_sobre = (LinearLayout) rootView.findViewById(R.id.btn_open_sobre);
            btn_open_sobre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent open = new Intent(getActivity(), View_Sobre.class);
                    startActivity(open);
                }
            });

            imageProfilePic();

        }catch (Exception e){ }
        return  rootView;
    };

    // Imagem do Peril
    private void imageProfilePic(){

        if(!profile.getString("profile_pic","").equals("null")){
            Picasso.get()
            .load(Uri.parse(profile.getString("profile_pic", null)))
            .resize(100,100)
            .centerCrop()
            .placeholder(R.drawable.ic_profile)
            .transform(new CropCircleTransformation())
            .into(img_profile);
        }else{
            Picasso.get()
            .load(R.drawable.ic_profile)
            .resize(100,100)
            .centerCrop()
            .placeholder(R.drawable.ic_profile)
            .transform(new CropCircleTransformation())
            .into(img_profile);
        };
    };

    @Override
    public void onResume(){
        super.onResume();
        imageProfilePic();
        name_profile.setText(profile.getString("name", null));
        email_profile.setText(profile.getString("email", null));
    };


    private void qrcode_Profile(){
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_qrcode_profile, null);
        dialogView.setBackgroundResource(R.color.white);
        mBuilder.setView(dialogView);
        mBuilder.setCancelable(true);
        final AlertDialog dialog_qrcode = mBuilder.create();
        dialog_qrcode.show();
        final ImageView imgqrcode = (ImageView) dialogView.findViewById(R.id.img_profile_qrcode_max);
        final TextView btn_share_qrcode = (TextView) dialog_qrcode.findViewById(R.id.btn_share_qrcode_profile);

        try {

            MultiFormatWriter multiFormatWriter= new MultiFormatWriter();
            BitMatrix bitMatrix = null;
            bitMatrix = multiFormatWriter.encode(QRCodeClass.qrcode_profile(getActivity()), BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            final Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imgqrcode.setImageBitmap(bitmap);
            final String bitmapPath = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap,"ClikShow QRCode", "ClikShow QRCode");

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
        }

        final LinearLayout close_img_qrcode_profile = dialog_qrcode.findViewById(R.id.close_img_qrcode_profile);
        close_img_qrcode_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_qrcode.dismiss();
            }
        });
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                }
                return;
            }
        };
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 5000:
                if(resultCode == getActivity().RESULT_OK){
                    ToastClass.curto(getActivity(), "Informações Atualizadas com Sucesso");
                };

                if(resultCode == getActivity().RESULT_CANCELED){

                };
            break;
        }
    }

};
