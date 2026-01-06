package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class GuestProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_profile);

        SharedPreferences prefs = getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE);
        String userEmail = prefs.getString("USER_EMAIL", "Guest");

        TextView tvUserEmail = findViewById(R.id.tv_user_email);
        if (tvUserEmail != null) {
            tvUserEmail.setText("User: " + userEmail);
        }

        Button btnLogout = findViewById(R.id.btn_logout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Clear SharedPreferences
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(GuestProfileActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }
        
        // Handle other buttons if they exist in the layout, e.g. manage notifications
        // Note: The switch is named switch_notifications in XML, not btn_notifications
        View switchNotifications = findViewById(R.id.switch_notifications);
        if (switchNotifications != null) {
            switchNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(GuestProfileActivity.this, "Notifications settings coming soon", Toast.LENGTH_SHORT).show();
                }
            });
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_profile);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_menu) {
                    Intent intent = new Intent(GuestProfileActivity.this, GuestMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.nav_bookings) {
                    Intent intent = new Intent(GuestProfileActivity.this, MyBookingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    return true;
                }
                return false;
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_profile);
    }
}