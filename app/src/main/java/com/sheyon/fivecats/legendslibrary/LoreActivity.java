package com.sheyon.fivecats.legendslibrary;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;

import java.util.Locale;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class LoreActivity extends AppCompatActivity
{
    private String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lore);

        Toolbar toolbar = (Toolbar) findViewById(R.id.loreActivity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int categoryNumber = getIntent().getIntExtra("catNumber", 0);
        String categoryString = getIntent().getStringExtra("catName");
        String titleString = getIntent().getStringExtra("loreTitle");
        searchString = getIntent().getStringExtra("searchString");

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

        //DID YOU COME HERE FROM THE SEARCH TAB? IF NOT, SEARCH STRING SHOULD BE NULL
        if (searchString == null) {
            buzzingTextview.setText(buzzingText);
            blackSignalTextview.setText(blackSignalText);
        }
        else {
            //IF YOU WERE LOOKING FOR SOMETHING, HIGHLIGHT IT
            highlight(buzzingText, buzzingTextview);
            if (blackSignalText != null) {
                highlight(blackSignalText, blackSignalTextview);
            }
        }

        //A LORE MAY OR MAY NOT HAVE A BLACK SIGNAL TO DISPLAY
        if (blackSignalText != null) {
            blackSignalTextview.setVisibility(View.VISIBLE);
            blackSignalImageview.setVisibility(View.VISIBLE);
        }

        cursor.close();
    }

    private CharSequence highlight(String originalText, TextView textView) {

        String normalizedText = originalText.toLowerCase(Locale.US);

        ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
        //TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);

        int start = normalizedText.indexOf(searchString);

        if (start < 0) {
            textView.setText(originalText);
            textView.setVisibility(View.VISIBLE);
            return null;
        } else {
            Spannable highlighted = new SpannableString(originalText);

            while (start >= 0) {
                //GETS THE START AND END POSITIONS OF THE WORD TO BE HIGHLIGHTED
                int spanStart = Math.min(start, originalText.length());
                int spanEnd = Math.min(start + searchString.length(), originalText.length());

                //HIGHLIGHTS THE WORD
                highlighted.setSpan(new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null),
                        spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(highlighted);
                textView.setVisibility(View.VISIBLE);

                //SETS THE NEW START POINT
                start = normalizedText.indexOf(searchString, spanEnd);
            }
            return null;
        }
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return false;
    }
}