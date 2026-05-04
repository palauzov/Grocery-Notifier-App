package com.example.grocerynotifier.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shop_lists")
public class ShopList {

    @PrimaryKey
    @NonNull
    public String id;

    public String accountId;

    public long createdAt;

    public ShopList(@NonNull String id, String accountId, long createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.createdAt = createdAt;
    }
}
