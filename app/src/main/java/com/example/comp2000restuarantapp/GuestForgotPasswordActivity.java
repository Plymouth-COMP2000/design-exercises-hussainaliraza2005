package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class GuestForgotPasswordActivity extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_forgot_password);

        repository = new Repository(this);

        TextInputEditText etEmail = findViewById(R.id.et_forgot_email);
        TextInputEditText etNewPassword = findViewById(R.id.et_new_password);
        TextInputEditText etConfirmPassword = findViewById(R.id.et_confirm_new_password);
        Button btnResetPassword = findViewById(R.id.btn_reset_password);
        ImageButton btnBack = findViewById(R.id.btn_back_to_login);
        TextView tvBack = findViewById(R.id.tv_back_to_login);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(GuestForgotPasswordActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String lowerEmail = email.toLowerCase();

                if (!repository.checkUserExists(lowerEmail)) {
                    Toast.makeText(GuestForgotPasswordActivity.this, "No account found for this email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(GuestForgotPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean success = repository.updateUserPassword(lowerEmail, newPassword);
                if (success) {
                    Toast.makeText(GuestForgotPasswordActivity.this, "Password Reset Successful", Toast.LENGTH_SHORT).show();

                    // Navigate back to Login
                    Intent intent = new Intent(GuestForgotPasswordActivity.this, GuestLoginActivity.class);
                    intent.putExtra("EMAIL_PREFILL", email);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(GuestForgotPasswordActivity.this, "Error resetting password", Toast.LENGTH_SHORT).show();
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