package com.example.grocerynotifier.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class AccountWithProducts {

    @Embedded
    public Account account;

    @Relation(parentColumn = "id", entityColumn = "accountId")
    public List<Product> products;
}

