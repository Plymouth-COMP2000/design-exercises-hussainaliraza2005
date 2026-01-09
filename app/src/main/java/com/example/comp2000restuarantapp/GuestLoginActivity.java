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

import java.util.Locale;

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

            // Normalise for consistent local cache key
            final String key = emailOrUsername.toLowerCase(Locale.ROOT);

            courseworkApi.readUser(emailOrUsername, new CourseworkApi.ApiCallback<User>() {
                @Override
                public void onSuccess(User apiUser) {
                    if (apiUser == null) {
                        Toast.makeText(GuestLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Optional: enforce role separation (prevents staff account using guest login)
                    String type = apiUser.getUserType();
                    if (type != null && !type.isEmpty() && !"Guest".equalsIgnoreCase(type)) {
                        Toast.makeText(GuestLoginActivity.this, "This account is Staff. Use Staff login.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (password.equals(apiUser.getPassword())) {
                        // Cache successful credentials for offline login continuity
                        if (!repository.checkUserExists(key)) {
                            repository.registerUser(key, password);
                        }
                        proceedToDashboard(apiUser.getEmail() != null && !apiUser.getEmail().isEmpty() ? apiUser.getEmail() : key);
                    } else {
                        Toast.makeText(GuestLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(CourseworkApi.ApiError error) {
                    int code = (error != null) ? error.getStatusCode() : -1;
                    String msg = (error != null && error.getMessage() != null) ? error.getMessage() : "Unknown error";

                    // Network unavailable => offline fallback is valid
                    if (code == 0) {
                        Toast.makeText(GuestLoginActivity.this,
                                "Network unavailable, trying offline login...",
                                Toast.LENGTH_SHORT).show();
                        fallbackToLocalLogin(key, password);
                        return;
                    }

                    // API reachable; do NOT fallback (prevents incorrect offline success paths)
                    if (code == 404) {
                        Toast.makeText(GuestLoginActivity.this, "No account found. Please register.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(GuestLoginActivity.this, "Login failed (" + code + "): " + msg, Toast.LENGTH_LONG).show();
                    }
                }
            });
        });

        tvRegister.setOnClickListener(v -> startActivity(new Intent(GuestLoginActivity.this, GuestRegisterActivity.class)));

        tvForgotPassword.setOnClickListener(v -> startActivity(new Intent(GuestLoginActivity.this, GuestForgotPasswordActivity.class)));

        View.OnClickListener backListener = v -> finish();
        btnBack.setOnClickListener(backListener);
        tvBack.setOnClickListener(backListener);
    }

    private void fallbackToLocalLogin(String emailLower, String password) {
        if (repository.checkUser(emailLower, password)) {
            proceedToDashboard(emailLower);
        } else {
            if (!repository.checkUserExists(emailLower)) {
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
