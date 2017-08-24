package com.android.hitech.calls.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAutoSave extends SQLiteOpenHelper {
    boolean result = false;
    Cursor cursor, cursor1;

    public DbAutoSave(Context context) {
        super(context, "DbAutoSave", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table autosave (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT,ROWID TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists autosave");
        onCreate(db);
    }

    public void insertData(String dataDate, String rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("DATE", dataDate);
        cv.put("ROWID", rowId);
        db.insert("autosave", null, cv);
    }

    public boolean getData(String queryData) {
        String[] selection = {queryData};
        SQLiteDatabase db = this.getWritableDatabase();
        cursor1 = db.rawQuery("select * from autosave", null);
        if (cursor1.getCount() > 20000) {
            onDelete();
        }
        String query = "select * from autosave where DATE =?";
        cursor = db.rawQuery(query, selection);
        if (cursor.getCount() != 0) {
            result = cursor.moveToLast();
        }
        cursor.close();
        return result;
    }

    public void onDelete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists autosave");
        onCreate(db);
    }
}
