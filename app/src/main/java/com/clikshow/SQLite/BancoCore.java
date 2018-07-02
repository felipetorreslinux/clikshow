package com.clikshow.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoCore  extends SQLiteOpenHelper{

    public BancoCore(Context context){
        super(context, "clikshow.db", null, 36);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        tabela_carrinho(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE carrinho");
        onCreate(database);
    }

    public void tabela_carrinho (SQLiteDatabase database){
        database.execSQL("CREATE TABLE carrinho (" +
            "_id INTEGER PRIMARY KEY," +
            "id_ingresso INTEGER," +
            "event_id INTEGER," +
            "event_name TEXT," +
            "description TEXT," +
            "event_thumb TEXT," +
            "qtd INTEGER," +
            "price INTEGER," +
            "total INTERGER)");
    }
}

