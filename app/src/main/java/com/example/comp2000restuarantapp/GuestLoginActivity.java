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

    private static final String PREFS_NAME = "RestaurantAppPrefs";
    private static final String KEY_USER_EMAIL = "USER_EMAIL";
    private static final String KEY_USER_PASSWORD = "USER_PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_guest_login);

        EditText etEmail = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView tvRegister = findViewById(R.id.tv_register_link);
        TextView tvForgotPassword = findViewById(R.id.tv_forgot_password_link);
        ImageButton btnBack = findViewById(R.id.btn_back_to_role);
        TextView tvBack = findViewById(R.id.tv_back_to_role);

        String prefillEmail = getIntent().getStringExtra("EMAIL_PREFILL");
        if (prefillEmail != null) {
            etEmail.setText(prefillEmail);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(GuestLoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String storedEmail = prefs.getString(KEY_USER_EMAIL, null);
                String storedPassword = prefs.getString(KEY_USER_PASSWORD, null);
                if (storedEmail == null || storedPassword == null) {
                    Toast.makeText(GuestLoginActivity.this, "No account found. Please register.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (storedEmail != null && storedPassword != null) {
                    boolean emailMatches = storedEmail.equalsIgnoreCase(email);
                    boolean passwordMatches = storedPassword.equals(password);
                    if (!emailMatches || !passwordMatches) {
                        Toast.makeText(GuestLoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Save email to SharedPreferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_USER_EMAIL, email);
                editor.apply();

                Intent intent = new Intent(GuestLoginActivity.this, GuestMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestLoginActivity.this, GuestRegisterActivity.class);
                startActivity(intent);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestLoginActivity.this, GuestForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };

        btnBack.setOnClickListener(backListener);
        tvBack.setOnClickListener(backListener);
    }
}
