package com.example.trevormobilefilm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.WatchViewHolder> {
    static final String FILM_ID = "filmId";
    static final String FILM_TYPE = "filmType";
    static final String FILM_NAME = "filmName";
    private final List<CardData> mScrollerItems;
    public Context mContext;
    public Gson gson;
    public List<Integer> watchList;
    SharedPreferences.Editor editor;

    public WatchAdapter(Context context, ArrayList<CardData> cardList, List<Integer> watchList, Gson gson, SharedPreferences.Editor editor) {
        mContext = context;
        mScrollerItems = cardList;
        this.watchList = watchList;
        this.gson = gson;
        this.editor = editor;
    }

    @NonNull
    @Override
    public WatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.watch_card, parent, false);
        return new WatchViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchViewHolder holder, int position) {
        CardData currentItem = mScrollerItems.get(position);

        holder.mType.setText(capitalizeString(currentItem.getFilmType()));
        Picasso.get().load(currentItem.getImgUrl()).into(holder.mCardView);

        // Set OnClickListener for current icon
        holder.mRemoveView.setOnClickListener(v -> {
            mScrollerItems.remove(position);
            watchList.remove(position);
            String jsonList = gson.toJson(watchList);
            editor.putString("orderList", jsonList);
            editor.apply();
            notifyDataSetChanged();
            Toast.makeText(mContext, currentItem.getFilmName() +
                    " was removed from watchlist", Toast.LENGTH_SHORT).show();
        });

        // Set OnClickListener for current image
        Bundle args = new Bundle();
        args.putString(FILM_TYPE, currentItem.getFilmType());
        args.putInt(FILM_ID, currentItem.getFilmId());
        args.putString(FILM_NAME, currentItem.getFilmName());
        holder.mCardView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtras(args);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mScrollerItems.size();
    }

    public static class WatchViewHolder extends RecyclerView.ViewHolder {
        public ImageView mCardView;
        public TextView mType;
        public ImageView mRemoveView;

        public WatchViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.watch_card);
            mCardView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mType = itemView.findViewById(R.id.watch_type);
            mRemoveView = itemView.findViewById(R.id.watch_remove);
        }
    }

    public static String capitalizeString(String str) {
        String retStr = str;
        try { // We can face index out of bound exception if the string is null
            retStr = str.substring(0, 1).toUpperCase() + str.substring(1);
        } catch (Exception e){}
        return retStr;
    }
}
