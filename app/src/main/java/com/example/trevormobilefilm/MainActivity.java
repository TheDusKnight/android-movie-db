package com.example.trevormobilefilm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    HomeFragment movieFragment;
    HomeFragment tvFragment;
    RequestQueue mQueue;
    private WatchViewModel watchViewModel;
//    public static final String URL = "http://10.0.2.2:8080";
    public static final String URL = "https://trevor-mobile-film.wl.r.appspot.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init view model in activity
        watchViewModel = new ViewModelProvider(this).get(WatchViewModel.class);

        // Receive data from fragment
        getSupportFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String fileType = result.getString("bundleKey");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (fileType.equals("movie")) {
                    ft.hide(movieFragment);
                    if (tvFragment.isAdded()) {
                        ft.show(tvFragment);
                    } else {
                        ft.add(R.id.fragment_container, tvFragment, null);
                    }
                } else {
                    ft.hide(tvFragment);
                    if (movieFragment.isAdded()) {
                        ft.show(movieFragment);
                    } else {
                        ft.add(R.id.fragment_container, movieFragment, null);
                    }
                }
                ft.commit();
            }
        });

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonParse(URL + "/current/movie", currentMovie -> {
            watchViewModel.setCurrentMovie(currentMovie);
            jsonParse(URL + "/trend/tv", trendTv -> {
                watchViewModel.setTrendTv(trendTv);
                jsonParse(URL + "/top/movie", topMovie -> {
                    watchViewModel.setTopMovie(topMovie);
                    jsonParse(URL + "/top/tv", topTV -> {
                        watchViewModel.setTopTv(topTV);
                        jsonParse(URL + "/pop/movie", popMovie -> {
                            watchViewModel.setPopMovie(popMovie);
                            jsonParse(URL + "/pop/tv", popTv -> {
                                watchViewModel.setPopTv(popTv);
                                createFragment();
                            });
                        });
                    });
                });
            });
        });

        // Create bottom nav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

//    private void loadData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("sharePrefs", MODE_PRIVATE);
//        sharedPreferences.getString("orderList", null);
//
//
//    }

    private void createFragment() { // Create and display HomeFragment onCreate
        // Pass data to HomeFragment with bundle
        movieFragment = HomeFragment.newInstance("movie", 1);
        tvFragment = HomeFragment.newInstance("tv", 2);

        RelativeLayout loading = findViewById(R.id.main_loading);
        loading.setVisibility(View.INVISIBLE);
        // Create and display HomeFragment onCreate
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, movieFragment).commit();
    }

    private void jsonParse(String url, final VolleyCallback callback) {
        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("results");
                        callback.onSuccess(jsonArray);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject mObject = jsonArray.getJSONObject(i);
//
//                            String posterPath = mObject.getString("poster_path");
//                            textView.append(posterPath + "\n");
//                            watchViewModel.setText(posterPath);
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);

        mQueue.add(request);

//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    // Display the first 500 characters of the response string.
//                    textView.setText("Response is: "+ response.substring(0,500));
//                }, error -> textView.setText(error.toString()));
//        // Add the request to the RequestQueue.
//        mQueue.add(stringRequest);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = movieFragment;
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.nav_watchlist:
                            selectedFragment = new WatchlistFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}