package com.example.comp2000restuarantapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class GuestMenuActivity extends AppCompatActivity {

    private RecyclerView rvMenu;
    private GuestMenuAdapter adapter;
    private Repository repository;
    private List<com.example.comp2000restuarantapp.MenuItem> originalMenuList;
    private TextView tvEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_menu);
        repository = new Repository(this);
        rvMenu = findViewById(R.id.rv_menu_items);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        loadData();
        setupSearch();
        setupBottomNavigation();
    }

    private void loadData() {
        // Assuming repository.getLocalMenuItems() returns List<MenuItem>
        originalMenuList = repository.getLocalMenuItems();
        if (originalMenuList == null) originalMenuList = new ArrayList<>();
        adapter = new GuestMenuAdapter(this, originalMenuList);
        rvMenu.setAdapter(adapter);
    }

    private void setupSearch() {
        TextInputEditText etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filter(String text) {
        List<com.example.comp2000restuarantapp.MenuItem> filteredList = new ArrayList<>();
        if (originalMenuList != null) {
            for (com.example.comp2000restuarantapp.MenuItem item : originalMenuList) {
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        if (adapter != null) {
            adapter.updateList(filteredList);
        }

        if (filteredList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvMenu.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvMenu.setVisibility(View.VISIBLE);
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_menu);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_menu) return true;
            if (id == R.id.nav_bookings) {
                startActivity(new Intent(this, MyBookingsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, GuestProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_menu);
        }
    }
}