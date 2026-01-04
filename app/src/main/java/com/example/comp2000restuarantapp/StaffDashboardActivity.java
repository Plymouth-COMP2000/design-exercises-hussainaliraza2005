package com.example.comp2000restuarantapp;
import android.content.Intent; import android.os.Bundle; import android.view.View; import android.widget.Button; import androidx.appcompat.app.AppCompatActivity;

public class StaffDashboardActivity extends AppCompatActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_staff_dashboard);
    Button btnManageMenu = findViewById(R.id.btnManageMenu);
    Button btnViewReservations = findViewById(R.id.btnViewReservations);
    Button btnLogout = findViewById(R.id.btnLogout);
    btnManageMenu.setOnClickListener(v -> 
        startActivity(new Intent(StaffDashboardActivity.this, MenuManagementActivity.class))
    );
    btnViewReservations.setOnClickListener(v -> 
        startActivity(new Intent(StaffDashboardActivity.this, ViewReservationsActivity.class))
    );
    btnLogout.setOnClickListener(v -> {
        Intent intent = new Intent(StaffDashboardActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    });
}
}