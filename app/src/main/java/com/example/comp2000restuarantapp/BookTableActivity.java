package com.example.comp2000restuarantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

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
        Button btnConfirm = findViewById(R.id.btn_confirm_booking);
        ImageButton btnBack = findViewById(R.id.btn_back_to_bookings);
        TextView tvBack = findViewById(R.id.tv_back_to_bookings);

        // Date Picker
        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(BookTableActivity.this,
                    (view, year1, month1, dayOfMonth) -> etDate.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year1, month1 + 1, dayOfMonth)), year, month, day);
            
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        // Time Picker
        etTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(BookTableActivity.this,
                    (view, hourOfDay, minute1) -> etTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1)), hour, minute, true);
            timePickerDialog.show();
        });

        btnConfirm.setOnClickListener(v -> {
            String date = etDate.getText().toString();
            String time = etTime.getText().toString();
            String guestsStr = etGuests.getText().toString();

            if (date.isEmpty() || time.isEmpty() || guestsStr.isEmpty()) {
                Toast.makeText(BookTableActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE);
            String userEmail = prefs.getString("USER_EMAIL", "Guest");

            String dateTime = date + " " + time;
            Reservation reservation = new Reservation(userEmail, dateTime, "Table for " + guestsStr);
            
            boolean success = repository.addLocalReservation(reservation);
            if (success) {
                Toast.makeText(BookTableActivity.this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                
                // TRIGGER NOTIFICATION
                NotificationHelper.show(BookTableActivity.this, "Booking Confirmed", 
                        "Your table for " + guestsStr + " at " + time + " on " + date + " is reserved.", 
                        (int) System.currentTimeMillis()); // Unique ID for notification

                finish();
            } else {
                Toast.makeText(BookTableActivity.this, "Error making booking", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
        tvBack.setOnClickListener(v -> finish());
    }
}