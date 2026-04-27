package com.example.grocerynotifier.daos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.grocerynotifier.model.Account;
import com.example.grocerynotifier.model.Product;
import com.example.grocerynotifier.model.User;

@Database(entities = {User.class, Account.class, Product.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract UserDao userDao();
    public abstract AccountDao accountDao();
    public abstract ProductDao productDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "grocery_db"
            ).build();
        }
        return instance;
    }
}
