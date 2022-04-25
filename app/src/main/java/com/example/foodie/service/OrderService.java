package com.example.foodie.service;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.foodie.adapter.MenuAdapter;
import com.example.foodie.model.Order;

import java.util.ArrayList;


public class OrderService extends SQLiteOpenHelper {
    private static final String TAG = OrderService.class.getSimpleName();
    public static final String DATABASE_NAME = "order.db";
    public static final String TABLE_NAME = "orders";
    public static final String ID = "id";
    public static final String USER = "user";
    public static final String MENU_ID = "menuId";
    public static final String RESTAURANT_ID = "restaurantId";
    public static final String COUNT = "count";
    public static final String PRICE = "price";
    public static final String IS_PAYED = "isPayed";

    public OrderService(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table " + TABLE_NAME + " " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user TEXT," +
                "menuId INTEGER," +
                "restaurantId INTEGER," +
                "count INTEGER," +
                "price REAL," +
                "isPayed INTEGER DEFAULT 0)";
        db.execSQL(sql);
        Log.d(TAG,"sql createTable: " + sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean newOrder(String user, String restaurantId, String menuId, Float price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER, user);
        contentValues.put(MENU_ID, menuId);
        contentValues.put(RESTAURANT_ID, restaurantId);
        contentValues.put(COUNT, 1);
        contentValues.put(PRICE, price);
        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.d(TAG, "new order created " + result + " contentValues + " + contentValues.toString());
        return result != -1;
    }

    public boolean incrCount(String user, String restaurantId, String menuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COUNT + " = " + COUNT + " + 1" + " WHERE " + USER + " = '" + user + "' AND " + RESTAURANT_ID + " = " + restaurantId + " AND " + MENU_ID + " = " + menuId );
        return true;
    }

    public  boolean decrCount(String user, String restaurantId, String menuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COUNT + " = " + COUNT + " - 1" + " WHERE " + USER + " = '" + user + "' AND " + RESTAURANT_ID + " = " + restaurantId + " AND " + MENU_ID + " = " + menuId );
        return true;
    }

    public  Integer getCount(String user, String restaurantId, String menuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COUNT + " from " + TABLE_NAME + " WHERE " + USER + " = ? AND " + RESTAURANT_ID + " = ? AND " + MENU_ID + " = ?" , new String[]{user, restaurantId, menuId});
        while(cursor.moveToFirst()) {
             return cursor.getInt(0) ;
        }
        return 0;
    }

    public ArrayList<Order> getAll(String user, String restaurantId) {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_NAME + " WHERE " + USER + " = ? AND " + RESTAURANT_ID + " = ? ", new String[]{user, restaurantId}, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                order.setUser(cursor.getString(cursor.getColumnIndex(USER)));
                order.setMenuId(cursor.getInt( cursor.getColumnIndex(MENU_ID)));
                order.setRestaurantId(cursor.getInt( cursor.getColumnIndex(RESTAURANT_ID)));
                order.setCount(cursor.getInt(cursor.getColumnIndex(COUNT)));
                order.setPrice(cursor.getFloat(cursor.getColumnIndex(PRICE)));
                orders.add(order);
            } while (cursor.moveToNext());

        }
        Log.d(TAG,"getAll: orders: " + orders.toString());
        return orders;
    }
}


