package com.clikshow.Profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.clikshow.API.APIServer;
import com.clikshow.Profile.Service.Service_Profile;
import com.clikshow.R;
import com.clikshow.Service.Toast.ToastClass;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class View_Editar_Usuario extends Activity implements View.OnClickListener {

    private ImageView btn_back_editar_profile;
    private ImageView img_edit_profile;
    private EditText name_edit_profile;
    private EditText username_edit_profile;
    private EditText email_edit_profile;
    private EditText cellphone_edit_profile;
    private EditText genre_edit_profile;
    private ImageView btn_edit_profile;
    private FrameLayout btn_editar_profile_pic;

    public static String encodedString;

    SharedPreferences profile;
    SharedPreferences.Editor profile_editor;

    public static Uri IMAGE_UPLOAD_EDIT_PROFILE;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_editar_usuario);

        IMAGE_UPLOAD_EDIT_PROFILE = null;

        try{
            ActivityCompat.requestPermissions(View_Editar_Usuario.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

            profile = getSharedPreferences("user_info", MODE_PRIVATE);
            profile_editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();

            img_edit_profile = (ImageView) findViewById(R.id.img_edit_profile);
            name_edit_profile = (EditText) findViewById(R.id.name_edit_profile);
            name_edit_profile.setText(profile.getString("name", null));
            name_edit_profile.setEnabled(false);

            username_edit_profile = (EditText) findViewById(R.id.username_edit_profile);
            username_edit_profile.setText(profile.getString("username", null));
            username_edit_profile.setEnabled(false);

            email_edit_profile = (EditText) findViewById(R.id.email_edit_profile);
            email_edit_profile.setText(profile.getString("email", null));

            cellphone_edit_profile = (EditText) findViewById(R.id.cellphone_edit_profile);
            cellphone_edit_profile.setEnabled(false);

            if(profile.getString("cellphone", "").length() > 5){
                cellphone_edit_profile.setText(profile.getString("cellphone", "").substring(2));
            }else{
                cellphone_edit_profile.setText("Não informado");
            };

            genre_edit_profile = (EditText) findViewById(R.id.genre_edit_profile);
            genre_edit_profile.setText(profile.getString("genre", null));
            genre_edit_profile.setEnabled(false);

            btn_edit_profile = (ImageView) findViewById(R.id.btn_edit_profile);
            btn_edit_profile.setOnClickListener(this);

            btn_editar_profile_pic = (FrameLayout) findViewById(R.id.btn_editar_profile_pic);
            btn_editar_profile_pic.setOnClickListener(this);

            imageProfilePic();

            btn_back_editar_profile = (ImageView) findViewById(R.id.btn_back_editar_profile);
            btn_back_editar_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }catch (Exception e){

        };

    };

    private void imageProfilePic(){
        if(!profile.getString("profile_pic",null).equals("null")){
            Picasso.get()
                    .load(Uri.parse(profile.getString("profile_pic", null)))
                    .resize(200,200)
                    .centerCrop()
                    .placeholder(R.drawable.ic_profile)
                    .transform(new CropCircleTransformation())
                    .into(img_edit_profile);
        }else{
            Picasso.get()
                    .load(R.drawable.ic_profile)
                    .resize(200,200)
                    .centerCrop()
                    .placeholder(R.drawable.ic_profile)
                    .transform(new CropCircleTransformation())
                    .into(img_edit_profile);
        };
    };

    public static String converterImage(String img){
        Bitmap bitmap = BitmapFactory.decodeFile(img);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String im = byteArrayOutputStream.toString();
        return im;
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_edit_profile:
                if(email_edit_profile.getText().toString().isEmpty()){
                    email_edit_profile.setHint("Informe seu email");
                    email_edit_profile.requestFocus();
                }else if(email_edit_profile.getText().toString().indexOf("@") == -1 || email_edit_profile.getText().toString().indexOf(".") == -1){
                    email_edit_profile.setText(null);
                    email_edit_profile.setHint("Informe um email válido");
                    email_edit_profile.requestFocus();
                }else{
                    Service_Profile.editar_dados(this,
                        email_edit_profile.getText().toString(),
                        profile_editor);
                };
            break;

            case R.id.btn_editar_profile_pic:
                ImagePicker.create(this)
                .returnMode(ReturnMode.ALL)
                .folderMode(true)
                .toolbarFolderTitle("Albums")
                .toolbarImageTitle("Escolha")
                .single()
                .limit(1)
                .showCamera(true)
                .start();
            break;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            CropImage.activity(Uri.fromFile(new File(ImagePicker.getFirstImageOrNull(data).getPath())))
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(250,250)
                    .start(this);
        };

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                IMAGE_UPLOAD_EDIT_PROFILE = result.getUri();
                Picasso.get()
                        .load(IMAGE_UPLOAD_EDIT_PROFILE)
                        .resize(250,250)
                        .transform(new CropCircleTransformation())
                        .into(img_edit_profile);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        };
    };

    @Override
    public void onBackPressed(){
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    };
}
