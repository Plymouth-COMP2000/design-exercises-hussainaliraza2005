package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CourseworkApi courseworkApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_role_selection);

        courseworkApi = new CourseworkApi(this);

        // One-time DB initialisation
        initializeStudentDatabase();

        MaterialButton btnGuest = findViewById(R.id.btn_continue_as_guest);
        MaterialButton btnStaff = findViewById(R.id.btn_continue_as_staff);
        MaterialButton btnApiCheck = findViewById(R.id.btn_api_check); // may not exist in layout

        btnGuest.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GuestLoginActivity.class)));
        btnStaff.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, StaffLoginActivity.class)));

        if (btnApiCheck != null) {
            btnApiCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Checking API…", Toast.LENGTH_SHORT).show();
                    courseworkApi.readAllUsers(new CourseworkApi.ApiCallback<List<User>>() {
                        @Override
                        public void onSuccess(List<User> data) {
                            int count = (data != null) ? data.size() : 0;
                            Toast.makeText(MainActivity.this, "API OK: " + count + " users", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(CourseworkApi.ApiError error) {
                            int code = (error != null) ? error.getStatusCode() : -1;
                            String msg = (error != null && error.getMessage() != null) ? error.getMessage() : "Unknown error";
                            Toast.makeText(MainActivity.this, "API check failed (" + code + "): " + msg, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }

    private void initializeStudentDatabase() {
        SharedPreferences prefs = getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE);
        boolean isInitialized = prefs.getBoolean("API_DB_INITIALISED", false);

        if (isInitialized) {
            Log.d("MainActivity", "API DB already initialised");
            return;
        }

        courseworkApi.createStudentDatabase(new CourseworkApi.ApiCallback<String>() {
            @Override
            public void onSuccess(String data) {
                prefs.edit().putBoolean("API_DB_INITIALISED", true).apply();
                Log.d("MainActivity", "API DB initialised: " + data);
            }

            @Override
            public void onError(CourseworkApi.ApiError error) {
                int code = (error != null) ? error.getStatusCode() : -1;
                String msg = (error != null && error.getMessage() != null) ? error.getMessage() : "Unknown error";
                Log.e("MainActivity", "API init failed (" + code + "): " + msg);

                // Do NOT set the flag on failure; allow retry next launch.
                Toast.makeText(MainActivity.this, "API init failed (" + code + ")", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
