package com.example.comp2000restuarantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<Reservation> reservationList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onCancelClick(int position);
        void onViewDetailsClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ReservationAdapter(Context context, List<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation currentReservation = reservationList.get(position);

        holder.tvGuestName.setText(currentReservation.getGuestName());
        String details = currentReservation.getTime() + " • " + currentReservation.getTableNumber();
        holder.tvDetails.setText(details);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        public TextView tvGuestName;
        public TextView tvDetails;
        public TextView btnViewDetails;
        public TextView btnCancel;

        public ReservationViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvGuestName = itemView.findViewById(R.id.tvGuestName);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnCancel = itemView.findViewById(R.id.btnCancel);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCancelClick(position);
                        }
                    }
                }
            });

            btnViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onViewDetailsClick(position);
                        }
                    }
                }
            });
        }
    }
}