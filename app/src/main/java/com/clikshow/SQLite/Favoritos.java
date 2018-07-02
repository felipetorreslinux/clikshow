package com.clikshow.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.clikshow.Fragmentos.Models.FavoritosModel;

public class Favoritos {

    static SQLiteDatabase banco;
    BancoCore bancoCore;

    public Favoritos(Context context){
        bancoCore = new BancoCore(context);
        banco = bancoCore.getWritableDatabase();
    }

    public static void add (FavoritosModel favoritosModel){

        String id = String.valueOf(favoritosModel.getId());

        Cursor cursor = banco.rawQuery("SELECT * FROM favoritos WHERE id_favorito = ?", new String[]{id});
        if(cursor != null){
            if(cursor.getCount() == 0 ){
                ContentValues contentValues = new ContentValues();
                contentValues.put("id_favorito", favoritosModel.getId());
                contentValues.put("event_id", favoritosModel.getEvent_id());
                contentValues.put("name", favoritosModel.getEvent_id());
                contentValues.put("type", favoritosModel.getEvent_id());
                contentValues.put("starts", favoritosModel.getEvent_id());
                contentValues.put("event_id", favoritosModel.getEvent_id());
                contentValues.put("event_id", favoritosModel.getEvent_id());
                contentValues.put("event_id", favoritosModel.getEvent_id());
                contentValues.put("event_id", favoritosModel.getEvent_id());
                contentValues.put("event_id", favoritosModel.getEvent_id());
                contentValues.put("event_id", favoritosModel.getEvent_id());
            }
        }

    }
}
