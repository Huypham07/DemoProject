package com.example.demoproject.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    protected static final String DB_NAME = "demoApp";
    protected static final int DB_VERSION = 2;
    protected static final String TABLE_NAME_1 = "account";
    protected static final String ID = "id";
    protected static final String USER_NAME = "user_name";
    protected static final String PASSWORD = "password";
    private static final String CREATE_TABLE_1 = "create table " + TABLE_NAME_1 + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_NAME + " TEXT NOT NULL, "
            + PASSWORD + " TEXT NOT NULL);";

    protected static final String TABLE_NAME_2 = "reminder";
    protected static final String ITEM_ID_ = "item_id";
    protected static final String userID = "id";
    protected static final String TITLE = "title";
    protected static final String DATE = "date";
    protected static final String TIME = "time";

    private static final String CREATE_TABLE_2 = "create table " + TABLE_NAME_2 + "("
            + ITEM_ID_ + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " TEXT NOT NULL, "
            + DATE + " TEXT NOT NULL, "
            + TIME + " TEXT NOT NULL, "
            + userID + " INTEGER NOT NULL, "
            + "FOREIGN KEY ("+userID +") REFERENCES " + TABLE_NAME_1 + "("+ID+"))";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_1);
        sqLiteDatabase.execSQL(CREATE_TABLE_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        onCreate(sqLiteDatabase);
    }

}
