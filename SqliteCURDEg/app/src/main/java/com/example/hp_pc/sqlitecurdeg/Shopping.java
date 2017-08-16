package com.example.hp_pc.sqlitecurdeg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.example.hp_pc.sqlitecurdeg.ShoppingDBOpenHelper.ALL_FIELDS;
import static com.example.hp_pc.sqlitecurdeg.ShoppingDBOpenHelper.ITEM_ID_FIELD;
import static com.example.hp_pc.sqlitecurdeg.ShoppingDBOpenHelper.ITEM_NAME_FIELD;
import static com.example.hp_pc.sqlitecurdeg.ShoppingDBOpenHelper.ITEM_QTY_FIELD;
import static com.example.hp_pc.sqlitecurdeg.ShoppingDBOpenHelper.SHOPPING_TBL;


/**
 * Created by hp-pc on 15-08-2017.
 */

public class Shopping {
    ShoppingDBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    public Shopping(Context context){
        dbOpenHelper = new ShoppingDBOpenHelper(context);
    }
    public void openDB(){
        db = dbOpenHelper.getWritableDatabase();
    }
    public void closeDB(){
        db.close();
        db = null;
    }

    public Cursor getAllItems(){
        Cursor cursor = db.query(SHOPPING_TBL, ALL_FIELDS,
                null, null, null, null, null);
        return cursor;
    }

    public Cursor getItemByID(long itemId){
        //Cursor cursor = db.rawQuery("SELECT * FROM "+SHOPPING_TBL);
        Cursor cursor = db.query(SHOPPING_TBL,
                ALL_FIELDS,ITEM_ID_FIELD+ " = ?",
                new String[]{Long.toString(itemId)},
                null,null,null);
        return cursor;
    }

    public Cursor getItemByIDName(long itemId, String itemName){
        Cursor cursor = db.query(SHOPPING_TBL,
                ALL_FIELDS,ITEM_ID_FIELD+ " = ? AND "+ITEM_NAME_FIELD+" = ?",
                new String[]{Long.toString(itemId),itemName},
                null,null,null);
        return cursor;
    }

    public long addItem(String itemName, int itemQty){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_NAME_FIELD,itemName);
        contentValues.put(ITEM_QTY_FIELD,itemQty);
        return db.insert(SHOPPING_TBL,null,contentValues);
    }

    public int updateItemById(long itemId,String itemName, int itemQty){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_NAME_FIELD,itemName);
        contentValues.put(ITEM_QTY_FIELD,itemQty);
        return db.update(SHOPPING_TBL, contentValues, ITEM_ID_FIELD + " = ?", new String[]{Long.toString(itemId)});
    }

    public void removeItemById(long itemId){
        db.delete(SHOPPING_TBL,ITEM_ID_FIELD+" = ?",new String[]{Long.toString(itemId)});
    }



}
