package com.example.comp2000restuarantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
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
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(BookTableActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etDate.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth));
                            }
                        }, year, month, day);
                
                // Set minimum date to today
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                
                datePickerDialog.show();
            }
        });

        // Time Picker
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(BookTableActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                etTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = etDate.getText().toString();
                String time = etTime.getText().toString();
                String guestsStr = etGuests.getText().toString();

                if (date.isEmpty() || time.isEmpty() || guestsStr.isEmpty()) {
                    Toast.makeText(BookTableActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Retrieve user email from SharedPreferences
                SharedPreferences prefs = getSharedPreferences("RestaurantAppPrefs", MODE_PRIVATE);
                String userEmail = prefs.getString("USER_EMAIL", "Guest");

                String dateTime = date + " " + time;
                Reservation reservation = new Reservation(userEmail, dateTime, "Table 1 (for " + guestsStr + ")");
                
                boolean success = repository.addLocalReservation(reservation);
                if (success) {
                    Toast.makeText(BookTableActivity.this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                    
                    // Trigger notification
                    NotificationHelper.show(BookTableActivity.this, "Booking Confirmed", 
                            "Your table for " + guestsStr + " guests at " + time + " on " + date + " is reserved.", 1001);

                    finish();
                } else {
                    Toast.makeText(BookTableActivity.this, "Error making booking", Toast.LENGTH_SHORT).show();
                }
            }
        });

        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        btnBack.setOnClickListener(backListener);
        tvBack.setOnClickListener(backListener);
    }
}