package com.example.trevormobilefilm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    private WatchViewModel detailViewModel;
    RequestQueue mQueue;

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
        jsonParse(MainActivity.URL + "/" + filmType + "/video/" + filmId, video -> {
            String videoId = "";
            try {
                videoId = video.getJSONObject(0).getString("key");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            createYouTube(videoId);
        });

        TextView textView = findViewById(R.id.detailText);
        textView.setText(filmId + filmType);


    }

    private void createYouTube(String video) {
        // Add YouTube Player as lifecycle observer
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        // TODO: 如果video key是tzkWB85ULJY，那么隐藏YouTube，显示backdrop_path图片
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                String videoId = "UpolBSznWp0";
                youTubePlayer.loadVideo(video, 0);
            }
        });
//        youTubePlayerView.setVisibility(View.INVISIBLE);
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