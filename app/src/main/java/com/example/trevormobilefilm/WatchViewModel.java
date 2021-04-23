package com.example.trevormobilefilm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class WatchViewModel extends ViewModel {
    private final MutableLiveData<CharSequence> text = new MutableLiveData<>();
    private final MutableLiveData<JSONArray> currentMovie = new MutableLiveData<>();
    private final MutableLiveData<JSONArray> trendTv = new MutableLiveData<>();
    private final MutableLiveData<JSONArray> topMovie = new MutableLiveData<>();
    private final MutableLiveData<JSONArray> topTv = new MutableLiveData<>();
    private final MutableLiveData<JSONArray> popMovie = new MutableLiveData<>();
    private final MutableLiveData<JSONArray> popTv = new MutableLiveData<>();
    private final MutableLiveData<JSONArray> recommendMovie = new MutableLiveData<>();
    private final MutableLiveData<JSONArray> recommendTv = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> addList = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, JSONObject>> addMap = new MutableLiveData<>();

    public MutableLiveData<List<Integer>> getAddList() {
        return addList;
    }

    public MutableLiveData<HashMap<Integer, JSONObject>> getAddMap() {
        return addMap;
    }

    public MutableLiveData<JSONArray> getCurrentMovie() {
        return currentMovie;
    }

    public MutableLiveData<JSONArray> getTrendTv() {
        return trendTv;
    }

    public MutableLiveData<JSONArray> getRecommendMovie() {
        return recommendMovie;
    }

    public MutableLiveData<JSONArray> getRecommendTv() {
        return recommendTv;
    }

    public MutableLiveData<JSONArray> getTopMovie() {
        return topMovie;
    }

    public MutableLiveData<JSONArray> getTopTv() {
        return topTv;
    }

    public MutableLiveData<JSONArray> getPopMovie() {
        return popMovie;
    }

    public MutableLiveData<JSONArray> getPopTv() {
        return popTv;
    }

    public void setCurrentMovie(JSONArray input) {
        currentMovie.setValue(input);
    }

    public void setTrendTv(JSONArray input) {
        trendTv.setValue(input);
    }

    public void setTopMovie(JSONArray input) {
        topMovie.setValue(input);
    }

    public void setTopTv(JSONArray input) {
        topTv.setValue(input);
    }

    public void setPopMovie(JSONArray input) {
        popMovie.setValue(input);
    }

    public void setPopTv(JSONArray input) {
        popTv.setValue(input);
    }

    public void setRecommendMovie(JSONArray input) {
        recommendMovie.setValue(input);
    }

    public void setRecommendTv(JSONArray input) {
        recommendTv.setValue(input);
    }

    public LiveData<CharSequence> getText() {
        return text;
    }

    public void setText(CharSequence input) {
        text.setValue(input);
    }

    public void setAddList(List<Integer> input) {
        addList.setValue(input);
    }

    public void setAddMap(HashMap<Integer, JSONObject> input) {
        addMap.setValue(input);
    }

}
