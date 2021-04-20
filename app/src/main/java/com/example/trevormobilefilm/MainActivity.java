package com.example.trevormobilefilm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    // 设置全局 homeFragment，可能有问题？
//    HomeFragment homeFragment = new HomeFragment();
    HomeFragment movieFragment;
    HomeFragment tvFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Receive data from fragment
        getSupportFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String fileType = result.getString("bundleKey");
                final TextView textView = (TextView) findViewById(R.id.textView);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (fileType.equals("movie")) {
                    textView.setText("switch from movie to tv");
                    ft.hide(movieFragment);
                    if (tvFragment.isAdded()) {
                        ft.show(tvFragment);
                    } else {
                        ft.add(R.id.fragment_container, tvFragment, null);
                    }
                }
                else {
                    textView.setText("switch from tv to movie");
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

        // TODO: Edit Volley example
//        final TextView textView = (TextView) findViewById(R.id.textView);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "http://10.0.2.2:8080/top/tv";
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    // Display the first 500 characters of the response string.
//                    textView.setText("Response is: "+ response.substring(0,500));
//                }, error -> textView.setText(error.toString()));
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);

        // Pass data to HomeFragment with bundle
        movieFragment = HomeFragment.newInstance("movie", 1);
        tvFragment = HomeFragment.newInstance("tv", 2);

        // Create bottom nav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Create and display HomeFragment onCreate
        // TODO: 改成add？
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                movieFragment).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, movieFragment, null).commit();
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