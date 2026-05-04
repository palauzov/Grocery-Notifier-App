package com.example.grocerynotifier.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerynotifier.R;
import com.example.grocerynotifier.daos.AccountDao;
import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.daos.UserDao;
import com.example.grocerynotifier.model.Account;
import com.example.grocerynotifier.model.AccountType;
import com.example.grocerynotifier.model.User;
import com.example.grocerynotifier.utils.DateFormatter;
import com.example.grocerynotifier.utils.IdGenerator;
import com.example.grocerynotifier.utils.ValidatorUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextName,
            editTextPhone, editTextDate, editTextConfPass, editTextAccountCode;

    private RadioGroup genderRadioGroup, radioGroupAccount;
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
        editTextAccountCode = findViewById(R.id.editTextAccountCode);

        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        radioGroupAccount = findViewById(R.id.radioGroupAcc);

        db = AppDatabase.getInstance(getApplicationContext());
        userDao = db.userDao();

        editTextDate.setOnClickListener(v -> showDatePicker());

        // Terms link
        findViewById(R.id.textViewTermsLink).setOnClickListener(v ->
                startActivity(new Intent(this, TermsAndConditionsActivity.class))
        );

        // Login redirect
        findViewById(R.id.textViewTerms).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Show/hide account code field
        radioGroupAccount.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = findViewById(checkedId);

            if (selected != null &&
                    selected.getText().toString()
                            .equals("Join an existing Roommate/Family Account")) {

                editTextAccountCode.setVisibility(View.VISIBLE);
            } else {
                editTextAccountCode.setVisibility(View.GONE);
                editTextAccountCode.setText("");
            }
        });
    }
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    String date = String.format("%02d-%02d-%d",
                            selectedDay,
                            selectedMonth + 1,
                            selectedYear);

                    editTextDate.setText(date);
                },
                year, month, day
        );

        // Prevent future dates
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onRegisterClick(View view) {

        executorService.execute(() -> {
            try {
                boolean success = registerUser();

                if (success) {
                    Account account = checkAccount();

                    SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    prefs.edit()
                            .putString("accountId", account.getId())
                            .putBoolean("isLoggedIn", true)
                            .apply();

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        navigateToLogin();
                    });
                }

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
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
                    ValidatorUtils.validatePassword(editTextPassword.getText().toString()),
                    confirmPass
            );

            String phone = ValidatorUtils.validatePhone(editTextPhone.getText().toString());
            Date birthDate = DateFormatter.toDate(editTextDate.getText().toString());
            String gender = checkGender();

            Account account = checkAccount();

            User user = new User(email, account.getId(), birthDate, phone, gender,
                    null, password, name, false);

            userDao.insert(user);

            return true;

        } catch (Exception e) {
            runOnUiThread(() -> showRetryDialog(e.getMessage()));
        }

        return false;
    }

    private void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showRetryDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Registration Failed")
                .setMessage(message)
                .setPositiveButton("Retry", (dialog, which) -> resetForm())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void resetForm() {
        editTextEmail.setText("");
        editTextName.setText("");
        editTextConfPass.setText("");
        editTextPassword.setText("");
        editTextPhone.setText("");
        editTextDate.setText("");
    }

    private String checkGender() throws Exception {
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) throw new Exception("Select gender");

        return ((RadioButton) findViewById(selectedId)).getText().toString();
    }

    private Account checkAccount() throws Exception {

        int selectedId = radioGroupAccount.getCheckedRadioButtonId();
        if (selectedId == -1) throw new Exception("Choose account type");

        RadioButton selected = findViewById(selectedId);
        String choice = selected.getText().toString();

        AccountDao accountDao = db.accountDao();

        switch (choice) {

            case "Single Account":
                Account a1 = new Account(IdGenerator.generateId(8), AccountType.SINGLE);
                accountDao.insert(a1);
                return a1;

            case "Roommate Account":
                Account a2 = new Account(IdGenerator.generateId(8), AccountType.ROOMMATES);
                accountDao.insert(a2);
                return a2;

            case "Family Account":
                Account a3 = new Account(IdGenerator.generateId(8), AccountType.FAMILY);
                accountDao.insert(a3);
                return a3;

            case "Join an existing Roommate/Family Account":

                String code = editTextAccountCode.getText().toString().trim();

                if (code.length() != 8) throw new Exception("Code must be 8 characters");

                Account existing = accountDao.getAccountById(code);

                if (existing == null) throw new Exception("Account not found");

                if (existing.getAccountType() == AccountType.SINGLE)
                    throw new Exception("Cannot join Single Account");

                return existing;
        }

        throw new Exception("Invalid selection");
    }
}