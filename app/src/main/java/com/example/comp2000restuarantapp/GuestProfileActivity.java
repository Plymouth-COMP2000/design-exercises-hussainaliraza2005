package com.example.comp2000restuarantapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.navigation.NavigationBarView;

public class GuestProfileActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private MaterialSwitch switchNotifications;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_profile);

        prefs = getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE);
        String userEmail = prefs.getString("USER_EMAIL", "Guest");

        TextView tvUserEmail = findViewById(R.id.tv_user_email);
        if (tvUserEmail != null) {
            tvUserEmail.setText("User: " + userEmail);
        }

        // Initialize Permission Launcher for Android 13+
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // Permission granted, keep switch ON and save preference
                prefs.edit().putBoolean("NOTIFICATIONS_ENABLED", true).apply();
                Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, turn switch OFF
                switchNotifications.setChecked(false);
                prefs.edit().putBoolean("NOTIFICATIONS_ENABLED", false).apply();
                Toast.makeText(this, "Permission denied. Notifications disabled.", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup Notification Switch
        View viewSwitch = findViewById(R.id.switch_notifications);
        if (viewSwitch instanceof MaterialSwitch) {
            switchNotifications = (MaterialSwitch) viewSwitch;
            
            // Set initial state
            boolean isEnabled = prefs.getBoolean("NOTIFICATIONS_ENABLED", false);
            switchNotifications.setChecked(isEnabled);

            switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // User turned ON
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ContextCompat.checkSelfPermission(GuestProfileActivity.this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                                // Permission already granted
                                prefs.edit().putBoolean("NOTIFICATIONS_ENABLED", true).apply();
                            } else {
                                // Request permission
                                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                            }
                        } else {
                            // Pre-Android 13, no runtime permission needed
                            prefs.edit().putBoolean("NOTIFICATIONS_ENABLED", true).apply();
                        }
                    } else {
                        // User turned OFF
                        prefs.edit().putBoolean("NOTIFICATIONS_ENABLED", false).apply();
                    }
                }
            });
        }

        Button btnLogout = findViewById(R.id.btn_logout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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