package com.example.grocerynotifier.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerynotifier.R;
import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.model.Account;
import com.example.grocerynotifier.model.AccountType;
import com.example.grocerynotifier.utils.IdGenerator;

import java.util.concurrent.Executors;

public class AccountChangeActivity extends AppCompatActivity {

    private EditText editCode;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_account_window);

        db = AppDatabase.getInstance(this);

        editCode = findViewById(R.id.editCode);

        findViewById(R.id.btnSingle).setOnClickListener(v ->
                createAccount(AccountType.SINGLE));

        findViewById(R.id.btnRoommates).setOnClickListener(v ->
                createAccount(AccountType.ROOMMATES));

        findViewById(R.id.btnFamily).setOnClickListener(v ->
                createAccount(AccountType.FAMILY));

        findViewById(R.id.btnJoin).setOnClickListener(v ->
                joinAccount());
    }

    private void createAccount(AccountType type) {

        Executors.newSingleThreadExecutor().execute(() -> {

            Account account = new Account(
                    IdGenerator.generateId(8),
                    type
            );

            db.accountDao().insert(account);

            SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            prefs.edit()
                    .putString("accountId", account.getId())
                    .apply();

            runOnUiThread(() ->
                    Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
            );
        });
    }

    private void joinAccount() {

        String code = editCode.getText().toString().trim();

        if (code.length() != 8) {
            Toast.makeText(this, "Invalid code", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {

            Account account = db.accountDao().getAccountById(code);

            runOnUiThread(() -> {

                if (account == null) {
                    Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                prefs.edit()
                        .putString("accountId", account.getId())
                        .apply();

                Toast.makeText(this, "Joined successfully", Toast.LENGTH_SHORT).show();
            });
        });
    }
}
