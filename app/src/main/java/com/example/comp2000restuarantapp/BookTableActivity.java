package com.example.comp2000restuarantapp;
import android.app.DatePickerDialog; import android.app.TimePickerDialog; import android.content.SharedPreferences; import android.os.Bundle; import android.widget.Button; import android.widget.Toast; import androidx.appcompat.app.AppCompatActivity; import com.google.android.material.textfield.TextInputEditText; import java.util.Calendar; import java.util.Locale;

public class BookTableActivity extends AppCompatActivity {

private Repository repository;
private TextInputEditText etDate, etTime, etGuests;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.e_booking_table);
    repository = new Repository(this);
    etDate = findViewById(R.id.et_date);
    etTime = findViewById(R.id.et_time);
    etGuests = findViewById(R.id.et_guests);
    
    findViewById(R.id.btn_back_to_bookings).setOnClickListener(v -> finish());
    // Date Picker with "Smart Validation" (No past dates)
    etDate.setOnClickListener(v -> {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this,
                (view, year, month, day) -> 
                    etDate.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, day)),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        
        // DISABLE PAST DATES
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.show();
    });
    // Time Picker
    etTime.setOnClickListener(v -> {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this,
                (view, hour, minute) -> 
                    etTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute)),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    });
    findViewById(R.id.btn_confirm_booking).setOnClickListener(v -> {
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String guests = etGuests.getText().toString();
        if (date.isEmpty() || time.isEmpty() || guests.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences prefs = getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE);
        String userEmail = prefs.getString("USER_EMAIL", "Guest");
        String dateTime = date + " " + time;
        
        Reservation res = new Reservation(userEmail, dateTime, "Table for " + guests);
        
        if (repository.addLocalReservation(res)) {
            Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error making booking", Toast.LENGTH_SHORT).show();
        }
    });
}
}