package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class GuestMenuActivity extends AppCompatActivity {

    private RecyclerView rvMenu;
    private GuestMenuAdapter adapter;
    private Repository repository;
    private CourseworkApi courseworkApi;
    private List<com.example.comp2000restuarantapp.MenuItem> originalMenuList;

    // Loading UI
    private ProgressBar pbLoading;
    private TextView tvStatusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_menu);

        repository = new Repository(this);
        courseworkApi = new CourseworkApi(this);

        rvMenu = findViewById(R.id.rv_menu_items);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Loading Views
        pbLoading = findViewById(R.id.pb_loading);
        tvStatusMessage = findViewById(R.id.tv_status_message);

        EditText etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMenu(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_menu);
        
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_menu) {
                    return true;
                } else if (itemId == R.id.nav_bookings) {
                    Intent intent = new Intent(GuestMenuActivity.this, MyBookingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(0, 0); // No animation
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(GuestMenuActivity.this, GuestProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(0, 0); // No animation
                    return true;
                }
                return false;
            }
        });

        loadData();
    }

    private void loadData() {
        // Show loading state on Main Thread
        showLoading(true);
        tvStatusMessage.setText("Loading menu...");

        originalMenuList = repository.getLocalMenuItems();
        adapter = new GuestMenuAdapter(this, new ArrayList<>(originalMenuList));
        rvMenu.setAdapter(adapter);
        
        // Hide loading
        showLoading(false);
    }
    
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            pbLoading.setVisibility(View.VISIBLE);
            tvStatusMessage.setVisibility(View.VISIBLE);
            rvMenu.setVisibility(View.GONE);
        } else {
            pbLoading.setVisibility(View.GONE);
            tvStatusMessage.setVisibility(View.GONE);
            rvMenu.setVisibility(View.VISIBLE);
        }
    }
    
    private void filterMenu(String text) {
        if (originalMenuList == null) return;

        List<com.example.comp2000restuarantapp.MenuItem> filteredList = new ArrayList<>();
        
        for (com.example.comp2000restuarantapp.MenuItem item : originalMenuList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        
        if (adapter != null) {
            adapter.updateList(filteredList);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Reload data to catch any updates from staff side
        loadData();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_menu);
    }
}