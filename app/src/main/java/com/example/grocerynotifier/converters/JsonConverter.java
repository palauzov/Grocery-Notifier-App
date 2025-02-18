package com.example.grocerynotifier.converters;

import androidx.room.TypeConverter;

import com.example.grocerynotifier.model.Account;
import com.example.grocerynotifier.model.Product;
import com.google.gson.Gson;

public class JsonConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static <T> String accountToJson(Account account){
        return (account == null) ? null : gson.toJson(account);
    }
    @TypeConverter
    public static Account jsonToAccount(String json) {
        return gson.fromJson(json, Account.class);
    }
    @TypeConverter
    public static String productToJson(Product product){
        return (product == null) ? null : gson.toJson(product);
    }
    @TypeConverter
    public static Product jsonToProduct(String json) {
        return gson.fromJson(json, Product.class);
    }
}
