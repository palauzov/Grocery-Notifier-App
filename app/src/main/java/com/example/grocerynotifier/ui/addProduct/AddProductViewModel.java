package com.example.grocerynotifier.ui.addProduct;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddProductViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddProductViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Add Products");
    }

    public LiveData<String> getText() {
        return mText;
    }
}