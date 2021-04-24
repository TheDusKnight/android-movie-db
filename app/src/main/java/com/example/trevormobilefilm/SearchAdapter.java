package com.example.trevormobilefilm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchAdapter extends  RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    static final String FILM_ID = "filmId";
    static final String FILM_TYPE = "filmType";
    static final String FILM_NAME = "filmName";
    private final List<CardData> mScrollerItems;
    public Context mContext;

    public SearchAdapter(Context context, ArrayList<CardData> cardList) {
        mContext = context;
        mScrollerItems = cardList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.search_card, parent, false);
        return new SearchViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        CardData currentItem = mScrollerItems.get(position);
        holder.mTitle.setText(currentItem.getFilmName());
        String dateString = currentItem.getDate();
        String year;
        String typeYear;
        if (!currentItem.getDate().equals("") || currentItem.getDate() != null) {
            year = Arrays.asList(dateString.split("-")).get(0);
            typeYear = currentItem.getFilmType() + " (" + year + " )";
        } else {
            typeYear = currentItem.getFilmType();
        }
        holder.mTypeYear.setText(typeYear);
        holder.mRate.setText(String.valueOf(currentItem.getRate()));
        Picasso.get().load(currentItem.getImgUrl())
                .into(holder.mImageView);

        holder.mImageView.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putInt(FILM_ID, currentItem.getFilmId());
            result.putString(FILM_NAME, currentItem.getFilmName());
            result.putString(FILM_TYPE, currentItem.getFilmType());
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtras(result);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mScrollerItems.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTypeYear;
        public TextView mRate;
        public TextView mTitle;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.search_card);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mTypeYear = itemView.findViewById(R.id.search_type);
            mRate = itemView.findViewById(R.id.search_rate);
            mTitle = itemView.findViewById(R.id.search_title);
        }
    }
}
