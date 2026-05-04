package com.example.grocerynotifier.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.grocerynotifier.R;
import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.model.Product;
import java.util.List;
import android.content.SharedPreferences;
import androidx.core.app.NotificationManagerCompat;
import java.util.concurrent.TimeUnit;

public class ExpiryWorker extends Worker {

    public ExpiryWorker(Context context, WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {

        Context context = getApplicationContext();
        AppDatabase db = AppDatabase.getInstance(context);

        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String accountId = prefs.getString("accountId", null);

        if (accountId == null) return Result.success();

        // GET PRODUCTS
        List<Product> products = db.productDao().getProductsByAccount(accountId);

        int count = 0;
        long now = System.currentTimeMillis();

        for (Product p : products) {
            long diff = p.getExpirationDate().getTime() - now;
            long days = TimeUnit.MILLISECONDS.toDays(diff);

            if (days <= 2 && days >= 0) {
                count++;
            }
        }

        if (count > 0) {
            showNotification(context, count);
        }

        return Result.success();
    }

    private void showNotification(Context context, int count) {

        String channelId = "expiry_channel";

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create channel (required for Android 8+)
        NotificationChannel channel = new NotificationChannel(
                channelId,
                "Expiry Alerts",
                NotificationManager.IMPORTANCE_HIGH
        );
        manager.createNotificationChannel(channel);

        String message = "You have " + count +
                " products expiring in 2 days 🍎⚠️ Eat them before it's too late!";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Grocery Reminder 🛒")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat.from(context).notify(1, builder.build());
    }
}
