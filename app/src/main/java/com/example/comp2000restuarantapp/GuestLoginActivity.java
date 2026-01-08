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

        if (getIntent().hasExtra("EMAIL_PREFILL")) {
            etEmail.setText(getIntent().getStringExtra("EMAIL_PREFILL"));
        }

        btnLogin.setOnClickListener(v -> {
            String emailOrUsername = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (emailOrUsername.isEmpty() || password.isEmpty()) {
                Toast.makeText(GuestLoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            courseworkApi.readUser(emailOrUsername, new CourseworkApi.ApiCallback<User>() {
                @Override
                public void onSuccess(User apiUser) {
                    if (apiUser != null && password.equals(apiUser.getPassword())) {
                        proceedToDashboard(apiUser.getEmail());
                    } else {
                        Toast.makeText(GuestLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(CourseworkApi.ApiError error) {
                    int code = (error != null) ? error.getStatusCode() : -1;
                    String msg = (error != null && error.getMessage() != null) ? error.getMessage() : "Unknown error";

                    if (code == 0) {
                        Toast.makeText(GuestLoginActivity.this,
                                "Network unavailable, trying offline login...",
                                Toast.LENGTH_SHORT).show();
                        fallbackToLocalLogin(username, password);
                        return;
                    }

                    // API is reachable; do NOT fallback (prevents incorrect “offline” success/paths).
                    if (code == 404) {
                        Toast.makeText(GuestLoginActivity.this, "No account found. Please register.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(GuestLoginActivity.this, "Login failed (" + code + "): " + msg, Toast.LENGTH_LONG).show();
                    }
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

    private void fallbackToLocalLogin(String email, String password) {
        String lowerEmail = email.toLowerCase();
        if (repository.checkUser(lowerEmail, password)) {
            proceedToDashboard(lowerEmail);
        } else {
            if (!repository.checkUserExists(lowerEmail)) {
                Toast.makeText(GuestLoginActivity.this, "No account found offline.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(GuestLoginActivity.this, "Invalid credentials (offline)", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void proceedToDashboard(String email) {
        getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE)
                .edit()
                .putString("USER_EMAIL", email)
                .apply();

        Intent intent = new Intent(GuestLoginActivity.this, GuestMenuActivity.class);
        startActivity(intent);
        finish();
    }
}