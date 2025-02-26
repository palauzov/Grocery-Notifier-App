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
import com.example.grocerynotifier.converters.ValidatorUtils;
import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.daos.UserDao;
import com.example.grocerynotifier.model.Account;
import com.example.grocerynotifier.model.User;

import java.util.Date;


public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword, editTextName, editTextPhone, editTextDate, editTextConfPass;
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
        editTextConfPass = findViewById(R.id.editTextTextConfirmPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDate = findViewById(R.id.editTextDate);
        btnRegister = findViewById(R.id.buttonRegister);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "users").build();
        userDao = db.userDao();

        btnRegister.setOnClickListener(this::registerUser);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void registerUser(View view) {
        try {
            String email = ValidatorUtils.validateEmail(editTextEmail.getText().toString(), userDao);
            String name = editTextName.getText().toString().trim();
            String confirmPass = editTextConfPass.getText().toString();
            String password = ValidatorUtils.validateConfirmPassword(
                    ValidatorUtils.validatePassword(editTextPassword.getText().toString()),
                    confirmPass
            );
            String phone = ValidatorUtils.validatePhone(editTextPhone.getText().toString());
            String birthDateStr = ValidatorUtils.validateDate(editTextDate.getText().toString());
            Date birthDate = DateFormatter.toDate(birthDateStr);

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || birthDateStr.isEmpty()) {
                showToast("All fields must be filled");
                return;
            }

            Account account = new Account();
            User user = new User(email, account, birthDate, phone, "token", password, name);

            new Thread(() -> {
                try {
                    userDao.insert(user);
                    runOnUiThread(() -> {
                        showToast("Registration successful!");
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> showToast("Error registering user. Email might already exist."));
                }
            }).start();

        } catch (IllegalArgumentException e) {
            showToast(e.getMessage());
        }
    }
    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
