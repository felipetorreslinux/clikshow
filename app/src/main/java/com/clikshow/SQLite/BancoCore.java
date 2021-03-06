package com.clikshow.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoCore  extends SQLiteOpenHelper{

    public BancoCore(Context context){
        super(context, "clikshow.db", null, 37);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        tabela_carrinho(database);
        tabela_conversa(database);
    };

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE carrinho");
        database.execSQL("DROP TABLE conversas");
        onCreate(database);
    };

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
    };

    public void tabela_conversa (SQLiteDatabase database){
        database.execSQL("CREATE TABLE conversas (" +
                "_id INTEGER PRIMARY KEY," +
                "de INTEGER," +
                "para INTEGER," +
                "name TEXT," +
                "username TEXT," +
                "thumb TEXT," +
                "message TEXT," +
                "data TEXT)");
    };
}

