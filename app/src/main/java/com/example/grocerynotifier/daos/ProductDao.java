package com.example.grocerynotifier.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.grocerynotifier.model.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);
    @Query("SELECT * FROM products WHERE accountId = :accId ORDER BY expirationDate ASC")
    List<Product> getProductsByAccount(String accId);

    @Query("SELECT * FROM products WHERE barcode = :barcode LIMIT 1")
    Product getByBarcode(String barcode);
}
