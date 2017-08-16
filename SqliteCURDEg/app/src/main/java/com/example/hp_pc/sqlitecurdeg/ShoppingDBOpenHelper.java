package com.example.hp_pc.sqlitecurdeg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by hp-pc on 15-08-2017.
 */

public class ShoppingDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myshopping_db.db";
    public static final String SHOPPING_TBL = "shopping";
    public static final String ITEM_NAME_FIELD = "_item_name";
    public static final String ITEM_QTY_FIELD = "_item_qty";
    public static final String ITEM_ID_FIELD = "_id";
    public static final int DB_VER = 1;
    public static final String[] ALL_FIELDS = new String[]{
            ITEM_ID_FIELD, ITEM_NAME_FIELD, ITEM_QTY_FIELD};
    public static final String CREATE_TBL = "CREATE table " + SHOPPING_TBL +
            " (" + ITEM_ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ITEM_NAME_FIELD + " TEXT NOT NULL," +
            ITEM_QTY_FIELD + " INTEGER NOT NULL)";

    public ShoppingDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP table " + SHOPPING_TBL);
            onCreate(db);

        }
    }

    public static void displayToast(Context context, String msg){
        Toast.makeText(context,msg, Toast.LENGTH_LONG).show();
    }
}


