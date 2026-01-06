package com.example.comp2000restuarantapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuManagementActivity extends AppCompatActivity {

    private RecyclerView rvMenu;
    private MenuAdapter adapter;
    private Repository repository;
    private List<MenuItem> menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_management);

        repository = new Repository(this);
        rvMenu = findViewById(R.id.rvMenu);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));

        Button btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEditDialog(null);
            }
        });

        TextView tvBack = findViewById(R.id.tvBack);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to the previous activity
            }
        });

        loadData();
    }

    private void loadData() {
        menuList = repository.getLocalMenuItems();
        adapter = new MenuAdapter(this, menuList);
        
        adapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                MenuItem item = menuList.get(position);
                boolean deleted = repository.deleteLocalMenuItem(item.getId());
                if (deleted) {
                    Toast.makeText(MenuManagementActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                    loadData(); // Refresh list
                } else {
                    Toast.makeText(MenuManagementActivity.this, "Error deleting item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEditClick(int position) {
                MenuItem item = menuList.get(position);
                showAddEditDialog(item);
            }
        });

        rvMenu.setAdapter(adapter);
    }

    private void showAddEditDialog(final MenuItem itemToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(itemToEdit == null ? "Add New Item" : "Edit Item");

        // Set up the input
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText inputName = new EditText(this);
        inputName.setHint("Item Name");
        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(inputName);

        // --- NEW: Description Input ---
        final EditText inputDesc = new EditText(this);
        inputDesc.setHint("Description");
        inputDesc.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        layout.addView(inputDesc);
        // -----------------------------

        final EditText inputPrice = new EditText(this);
        inputPrice.setHint("Price (e.g. 10.00)");
        inputPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(inputPrice);

        if (itemToEdit != null) {
            inputName.setText(itemToEdit.getName());
            inputDesc.setText(itemToEdit.getDescription()); // Pre-fill description
            inputPrice.setText(String.valueOf(itemToEdit.getPrice()));
        }

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = inputName.getText().toString().trim();
                String description = inputDesc.getText().toString().trim(); // Get description
                String priceStr = inputPrice.getText().toString().trim();

                if (name.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(MenuManagementActivity.this, "Please fill Name and Price fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double price = Double.parseDouble(priceStr);
                    boolean success;
                    
                    if (itemToEdit == null) {
                         // Pass description to constructor
                         MenuItem newItem = new MenuItem(name, price, description);
                         success = repository.addLocalMenuItem(newItem);
                         if (success) {
                            Toast.makeText(MenuManagementActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MenuManagementActivity.this, "Error adding item", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        itemToEdit.setName(name);
                        itemToEdit.setPrice(price);
                        itemToEdit.setDescription(description); // Update description
                        success = repository.updateLocalMenuItem(itemToEdit);
                        if (success) {
                            Toast.makeText(MenuManagementActivity.this, "Item updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MenuManagementActivity.this, "Error updating item", Toast.LENGTH_SHORT).show();
                        }
                    }
                    
                    if (success) {
                        loadData();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MenuManagementActivity.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}