package com.example.simonisb.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.simonisb.myapplication.contentprovider.MyContentProvider;
import com.example.simonisb.myapplication.db.SQLiteDatabaseHelper;


public class ContentProviderListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider_list);

        // In der oncreate Daten über CP abfragen und sofort anzeigen
        fillData();
        getListView().setDividerHeight(15);
    }

    private void fillData() {

        getLoaderManager().initLoader(0, null, this);
        String[] from = new String[]{SQLiteDatabaseHelper.TABLE_STRINGS_ID, SQLiteDatabaseHelper.TABLE_STRINGS_VALUE };
        int[] to = new int[]{ R.id.tv_cpList_id, R.id.tv_cpList_label};
        adapter = new SimpleCursorAdapter(this, R.layout.activity_content_provider_list_row_item, null, from, to, 0);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI+"/"+id);
        int rowsDeleted = getContentResolver().delete(uri, null, null);
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Rows deleted: " + rowsDeleted + " ID: "+ id, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content_provider_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // BEGINN: Methoden die für den Loader erforderlich sind
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {SQLiteDatabaseHelper.TABLE_STRINGS_ID, SQLiteDatabaseHelper.TABLE_STRINGS_VALUE };
        return new CursorLoader(
                this,
                MyContentProvider.CONTENT_URI,
                projection,
                null,
                null,
                null );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor data) {
        if (data != null){
            adapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> objectLoader) {
        // // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }
    // END: Methoden die für den Loader erforderlich sind
}
