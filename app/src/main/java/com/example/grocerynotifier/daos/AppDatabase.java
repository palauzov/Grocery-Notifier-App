package com.example.grocerynotifier.daos;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.grocerynotifier.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
