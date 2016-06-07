package com.example.felipe.aedesmap.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Felipe on 24/05/2016.
 */
public class ImageDAO extends SQLiteOpenHelper {

    public static final String TBL = "imagem";
    public static final String COD = "cod_img";
    public static final String IMGBLOB = "img_blob";
    public static final String LAT = "latitude";
    public static final String LGN = "longitude";
    public static final String DATA_IN = "data_in";
    protected static final String teste = "teste";

    private static final String DATABASE_NAME = "imagem.db";
    private static final int DATABASE_VERSION = 4;

    private static final String CREATE_TABLE = "CREATE TABLE "+TBL+"(\n" +
            COD+" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            IMGBLOB+" BLOB,\n" +
            LAT+" REAL,\n" +
            LGN+" REAL,\n" +
            DATA_IN+" DATETIME DEFAULT CURRENT_DATE\n" +
            ")";


    public ImageDAO(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Caso seja necessário mudar a estrutura da tabela
        //deverá primeiro excluir a tabela e depois recriá-la
        db.execSQL("DROP TABLE IF EXISTS " + TBL);
        onCreate(db);
    }

    public Cursor getAll() {
        return(getReadableDatabase().rawQuery("SELECT "+IMGBLOB+" FROM "+TBL,null));
    }

    public int insert(byte[] bytes,double lat, double lng,SQLiteDatabase db){
        ContentValues cv=new ContentValues();
        cv.put(IMGBLOB,bytes);
        cv.put(LAT,lat);
        cv.put(LGN,lng);
        //cv.put(DATA_IN,dataInserida);
        Log.d("byte", "inserted");
        return (int) db.insert(TBL,null,cv);



    }

    public Cursor getOccurrenceByMonth(){
        return (getReadableDatabase().rawQuery("SELECT "+DATA_IN+", strftime('%m', "+DATA_IN+") as mes, strftime('%Y', "+DATA_IN+") as ano, count(*) as soma FROM "+TBL+"\n" +
                "group by strftime('%m', "+DATA_IN+")\n" +
                "order by "+DATA_IN+" asc",null));
    }

    public Cursor getOccurrenceByDay(){
        return (getReadableDatabase().rawQuery("SELECT "+DATA_IN+", strftime('%d', "+DATA_IN+") as day, count(*) as soma FROM "+TBL+"\n" +
                "where strftime('%Y',"+DATA_IN+") = strftime('%Y',CURRENT_DATE) and strftime('%m',"+DATA_IN+") = strftime('%m',CURRENT_DATE) \n" +
                "group by strftime('%d', "+DATA_IN+")\n" +
                "order by "+DATA_IN+" asc",null));
    }

    public byte[] getImage(Cursor c){
        return(c.getBlob(1));
    }
}
