package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class GuestRegisterActivity extends AppCompatActivity {

    private Repository repository;
    private CourseworkApi courseworkApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_register);

        repository = new Repository(this);
        courseworkApi = new CourseworkApi(this);

        TextInputEditText etEmail = findViewById(R.id.et_register_email);
        TextInputEditText etPassword = findViewById(R.id.et_register_password);
        TextInputEditText etConfirmPassword = findViewById(R.id.et_register_confirm_password);
        Button btnRegister = findViewById(R.id.btn_register);
        ImageButton btnBack = findViewById(R.id.btn_back_to_login);
        TextView tvBack = findViewById(R.id.tv_back_to_login);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(GuestRegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(GuestRegisterActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(GuestRegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create User object for API
                // Assuming username matches email for this flow, or you could split it
                // Firstname/lastname/contact are empty for this simple flow, or you can add fields to UI
                User newUser = new User();
                newUser.setUsername(email); // Use email as username
                newUser.setEmail(email);
                newUser.setPassword(password);
                newUser.setFirstname("Guest"); // Default
                newUser.setLastname("User");   // Default
                newUser.setContact("");
                newUser.setUserType("Guest");

                // Call API
                courseworkApi.createUser(newUser, new CourseworkApi.ApiCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        // On success, we also save locally to SQLite as a cache/fallback
                        String lowerEmail = email.toLowerCase();
                        if (!repository.checkUserExists(lowerEmail)) {
                            repository.registerUser(lowerEmail, password);
                        }

                        Toast.makeText(GuestRegisterActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();

                        // Navigate back to Login
                        Intent intent = new Intent(GuestRegisterActivity.this, GuestLoginActivity.class);
                        intent.putExtra("EMAIL_PREFILL", email);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(GuestRegisterActivity.this, "Registration Error: " + message, Toast.LENGTH_LONG).show();
                    }
                });
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