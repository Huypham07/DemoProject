package com.example.demoproject.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    //--ACCOUNT--
    public boolean checkingLogin(String userName, String password) {
        Cursor cursor = database.rawQuery("select * from " + DBHelper.TABLE_NAME_1
                + " where " + DBHelper.USER_NAME + " = ? and " + DBHelper.PASSWORD + " = ?"
                , new String[] {userName, password});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
    public void insert(String userName, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.USER_NAME, userName);
        contentValues.put(DBHelper.PASSWORD, password);
        database.insert(DBHelper.TABLE_NAME_1, null, contentValues);
    }

    public boolean UserNameAlreadyExists(String userName) {
        Cursor cursor = database.rawQuery("select * from " + DBHelper.TABLE_NAME_1
                + " where " + DBHelper.USER_NAME + " = ?", new String[] {userName});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    @SuppressLint("Range")
    public long getUserID(String userName) {
        int userID = -1;

        Cursor cursor = database.rawQuery("select " + DBHelper.ID + " from "
                        + DBHelper.TABLE_NAME_1 + " WHERE " + DBHelper.USER_NAME + " = ?",
                        new String[]{userName});

        if (cursor.moveToFirst()) {
            userID = cursor.getInt(cursor.getColumnIndex(DBHelper.ID));
        }

        cursor.close();
        return userID;
    }
    public int update(long id, String userName, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.USER_NAME, userName);
        contentValues.put(dbHelper.PASSWORD, password);
        int i = database.update(dbHelper.TABLE_NAME_1, contentValues, dbHelper.ID + " = " + id, null);
        return i;
    }

    public void delete(long id) {
        database.delete(dbHelper.TABLE_NAME_1, dbHelper.ID + "=" + id, null);
    }

    //--ITEM IN ACCOUNT--
    public int getNumberOfRemider(long id) throws SQLException {
        int numberReminder = 0;
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME_2
                + " WHERE " + DBHelper.userID + " = " + id, null);
        if (cursor.getCount() > 0) {
            numberReminder = cursor.getCount();
        }
        cursor.close();
        return numberReminder;
    }


    public void insertReminder(long id, String title, String date, String time) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.userID, id);
        contentValues.put(dbHelper.TITLE, title);
        contentValues.put(dbHelper.DATE, date);
        contentValues.put(dbHelper.TIME, time);
        database.insert(dbHelper.TABLE_NAME_2, null, contentValues);
    }

    public Cursor getAllReminder(long id) throws SQLException {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME_2
                + " WHERE " + DBHelper.userID + " = " + id, null);
        return cursor;
    }

    public void clearAllReminder(long id) {
        database.delete(dbHelper.TABLE_NAME_2, dbHelper.userID + "=" + id, null);
    }
}
