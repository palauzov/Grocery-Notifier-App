package com.example.grocerynotifier.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword, editTextName, editTextPhone, editTextDate, editTextConfPass;
    private Button btnRegister;
    private RadioGroup genderRadioGroup;
    private AppDatabase db;
    private UserDao userDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

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
        genderRadioGroup = findViewById(R.id.genderRadioGroup);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "users").build();
        userDao = db.userDao();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onRegisterClick(View view) {
        executorService.execute(() -> {
            boolean success = registerUser();
            if (success) {
                runOnUiThread(() -> {
                    showToast("Registration successful!");
                    navigateToLogin();
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean registerUser() {

        try {
            String email = ValidatorUtils.validateEmail(editTextEmail.getText().toString(), userDao);
            String name = editTextName.getText().toString().trim();
            String confirmPass = editTextConfPass.getText().toString();
            String password = ValidatorUtils.validateConfirmPassword(
                    ValidatorUtils.validatePassword(editTextPassword.getText().toString()), confirmPass);
            String phone = ValidatorUtils.validatePhone(editTextPhone.getText().toString());
            Date birthDate = DateFormatter.toDate(editTextDate.getText().toString());
            String gender = checkGender();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled.");
            }

            Account account = new Account();
            User user = new User(email, account, birthDate, phone, gender, null, password, name);

            userDao.insert(user);
            return true;

        } catch (IllegalArgumentException e) {
            runOnUiThread(() -> showRetryDialog("Validation Error: " + e.getMessage()));
        } catch (Exception e) {
            runOnUiThread(() -> showRetryDialog("Registration failed: " + e.getMessage()));
        }
        return false;
    }

    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    private void showRetryDialog(String message) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setTitle("Registration Failed")
                    .setMessage(message)
                    .setPositiveButton("Retry", (dialog, which) -> resetForm())
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void resetForm() {
        runOnUiThread(() -> {
            editTextEmail.setText("");
            editTextName.setText("");
            editTextConfPass.setText("");
            editTextPassword.setText("");
            editTextPhone.setText("");
            editTextDate.setText("");
        });
    }
    private String checkGender() throws Exception{
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedId == -1){
            throw new Exception("Please, select Gender");
        }
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton.getText().toString();
    }
}
