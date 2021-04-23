package com.example.trevormobilefilm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ScrollerAdapter extends RecyclerView.Adapter<ScrollerAdapter.ScrollerViewHolder> {
    static final String FILM_ID = "filmId";
    static final String FILM_TYPE = "fileType";
    // list for storing urls of images.
    private final List<CardData> mScrollerItems;
    public Context mContext;

    public ScrollerAdapter(Context context, ArrayList<CardData> cardList) {
        mContext = context;
        mScrollerItems = cardList;
    }

    @NonNull
    @Override
    public ScrollerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.detail_card, parent, false);
        return new ScrollerViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ScrollerViewHolder holder, int position) {
        CardData currentItem = mScrollerItems.get(position);

        Glide.with(holder.mItemView).load(currentItem.getImgUrl()).into(holder.mImageView);
        holder.mMenu.setText(currentItem.getFilmId() + "");
    }

    @Override
    public int getItemCount() {
        return mScrollerItems.size();
    }

    public static class ScrollerViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public ImageView mImageView;
        public TextView mMenu;

        public ScrollerViewHolder(@NonNull View itemView) {
            // itemView = Inflated template xml
            super(itemView);
            mImageView = itemView.findViewById(R.id.detail_card);
            mMenu = itemView.findViewById(R.id.detail_menu);
            this.mItemView = itemView;
        }
    }
}
