package com.example.bottomnavigatonview.ui.garden;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GardenViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GardenViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is garden fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }
}