package com.example.trevormobilefilm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WatchViewModel extends ViewModel {
    private final MutableLiveData<CharSequence> text = new MutableLiveData<>();

    public void setText(CharSequence input) {
        text.setValue(input);
    }

    public LiveData<CharSequence> getText() {
        return text;
    }
}
