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
        TextInputEditText etContact = findViewById(R.id.et_register_contact);
        TextInputEditText etPassword = findViewById(R.id.et_register_password);
        TextInputEditText etConfirmPassword = findViewById(R.id.et_register_confirm_password);
        Button btnRegister = findViewById(R.id.btn_register);
        ImageButton btnBack = findViewById(R.id.btn_back_to_login);
        TextView tvBack = findViewById(R.id.tv_back_to_login);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String contact = etContact.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || contact.isEmpty()) {
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

            // Create User object for the API request.
            User newUser = new User();
            newUser.setUsername(email); // Use email for username as UI has no separate field
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setFirstname("Guest"); // Default value
            newUser.setLastname("User");   // Default value
            newUser.setContact(contact);   // User provided contact
            newUser.setUserType("Guest");  // Default value

            // 1. Call API to create the user remotely
            courseworkApi.createUser(newUser, new CourseworkApi.ApiCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    // 2. On remote success, cache the user locally for offline access
                    String lowerEmail = email.toLowerCase();
                    if (!repository.checkUserExists(lowerEmail)) {
                        repository.registerUser(lowerEmail, password);
                    }

                    Toast.makeText(GuestRegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                    // 3. Navigate back to Login screen
                    Intent intent = new Intent(GuestRegisterActivity.this, GuestLoginActivity.class);
                    intent.putExtra("EMAIL_PREFILL", email);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String message) {
                    // Show specific API error message
                    Toast.makeText(GuestRegisterActivity.this, "Registration Error: " + message, Toast.LENGTH_LONG).show();
                }
            });
        });

        View.OnClickListener backListener = v -> finish();
        btnBack.setOnClickListener(backListener);
        tvBack.setOnClickListener(backListener);
    }
}