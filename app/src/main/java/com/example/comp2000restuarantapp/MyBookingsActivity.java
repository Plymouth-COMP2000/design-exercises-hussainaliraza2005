package com.example.comp2000restuarantapp;
import android.content.Intent; import android.content.SharedPreferences; import android.os.Bundle; import android.view.View; import android.widget.TextView; import android.widget.Toast; import androidx.appcompat.app.AppCompatActivity; import androidx.recyclerview.widget.LinearLayoutManager; import androidx.recyclerview.widget.RecyclerView; import com.google.android.material.bottomnavigation.BottomNavigationView; import java.util.ArrayList; import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {

private RecyclerView rvBookings;
private ReservationAdapter adapter;
private Repository repository;
private TextView tvEmpty;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.d_my_bookings);
    repository = new Repository(this);
    rvBookings = findViewById(R.id.rv_bookings);
    tvEmpty = findViewById(R.id.tvEmpty);
    rvBookings.setLayoutManager(new LinearLayoutManager(this));
    findViewById(R.id.btn_new_booking).setOnClickListener(v -> 
        startActivity(new Intent(MyBookingsActivity.this, BookTableActivity.class))
    );
    setupBottomNavigation();
}
private void loadBookings() {
    SharedPreferences prefs = getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE);
    String currentUser = prefs.getString("USER_EMAIL", "");
    List<Reservation> allReservations = repository.getLocalReservations();
    List<Reservation> myReservations = new ArrayList<>();
    // Filter: Only show bookings for this user
    for (Reservation r : allReservations) {
        if (r.getGuestName().equalsIgnoreCase(currentUser)) {
            myReservations.add(r);
        }
    }
    if (myReservations.isEmpty()) {
        tvEmpty.setVisibility(View.VISIBLE);
        rvBookings.setVisibility(View.GONE);
    } else {
        tvEmpty.setVisibility(View.GONE);
        rvBookings.setVisibility(View.VISIBLE);
    }
    adapter = new ReservationAdapter(this, myReservations);
    adapter.setOnItemClickListener(position -> {
        Reservation res = myReservations.get(position);
        if (repository.deleteLocalReservation(res.getId())) {
            Toast.makeText(this, "Booking Cancelled", Toast.LENGTH_SHORT).show();
            loadBookings(); // Refresh
        }
    });
    rvBookings.setAdapter(adapter);
}
private void setupBottomNavigation() {
    BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
    bottomNav.setSelectedItemId(R.id.nav_bookings);
    bottomNav.setOnItemSelectedListener(item -> {
        int id = item.getItemId();
        if (id == R.id.nav_bookings) return true;
        if (id == R.id.nav_menu) {
            startActivity(new Intent(this, GuestMenuActivity.class));
            overridePendingTransition(0,0);
            return true;
        }
        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, GuestProfileActivity.class));
            overridePendingTransition(0,0);
            return true;
        }
        return false;
    });
}
@Override
protected void onResume() {
    super.onResume();
    loadBookings();
    BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
    bottomNav.setSelectedItemId(R.id.nav_bookings);
}
}