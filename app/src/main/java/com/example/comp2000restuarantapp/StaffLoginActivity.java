package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.os.Bundle;
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

        EditText etUsername = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        ImageButton btnBack = findViewById(R.id.btn_back_to_role);
        TextView tvBack = findViewById(R.id.tv_back_to_role);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            courseworkApi.readUser(username, new CourseworkApi.ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    if (user == null || user.getPassword() == null) {
                        Toast.makeText(StaffLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!password.equals(user.getPassword())) {
                        Toast.makeText(StaffLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!"Staff".equalsIgnoreCase(user.getUserType())) {
                        Toast.makeText(StaffLoginActivity.this, "Access denied: not a staff account", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(StaffLoginActivity.this, StaffDashboardActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(CourseworkApi.ApiError error) {
                    int code = (error != null) ? error.getStatusCode() : -1;
                    String msg = (error != null && error.getMessage() != null) ? error.getMessage() : "Unknown error";
                    Toast.makeText(StaffLoginActivity.this, "Login failed (" + code + "): " + msg, Toast.LENGTH_LONG).show();
                }
            });
        });

        btnBack.setOnClickListener(v -> finish());
        tvBack.setOnClickListener(v -> finish());
    }
}
