package com.example.trevormobilefilm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
    private static final String ARG_TEXT = "argText";
    private static final String ARG_NUMBER = "argNumber";
    private WatchViewModel watchViewModel;
    private String filmType;
    private int number;
    TextView textView;

    // Self create
    public static HomeFragment newInstance(String text, int number) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView movieClick = view.findViewById(R.id.movie_click);
        TextView tvClick = view.findViewById(R.id.tv_click);
        TextView footer = view.findViewById(R.id.footer);

        movieClick.setOnClickListener(v -> {
            if (filmType.equals("tv")) {
                Bundle result = new Bundle();
                result.putString("bundleKey", filmType);
                getParentFragmentManager().setFragmentResult("requestKey", result);
            }
        });
        tvClick.setOnClickListener(v -> {
            if (filmType.equals("movie")) {
                Bundle result = new Bundle();
                result.putString("bundleKey", filmType);
                getParentFragmentManager().setFragmentResult("requestKey", result);
            }
        });
        footer.setOnClickListener(v -> {
            String url = "https://www.themoviedb.org";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            getContext().startActivity(i);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView movieClick = view.findViewById(R.id.movie_click);
        TextView tvClick = view.findViewById(R.id.tv_click);

        // Init view model in fragment
        watchViewModel = new ViewModelProvider(requireActivity()).get(WatchViewModel.class);

        // Inflate menu in toolbar
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.myToolbar);
//        toolbar.inflateMenu(R.menu.toolbar_menu);

        // Get data from activity
        if (getArguments() != null) {
            filmType = getArguments().getString(ARG_TEXT);
            number = getArguments().getInt(ARG_NUMBER);
        }

//        watchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
//            @Override
//            public void onChanged(CharSequence charSequence) {
//                textView.setText(charSequence);
//            }
//        });

        if (filmType.equals("movie")) {
            tvClick.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            JSONArray currentMovies = watchViewModel.getCurrentMovie().getValue();
            JSONArray topMovies = watchViewModel.getTopMovie().getValue();
            JSONArray popMovies = watchViewModel.getPopMovie().getValue();

            createSliderView(view, currentMovies);
            createScrollerView(view, topMovies, popMovies);
        } else {
            movieClick.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            JSONArray trendTv = watchViewModel.getTrendTv().getValue();
            JSONArray topTv = watchViewModel.getTopTv().getValue();
            JSONArray popTv = watchViewModel.getPopTv().getValue();

            createSliderView(view, trendTv);
            createScrollerView(view, topTv, popTv);
        }
        return view;
    }

    private void createScrollerView(View view, JSONArray topMovies, JSONArray popMovies) {

        ArrayList<CardData> cardTopDataArrayList = new ArrayList<>();
        ArrayList<CardData> cardPopDataArrayList = new ArrayList<>();
        RecyclerView topScrollView = view.findViewById(R.id.top_scroller);
        RecyclerView popScrollView = view.findViewById(R.id.pop_scroller);

        try {
            for (int i = 0; i < topMovies.length(); i++) {
                JSONObject mObject = topMovies.getJSONObject(i);
                String posterPath = mObject.getString("poster_path");
                String filmType = mObject.getString("media_type");
                String filmName = mObject.getString("name");
                int filmId = mObject.getInt("id");
                boolean add = false;
                cardTopDataArrayList.add(new CardData(posterPath, add, filmType, filmName, filmId));
            }

            for (int i = 0; i < popMovies.length(); i++) {
                JSONObject mObject = popMovies.getJSONObject(i);
                String posterPath = mObject.getString("poster_path");
                String filmType = mObject.getString("media_type");
                String filmName = mObject.getString("name");
                int filmId = mObject.getInt("id");
                boolean add = false;
                cardPopDataArrayList.add(new CardData(posterPath, add, filmType, filmName, filmId));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager topLayoutManager = new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        ScrollerAdapter topAdapter = new ScrollerAdapter(this.getContext(), cardTopDataArrayList, "detail");
        topScrollView.setLayoutManager(topLayoutManager);
        topScrollView.setAdapter(topAdapter);

        RecyclerView.LayoutManager popLayoutManager = new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        ScrollerAdapter popAdapter = new ScrollerAdapter(this.getContext(), cardPopDataArrayList, "detail");
        popScrollView.setLayoutManager(popLayoutManager);
        popScrollView.setAdapter(popAdapter);
    }

    private void createSliderView(View view, JSONArray currentMovies) {
//        FrameLayout homeLayout = view.findViewById(R.id.home_layout);
//        homeLayout.setVisibility(View.INVISIBLE);


        // we are creating array list for storing our image urls.
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = view.findViewById(R.id.slider);

        try {
            for (int i = 0; i < currentMovies.length(); i++) {
                JSONObject mObject = currentMovies.getJSONObject(i);
                String posterPath = mObject.getString("poster_path");
                String filmType = mObject.getString("media_type");
                int filmId = mObject.getInt("id");
                String filmName = mObject.getString("name");
                sliderDataArrayList.add(new SliderData(posterPath, filmType, filmId, filmName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();

    }
}
