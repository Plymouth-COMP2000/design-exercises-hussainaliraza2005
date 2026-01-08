package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GuestLoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private Repository repository;
    private CourseworkApi courseworkApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_guest_login);

        repository = new Repository(this);
        courseworkApi = new CourseworkApi(this);

        etEmail = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView tvRegister = findViewById(R.id.tv_register_link);
        TextView tvForgotPassword = findViewById(R.id.tv_forgot_password_link);
        ImageButton btnBack = findViewById(R.id.btn_back_to_role);
        TextView tvBack = findViewById(R.id.tv_back_to_role);

        // Check for prefill email from registration or password reset
        if (getIntent().hasExtra("EMAIL_PREFILL")) {
            etEmail.setText(getIntent().getStringExtra("EMAIL_PREFILL"));
        }

        btnLogin.setOnClickListener(v -> {
            String inputEmailOrUsername = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (inputEmailOrUsername.isEmpty() || password.isEmpty()) {
                Toast.makeText(GuestLoginActivity.this, "Please enter email/username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Attempt API Login first
            courseworkApi.readUser(inputEmailOrUsername, new CourseworkApi.ApiCallback<User>() {
                @Override
                public void onSuccess(User apiUser) {
                    // API call succeeded, check if passwords match
                    if (apiUser != null && apiUser.getPassword() != null && apiUser.getPassword().equals(password)) {
                        // Password correct - Valid API Login
                        String emailToSave = (apiUser.getEmail() != null && !apiUser.getEmail().isEmpty()) 
                                ? apiUser.getEmail() 
                                : inputEmailOrUsername;
                        proceedToDashboard(emailToSave);
                    } else {
                        // Password incorrect or user data malformed
                        Toast.makeText(GuestLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String message) {
                    // API call failed (e.g., network error, or user not found 404)
                    // Fallback to Local SQLite Login
                    fallbackToLocalLogin(inputEmailOrUsername, password);
                }
            });
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(GuestLoginActivity.this, GuestRegisterActivity.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(GuestLoginActivity.this, GuestForgotPasswordActivity.class);
            startActivity(intent);
        });

        View.OnClickListener backListener = v -> finish();
        btnBack.setOnClickListener(backListener);
        tvBack.setOnClickListener(backListener);
    }

    /**
     * Attempts to authenticate the user against the local SQLite database.
     * This is used as a fallback if the remote API is unavailable.
     */
    private void fallbackToLocalLogin(String email, String password) {
        String lowerEmail = email.toLowerCase();
        
        if (repository.checkUser(lowerEmail, password)) {
            // Local login success
            proceedToDashboard(lowerEmail);
        } else {
            // Local login failed
            if (!repository.checkUserExists(lowerEmail)) {
                Toast.makeText(GuestLoginActivity.this, "No account found. Please register.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(GuestLoginActivity.this, "Invalid credentials (local)", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Handles successful login by saving session and navigating to the next screen.
     */
    private void proceedToDashboard(String email) {
        // Save session email to SharedPreferences
        getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE)
                .edit()
                .putString("USER_EMAIL", email)
                .apply();

        // Navigate to the main guest screen (Dashboard)
        Intent intent = new Intent(GuestLoginActivity.this, GuestMenuActivity.class);
        startActivity(intent);
        finish(); // Prevent user from pressing back to return to the login screen
    }
}