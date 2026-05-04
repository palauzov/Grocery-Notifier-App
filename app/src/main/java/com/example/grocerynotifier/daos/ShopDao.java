package com.example.grocerynotifier.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.grocerynotifier.model.ShopItem;
import com.example.grocerynotifier.model.ShopList;

import java.util.List;

@Dao
public interface ShopDao {

    @Insert
    void insertList(ShopList list);

    @Insert
    void insertItem(ShopItem item);

    @Update
    void updateItem(ShopItem item);

    @Query("DELETE FROM shop_lists WHERE id = :listId")
    void deleteList(String listId);

    @Query("SELECT * FROM shop_lists WHERE accountId = :accountId")
    List<ShopList> getListsByAccount(String accountId);

    @Query("SELECT * FROM shop_lists")
    List<ShopList> getAllLists();


    @Query("SELECT * FROM shop_items WHERE listId = :listId")
    List<ShopItem> getItems(String listId);

    @Query("DELETE FROM shop_items WHERE listId = :listId")
    void deleteItemsByList(String listId);
}
