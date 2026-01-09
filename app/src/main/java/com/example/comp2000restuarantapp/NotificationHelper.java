package com.example.comp2000restuarantapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "booking_channel";
    private static final String CHANNEL_NAME = "Booking Notifications";
    private static final String CHANNEL_DESC = "Notifications for booking confirmations and updates";

    /**
     * Creates the notification channel on API 26+ devices.
     * This is safe to call multiple times; the system will ignore it if the channel already exists.
     * @param context The application context.
     */
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Shows a notification after checking both user preference and system permissions.
     * @param context The context from which the notification is being sent.
     * @param title The title of the notification.
     * @param message The main content/body of the notification.
     * @param notificationId A unique ID for the notification.
     */
    public static void show(Context context, String title, String message, int notificationId) {
        // 1. Check if user has enabled notifications in the app's settings
        SharedPreferences prefs = context.getSharedPreferences("RestaurantAppPrefs", Context.MODE_PRIVATE);
        boolean isEnabled = prefs.getBoolean("NOTIFICATIONS_ENABLED", false);
        if (!isEnabled) {
            return; // Exit if notifications are disabled by the user
        }

        // 2. On Android 13+, check if the system permission has been granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) 
                    != PackageManager.PERMISSION_GRANTED) {
                // If permission is not granted, we cannot show the notification.
                // The permission request logic is handled in the UI (GuestProfileActivity).
                return;
            }
        }
        
        // Ensure the channel is created before attempting to show a notification
        createNotificationChannel(context);

        // 3. Build and display the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_calendar) // Use an existing relevant icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Dismiss notification when tapped

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}
