package com.example.comp2000restuarantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MenuAdapter(Context context, List<MenuItem> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_food, parent, false);
        return new MenuViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem currentItem = menuList.get(position);

        String detailsText = String.format(Locale.getDefault(), "%s - £%.2f", 
                currentItem.getName(), currentItem.getPrice());
        holder.tvNamePrice.setText(detailsText);
        
        String desc = currentItem.getDescription();
        if (desc == null || desc.isEmpty()) {
             holder.tvDescription.setVisibility(View.GONE);
        } else {
             holder.tvDescription.setText(desc);
             holder.tvDescription.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNamePrice;
        public TextView tvDescription;
        public TextView btnEdit;
        public TextView btnDelete;

        public MenuViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvNamePrice = itemView.findViewById(R.id.tvNamePrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });
        }
    }
}