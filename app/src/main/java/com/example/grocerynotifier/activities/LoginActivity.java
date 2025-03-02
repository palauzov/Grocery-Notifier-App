package com.example.grocerynotifier.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.grocerynotifier.R;
import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.daos.UserDao;
import com.example.grocerynotifier.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;
    private AppDatabase db;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextTextEmailAddress2);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        btnLogin = findViewById(R.id.buttonLogin);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "users").build();
        userDao = db.userDao();

        btnLogin.setOnClickListener(this::loginUser);
    }

    private void loginUser(View view) {
        String email = editTextEmail.getText().toString().trim();
        String passwordInput = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || passwordInput.isEmpty()) {
            showToast("Please fill in both email and password.");
            return;
        }

        new Thread(() -> {
            try {
                User user = userDao.getUserByEmail(email);

                if (user == null || !user.getPassword().equals(passwordInput)) {
                    runOnUiThread(() -> showToast("Invalid email or password."));
                    return;
                }

                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    sharedPref.edit().putBoolean("isLoggedIn", true).apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });

            } catch (Exception e) {
                runOnUiThread(() -> showToast("An error occurred: " + e.getMessage()));
            }
        }).start();
    }


    public void toRegisterWindow(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void forgotPasswordProcess(View view) {
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
