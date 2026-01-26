package com.example.grocerynotifier.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class AccountWithUsers {

    @Embedded
    public Account account;

    @Relation(parentColumn = "id", entityColumn = "accountId")
    public List<User> users;
}
