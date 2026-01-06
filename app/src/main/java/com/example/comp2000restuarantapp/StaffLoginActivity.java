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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        EditText etUsername = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        ImageButton btnBack = findViewById(R.id.btn_back_to_role);
        TextView tvBack = findViewById(R.id.tv_back_to_role);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if ("admin".equals(username) && "password".equals(password)) {
                    Intent intent = new Intent(StaffLoginActivity.this, StaffDashboardActivity.class);
                    startActivity(intent);
                    finish(); // Optional: prevent going back to login
                } else {
                    Toast.makeText(StaffLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
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