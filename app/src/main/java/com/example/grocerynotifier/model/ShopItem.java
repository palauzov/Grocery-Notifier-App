package com.example.grocerynotifier.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shop_items")
public class ShopItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String listId;
    public int quantity;
    public boolean isChecked;

    public ShopItem(String name, String listId, int quantity) {
        this.name = name;
        this.listId = listId;
        this.isChecked = false;
        this.quantity = quantity;
    }
}
