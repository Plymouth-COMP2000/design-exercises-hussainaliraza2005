package com.example.comp2000restuarantapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
public class StaffLoginActivity extends AppCompatActivity { @Override protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_staff_login);

    EditText etEmail = findViewById(R.id.et_email);
    EditText etPass = findViewById(R.id.et_password);
    
    findViewById(R.id.btn_login).setOnClickListener(v -> {
        String user = etEmail.getText().toString();
        String pass = etPass.getText().toString();
        
        // Hardcoded Auth for Prototype
        if(user.equals("admin") && pass.equals("password")) {
            startActivity(new Intent(this, StaffDashboardActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    });
    
    findViewById(R.id.btn_back_to_role).setOnClickListener(v -> finish());
}
}