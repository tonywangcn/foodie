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
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+  TABLE_NAME + " create table " + TABLE_NAME + " " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user TEXT," +
                "menuId INTEGER," +
                "restaurantId INTEGER," +
                "count INTEGER," +
                "price REAL," +
                "isPayed INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
        Log.d(TAG, "new order created " + result);
        return result != -1;
    }

    public boolean incrCount(String user, String restaurantId, String menuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COUNT + " = " + COUNT + " + 1" + " WHERE " + USER + " = " + user + " AND " + RESTAURANT_ID + " = " + restaurantId + " AND " + MENU_ID + " = " + menuId );
        return true;
    }

    public  boolean decrCount(String user, String restaurantId, String menuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COUNT + " = " + COUNT + " - 1" + " WHERE " + USER + " = " + user + " AND " + RESTAURANT_ID + " = " + restaurantId + " AND " + MENU_ID + " = " + menuId );
        return true;
    }

    public  Integer getCount(String user, String restaurantId, String menuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COUNT + " from " + TABLE_NAME + " WHERE " + USER + " = ? AND " + RESTAURANT_ID + " = ? AND " + MENU_ID + " = ?" , new String[]{user, restaurantId, menuId});
        while(cursor.moveToFirst()) {
             return cursor.getInt(cursor.getColumnIndex(COUNT)) ;
        }
        return 0;
    }

    public ArrayList<Order> getAll(String user, String restaurantId) {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COUNT + " from " + TABLE_NAME + " WHERE " + USER + " = ? AND " + RESTAURANT_ID + " = ? ", new String[]{user, restaurantId}, null);
        Boolean next = cursor.moveToNext();
        while(next) {
            Log.d(TAG, "next :" + next.toString() + " cursor " + cursor.getInt(0) + " second " + cursor.getString(1));
            Order order = new Order();
            order.setId(cursor.getInt(0));
            order.setUser(cursor.getString(1));
            order.setMenuId(cursor.getInt(2));
            order.setRestaurantId(cursor.getInt(3));
            order.setCount(cursor.getInt(4));
            order.setPrice(cursor.getFloat(5));
            orders.add(order);
        }
        return orders;
    }
}


