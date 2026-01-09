package com.example.comp2000restuarantapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewReservationsActivity extends AppCompatActivity {

    private RecyclerView rvReservations;
    private ReservationAdapter adapter;
    private Repository repository;
    private List<Reservation> reservationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservations);

        repository = new Repository(this);
        rvReservations = findViewById(R.id.rvReservations);
        rvReservations.setLayoutManager(new LinearLayoutManager(this));

        TextView tvBack = findViewById(R.id.tvBack);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back
            }
        });

        loadData();
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
                    Toast.makeText(ViewReservationsActivity.this, "Reservation cancelled", Toast.LENGTH_SHORT).show();
                    loadData(); // Refresh list
                } else {
                    Toast.makeText(ViewReservationsActivity.this, "Error cancelling reservation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onViewDetailsClick(int position) {
                Reservation reservation = reservationList.get(position);
                String details = "Guest: " + reservation.getGuestName() + "\n" +
                                 "Time: " + reservation.getTime() + "\n" +
                                 "Table: " + reservation.getTableNumber();
                
                Toast.makeText(ViewReservationsActivity.this, details, Toast.LENGTH_LONG).show();
            }
        });

        rvReservations.setAdapter(adapter);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}