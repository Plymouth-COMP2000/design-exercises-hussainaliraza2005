package com.example.comp2000restuarantapp;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;

import java.util.List;

public class Repository {

    private DatabaseHelper dbHelper;
    private VolleySingleton volley;

    public Repository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.volley = VolleySingleton.getInstance(context);
    }

    // --- Menu Data Methods ---
    public List<MenuItem> getLocalMenuItems() {
        return dbHelper.getAllMenuItems();
    }

    public boolean addLocalMenuItem(MenuItem item) {
        return dbHelper.addMenuItem(item);
    }

    public boolean updateLocalMenuItem(MenuItem item) {
        return dbHelper.updateMenuItem(item);
    }

    public boolean deleteLocalMenuItem(int id) {
        return dbHelper.deleteMenuItem(id);
    }

    // --- Reservation Data Methods ---
    public List<Reservation> getLocalReservations() {
        return dbHelper.getAllReservations();
    }

    public boolean addLocalReservation(Reservation reservation) {
        return dbHelper.addReservation(reservation);
    }

    public boolean deleteLocalReservation(int id) {
        return dbHelper.deleteReservation(id);
    }
}