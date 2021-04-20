package com.example.trevormobilefilm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment {
    private static final String ARG_TEXT = "argText";
    private static final String ARG_NUMBER = "argNumber";
    private String filmType;
    private int number;

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
        TextView movieClick = (TextView) view.findViewById(R.id.movie_click);
        TextView tvClick = (TextView) view.findViewById(R.id.tv_click);

        movieClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filmType.equals("tv")) {
                    Bundle result = new Bundle();
                    result.putString("bundleKey", filmType);
                    getParentFragmentManager().setFragmentResult("requestKey", result);
                }
            }
        });
        tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filmType.equals("movie")) {
                    Bundle result = new Bundle();
                    result.putString("bundleKey", filmType);
                    getParentFragmentManager().setFragmentResult("requestKey", result);
                }
            }
        });

//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.myToolbar);
//        toolbar.setOnMenuItemClickListener(item -> {
//            switch (item.getItemId()) {
//                // Navigate to different tabs
//                case R.id.movie_tab:
//                    textView.setText("movie tab clicked");
//                    return true;
//                case R.id.tv_tab:
//                    textView.setText("tv tab clicked");
//                    return true;
//                default:
//                    return false;
//            }
//        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = view.findViewById(R.id.home_text);
        TextView movieClick = view.findViewById(R.id.movie_click);
        TextView tvClick = view.findViewById(R.id.tv_click);

        // Inflate menu in toolbar
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.myToolbar);
//        toolbar.inflateMenu(R.menu.toolbar_menu);

        // Get data from activity
        if (getArguments() != null) {
            filmType = getArguments().getString(ARG_TEXT);
            number = getArguments().getInt(ARG_NUMBER);
        }
        textView.setText("fragment" + number);

        if (filmType.equals("movie")) {
            movieClick.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        } else {
            tvClick.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }

        return view;
    }
}
