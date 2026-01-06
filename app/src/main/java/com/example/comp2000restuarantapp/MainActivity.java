package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_role_selection);

        MaterialButton btnGuest = findViewById(R.id.btn_continue_as_guest);
        MaterialButton btnStaff = findViewById(R.id.btn_continue_as_staff);

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuestLoginActivity.class);
                startActivity(intent);
            }
        });

        btnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StaffLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}