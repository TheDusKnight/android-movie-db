package com.example.trevormobilefilm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
        // Set default add boolean
        AtomicBoolean add = new AtomicBoolean(false);

        Picasso.get().load(currentItem.getImgUrl())
                .transform(new RoundedTransformation(60, 0))
                .into(holder.mImageView);

        holder.mMenu.setOnClickListener( v -> {
            // Get cached films from storage
            List<Integer> orderList = loadData();
            // Set add true or false based on orderList
            add.set(orderList.contains((currentItem.getFilmId())));
            // Initializing the popup menu and giving the reference as current context
            PopupMenu popupMenu = new PopupMenu(mContext, holder.mMenu);
            // Inflating popup menu from popup_menu.xml file
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            // Set menu title whenever click the menu button
            Menu menuOpts = popupMenu.getMenu();
            // Set title based on storage
            if (add.get()) {
                menuOpts.findItem(R.id.menu_watchlist).setTitle("Remove from Watchlist");
            } else {
                menuOpts.findItem(R.id.menu_watchlist).setTitle("Add to Watchlist");
            }
            // Set popup menu click listener
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                // Toast message on menu item clicked
                switch (menuItem.getItemId()) {
                    case R.id.menu_tmdb:
                        return true;
                    case R.id.menu_facebook:
                        return true;
                    case R.id.menu_twitter:
                        return true;
                    case R.id.menu_watchlist:
                        if (add.get()) {
                            Toast.makeText(mContext, currentItem.getFilmName() +
                                    " was removed from Watchlist", Toast.LENGTH_SHORT).show();
                            add.set(false);
                            orderList.remove(Integer.valueOf(currentItem.getFilmId()));
                        } else {
                            Toast.makeText(mContext, currentItem.getFilmName() +
                                    " was added to Watchlist", Toast.LENGTH_SHORT).show();
                            add.set(true);
                            orderList.add(currentItem.getFilmId());
                        }
                        // TODO: orderList 写入 sharedPreference
                        saveDate(orderList, currentItem);
                        return true;
                    default:
                        return false;
                }
            });
            // Showing the popup menu
            popupMenu.show();
        });

        // set OnClickListener for current image
        Bundle args = new Bundle();
        args.putString(FILM_TYPE, currentItem.getFilmType());
        args.putInt(FILM_ID, currentItem.getFilmId());
        holder.mImageView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtras(args);
            mContext.startActivity(intent);
        });
    }

    private List<Integer> loadData() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sharePrefs", Context.MODE_PRIVATE);
        String jsonOrderList = sharedPreferences.getString("orderList", "[]");
        return new ArrayList<>(Arrays.asList(gson.fromJson(jsonOrderList, Integer[].class)));
    }

    private void saveDate(List<Integer> orderList, CardData currentItem) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sharePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonList = gson.toJson(orderList);
        editor.putString("orderList", jsonList);
        // Add additional info to storage about the added film
        List<String> filmList = new ArrayList<>();
        filmList.add(currentItem.getFilmName());
        filmList.add(currentItem.getFilmType());
        editor.putString(String.valueOf(currentItem.getFilmId()), gson.toJson(filmList));
        editor.apply();
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
            // itemView means Inflated template xml
            super(itemView);
            mImageView = itemView.findViewById(R.id.detail_card);
            mMenu = itemView.findViewById(R.id.detail_menu);
            this.mItemView = itemView;
        }
    }
}
