package com.clikshow.Direct.Banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.clikshow.Direct.Models.Conversa_Model;
import com.clikshow.SQLite.BancoCore;

import static com.clikshow.Fragmentos.Feed_Fragment.count_carrinho_feed;

public class Banco_Direct {
    public static SQLiteDatabase banco;
    BancoCore bancoCore;

    public Banco_Direct(Context context){
        bancoCore = new BancoCore(context);
        banco = bancoCore.getWritableDatabase();
    }

    public boolean add (int de, int para, String name, String username, String thumb, String message, String data){
        ContentValues contentValues = new ContentValues();
        contentValues.put("de", de);
        contentValues.put("para", para);
        contentValues.put("name", name);
        contentValues.put("username", username);
        contentValues.put("thumb", thumb);
        contentValues.put("message", message);
        contentValues.put("data", data);
        banco.insert("conversas", null, contentValues);
        if(banco.inTransaction()){
            return true;
        }else {
            return false;
        }
    }

    public int count(int id){
        int count_cart = 0;
        Cursor cursor = banco.rawQuery("select * from conversas where para = ?", new String[]{String.valueOf(id)});
        if(cursor != null){
            count_cart = cursor.getCount();
        }else{
            count_cart = 0;
        }
        return count_cart;
    };

}
