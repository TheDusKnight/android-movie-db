package com.example.trevormobilefilm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String filmType = bundle.getString(SliderAdapter.FILM_TYPE);
        int filmId = bundle.getInt(SliderAdapter.FILM_ID);

        TextView textView = findViewById(R.id.detailText);
        textView.setText(filmId + filmType);


    }
}