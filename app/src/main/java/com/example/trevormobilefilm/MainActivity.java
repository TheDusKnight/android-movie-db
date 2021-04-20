package com.example.trevormobilefilm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    // 设置全局 homeFragment，可能有问题？
//    HomeFragment homeFragment = new HomeFragment();
    HomeFragment homeFragment;
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
                if (fileType.equals("movie")) {
                    textView.setText("It is movie");

                    // Switch from movie fragment to tv fragment
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    ft.beginTransaction().add(R.id.fragment_container, tvFragment).commit();
//                    ft.beginTransaction().hide(curFragment).commit();
//                    getSupportFragmentManager().beginTransaction().hide(homeFragment).show(tvFragment).commit();
                      ft.hide(homeFragment);
                      ft.add(R.id.fragment_container, tvFragment, null);
                      ft.commit();
//                    getSupportFragmentManager().beginTransaction().show(tmpFragment);

                }
//                else {
////                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home, homeFragment);
//                    getSupportFragmentManager().beginTransaction().hide(tvFragment).commit();
//                }
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
        homeFragment = HomeFragment.newInstance("movie", 1);
        tvFragment = HomeFragment.newInstance("tv", 2);

        // Create bottom nav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Create and display HomeFragment onCreate
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                homeFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment, null).commit();
//        getSupportFragmentManager().beginTransaction().remove(homeFragment).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = homeFragment;
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