package com.example.trevormobilefilm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WatchlistFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        TextView watchEmpty = view.findViewById(R.id.watch_empty);
        // Get shared preference on create;
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharePrefs", Context.MODE_PRIVATE);

        createGridView(gson, sharedPreferences, view);
        return view;
    }

    private void createGridView(Gson gson, SharedPreferences sharedPreferences, View view) {
        ArrayList<CardData> cardWatchList = new ArrayList<>();
        RecyclerView watchView  = view.findViewById(R.id.watch_list);

        List<Integer> watchList = loadData(gson, sharedPreferences);
        for (int i = 0; i < watchList.size(); i++) {
            int watchId = watchList.get(i);
            String watchCardInfo = sharedPreferences.getString(String.valueOf(watchId), "");
            String[] nameType = gson.fromJson(watchCardInfo, String[].class);
//            cardWatchList.add(new CardData())
        }
    }

    private List<Integer> loadData(Gson gson, SharedPreferences sharedPreferences) {
        String jsonOrderList = sharedPreferences.getString("orderList", "[]");
        return new ArrayList<>(Arrays.asList(gson.fromJson(jsonOrderList, Integer[].class)));
    }
}
