package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {

    private RecyclerView rvBookings;
    private ReservationAdapter adapter;
    private Repository repository;
    private List<Reservation> reservationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_bookings);

        repository = new Repository(this);
        rvBookings = findViewById(R.id.rv_bookings_list);
        rvBookings.setLayoutManager(new LinearLayoutManager(this));

        Button btnBookTable = findViewById(R.id.btn_book_new_table);
        btnBookTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyBookingsActivity.this, BookTableActivity.class);
                startActivity(intent);
            }
        });

        loadData();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_bookings);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_menu) {
                    Intent intent = new Intent(MyBookingsActivity.this, GuestMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(0, 0); // No animation
                    return true;
                } else if (itemId == R.id.nav_bookings) {
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(MyBookingsActivity.this, GuestProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(0, 0); // No animation
                    return true;
                }
                return false;
            }
        });
    }

    private void loadData() {
        reservationList = repository.getLocalReservations();
        adapter = new ReservationAdapter(this, reservationList);

        adapter.setOnItemClickListener(new ReservationAdapter.OnItemClickListener() {
            @Override
            public void onCancelClick(int position) {
                Reservation reservation = reservationList.get(position);
                boolean deleted = repository.deleteLocalReservation(reservation.getId());
                
                if (deleted) {
                    Toast.makeText(MyBookingsActivity.this, "Booking Cancelled", Toast.LENGTH_SHORT).show();
                    loadData(); // Refresh list
                } else {
                    Toast.makeText(MyBookingsActivity.this, "Error cancelling booking", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onViewDetailsClick(int position) {
                Reservation reservation = reservationList.get(position);
                String details = "Time: " + reservation.getTime() + "\n" +
                                 "Table: " + reservation.getTableNumber();
                Toast.makeText(MyBookingsActivity.this, details, Toast.LENGTH_LONG).show();
            }
        });

        rvBookings.setAdapter(adapter);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_bookings);
    }
}