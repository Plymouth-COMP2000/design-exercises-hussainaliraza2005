package com.example.comp2000restuarantapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
public class GuestLoginActivity extends AppCompatActivity { @Override protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.b_guest_login);

    EditText etEmail = findViewById(R.id.et_email);
    EditText etPass = findViewById(R.id.et_password);
    
    findViewById(R.id.btn_login).setOnClickListener(v -> {
        String email = etEmail.getText().toString();
        if(email.isEmpty()) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        // Save Session
        SharedPreferences prefs = getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE);
        prefs.edit().putString("USER_EMAIL", email).apply();
        
        // Navigate
        startActivity(new Intent(this, GuestMenuActivity.class));
        finish();
    });
    
    findViewById(R.id.btn_back_to_role).setOnClickListener(v -> finish());
}
}