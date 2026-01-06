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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_guest_login);

        EditText etEmail = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView tvRegister = findViewById(R.id.tv_register_link);
        ImageButton btnBack = findViewById(R.id.btn_back_to_role);
        TextView tvBack = findViewById(R.id.tv_back_to_role);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(GuestLoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save email to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("USER_EMAIL", email);
                editor.apply();

                Intent intent = new Intent(GuestLoginActivity.this, GuestMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuestLoginActivity.this, "Registration coming soon", Toast.LENGTH_SHORT).show();
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