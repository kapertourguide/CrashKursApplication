package com.example.simonisb.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by simonisb on 17.03.2015.
 */
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bspdatabase.db";
    private static final int DATABASE_VERSION = 1;

    //Beginn Beispiel Table

    public static final String TABLE_STRINGS_TABLENAME = "strings";
    public static final String TABLE_STRINGS_ID = "_id";
    public static final String TABLE_STRINGS_VALUE = "value";

    static final String TABLE_STRINGS_CREATE = "create table "
            + TABLE_STRINGS_TABLENAME + " ("
            + TABLE_STRINGS_ID + " integer primary key autoincrement, "
            + TABLE_STRINGS_VALUE + " text not null);";


    //Ende Beispiel Table



    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_STRINGS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STRINGS_TABLENAME);
        onCreate(db);
    }
}
