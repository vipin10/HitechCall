package com.android.hitech.calls.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseClass extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "MyDataBase";
    public static final String COLOUMN_ID = "_id";
    public static final String COLOUMN_ACC_NAME = "accountName";
    public static final String COLOUMN_CONTACT = "contactPerson";
    public static final String COLOUMN_DESTINATION = "Destination";
    public static final String COLOUMN_START_DATE = "startDate";
    public static final String COLOUMN_START_TIME = "startTime";
    public static final String COLOUMN_END_DATE = "endDate";
    public static final String COLOUMN_END_TIME = "endTime";

    public DataBaseClass(Context context) {
        super(context, "MyDataBase", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table MyDataBase(_id Integer Primary key autoincrement, accountName Text, contactPerson Text, " +
                "usersAttending Text, Destination Text, startDate Varchar," +
                " internalUsers Text, contactPer Text, startTime Varchar,endDate Varchar,endTime Varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE MyDataBase ADD COLUMN usersAttending INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE MyDataBase ADD internalUsers Text");
            db.execSQL("ALTER TABLE MyDataBase ADD contactPer Text");
            db.execSQL("ALTER TABLE MyDataBase ADD startTime Varchar");
            db.execSQL("ALTER TABLE MyDataBase ADD endDate Varchar");
            db.execSQL("ALTER TABLE MyDataBase ADD endTime Varchar");
            String myString = "DROP TABLE IF EXISTS" + TABLE_NAME;
            Log.d("onUpgrade() : ", myString);
            db.execSQL(myString);
            onCreate(db);
        }
    }
 }