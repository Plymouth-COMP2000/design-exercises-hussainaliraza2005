package com.example.comp2000restuarantapp;
import android.content.Intent; import android.content.SharedPreferences; import android.os.Bundle; import android.widget.TextView; import androidx.appcompat.app.AppCompatActivity; import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GuestProfileActivity extends AppCompatActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.f_profile);
    SharedPreferences prefs = getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE);
    String email = prefs.getString("USER_EMAIL", "Guest");
    
    TextView tvEmail = findViewById(R.id.tv_email_display);
    tvEmail.setText(email);
    findViewById(R.id.btn_logout).setOnClickListener(v -> {
        // Clear Session
        prefs.edit().clear().apply();
        
        // Return to Role Selection
        Intent intent = new Intent(GuestProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    });
    setupBottomNavigation();
}
private void setupBottomNavigation() {
    BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
    bottomNav.setSelectedItemId(R.id.nav_profile);
    bottomNav.setOnItemSelectedListener(item -> {
        int id = item.getItemId();
        if (id == R.id.nav_profile) return true;
        if (id == R.id.nav_menu) {
            startActivity(new Intent(this, GuestMenuActivity.class));
            overridePendingTransition(0,0);
            return true;
        }
        if (id == R.id.nav_bookings) {
            startActivity(new Intent(this, MyBookingsActivity.class));
            overridePendingTransition(0,0);
            return true;
        }
        return false;
    });
}
@Override protected void onResume() { super.onResume(); BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation); bottomNav.setSelectedItemId(R.id.nav_profile); }


}