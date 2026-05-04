package com.example.grocerynotifier.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.example.grocerynotifier.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.grocerynotifier.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FloatingActionButton fab;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isFirstRun) {
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply();
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
            return;
        }

        if (!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        scheduleDailyNotification();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fab = findViewById(R.id.fab_button);

        setupNavigation();
        setupFab();
    }

    // ✅ FIXED NAVIGATION
    private void setupNavigation() {

        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_activity_main);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_clients_list,
                R.id.navigation_add_product,
                R.id.navigation_shopping_list,
                R.id.navigation_profile
        ).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    // OPTIONAL: toolbar back button support
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private void setupFab() {
        fab.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setPrompt("Scan a barcode");
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(true);
            integrator.initiateScan();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null && result.getContents() != null) {

            String barcode = result.getContents();
            Toast.makeText(this, "Scanned: " + barcode, Toast.LENGTH_LONG).show();

            Bundle bundle = new Bundle();
            bundle.putString("barcode", barcode);

            navController.navigate(R.id.navigation_add_product, bundle);
        } else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleDailyNotification() {

        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();

        target.set(Calendar.HOUR_OF_DAY, 9);
        target.set(Calendar.MINUTE, 0);
        target.set(Calendar.SECOND, 0);

        if (target.before(now)) {
            target.add(Calendar.DAY_OF_YEAR, 1);
        }

        long delay = target.getTimeInMillis() - now.getTimeInMillis();

        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(
                        com.example.grocerynotifier.workers.ExpiryWorker.class,
                        24, TimeUnit.HOURS
                )
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .build();

        WorkManager.getInstance(this).enqueue(request);
    }
}