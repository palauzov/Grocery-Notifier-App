package com.example.grocerynotifier.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isFirstRun = sharedPref.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            // First run - Show Register screen
            sharedPref.edit().putBoolean("isFirstRun", false).apply();
            startActivity(new Intent(this, RegisterActivity.class));
        } else {
            // Not the first run - Show Main Activity
            startActivity(new Intent(this, MainActivity.class));
        }

        // Close the Splash screen activity
        finish();
    }
}
