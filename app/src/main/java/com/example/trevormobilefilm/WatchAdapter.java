package com.example.trevormobilefilm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.WatchViewHolder> {
    static final String FILM_ID = "filmId";
    static final String FILM_TYPE = "filmType";
    static final String FILM_NAME = "filmName";
    private final List<CardData> mScrollerItems;
    public Context mContext;

    public WatchAdapter(Context context, ArrayList<CardData> cardList) {
        mContext = context;
        mScrollerItems = cardList;
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

        holder.mType.setText(currentItem.getFilmType());
        Picasso.get().load(currentItem.getImgUrl()).into(holder.mCardView);
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
            mType = itemView.findViewById(R.id.watch_type);
            mRemoveView = itemView.findViewById(R.id.watch_remove);
        }
    }
}
