package com.example.trevormobilefilm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // Get Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String rate = bundle.getString("rateKey");
        String name = bundle.getString("nameKey");
        String date = bundle.getString("dateKey");
        String content = bundle.getString("contentKey");
        String title = "by " + name + " on " + date;

        LinearLayout ll = findViewById(R.id.full_review_layout);
        TextView rateView = findViewById(R.id.full_review_rate);
        TextView titleView = findViewById(R.id.full_review_title);
        TextView contentView = findViewById(R.id.full_review_content);
        rateView.setText(rate);
        titleView.setText(title);
        contentView.setText(content);
    }
}