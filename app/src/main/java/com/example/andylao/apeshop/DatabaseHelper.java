package com.example.andylao.apeshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Created by Andy Lao on 2017-11-11.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String
            dbName = "apeshopered.db",
            tableName = "users",
            colId = "id",
            colFName = "firstName",
            colLName = "colLName",
            colEmail = "email",
            colPass = "password",
            colAddress = "address",
            colPostalCode = "postalCode",
            colCountry = "country",
            colProvince = "province";

    public SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "( " +
                colId + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                colFName + " TEXT," +
                colLName + " TEXT," +
                colEmail + " TEXT," +
                colPass + " TEXT," +
                colAddress + " TEXT," +
                colPostalCode + " TEXT," +
                colCountry + " TEXT," +
                colProvince + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public void addUser(String firstName, String lastName, String email, String password, String address, String postalCode, String country, String province) {

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(colFName, firstName);
        contentValues.put(colLName, lastName);
        contentValues.put(colEmail, email);
        contentValues.put(colPass, password);
        contentValues.put(colAddress, address);
        contentValues.put(colPostalCode, postalCode);
        contentValues.put(colCountry, country);
        contentValues.put(colProvince, province);

        db.insert(tableName, null, contentValues);

        db.close();
    }

    public boolean checkUser(String email, String password) {

        String[] columns = {
                colId
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = colEmail + " = ?" + " AND " + colPass + " = ?";

        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(tableName,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public  DatabaseHelper open() throws SQLException
    {
        db = this.getWritableDatabase();
        return this;
    }


}


