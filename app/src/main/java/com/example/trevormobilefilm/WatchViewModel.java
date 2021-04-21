package com.example.trevormobilefilm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.List;

public class WatchViewModel extends ViewModel {
    private final MutableLiveData<CharSequence> text = new MutableLiveData<>();
    private final MutableLiveData<List<JSONObject>> topMovie = new MutableLiveData<>();
    private final MutableLiveData<List<JSONObject>> topTv = new MutableLiveData<>();
    private final MutableLiveData<List<JSONObject>> popMovie = new MutableLiveData<>();
    private final MutableLiveData<List<JSONObject>> popTv = new MutableLiveData<>();
    private final MutableLiveData<List<JSONObject>> recommendMovie = new MutableLiveData<>();
    private final MutableLiveData<List<JSONObject>> recommendTv = new MutableLiveData<>();

    public MutableLiveData<List<JSONObject>> getRecommendMovie() {
        return recommendMovie;
    }

    public MutableLiveData<List<JSONObject>> getRecommendTv() {
        return recommendTv;
    }

    public MutableLiveData<List<JSONObject>> getTopMovie() {
        return topMovie;
    }

    public MutableLiveData<List<JSONObject>> getTopTv() {
        return topTv;
    }

    public MutableLiveData<List<JSONObject>> getPopMovie() {
        return popMovie;
    }

    public MutableLiveData<List<JSONObject>> getPopTv() {
        return popTv;
    }

    public void setTopMovie(List<JSONObject> input) {
        topMovie.setValue(input);
    }

    public void setTopTv(List<JSONObject> input) {
        topTv.setValue(input);
    }

    public void setPopMovie(List<JSONObject> input) {
        popMovie.setValue(input);
    }

    public void setPopTv(List<JSONObject> input) {
        popTv.setValue(input);
    }

    public void setRecommendMovie(List<JSONObject> input) {
        recommendMovie.setValue(input);
    }

    public void setRecommendTv(List<JSONObject> input) {
        recommendTv.setValue(input);
    }

    public LiveData<CharSequence> getText() {
        return text;
    }

    public void setText(CharSequence input) {
        text.setValue(input);
    }

}
