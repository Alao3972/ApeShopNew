package com.example.andylao.apeshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Andy Lao on 2017-11-11.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String
            dbName = "apeshoperedsessft.db",
            tableName = "users",
            colId = "id",
            colFName = "firstName",
            colLName = "colLName",
            colEmail = "email",
            colPass = "password",
            colAddress = "address",
            colPostalCode = "postalCode",
            colCountry = "country",
            colProvince = "province",
            colItemId = "itemId",
            colTitle = "title",
            colDescription = "description",
            colCategory = "category",
            colPrice = "price",
            itemTable = "items",
            colPhone = "phone",
            employeeTable = "employee",
            colEmployeeId = "employeeId",
            colEmployeeFirstName = "employeeFirstName",
            colEmployeeLastName = "employeeLastName",
            colImage = "itemImage";


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

        db.execSQL("CREATE TABLE IF NOT EXISTS " + itemTable + "( " +
                colItemId + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                colId + " INTEGER," +
                colTitle + " TEXT," +
                colDescription + " TEXT," +
                colCategory + " TEXT," +
                colPrice + " TEXT," +
                colEmail + " TEXT," +
                colAddress + " TEXT," +
                colPostalCode + " TEXT," +
                colCountry + " TEXT," +
                colProvince + " TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + employeeTable + "(" +
                colEmployeeId + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                colEmployeeFirstName + " INTEGER," +
                colEmployeeLastName + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public void addUser(User user) {

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(colFName, user.getFirstName());
        contentValues.put(colLName, user.getLastName());
        contentValues.put(colEmail, user.getEmail());
        contentValues.put(colPass, user.getPassword());
        contentValues.put(colAddress, user.getAddress());
        contentValues.put(colPostalCode, user.getPostalCode());
        contentValues.put(colCountry, user.getCountry());
        contentValues.put(colProvince, user.getProvince());

        db.insert(tableName, null, contentValues);

        db.close();
    }

    public int getUserId(String email){
        int id = -1;
        String[] columns = {
                colId
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = colEmail + " = ?";

        String[] selectionArgs = {email};

        Cursor cursor = db.query(tableName,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex(colId));;
        }
        cursor.close();
        db.close();
        return id;
    }

    public void addItem(Item item) {

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(colId, item.getId());
        contentValues.put(colTitle, item.getTitle());
        contentValues.put(colDescription, item.getDescription());
        contentValues.put(colCategory, item.getCategory());
        contentValues.put(colPrice, item.getPrice());
        contentValues.put(colEmail, item.getEmail());
        contentValues.put(colAddress, item.getAddress());
        contentValues.put(colPostalCode, item.getPostalCode());
        contentValues.put(colCountry, item.getCountry());
        contentValues.put(colProvince, item.getProvince());


        db.insert(itemTable, null, contentValues);

        db.close();
    }

    public boolean updateItem(String id,String title,String description,String category, int price, String email, String address, String postalCode, String country, String province) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(colTitle, title);
        contentValues.put(colDescription, description);
        contentValues.put(colCategory, category);
        contentValues.put(colPrice, price);
        contentValues.put(colEmail, email);
        contentValues.put(colAddress, address);
        contentValues.put(colPostalCode, postalCode);
        contentValues.put(colCountry, country);
        contentValues.put(colProvince, province);
        db.update(itemTable, contentValues, "itemId = ?",new String[] { id });
        return true;
    }

    public Cursor getItemList(int userId){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + itemTable +
                " WHERE " + colId + " = '" + userId + "'";
        Cursor itemId = db.rawQuery(query, null);
        return itemId;
    }

    public Cursor getItem(int itemId){
        db = this.getWritableDatabase();
        String query = "SELECT * FROM " + itemTable;
        Cursor data = db.rawQuery(query, null);
        return data;
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


