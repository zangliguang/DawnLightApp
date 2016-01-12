package com.liguang.dawnlightapp.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.liguang.dawnlightapp.constants.LocalConstants;

/**
 * Created by zangliguang on 15/12/19.
 */
public class DawnLightSQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase mDatabase = null;

    public DawnLightSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, LocalConstants.Paths.DB_PATH, factory, DATABASE_VERSION, errorHandler);
    }

    public DawnLightSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, LocalConstants.Paths.DB_PATH, null, DATABASE_VERSION);
    }

    public DawnLightSQLiteHelper(Context context) {
        super(context, LocalConstants.Paths.DB_PATH, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }
}
