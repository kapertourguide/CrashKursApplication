package com.example.simonisb.myapplication.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.simonisb.myapplication.db.SQLiteDatabaseHelper;

public class MyContentProvider extends ContentProvider {

    // databaseHelper
    private SQLiteDatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    // used for the UriMacher
    private static final int ELEMENT = 10;
    private static final int ELEMENT_ID = 20;

    private static final String AUTHORITY = "my.uri.authority.contentprovider";

    private static final String BASE_PATH = "elements";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/elements";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/element";

    private static final UriMatcher myURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        myURIMatcher.addURI(AUTHORITY, BASE_PATH, ELEMENT);
        myURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ELEMENT_ID);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriType = myURIMatcher.match(uri);
        SQLiteDatabase sqlDB = databaseHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case ELEMENT:
                rowsDeleted = sqlDB.delete(SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME, selection,
                        selectionArgs);
                break;
            case ELEMENT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME,
                            SQLiteDatabaseHelper.TABLE_STRINGS_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME,
                            SQLiteDatabaseHelper.TABLE_STRINGS_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new SQLiteDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME);

        int uriMatch = myURIMatcher.match(uri);

        switch ( uriMatch){
            case ELEMENT:
                // Tabelle setzen
                builder.setTables(SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME);
                // es werden alle Elemente abgefragt
                // keine weiteren Abfrageoptionen nötig
                break;
            case ELEMENT_ID:
                // Tabelle setzen
                builder.setTables(SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME);
                // es wird ein Element abgefragt
                // weitere Abfrageoptionen einfügen für z.B. ID
                // -> queryBuilder erweitern
                builder.appendWhere(SQLiteDatabaseHelper.TABLE_STRINGS_ID + " = " +uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (database == null) {
            database = databaseHelper.getWritableDatabase();
        }

        Cursor cursor = null;
        try {
            cursor = builder.query(database, projection, selection,
                    selectionArgs, null, null, sortOrder);

            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Fehler bei der Abfrage von Daten mit dem CP", e);
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
