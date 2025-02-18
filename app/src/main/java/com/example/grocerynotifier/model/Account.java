package com.example.grocerynotifier.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.grocerynotifier.converters.JsonConverter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity(tableName = "accounts")
@TypeConverters(JsonConverter.class)
public class Account {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    private String id;
    @NotNull
    private AccountType accountType;
    @NotNull
    private List<User> users;
    private List<Product>  products;

    public Account(String id, List<Product> products, List<User> users, AccountType accountType) {
        this.id = id;
        this.products = products;
        this.users = users;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
