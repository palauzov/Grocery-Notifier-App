package com.example.grocerynotifier.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.grocerynotifier.R;
import com.example.grocerynotifier.converters.ValidatorUtils;
import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.daos.UserDao;
import com.example.grocerynotifier.model.User;

import java.util.concurrent.ExecutionException;

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
        try {
            String email = ValidatorUtils.validateEmail(editTextEmail.getText().toString(), userDao);
            String password = ValidatorUtils.validatePassword(editTextPassword.getText().toString());

            new Thread(() -> {
                User user = userDao.getUserByEmail(email);

                if (user != null && user.getPassword().equals(password)) {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show());
                }
            }).start();

        } catch (IllegalArgumentException e) {
            showToast(e.getMessage());
        }
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
