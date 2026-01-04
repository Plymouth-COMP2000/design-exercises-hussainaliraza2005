package com.example.comp2000restuarantapp;
import android.os.Bundle; import android.widget.Toast; import androidx.appcompat.app.AppCompatActivity; import androidx.recyclerview.widget.LinearLayoutManager; import androidx.recyclerview.widget.RecyclerView; import java.util.List;

public class ViewReservationsActivity extends AppCompatActivity {

private RecyclerView rvBookings;
private ReservationAdapter adapter;
private Repository repository;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_reservations);
    repository = new Repository(this);
    rvBookings = findViewById(R.id.rvAllBookings);
    rvBookings.setLayoutManager(new LinearLayoutManager(this));
    findViewById(R.id.tvBack).setOnClickListener(v -> finish());
    loadData();
}
private void loadData() {
    List<Reservation> list = repository.getLocalReservations();
    adapter = new ReservationAdapter(this, list);
    
    // Staff can delete/cancel any reservation
    adapter.setOnItemClickListener(position -> {
        Reservation res = list.get(position);
        if (repository.deleteLocalReservation(res.getId())) {
            Toast.makeText(this, "Reservation Removed", Toast.LENGTH_SHORT).show();
            loadData();
        }
    });
    rvBookings.setAdapter(adapter);
}
}