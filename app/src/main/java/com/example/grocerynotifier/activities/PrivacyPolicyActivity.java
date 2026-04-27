package com.example.grocerynotifier.activities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerynotifier.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy_activity);

        Button backButton = findViewById(R.id.buttonBackPrivacy);
        backButton.setOnClickListener(v -> finish());
    }
}
