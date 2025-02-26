package com.example.grocerynotifier.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.grocerynotifier.model.User;

import java.util.concurrent.Future;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
    @Query("SELECT * FROM users WHERE email = :inputEmail")
    User getUserByEmail(String inputEmail);
}
