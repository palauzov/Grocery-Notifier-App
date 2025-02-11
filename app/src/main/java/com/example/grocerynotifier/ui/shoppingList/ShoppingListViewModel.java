package com.example.grocerynotifier.ui.shoppingList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShoppingListViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ShoppingListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Shopping List fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}