package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

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
        TextInputEditText etContact = findViewById(R.id.et_register_contact);
        TextInputEditText etPassword = findViewById(R.id.et_register_password);
        TextInputEditText etConfirmPassword = findViewById(R.id.et_register_confirm_password);

        Button btnRegister = findViewById(R.id.btn_register);
        ImageButton btnBack = findViewById(R.id.btn_back_to_login);
        TextView tvBack = findViewById(R.id.tv_back_to_login);

        btnRegister.setOnClickListener(v -> {
            String emailRaw = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String contact = etContact.getText() != null ? etContact.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
            String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

            if (emailRaw.isEmpty() || contact.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailRaw).matches()) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Normalise for consistent API username + local cache key
            String email = emailRaw.toLowerCase(Locale.ROOT);

            // Local pre-check (optional)
            if (repository.checkUserExists(email)) {
                Toast.makeText(this, "Email already registered (local cache)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Build API user object (Week 8 schema)
            User newUser = new User();
            newUser.setUsername(email);
            newUser.setPassword(password);
            newUser.setFirstname("Guest");
            newUser.setLastname("User");
            newUser.setEmail(email);
            newUser.setContact(contact);
            newUser.setUserType("Guest");

            courseworkApi.createUser(newUser, new CourseworkApi.ApiCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    // Cache locally for offline login
                    if (!repository.checkUserExists(email)) {
                        repository.registerUser(email, password);
                    }

                    Toast.makeText(GuestRegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(GuestRegisterActivity.this, GuestLoginActivity.class);
                    intent.putExtra("EMAIL_PREFILL", email);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(CourseworkApi.ApiError error) {
                    int code = (error != null) ? error.getStatusCode() : -1;
                    String msg = (error != null && error.getMessage() != null) ? error.getMessage() : "Unknown error";

                    if (code == 0) {
                        Toast.makeText(GuestRegisterActivity.this,
                                "Network error. Please check your connection and try again.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(GuestRegisterActivity.this,
                                "Registration failed (" + code + "): " + msg,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        });

        btnBack.setOnClickListener(v -> finish());
        tvBack.setOnClickListener(v -> finish());
    }
}
