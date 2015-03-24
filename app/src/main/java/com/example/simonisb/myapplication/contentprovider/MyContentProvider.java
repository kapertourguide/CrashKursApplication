package com.example.simonisb.myapplication.contentprovider;

import android.content.ContentProvider;
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
    // Varibale für Datenbank
    private SQLiteDatabase database;

    // used for the UriMacher
    // mit diesen Integerwerten kann später bei einem switch case richtig "geroutet" werden.
    private static final int ELEMENT = 10;
    private static final int ELEMENT_ID = 20;

    // Eine Authority die den Contentprovider eindeutig identifiziert
    private static final String AUTHORITY = "my.uri.authority.contentprovider";

    //Pfad für Strings/Daten in unserer Beispieltabelle
    private static final String STRING_ELEMENTS_PATH = "sting_elements";
    // Zusammenbau einer Uri die auf alle Elemente von unseren String-Elementen zeigt
    public static final Uri CONTENT_URI_ALL_ELEMENTS = Uri.parse("content://" + AUTHORITY
            + "/" + STRING_ELEMENTS_PATH);


    //Ein Objekt vom Typ UriMatcher über das ein Integerwert abgefragt werden kann,
    // mit dem dann in den CRUD-Methoden die richtige Tabelle gesetzt werden kann.
    // Für eine URI mit dem Pfad STRING_ELEMENTS_PATH wird 10 zurückgegeben.
    // -> Für alle Elemente
    // Für eine URI mit dem Pfad STRING_ELEMENTS_PATH + eine ID wird 20 zurückgegeben.
    // -> Für eine Element
    private static final UriMatcher myURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        myURIMatcher.addURI(AUTHORITY, STRING_ELEMENTS_PATH, ELEMENT);
        myURIMatcher.addURI(AUTHORITY, STRING_ELEMENTS_PATH + "/#", ELEMENT_ID);
    }

    // Konstruktor für ContentProvider
    public MyContentProvider() {
    }

    // Delete-Methode des ContentProvider
    //
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Mit dem URI-Matcher wird geprüft, um welche Art Von URI es sich handelt
        // Diese Wird durch einen int-Wert repräsentiert (siehe oben - z.B. ELEMENT = 10)
        int uriType = myURIMatcher.match(uri);
        // die Datenbank wird abgefragt und eine Referant darauf in der Variablen sqlDB gesichert.
        SQLiteDatabase sqlDB = databaseHelper.getWritableDatabase();
        // Variable um zurückzugeben wie viele Elemente gelöscht wurden
        int rowsDeleted = 0;

        // Mit diesem Switch wird geprüft, welche URI der deleteMetode übergeben wurde
        // bzw. welche Elemente der Datenbank nun gelöscht werden sollen.
        switch (uriType) {

            case ELEMENT:
                // Wenn der URI-Matcher feststellt, dass die URI alle Elemente der Tabelle strings repräsentiert,
                // dann kann nun im delete-Statement das auf der Datenbank aufgerufen wird die Tabelle gestetz werden
                // auch die Selection und die SelectionArgs werden übergeben,
                // in unserem Fall sind diese beiden Argumente null, da wir keine weitere Auswahl treffen wollen,
                // sondern in diesem Fall alle Elemente löschen wollen
                rowsDeleted = sqlDB.delete(SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME, selection,
                        selectionArgs);
                break;

            case ELEMENT_ID:
                // Wenn der URI-Matcher feststellt, dass die URI auf ein bestimmtes Element der
                // Tabelle strings verweißt, kann auch hier im deleteStatement,
                // das auf der Datenbank ausgeführt werden soll die Tabelle strings gesetzt werden.
                // Bevor das Statement ausgeführt wird, lesen wir allerdings die id des Elements das
                // gelöscht werden soll aus. Die ID ist das letzte Pfadsegment der URI.
                String id = uri.getLastPathSegment();

                // (zur Vereinfachung auskommentiert - in unserem Fall reicht das)
                // if (TextUtils.isEmpty(selection)) {

                    // Die Tabelle strings wid gesetzt
                    // Als Selection wollen wir nun das Element mit eine bestimmten ID
                    // Die selection ist daher _id=id (_id ist die Spalte der Tabelle, id die ausgelesene id der URI)
                    // Die id könnte auch als Slection-Argument übergeben werden (3.Parameter der delete-Methode)
                    rowsDeleted = sqlDB.delete(SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME,
                            SQLiteDatabaseHelper.TABLE_STRINGS_ID + "=" + id,
                            null);



                /*} else {
                    rowsDeleted = sqlDB.delete(SQLiteDatabaseHelper.TABLE_STRINGS_TABLENAME,
                            SQLiteDatabaseHelper.TABLE_STRINGS_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }*/
                break;

            default:
                //sollte in unserem Fall eine andere URI als unsere oben definierte übergeben werden,
                // dann wird eine Exception geworfen
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        // mit notifyChange() informieren wir alle Elemente die von der URI Daten abfragen
        // z.B. den Cursor der die Liste aller Elemente ausgelesen hat.
        // Dieser startet nun seine Abfrage nochmals
        getContext().getContentResolver().notifyChange(uri, null);
        // return wie viele Elemente aus der Datenbank gelöscht wurden
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
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();


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
