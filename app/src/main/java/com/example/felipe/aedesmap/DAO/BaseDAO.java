package com.example.felipe.aedesmap.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Felipe on 03/05/2016.
 */
public class BaseDAO extends SQLiteOpenHelper{
    public static final String TBL = "position";
    public static final String COD = "cod_pos";
    public static final String LAT = "latitude";
    public static final String LGN = "longitude";
    public static final String DATA_IN = "data_inserida";


    private static final String DATABASE_NAME = "geoPosition.db";
    private static final int DATABASE_VERSION = 2;

    //Estrutura da tabela Agenda (sql statement)
    private static final String CREATE_TABLE = "CREATE TABLE "+TBL+"(\n" +
            COD+" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            LGN+" REAL,\n" +
            LAT+" REAL,\n" +
            DATA_IN+" DATETIME\n" +
            ")";

    public BaseDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Criação da tabela
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Caso seja necessário mudar a estrutura da tabela
        //deverá primeiro excluir a tabela e depois recriá-la
        db.execSQL("DROP TABLE IF EXISTS " + TBL);
        onCreate(db);
    }
}
