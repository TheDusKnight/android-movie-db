package com.example.trevormobilefilm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

// TODO: change font problem
public class SearchFragment extends Fragment {
    RequestQueue mQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        SearchView searchView = view.findViewById(R.id.search_view);
        TextView noView = view.findViewById(R.id.search_no_view);

        mQueue = VolleySingleton.getInstance(this.getContext()).getRequestQueue();

        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    if (newText.length() == 0) {
                        noView.setVisibility(View.INVISIBLE);
                    } else {
                        jsonParse(MainActivity.URL + "/search/multi/" + URLEncoder.encode(newText,
                                StandardCharsets.UTF_8.toString()), searchFilms -> {
                            ArrayList<CardData> cardSearchDataArrayList = new ArrayList<>();
                            RecyclerView searchScrollView = view.findViewById(R.id.search_scroller);

                            if (searchFilms.length() < 1) {
                                noView.setVisibility(View.VISIBLE);
                            } else {
                                noView.setVisibility(View.INVISIBLE);
                            }

                            for (int i = 0; i < searchFilms.length(); i++) {
                                JSONObject mObject = searchFilms.getJSONObject(i);
                                String backPath = mObject.getString("backdrop_path");
                                String filmType = mObject.getString("media_type");
                                String filmName = mObject.getString("name");
                                int filmId = mObject.getInt("id");
                                int filmRate = mObject.getInt("rate");
                                String filmDate = mObject.getString("date");
                                cardSearchDataArrayList.add(new CardData(backPath, filmType, filmName, filmId, filmDate, filmRate));
                            }

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            SearchAdapter searchAdapter = new SearchAdapter(getContext(), cardSearchDataArrayList);
                            searchScrollView.setLayoutManager(layoutManager);
                            searchScrollView.setAdapter(searchAdapter);
                        });
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

        return view;
    }

    private void jsonParse(String url, final VolleyCallback callback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("results");
                callback.onSuccess(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        mQueue.add(request);
    }
}
