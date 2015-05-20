package com.app.regmd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 王海 on 2015/4/8.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_User = "create table user " +
            "(id integer primary key autoincrement, name text, password text, email text, flag text)";
    private static final String CREATE_LogRecord = "create table logRecord " +
            "(id integer primary key autoincrement, dayNumber integer, dayDate text, userId integer)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_User);
        db.execSQL(CREATE_LogRecord);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" +CREATE_User);
        db.execSQL("DROP TABLE IF EXISTS" +CREATE_LogRecord);
        onCreate(db);
    }
}
