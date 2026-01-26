package com.example.grocerynotifier.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.grocerynotifier.utils.JsonConverter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity(tableName = "accounts")
@TypeConverters(JsonConverter.class)
public class Account {
    @PrimaryKey
    @NotNull
    private String id;
    @NotNull
    private AccountType accountType;

    public Account(String id, AccountType accountType) {
        this.id = id;
        this.accountType = accountType;
    }

    public Account() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
