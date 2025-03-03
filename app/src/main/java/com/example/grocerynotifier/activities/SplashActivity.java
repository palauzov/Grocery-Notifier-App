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
        boolean isLoggedIn = sharedPref.getBoolean("isLoggedIn", false);

        if (isFirstRun) {
            sharedPref.edit().putBoolean("isFirstRun", false).apply();
            startActivity(new Intent(this, RegisterActivity.class));
        } else {
            if (isLoggedIn) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        }

        finish();
    }
}
