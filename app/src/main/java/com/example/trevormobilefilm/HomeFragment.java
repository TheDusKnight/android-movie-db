package com.example.trevormobilefilm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        textView = view.findViewById(R.id.home_text);
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
        if (watchViewModel.getText().getValue() != null) {
            textView.setText(watchViewModel.getText().getValue());
        } else {
            textView.setText("view model not ready");
        }

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
            createSliderView(view, trendTv);
        }

        return view;
    }

    private void createScrollerView(View view, JSONArray topMovies, JSONArray popMovies) {

        ArrayList<CardData> cardDataArrayList = new ArrayList<>();
        RecyclerView scrollView = view.findViewById(R.id.top_scroller);

        try {
            for (int i = 0; i < topMovies.length(); i++) {
                JSONObject mObject = topMovies.getJSONObject(i);
                String posterPath = mObject.getString("poster_path");
                String filmType = mObject.getString("media_type");
                String filmName = mObject.getString("name");
                int filmId = mObject.getInt("id");
                // TODO: set "add" field and listener from ViewModel to change SharePreferences
                boolean add = false;
                cardDataArrayList.add(new CardData(posterPath, add, filmType, filmName, filmId));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ScrollerAdapter adapter = new ScrollerAdapter(this.getContext(), cardDataArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        scrollView.setLayoutManager(layoutManager);
//        scrollView.setItemAnimator(new DefaultItemAnimator());
        scrollView.setAdapter(adapter);
    }

    private void createSliderView(View view, JSONArray currentMovies) {

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
                sliderDataArrayList.add(new SliderData(posterPath, filmType, filmId));
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
