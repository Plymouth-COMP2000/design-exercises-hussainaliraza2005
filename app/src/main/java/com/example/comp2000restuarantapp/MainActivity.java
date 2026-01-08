package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private CourseworkApi courseworkApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_role_selection);

        courseworkApi = new CourseworkApi(this);

        // --- One-time DB Initialisation ---
        initializeStudentDatabase();
        // ----------------------------------

        MaterialButton btnGuest = findViewById(R.id.btn_continue_as_guest);
        MaterialButton btnStaff = findViewById(R.id.btn_continue_as_staff);

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuestLoginActivity.class);
                startActivity(intent);
            }
        });

        btnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StaffLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeStudentDatabase() {
        SharedPreferences prefs = getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE);
        boolean isInitialized = prefs.getBoolean("API_DB_INITIALISED", false);

        if (!isInitialized) {
            // Show a toast that we are initializing, so user knows background work is happening
            Toast.makeText(this, "Initializing Remote Database...", Toast.LENGTH_SHORT).show();

            courseworkApi.createStudentDatabase(new CourseworkApi.ApiCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    // Mark as initialized
                    prefs.edit().putBoolean("API_DB_INITIALISED", true).apply();
                    Toast.makeText(MainActivity.this, "Remote DB Initialized: " + data, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String message) {
                    // Log or show error, but don't block the user
                    Toast.makeText(MainActivity.this, "Remote DB Init Failed: " + message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}