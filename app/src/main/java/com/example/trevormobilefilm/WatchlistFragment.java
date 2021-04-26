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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WatchlistFragment extends Fragment{
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_watchlist, container, false);
//        TextView watchEmpty = view.findViewById(R.id.watch_empty);
//        // Get shared preference on create;
//        Gson gson = new Gson();
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharePrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        // Listen for sharedPreference changes?
//
//        createGridView(gson, sharedPreferences, editor, view, watchEmpty);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView watchEmpty = view.findViewById(R.id.watch_empty);
        // Get shared preference on create;
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Listen for sharedPreference changes?

        createGridView(gson, sharedPreferences, editor, view, watchEmpty);
    }

    private void createGridView(Gson gson, SharedPreferences sharedPreferences,
                                SharedPreferences.Editor editor, View view, TextView watchEmpty) {
        ArrayList<CardData> cardWatchList = new ArrayList<>();
        RecyclerView watchView  = view.findViewById(R.id.watch_list);

        List<Integer> watchList = loadData(gson, sharedPreferences);
        for (int i = 0; i < watchList.size(); i++) {
            int watchId = watchList.get(i);
            String watchCardInfo = sharedPreferences.getString(String.valueOf(watchId), "");
            String[] cardContent = gson.fromJson(watchCardInfo, String[].class);
            String name = cardContent[0];
            String type = cardContent[1];
            String path = cardContent[2];
            cardWatchList.add(new CardData(path, type, name, watchId));
        }

        RecyclerView.LayoutManager watchLayoutManager = new GridLayoutManager(view.getContext(), 3);
        WatchAdapter watchAdapter = new WatchAdapter(view.getContext(), cardWatchList, watchList, gson, editor, watchEmpty);
        watchView.setLayoutManager(watchLayoutManager);
        watchView.setAdapter(watchAdapter);

        // Create simple callback for drag and drop
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.
                SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Collections.swap(cardWatchList, fromPosition, toPosition);
//                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                recyclerView.getAdapter().notifyDataSetChanged();
                // Store updated list
                Collections.swap(watchList, fromPosition, toPosition);
                String jsonList = gson.toJson(watchList);
                editor.putString("orderList", jsonList);
                editor.apply();
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(watchView);
    }

    private List<Integer> loadData(Gson gson, SharedPreferences sharedPreferences) {
        String jsonOrderList = sharedPreferences.getString("orderList", "[]");
        return new ArrayList<>(Arrays.asList(gson.fromJson(jsonOrderList, Integer[].class)));
    }
}
