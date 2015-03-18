package com.example.simonisb.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.UrlQuerySanitizer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simonisb.myapplication.db.SQLiteDatabaseHelper;

import java.util.ArrayList;


public class Unteractivity extends ActionBarActivity {

    private static final String TAG = "Unteractivity";
    SQLiteDatabaseHelper dbhelper;

    EditText editText;
    Button btn_insert;
    Button btn_read;
    ListView mlist;
    SQLiteDatabase db;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_unteractivity);

        editText = (EditText) findViewById( R.id.editText_zum_einfuegen);
        btn_insert = (Button) findViewById( R.id.btn_insert_text);
        btn_read = (Button) findViewById( R.id.btn_read_text);
        mlist = (ListView) findViewById( R.id.listview_DBlist);


        dbhelper = new SQLiteDatabaseHelper( this);
        db = dbhelper.getReadableDatabase();

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ContentValues values = new ContentValues();
                    values.put(SQLiteDatabaseHelper.TABLE_STRINGS_VALUE, editText.getText().toString());
                    long insertID = db.insert(SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME, null, values);
                }catch (Exception e){
                    String message = e.getMessage();
                }
            }
        });

        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showElementsInList();
            }
        });

        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick in Unteractivity");
                deleteClickedItem(view);
                showElementsInList();
            }
        });
    }

    private void deleteClickedItem(View view) {
        String str = "";
        if (view instanceof TextView)
        {
            str = ((TextView) view).getText().toString();
        }
        db.delete(SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME, SQLiteDatabaseHelper.TABLE_STRINGS_VALUE + " = ?", new String[]{str});
    }

    private void showElementsInList() {
        String[] projektion = {SQLiteDatabaseHelper.TABLE_STRINGS_VALUE};
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                SQLiteDatabaseHelper.TABLE_STRINGS_VALUE + " DESC";
        try{
            Cursor cursor = db.query(
                SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME,
                projektion,
                null, null, null, null,
                sortOrder);

            // Alternative Query
            // Cursor cursor =db.rawQuery("SELECT * FROM " + SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME , null);


            ArrayList<String> liste = new ArrayList<String>();

            if (cursor.moveToFirst()) {
                do {
                    int valueIndex = cursor.getColumnIndex(SQLiteDatabaseHelper.TABLE_STRINGS_VALUE);
                    String str = cursor.getString(valueIndex);
                    liste.add(str);
                } while (cursor.moveToNext());
            }

            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, liste);

            mlist.setAdapter(adapter);

            Log.d("Unteractivity", "TempCode");
        }catch (Exception e) {
                 e.getMessage();
        }
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_unteractivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected( item);
    }
}
