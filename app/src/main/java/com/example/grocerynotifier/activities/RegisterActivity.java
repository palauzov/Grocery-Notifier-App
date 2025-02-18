package com.example.grocerynotifier.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.grocerynotifier.R;
import com.example.grocerynotifier.converters.DateFormatter;
import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.daos.UserDao;
import com.example.grocerynotifier.model.Account;
import com.example.grocerynotifier.model.User;

import java.time.LocalDate;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword, editTextName, editTextPhone;
    private Button btnRegister;

    private AppDatabase db;
    private UserDao userDao;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        btnRegister = findViewById(R.id.buttonRegister);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "users").build();
        userDao = db.userDao();

        btnRegister.setOnClickListener(v -> registerUser());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void registerUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        Account account = new Account();
        User user = new User(email, account, DateFormatter.toDate("31-12-2004"), phone, "token", password, name);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    userDao.insert(user);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Error registering user.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
