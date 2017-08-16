package com.example.hp_pc.sqlitecurdeg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText itemQty;
    private EditText itemName;
    private ListView itemListView;
    private Button addItem;
    private long selectedItem = -1;
    private Shopping shopping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("ShoppingDBOpenHelper List");
        shopping = new Shopping(this);
        shopping.openDB();
        itemName = (EditText)findViewById(R.id.item_name);
        itemQty = (EditText)findViewById(R.id.item_qty);
        itemListView = (ListView)findViewById(R.id.item_list);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = shopping.getItemByID(id);
                cursor.moveToFirst();
                itemName.setText(cursor.getString(cursor.getColumnIndex(ShoppingDBOpenHelper.ITEM_NAME_FIELD)));
                itemQty.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(ShoppingDBOpenHelper.ITEM_QTY_FIELD))));
                addItem.setText("Edit");
                cursor.close();
                selectedItem = id;
            }
        });
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                displayDialog(id);
                return true;
            }
        });
        addItem = (Button)findViewById(R.id.additem);
        addItem.setOnClickListener(this);

        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        //itemListView.setAdapter(adapter);
        reloadAdapter();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        shopping.closeDB();
    }
    private void displayDialog(final long selected){
        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you really want to delete this entry?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // proceed delete
                shopping.removeItemById(selected);
                reloadAdapter();
                ShoppingDBOpenHelper.displayToast(MainActivity.this, "Successfully deleted");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void reloadAdapter(){
        Cursor cursor = shopping.getAllItems();
        cursor.moveToFirst(); // index 0
        /*
        for(int i = 0; i< cursor.getCount(); i++){
            Log.w("ShoppingDBOpenHelper",cursor.getLong(0)+"");
            Log.w("ShoppingDBOpenHelper",cursor.getString(1));
            Log.w("ShoppingDBOpenHelper",cursor.getInt(2)+"");
            cursor.moveToNext();
        }
        */
        /* Right approach
        for(int i = 0; i< cursor.getCount(); i++){
            Log.w("ShoppingDBOpenHelper",cursor.getLong(cursor.getColumnIndex(ShoppingDBOpenHelper.ITEM_ID_FIELD))+"");
            Log.w("ShoppingDBOpenHelper",cursor.getString(cursor.getColumnIndex(ShoppingDBOpenHelper.ITEM_NAME_FIELD)));
            Log.w("ShoppingDBOpenHelper",cursor.getInt(cursor.getColumnIndex(ShoppingDBOpenHelper.ITEM_QTY_FIELD))+"");
            cursor.moveToNext();
        }
        cursor.close();
        */
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,R.layout.item_box,cursor,new String[]{ShoppingDBOpenHelper.ITEM_NAME_FIELD, ShoppingDBOpenHelper.ITEM_QTY_FIELD},
                new int[]{R.id.item_name_list,R.id.item_qty_list},1);
        itemListView.setAdapter(simpleCursorAdapter);
        itemName.setText("");
        itemQty.setText("");
    }

    @Override
    public void onClick(View v) {
        if (!itemName.getText().toString().equals("") && !itemQty.getText().toString().equals("")){
            if (selectedItem == -1){
                shopping.addItem(itemName.getText().toString(), Integer.parseInt(itemQty.getText().toString()));
                ShoppingDBOpenHelper.displayToast(this, "Successfully Added");
            }else{
                shopping.updateItemById(selectedItem,itemName.getText().toString(),Integer.parseInt(itemQty.getText().toString()));
                selectedItem = -1;
                addItem.setText("Add");
                ShoppingDBOpenHelper.displayToast(this,"Successfully Edited");
            }
            reloadAdapter();
        }else{
        }
    }
}