package com.clikshow.QRCode;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class QRCodeClass {

    public static String qrcode_profile (final Activity activity){
        String qrcode = null;
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", sharedPreferences.getInt("id", 0));
            jsonObject.put("name", sharedPreferences.getString("name", null));
            jsonObject.put("cpf", sharedPreferences.getString("cpf", null));
            jsonObject.put("profile_pic", sharedPreferences.getString("profile_pic", null));
            jsonObject.put("email", sharedPreferences.getString("email", null));
            jsonObject.put("cellphone", sharedPreferences.getString("cellphone", ""));
            jsonObject.put("usertype", sharedPreferences.getInt("usertype", 0));
            qrcode = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            qrcode=null;
        }
        return qrcode;
    }
}
