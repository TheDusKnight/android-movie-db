package com.example.trevormobilefilm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class DetailActivity extends AppCompatActivity {
    RequestQueue mQueue;
    TextView titleView;
    TextView overView;
    TextView genresView;
    TextView yearView;
    ImageView backDropView;
    ScrollView detailView;
    RelativeLayout detailLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailView = findViewById(R.id.detail_view);
        detailView.setVisibility(View.INVISIBLE);
        detailLoading = findViewById(R.id.detail_loading);

        // Get Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String filmType = bundle.getString(SliderAdapter.FILM_TYPE);
        int filmId = bundle.getInt(SliderAdapter.FILM_ID);
        String filmName = bundle.getString(SliderAdapter.FILM_NAME);

//        setListener(filmType, filmId, filmName);

        // Send request
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonParse(MainActivity.URL + "/" + filmType + "/detail/" + filmId, detail -> {
            String title = "N/A";
            String overview = "N/A";
            String genres = "N/A";
            String year = "N/A";
            String backDropImage = null;
            String posterPath;
            try {
                posterPath = detail.getJSONObject(0).getString("poster_path");
                // Set onclick listener after fetch from detail api
                setListener(filmType, filmId, filmName, posterPath);
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

                jsonParse(MainActivity.URL + "/recommend/" + filmType + "/" + filmId, recommendFilms -> {
                    ArrayList<CardData> cardTrendDataArrayList = new ArrayList<>();
                    RecyclerView trendScrollView = findViewById(R.id.recommend_scroller);

                    try {
                        for (int i = 0; i < recommendFilms.length(); i++) {
                            JSONObject mObject = recommendFilms.getJSONObject(i);
                            String innerPosterPath = mObject.getString("poster_path");
                            String innerFilmType = mObject.getString("media_type");
                            String innerFilmName = mObject.getString("name");
                            int innerFilmId = mObject.getInt("id");
                            boolean innerAdd = false;
                            cardTrendDataArrayList.add(new CardData(innerPosterPath, innerAdd,
                                    innerFilmType, innerFilmName, innerFilmId));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RecyclerView.LayoutManager recommendLayoutManager = new LinearLayoutManager(this,
                            LinearLayoutManager.HORIZONTAL, false);
                    ScrollerAdapter topAdapter = new ScrollerAdapter(this,
                            cardTrendDataArrayList, "basic");
                    trendScrollView.setLayoutManager(recommendLayoutManager);
                    trendScrollView.setAdapter(topAdapter);

                    fillCast(filmType, filmId);
                });
            });
        });



//        fillCast(filmType, filmId);
//        fillReview(filmType, filmId);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        RelativeLayout detailLoading = findViewById(R.id.detail_loading);
//        detailLoading.setVisibility(View.INVISIBLE);
//
//        ScrollView detailLayout = findViewById(R.id.detail_view);
//        detailLayout.setVisibility(View.VISIBLE);
    }

    private void setListener(String filmType, int filmId, String filmName, String posterPath) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = this.getSharedPreferences("sharePrefs", Context.MODE_PRIVATE);
        String jsonOrderList = sharedPreferences.getString("orderList", "[]");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<Integer> watchList = new ArrayList<>(Arrays.asList(gson.fromJson(jsonOrderList, Integer[].class)));
        List<String> filmList = new ArrayList<>();
        filmList.add(filmName);
        filmList.add(filmType);
        filmList.add(posterPath);

        ImageView addWatch = findViewById(R.id.watch_icon);
        ImageView facebook = findViewById(R.id.facebook_icon);
        ImageView twitter = findViewById(R.id.twitter_icon);

        facebook.setOnClickListener(v -> {
            String url = "https://www.facebook.com/sharer/sharer.php?u="
                    + "https://www.themoviedb.org/" + filmType + "/"
                    + filmId + "&amp;src=sdkpreparse";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        twitter.setOnClickListener(v -> {
            String url = null;
            try {
                url = "https://twitter.com/intent/tweet?text="
                        + "Check this out!" + "&url=" +
                        URLEncoder.encode("https://www.themoviedb.org/"
                                        + filmType + "/"
                                        + filmId,
                                StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        if (watchList.contains(filmId)) {
            addWatch.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
        } else {
            addWatch.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
        }

        addWatch.setOnClickListener(v -> {
            if (watchList.contains(filmId)) {
                Toast.makeText(this, filmName +
                        " was removed from Watchlist", Toast.LENGTH_SHORT).show();
                addWatch.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
                watchList.remove(Integer.valueOf(filmId));
                // not need to remove anything from film cache
            } else {
                Toast.makeText(this, filmName +
                        " was added to Watchlist", Toast.LENGTH_SHORT).show();
                addWatch.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
                watchList.add(filmId);
                editor.putString(String.valueOf(filmId), gson.toJson(filmList));
            }
            String jsonList = gson.toJson(watchList);
            editor.putString("orderList", jsonList);
            editor.apply();
        });
    }

    private void fillReview(String filmType, int filmId) {
        jsonParse(MainActivity.URL + "/" + filmType + "/review/" + filmId, review -> {
            int[] cardId = new int[]{R.id.card_view1, R.id.card_view2, R.id.card_view3};
            int[] titleId = new int[]{R.id.review_title1, R.id.review_title2, R.id.review_title3};
            int[] rateId = new int[]{R.id.review_rate1, R.id.review_rate2, R.id.review_rate3};
            int[] contentId = new int[]{R.id.review_content1, R.id.review_content2, R.id.review_content3};
            if (review.length() > 0) {
                TextView reviewTitleView = findViewById(R.id.reviews_title);
                reviewTitleView.setVisibility(View.VISIBLE);
                for (int i = 0; i < review.length(); i++) {
                    String rate = "0/5";
                    String name = "N/A";
                    String date = "N/A";
                    String reviewText = "N/A";

                    CardView cardView = findViewById(cardId[i]);
                    TextView titleView = findViewById(titleId[i]);
                    TextView rateView = findViewById(rateId[i]);
                    TextView contentView = findViewById(contentId[i]);

                    try {
                        rate = review.getJSONObject(i).getInt("rating") / 2 + "/5";
                        name = review.getJSONObject(i).getString("author") != null
                                ? review.getJSONObject(i).getString("author") : "anonymous user";
                        String dateRaw = review.getJSONObject(i).getString("created_at");
                        if (dateRaw != null) {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                            Date d = dateFormat.parse(dateRaw);
                            DateFormat df = new SimpleDateFormat("E, MMM dd yyyy", Locale.US);
                            date = df.format(d);
                        } else {
                            date = "N/A";
                        }
                        reviewText = review.getJSONObject(i).getString("content");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    String title = "by " + name + " on " + date;
                    titleView.setText(title);
                    rateView.setText(rate);
                    contentView.setText(reviewText);
                    cardView.setVisibility(View.VISIBLE);

                    // set click for each card
                    String finalRate = rate;
                    String finalName = name;
                    String finalDate = date;
                    String finalReviewText = reviewText;
                    cardView.setOnClickListener(v -> {
                        Bundle result = new Bundle();
                        result.putString("rateKey", finalRate);
                        result.putString("nameKey", finalName);
                        result.putString("dateKey", finalDate);
                        result.putString("contentKey", finalReviewText);
                        Intent intent = new Intent(this, ReviewActivity.class);
                        intent.putExtras(result);
                        startActivity(intent);
                    });
                }
            }

            detailView.setVisibility(View.VISIBLE);
            detailLoading.setVisibility(View.GONE);
        });
    }

    private void fillCast(String filmType, int filmId) {
        jsonParse(MainActivity.URL + "/" + filmType + "/cast/" + filmId, cast -> {
            int[] castImgIds = new int[]{R.id.profile_image1, R.id.profile_image2, R.id.profile_image3,
                    R.id.profile_image4, R.id.profile_image5, R.id.profile_image6};
            int[] castNameIds = new int[]{R.id.name_text1, R.id.name_text2, R.id.name_text3,
                    R.id.name_text4, R.id.name_text5, R.id.name_text6};
            TextView castTitleView = findViewById(R.id.cast_title);
            TableLayout castListView = findViewById(R.id.cast_layout);
            // Hide all cast view if not cast
            if (cast.length() < 1) {
                castTitleView.setVisibility(View.GONE);
                castListView.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < cast.length(); i++) {
                    String url = null;
                    String name = "";
                    ImageView castImg = findViewById(castImgIds[i]);
                    TextView castName = findViewById(castNameIds[i]);
                    castImg.setVisibility(View.VISIBLE);
                    castName.setVisibility(View.VISIBLE);
                    try {
                        url = cast.getJSONObject(i).getString("profile_path");
                        name = cast.getJSONObject(i).getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Glide.with(this).load(url).into(castImg);
                    castName.setText(name);
                }
                // Show less views if less than 4
                if (cast.length() < 4) {
                    findViewById(R.id.less_cast_name).setVisibility(View.GONE);
                    findViewById(R.id.less_cast_img).setVisibility(View.GONE);
                }
            }

            fillReview(filmType, filmId);
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