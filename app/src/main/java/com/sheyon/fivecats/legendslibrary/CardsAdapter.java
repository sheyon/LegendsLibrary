package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CardsAdapter extends ArrayAdapter {

    Context mContext;
    String[] mItems;
    String[] mAltItems;
    ViewHolder viewHolder;
    int mPosition;


    private static class ViewHolder {
        ImageView mImageView;
    }

    public CardsAdapter(@NonNull Context context, int resource, @NonNull String[] items, @Nullable String[] altItems) {
        super(context, resource, items);
        mContext = context;
        mItems = items;
        mAltItems = altItems;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        mPosition = position;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cards_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mImageView = convertView.findViewById(R.id.cards_imageView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mImageView.setImageResource(getImageId(mContext, mItems[position]));

        viewHolder.mImageView.setOnClickListener(v -> {

        });
        viewHolder.mImageView.setOnLongClickListener(v -> {
            viewHolder.mImageView.setImageResource(getImageId(mContext, mAltItems[position]));
            return false;
        });
        return convertView;
    }

    private static int getImageId(Context context, String imageResource) {
        return context.getResources().getIdentifier(imageResource, "drawable", context.getPackageName());
    }
}
