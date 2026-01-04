package com.example.comp2000restuarantapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all SQLite database operations including creation, upgrades,
 * and CRUD operations for Menu Items and Reservations.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restaurant.db";
    private static final int DATABASE_VERSION = 1;
    // --- Table: Menu ---
    public static final String TABLE_MENU = "menu";
    public static final String COL_MENU_ID = "id";
    public static final String COL_MENU_NAME = "name";
    public static final String COL_MENU_PRICE = "price";
    public static final String COL_MENU_DESC = "description";
    // --- Table: Reservations ---
    public static final String TABLE_RESERVATIONS = "reservations";
    public static final String COL_RES_ID = "id";
    public static final String COL_RES_GUEST = "guest_name";
    public static final String COL_RES_TIME = "time";
    public static final String COL_RES_TABLE = "table_number";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMenuTable = "CREATE TABLE " + TABLE_MENU + " (" +
                COL_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MENU_NAME + " TEXT, " +
                COL_MENU_PRICE + " REAL, " +
                COL_MENU_DESC + " TEXT)";
        String createReservationsTable = "CREATE TABLE " + TABLE_RESERVATIONS + " (" +
                COL_RES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RES_GUEST + " TEXT, " +
                COL_RES_TIME + " TEXT, " +
                COL_RES_TABLE + " TEXT)";
        db.execSQL(createMenuTable);
        db.execSQL(createReservationsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        onCreate(db);
    }

    // --- Menu Methods ---
    public boolean addMenuItem(MenuItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MENU_NAME, item.getName());
        values.put(COL_MENU_PRICE, item.getPrice());
        values.put(COL_MENU_DESC, item.getDescription());
        long result = db.insert(TABLE_MENU, null, values);
        return result != -1;
    }

    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_MENU_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_MENU_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_MENU_PRICE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_MENU_DESC));
                menuList.add(new MenuItem(id, name, price, desc));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menuList;
    }

    public boolean updateMenuItem(MenuItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MENU_NAME, item.getName());
        values.put(COL_MENU_PRICE, item.getPrice());
        values.put(COL_MENU_DESC, item.getDescription());
        int rows = db.update(TABLE_MENU, values, COL_MENU_ID + "=?", new String[]{String.valueOf(item.getId())});
        return rows > 0;
    }

    public boolean deleteMenuItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_MENU, COL_MENU_ID + "=?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    // --- Reservation Methods ---
    public boolean addReservation(Reservation reservation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RES_GUEST, reservation.getGuestName());
        values.put(COL_RES_TIME, reservation.getTime());
        values.put(COL_RES_TABLE, reservation.getTableNumber());
        long result = db.insert(TABLE_RESERVATIONS, null, values);
        return result != -1;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_RES_ID));
                String guestName = cursor.getString(cursor.getColumnIndexOrThrow(COL_RES_GUEST));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COL_RES_TIME));
                String tableNum = cursor.getString(cursor.getColumnIndexOrThrow(COL_RES_TABLE));
                reservationList.add(new Reservation(id, guestName, time, tableNum));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reservationList;
    }

    public boolean deleteReservation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_RESERVATIONS, COL_RES_ID + "=?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }
}