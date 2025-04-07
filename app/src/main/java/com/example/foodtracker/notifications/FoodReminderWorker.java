package com.example.foodtracker.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.foodtracker.R;

public class FoodReminderWorker extends Worker {

    public static final String KEY_FOOD_NAME = "food_name";
    public static final String KEY_EXPIRATION_DATE = "expiration_date";

    public FoodReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Get input data
        String foodName = getInputData().getString(KEY_FOOD_NAME);
        String expirationDate = getInputData().getString(KEY_EXPIRATION_DATE);

        // Show the notification
        showNotification(foodName, expirationDate);

        return Result.success();
    }

    private void showNotification(String foodName, String expirationDate) {
        String channelId = "food_reminder_channel";
        String channelName = "Food Reminder Notifications";

        // Create notification channel if necessary (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_notification) // Replace with your own icon
                .setContentTitle("Expiring Soon: " + foodName)
                .setContentText(foodName + " expires on " + expirationDate)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(foodName.hashCode(), builder.build()); // Unique ID based on name
        }
    }
}
