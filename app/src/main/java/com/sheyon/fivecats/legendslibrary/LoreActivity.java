package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;

public class LoreActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lore);

        int categoryNumber = getIntent().getIntExtra("catPosition", 0);
        String categoryString = getIntent().getStringExtra("catName");
        String titleString = getIntent().getStringExtra("searchParam");

        LegendsHelper legendsHelper = new LegendsHelper(this);
        SQLiteDatabase legendsDB = legendsHelper.getReadableDatabase();

        String[] selectionArgs = { Integer.toString(categoryNumber), titleString };
        Cursor cursor = legendsDB.rawQuery(Queries.SINGLE_LORE, selectionArgs);
        cursor.moveToFirst();

        String buzzingText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_BUZZING));
        String blackSignalText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_BLACK_SIGNAL));

        TextView titleTextview = (TextView) findViewById(R.id.loreActivity_title_text_view);
        TextView categoryTextview = (TextView) findViewById(R.id.loreActivity_category_text_view);
        TextView buzzingTextview = (TextView) findViewById(R.id.loreActivity_buzzing_text_view);

        TextView blackSignalTextview = (TextView) findViewById(R.id.loreActivity_signal_text_view);
        ImageView blackSignalImageview = (ImageView) findViewById(R.id.loreActivity_signal_image_view);

        titleTextview.setText(titleString);
        categoryTextview.setText(categoryString);
        buzzingTextview.setText(buzzingText);
        blackSignalTextview.setText(blackSignalText);

        if (blackSignalText != null)
        {
            blackSignalTextview.setVisibility(View.VISIBLE);
            blackSignalImageview.setVisibility(View.VISIBLE);
        }

        cursor.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
