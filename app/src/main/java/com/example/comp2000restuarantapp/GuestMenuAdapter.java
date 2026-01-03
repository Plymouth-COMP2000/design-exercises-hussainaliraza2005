package com.example.comp2000restuarantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class GuestMenuAdapter extends RecyclerView.Adapter<GuestMenuAdapter.GuestMenuViewHolder> {

    private List<MenuItem> menuList;
    private Context context;

    public GuestMenuAdapter(Context context, List<MenuItem> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    public void updateList(List<MenuItem> newList) {
        this.menuList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GuestMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_guest, parent, false);
        return new GuestMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestMenuViewHolder holder, int position) {
        MenuItem currentItem = menuList.get(position);
        holder.tvItemName.setText(currentItem.getName());
        holder.tvItemPrice.setText(String.format(Locale.getDefault(), "£%.2f", currentItem.getPrice()));

        String desc = currentItem.getDescription();
        if (desc == null || desc.isEmpty()) {
            desc = "No description available.";
        }
        holder.tvItemDescription.setText(desc);
        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, currentItem.getName(), Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class GuestMenuViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName, tvItemPrice, tvItemDescription;

        public GuestMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvItemDescription = itemView.findViewById(R.id.tvItemDescription);
        }
    }
}