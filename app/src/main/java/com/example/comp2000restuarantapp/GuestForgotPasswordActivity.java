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

import java.util.Locale;

public class GuestForgotPasswordActivity extends AppCompatActivity {

    private Repository repository;
    private CourseworkApi courseworkApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_forgot_password);

        repository = new Repository(this);
        courseworkApi = new CourseworkApi(this);

        // IMPORTANT: These IDs match your XML layout
        TextInputEditText etEmail = findViewById(R.id.et_forgot_email);
        TextInputEditText etNewPassword = findViewById(R.id.et_new_password);
        TextInputEditText etConfirmPassword = findViewById(R.id.et_confirm_new_password);

        Button btnResetPassword = findViewById(R.id.btn_reset_password);
        ImageButton btnBack = findViewById(R.id.btn_back_to_login);
        TextView tvBack = findViewById(R.id.tv_back_to_login);

        btnResetPassword.setOnClickListener(v -> {
            String emailRaw = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String newPassword = etNewPassword.getText() != null ? etNewPassword.getText().toString().trim() : "";
            String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

            if (emailRaw.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailRaw).matches()) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            String username = emailRaw.toLowerCase(Locale.ROOT);

            // API-first: read -> update password -> update_user
            courseworkApi.readUser(username, new CourseworkApi.ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    if (user == null) {
                        Toast.makeText(GuestForgotPasswordActivity.this, "Account not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    user.setPassword(newPassword);

                    courseworkApi.updateUser(username, user, new CourseworkApi.ApiCallback<User>() {
                        @Override
                        public void onSuccess(User updated) {
                            // Sync local cache for offline continuity
                            if (repository.checkUserExists(username)) {
                                repository.updateUserPassword(username, newPassword);
                            } else {
                                repository.registerUser(username, newPassword);
                            }

                            Toast.makeText(GuestForgotPasswordActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                            goToLogin(username);
                        }

                        @Override
                        public void onError(CourseworkApi.ApiError error) {
                            int code = (error != null) ? error.getStatusCode() : -1;
                            String msg = (error != null && error.getMessage() != null) ? error.getMessage() : "Unknown error";
                            Toast.makeText(GuestForgotPasswordActivity.this,
                                    "Reset failed (" + code + "): " + msg,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onError(CourseworkApi.ApiError error) {
                    // Offline fallback: allow local reset only if user exists in local cache
                    if (repository.checkUserExists(username)) {
                        boolean success = repository.updateUserPassword(username, newPassword);
                        if (success) {
                            Toast.makeText(GuestForgotPasswordActivity.this,
                                    "Offline reset applied (local only). Sync when online.",
                                    Toast.LENGTH_LONG).show();
                            goToLogin(username);
                        } else {
                            Toast.makeText(GuestForgotPasswordActivity.this,
                                    "Offline reset failed (local update error).",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        int code = (error != null) ? error.getStatusCode() : -1;
                        String msg = (error != null && error.getMessage() != null) ? error.getMessage() : "Unknown error";
                        Toast.makeText(GuestForgotPasswordActivity.this,
                                "Cannot reset offline; no local account. (" + code + "): " + msg,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        });

        View.OnClickListener backListener = v -> finish();
        btnBack.setOnClickListener(backListener);
        tvBack.setOnClickListener(backListener);
    }

    private void goToLogin(String emailLower) {
        Intent intent = new Intent(GuestForgotPasswordActivity.this, GuestLoginActivity.class);
        intent.putExtra("EMAIL_PREFILL", emailLower);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
