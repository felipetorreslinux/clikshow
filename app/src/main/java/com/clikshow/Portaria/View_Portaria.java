package com.clikshow.Portaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clikshow.Permissoes.Permissoes;
import com.clikshow.Portaria.Fragments.Inicio_Portaria_Fragment;
import com.clikshow.Portaria.Service.Portaria_Service;
import com.clikshow.R;
import com.clikshow.Service.Datas;
import com.clikshow.Service.Toast.ToastClass;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.SourceData;
import com.journeyapps.barcodescanner.camera.PreviewCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class View_Portaria extends Activity implements SurfaceHolder.Callback, View.OnClickListener, Camera.AutoFocusCallback {

    private Context context;
    private Camera camera;
    private SurfaceView surfaceView;
    SurfaceHolder holder;

    ImageView imageview_back_porteiro;
    ImageView image_view_open_camera_porteiro;
    EditText cpf_valida_porteiro;
    SharedPreferences sharedPreferences;
    Portaria_Service portaria_service;

    static int EVENT_ID = 0;
    static int PASS_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_portaria);

        portaria_service = new Portaria_Service(this);

        EVENT_ID = getIntent().getExtras().getInt("event_id");
        PASS_ID = getIntent().getExtras().getInt("pass_id");

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        image_view_open_camera_porteiro = (ImageView) findViewById(R.id.image_view_open_camera_porteiro);
        image_view_open_camera_porteiro.setOnClickListener(this);

        context = getApplicationContext();
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview_camera_porteiro);
        surfaceView.setOnClickListener(this);

        cpf_valida_porteiro = (EditText) findViewById(R.id.cpf_valida_porteiro);
        cpf_valida_porteiro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() == 11){
                    String cpf = charSequence.toString().trim();
                    portaria_service.checar_ingresso(PASS_ID, cpf, EVENT_ID);
                    cpf_valida_porteiro.setText(null);
                }
            }
        });
        imageview_back_porteiro = (ImageView) findViewById(R.id.imageview_back_porteiro);
        imageview_back_porteiro.setOnClickListener(this);

    };

    @Override
    protected void onResume() {
        super.onResume();
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        surfaceView.setVisibility(View.GONE);
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_back_porteiro:
                onBackPressed();
                break;

            case R.id.image_view_open_camera_porteiro:
                try{
                    surfaceView.setVisibility(View.VISIBLE);
                }catch (NullPointerException e){
                  ToastClass.curto(this, "Câmera do aparelho com defeito");
                };
                break;

            case R.id.surfaceview_camera_porteiro:
                if(camera != null){
                    camera.autoFocus(this);
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        encerrarPortaria();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        try{
            camera.setPreviewDisplay(holder);
        }catch (Exception e){}
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.setPreviewCallback(previewCallback);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        Camera.Size size = sizes.get(0);
        parameters.setPreviewSize(size.width, size.height);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.setPreviewCallback(null);
        camera.release();
        camera = null;
    }

    @Override
    public void onAutoFocus(boolean success, final Camera camera) {

    }

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Size size = camera.getParameters().getPreviewSize();
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                    data, size.width, size.height, 0,0,size.width, size.height, false);

            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new MultiFormatReader();
            Result result = null;
            try{

                result = reader.decode(bitmap);
                JSONObject jsonObject = new JSONObject(result.getText());
                String cpf = jsonObject.getString("cpf").replace(".", "").replace("-", "").trim();
                if(cpf.length() == 11){
                    cpf_valida_porteiro.setText(cpf);
                    surfaceView.setVisibility(View.GONE);
                }
            }catch (NotFoundException e){

            }catch (ChecksumException e){

            }catch (FormatException e){

            }catch (JSONException e){

            }
        }
    };

    private void encerrarPortaria(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Deseja realmente sair da portaria?");
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.negative_button_dialog, null);
        builder.create().show();
    }

}
