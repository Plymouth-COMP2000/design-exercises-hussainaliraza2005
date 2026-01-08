package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StaffLoginActivity extends AppCompatActivity {

    private CourseworkApi courseworkApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        courseworkApi = new CourseworkApi(this);

        EditText etUsername = findViewById(R.id.et_email); // XML uses et_email as ID
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        ImageButton btnBack = findViewById(R.id.btn_back_to_role);
        TextView tvBack = findViewById(R.id.tv_back_to_role);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(StaffLoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Authenticate via API
            courseworkApi.readUser(username, new CourseworkApi.ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    // User found, now check password and user type
                    if (user != null && user.getPassword().equals(password)) {
                        if ("Staff".equalsIgnoreCase(user.getUserType())) {
                            // Correct credentials and user type
                            Intent intent = new Intent(StaffLoginActivity.this, StaffDashboardActivity.class);
                            startActivity(intent);
                            finish(); // Prevent going back to login
                        } else {
                            // Correct login, but not a staff member
                            Toast.makeText(StaffLoginActivity.this, "Access Denied: Not a staff account", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Password incorrect or user object is malformed
                        Toast.makeText(StaffLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String message) {
                    // API error (user not found, network issue, etc.)
                    Toast.makeText(StaffLoginActivity.this, "Login Failed: " + message, Toast.LENGTH_LONG).show();
                }
            });
        });

        View.OnClickListener backListener = v -> finish();
        btnBack.setOnClickListener(backListener);
        tvBack.setOnClickListener(backListener);
    }
}