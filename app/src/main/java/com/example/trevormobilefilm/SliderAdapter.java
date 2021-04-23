package com.example.trevormobilefilm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {
    static final String FILM_ID = "filmId";
    static final String FILM_TYPE = "fileType";

    // list for storing urls of images.
    private final List<SliderData> mSliderItems;
    public Context context;

    // Constructor
    public SliderAdapter(HomeFragment fragment, ArrayList<SliderData> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        this.context = fragment.getContext();
    }
    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }
    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final SliderData sliderItem = mSliderItems.get(position);

        // Glide is use to load image
        // from url in your image view.
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImgUrl())
//                .transform(new BlurTransformation(viewHolder.itemView.getContext()))
                .transform(
                        new MultiTransformation(
                            new jp.wasabeef.glide.transformations.BlurTransformation(25, 2)
                        ))
                .into(viewHolder.imageViewBackground);

        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImgUrl())
                .into(viewHolder.imageViewFront);

        // set OnClickListener for current image
        Bundle args = new Bundle();
        args.putString(FILM_TYPE, sliderItem.getFilmType());
        args.putInt(FILM_ID, sliderItem.getFilmId());
        viewHolder.imageViewFront.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtras(args);
            context.startActivity(intent);
        });
        viewHolder.imageViewBackground.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtras(args);
            context.startActivity(intent);
        });
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;
        ImageView imageViewFront;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.top_image_back);
            imageViewFront = itemView.findViewById(R.id.top_image_front);
            this.itemView = itemView;

//            imageViewFront.setOnClickListener(v -> {
//                Intent intent = new Intent(SliderAdapter.this.context, DetailActivity.class);
//                SliderAdapter.this.context.startActivity(intent);
//            });
        }
    }
}

