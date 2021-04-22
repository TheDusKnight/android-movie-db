package com.example.trevormobilefilm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private WatchViewModel detailViewModel;
    RequestQueue mQueue;
    TextView titleView;
    TextView overView;
    TextView genresView;
    TextView yearView;
    ImageView backDropView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailViewModel = new ViewModelProvider(this).get(WatchViewModel.class);

        // Get Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String filmType = bundle.getString(SliderAdapter.FILM_TYPE);
        int filmId = bundle.getInt(SliderAdapter.FILM_ID);

        // Send request
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonParse(MainActivity.URL + "/" + filmType + "/detail/" + filmId, detail -> {
            String title = "N/A";
            String overview = "N/A";
            String genres = "N/A";
            String year = "N/A";
            String backDropImage = null;
            try {
                title = detail.getJSONObject(0).getString("name")
                        != null ? detail.getJSONObject(0).getString("name") : "N/A";
                overview = detail.getJSONObject(0).getString("overview")
                        != null ? detail.getJSONObject(0).getString("overview") : "N/A";
                List<String> genresList = new ArrayList<>();
                JSONArray genresJson = detail.getJSONObject(0).getJSONArray("genres");
                if (genresJson != null && genresJson.length() > 0) {
                    for (int i = 0; i < genresJson.length(); i++) {
                        genresList.add(genresJson.getString(i));
                    }
                    genres = String.join(", ", genresList);
                }
                String dateString = detail.getJSONObject(0).getString("release_date");
                if (dateString != null) {
                    List<String> dataList = Arrays.asList(dateString.split("-"));
                    year = dataList.get(0);
                }
                backDropImage = detail.getJSONObject(0).getString("backdrop_path");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            titleView = findViewById(R.id.title_text);
            titleView.setText(title);
            overView = findViewById(R.id.overview_text);
            overView.setText(overview);
            genresView = findViewById(R.id.genres_text);
            genresView.setText(genres);
            yearView = findViewById(R.id.year_text);
            yearView.setText(year);

            String finalBackDropImage = backDropImage;
            jsonParse(MainActivity.URL + "/" + filmType + "/video/" + filmId, video -> {
                String videoId = "";
                try {
                    videoId = video.getJSONObject(0).getString("key");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Create YouTube player or set background image
                createYouTube(videoId, finalBackDropImage);
            });
        });

    }

    private void createYouTube(String videoId, String backDropImage) {
        // Add YouTube Player as lifecycle observer
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        if (!videoId.equals("tzkWB85ULJY") && !videoId.isEmpty()) {
            getLifecycle().addObserver(youTubePlayerView);

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0);
                }
            });
        } else {
            youTubePlayerView.setVisibility(View.GONE);
            backDropView = findViewById(R.id.back_drop_view);
            Glide.with(this).load(backDropImage).into(backDropView);
            backDropView.setVisibility(View.VISIBLE);
        }
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