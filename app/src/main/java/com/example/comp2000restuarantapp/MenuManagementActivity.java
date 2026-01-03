package com.example.comp2000restuarantapp;
import android.content.DialogInterface; import android.os.Bundle; import android.text.InputType; import android.view.View; import android.widget.Button; import android.widget.EditText; import android.widget.LinearLayout; import android.widget.TextView; import android.widget.Toast;

import androidx.appcompat.app.AlertDialog; import androidx.appcompat.app.AppCompatActivity; import androidx.recyclerview.widget.LinearLayoutManager; import androidx.recyclerview.widget.RecyclerView;

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
    btnAddItem.setOnClickListener(v -> showAddEditDialog(null));
    TextView tvBack = findViewById(R.id.tvBack);
    tvBack.setOnClickListener(v -> finish());
    loadData();
}
private void loadData() {
    menuList = repository.getLocalMenuItems();
    adapter = new MenuAdapter(this, menuList);
    
    adapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
        @Override
        public void onDeleteClick(int position) {
            MenuItem item = menuList.get(position);
            if (repository.deleteLocalMenuItem(item.getId())) {
                Toast.makeText(MenuManagementActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                loadData();
            }
        }
        @Override
        public void onEditClick(int position) {
            showAddEditDialog(menuList.get(position));
        }
    });
    rvMenu.setAdapter(adapter);
}
private void showAddEditDialog(final MenuItem itemToEdit) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(itemToEdit == null ? "Add New Item" : "Edit Item");
    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setPadding(50, 40, 50, 10);
    final EditText inputName = new EditText(this);
    inputName.setHint("Item Name");
    layout.addView(inputName);
    final EditText inputDesc = new EditText(this);
    inputDesc.setHint("Description");
    layout.addView(inputDesc);
    final EditText inputPrice = new EditText(this);
    inputPrice.setHint("Price (e.g. 10.00)");
    inputPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    layout.addView(inputPrice);
    if (itemToEdit != null) {
        inputName.setText(itemToEdit.getName());
        inputDesc.setText(itemToEdit.getDescription());
        inputPrice.setText(String.valueOf(itemToEdit.getPrice()));
    }
    builder.setView(layout);
    builder.setPositiveButton("Save", (dialog, which) -> {
        String name = inputName.getText().toString().trim();
        String desc = inputDesc.getText().toString().trim();
        String priceStr = inputPrice.getText().toString().trim();
        if (name.isEmpty() || priceStr.isEmpty()) return;
        try {
            double price = Double.parseDouble(priceStr);
            if (itemToEdit == null) {
                repository.addLocalMenuItem(new MenuItem(name, price, desc));
            } else {
                itemToEdit.setName(name);
                itemToEdit.setPrice(price);
                itemToEdit.setDescription(desc);
                repository.updateLocalMenuItem(itemToEdit);
            }
            loadData();
        } catch (NumberFormatException e) {
            Toast.makeText(MenuManagementActivity.this, "Invalid price", Toast.LENGTH_SHORT).show();
        }
    });
    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
    builder.show();
}
}