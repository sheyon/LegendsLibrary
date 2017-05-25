package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class LoreFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lore, container, false);

//        int categoryNumber = getIntent().getIntExtra("catPosition", 0);
//        String categoryString = getIntent().getStringExtra("catName");
//        String titleString = getIntent().getStringExtra("searchParam");
//
//        String[] selectionArgs = { Integer.toString(categoryNumber), titleString };
//        Cursor cursor = legendsDB.rawQuery(Queries.SINGLE_LORE, selectionArgs);
//
//        if (cursor != null)
//        {
//            cursor.moveToFirst();
//
//            String buzzingText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_BUZZING));
//            String blackSignalText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_BLACK_SIGNAL));
//
//            TextView titleTextview = (TextView) view.findViewById(R.id.loreActivity_title_text_view);
//            TextView categoryTextview = (TextView) view.findViewById(R.id.loreActivity_category_text_view);
//            TextView buzzingTextview = (TextView) view.findViewById(R.id.loreActivity_buzzing_text_view);
//
//            TextView blackSignalTextview = (TextView) view.findViewById(R.id.loreActivity_signal_text_view);
//            ImageView blackSignalImageview = (ImageView) view.findViewById(R.id.loreActivity_signal_image_view);
//
//            titleTextview.setText(titleString);
//            categoryTextview.setText(categoryString);
//            buzzingTextview.setText(buzzingText);
//            blackSignalTextview.setText(blackSignalText);
//
//            if (blackSignalText != null) {
//                blackSignalTextview.setVisibility(View.VISIBLE);
//                blackSignalImageview.setVisibility(View.VISIBLE);
//            }
//
//            cursor.close();
//
//        }

        return view;
    }
}
