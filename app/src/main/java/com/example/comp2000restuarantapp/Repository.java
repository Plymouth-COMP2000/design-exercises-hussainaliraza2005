package com.example.comp2000restuarantapp;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Repository {

    private DatabaseHelper dbHelper;
    private VolleySingleton volley;

    public Repository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.volley = VolleySingleton.getInstance(context);
    }

    // --- Menu Data Methods ---

    // Fetch from Local DB
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

    // Example: Fetch from API
    // Note: You would typically replace the URL with your actual endpoint.
    public void fetchRemoteMenuItems(String url, final DataCallback<List<MenuItem>> callback) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Parse JSON into List<MenuItem> and return via callback
                        // For now, this is a placeholder implementation
                        callback.onSuccess(null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                });
        volley.addToRequestQueue(request);
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

    // --- User Methods ---
    
    public boolean registerUser(String email, String password) {
        return dbHelper.registerUser(email, password);
    }

    public boolean checkUser(String email, String password) {
        return dbHelper.checkUser(email, password);
    }

    public boolean checkUserExists(String email) {
        return dbHelper.checkUserExists(email);
    }

    public boolean updateUserPassword(String email, String newPassword) {
        return dbHelper.updateUserPassword(email, newPassword);
    }

    // Callback interface for async operations
    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}