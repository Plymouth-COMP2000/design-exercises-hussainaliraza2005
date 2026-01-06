package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GuestForgotPasswordActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "RestaurantAppPrefs";
    private static final String KEY_USER_EMAIL = "USER_EMAIL";
    private static final String KEY_USER_PASSWORD = "USER_PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_forgot_password);

        EditText etEmail = findViewById(R.id.et_forgot_email);
        EditText etNewPassword = findViewById(R.id.et_forgot_password);
        EditText etConfirmPassword = findViewById(R.id.et_forgot_confirm_password);
        Button btnReset = findViewById(R.id.btn_reset_password);
        ImageButton btnBack = findViewById(R.id.btn_back_to_login);
        TextView tvBack = findViewById(R.id.tv_back_to_login);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(GuestForgotPasswordActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(GuestForgotPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String storedEmail = prefs.getString(KEY_USER_EMAIL, null);
                if (storedEmail == null || !storedEmail.equalsIgnoreCase(email)) {
                    Toast.makeText(GuestForgotPasswordActivity.this, "No account found for this email", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_USER_PASSWORD, newPassword);
                editor.apply();

                Toast.makeText(GuestForgotPasswordActivity.this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GuestForgotPasswordActivity.this, GuestLoginActivity.class);
                intent.putExtra("EMAIL_PREFILL", email);
                startActivity(intent);
                finish();
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
