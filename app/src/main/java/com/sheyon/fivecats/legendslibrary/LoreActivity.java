package com.sheyon.fivecats.legendslibrary;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

import java.text.Normalizer;
import java.util.Locale;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class LoreActivity extends AppCompatActivity implements View.OnClickListener
{
    private Boolean startupComplete = false;
    private Boolean wildcardFlag = false;
    private String searchString;
    private String titleString;
    private String fullTitleString;
    private ImageView favedImageView;
    private LegendsPreferences legendsPrefs;

    private static class ViewHolder {
        private LinearLayout mImageLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lore);

        legendsPrefs = LegendsPreferences.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.loreActivity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int categoryNumber = getIntent().getIntExtra("catNumber", 0);
        titleString = getIntent().getStringExtra("loreTitle");
        searchString = getIntent().getStringExtra("searchString");

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] union = { Queries.SINGLE_LORE_UNION_1, Queries.SINGLE_LORE_UNION_2 };
        String joinedQuery = qb.buildUnionQuery(union, null, null);

        String[] selectionArgs = { Integer.toString(categoryNumber), titleString, Integer.toString(categoryNumber), titleString };
        Cursor cursor = legendsDB.rawQuery(joinedQuery, selectionArgs);
        cursor.moveToFirst();

        String buzzingText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_BUZZING));
        String blackSignalText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_BLACK_SIGNAL));
        String categoryString = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_CATEGORY_NAME));
        int faved = cursor.getInt(cursor.getColumnIndex(LoreLibrary.COLUMN_FAVED));

        LinearLayout faveClickable = (LinearLayout) findViewById(R.id.loreActivity_fave_clickable);
        ViewHolder holder = new ViewHolder();
        holder.mImageLayout = faveClickable;
        holder.mImageLayout.setOnClickListener(this);

        TextView titleTextview = (TextView) findViewById(R.id.loreActivity_title_text_view);
        TextView categoryTextview = (TextView) findViewById(R.id.loreActivity_category_text_view);
        TextView buzzingTextview = (TextView) findViewById(R.id.loreActivity_buzzing_text_view);
        favedImageView = (ImageView) findViewById(R.id.loreActivity_fave_imageView);

        TextView blackSignalTextview = (TextView) findViewById(R.id.loreActivity_signal_text_view);
        ImageView blackSignalImageview = (ImageView) findViewById(R.id.loreActivity_signal_image_view);

        titleTextview.setText(titleString);
        categoryTextview.setText(categoryString);

        //FULL TITLE STRING IS FOR THE FAVE TOASTS; SAVE IT SO YOU CAN USE IT LATER
        fullTitleString = titleString;
        //TRUNCATES THE TITLE SO PREFIXES DON'T BREAK FAVES
        titleString = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_TITLE));

        setStar(faved);

        //DID YOU COME HERE FROM THE SEARCH TAB? IF NOT, SEARCH STRING SHOULD BE NULL
        if (searchString == null) {
            buzzingTextview.setText(buzzingText);
            blackSignalTextview.setText(blackSignalText);
        }
        //IF YOU WERE LOOKING FOR SOMETHING...
        else {
            //SANITIZE THE SEARCH STRING
            if (searchString.startsWith("*") || searchString.endsWith("*")) {
                searchString = searchString.replace("*", " ").trim();
                wildcardFlag = true;
            }
            //SINGLE-QUOTES WILL NOT CRASH THE SEARCH BUT IT WILL KEEP THE HIGHIGHTER FROM WORKING
            if (searchString.startsWith("'") || searchString.endsWith("'")) {
                searchString = searchString.replace("'", " ").trim();
            }
            //THEN HIGHLIGHT YOUR RESULTS
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
        startupComplete = true;
    }

    private CharSequence highlight(String originalText, TextView textView) {
        Boolean normalize = legendsPrefs.getNormalizationPref();

        //IF ROMANIAN CHARACTERS FOUND, OVERRIDE NORMALIZATION (ENGLISH ONLY)
        if ( legendsPrefs.getLangPref() == 0 &&
                ( searchString.contains("ș") || searchString.contains("ă") || originalText.contains("ș") || originalText.contains("ă")) ) {
            searchString = searchString.replaceAll("ș", "s");
            searchString = searchString.replaceAll("ă", "a");
            normalize = true;
        }

        //BOTH NORMAL AND UN-NORMALIZED MODES MUST BE IN LOWER-CASE!
        String normalizedText = originalText.toLowerCase(Locale.getDefault());

        if (normalize) {
            //REMOVE ALL DIACRITICS
            normalizedText = Normalizer.normalize(normalizedText, Normalizer.Form.NFD);
            normalizedText = normalizedText.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        }

        ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.CYAN});
        //TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);

        int start = normalizedText.indexOf(searchString);

        if (start < 0) {
            textView.setText(originalText);
            textView.setVisibility(View.VISIBLE);
            return null;
        }
        else {
            Spannable highlighted = new SpannableString(originalText);

            //THIS PREVENTS THE HIGHLIGHTER FROM FINDING A SINGLE HIT THEN ABORTING, RESULTING IN AN EMPTY STRING
            textView.setText(originalText);
            textView.setVisibility(View.VISIBLE);

            while (start >= 0) {
                //GETS THE START AND END POSITIONS OF THE WORD TO BE HIGHLIGHTED
                int spanStart = Math.min(start, originalText.length());
                int spanEnd = Math.min(start + searchString.length(), originalText.length());

                //HIGHLIGHTS THE WORD
                TextAppearanceSpan span = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                highlighted.setSpan(span, spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                //Log.v ("***DEBUG", "a" + highlighted.charAt(spanStart-1) + "b");
                //Log.v ("***DEBUG", "c" + highlighted.charAt(spanEnd) + "d");

                //THIS PREVENTS THE HIGHLIGHTER FROM MARKING RESULTS IN THE MIDDLE OF A WORD
                if (!wildcardFlag){
                    //WILDCARD-OFF WILL RETURN WHOLE WORDS ONLY
                    if ( (highlighted.charAt(spanStart - 1) == ' ' || highlighted.charAt(spanStart - 1) == '\n' || highlighted.charAt(spanStart - 1) == '-' ||
                    highlighted.charAt(spanStart - 1) == '\"' || highlighted.charAt(spanStart - 1) == '\'') &&
                            (highlighted.charAt(spanEnd) == ' ' || highlighted.charAt(spanEnd) == '.' || highlighted.charAt(spanEnd) == ',' ||
                                    highlighted.charAt(spanEnd) == '\'' || highlighted.charAt(spanEnd) == '-' || highlighted.charAt(spanEnd) == '?' ||
                                    highlighted.charAt(spanEnd) == '!' || highlighted.charAt(spanEnd) == ';' || highlighted.charAt(spanEnd) == ':' ||
                                    highlighted.charAt(spanEnd) == '\"' ) ) {
                        textView.setText(highlighted);
                    }
                    else {
                        highlighted.removeSpan(span);
                    }
                }
                else {
                    //DOUBLE WILDCARDS WILL RETURN *RESULT, *RESULT*, and RESULT*
                    if (legendsPrefs.getDoubleWildcardPref()) {
                        textView.setText(highlighted);
                    }
                    else {
                        //SIMPLE WILDCARD WILL RETURN ONLY RESULT*
                        if ( highlighted.charAt(spanStart - 1) == ' ' || highlighted.charAt(spanStart - 1) == '\n' || highlighted.charAt(spanStart - 1) == '-' ||
                                highlighted.charAt(spanStart - 1) == '\"' || highlighted.charAt(spanStart - 1) == '\'') {
                            textView.setText(highlighted);
                        }
                        else {
                            highlighted.removeSpan(span);
                        }
                    }
                }
                //SETS THE NEW START POINT AND THEN LOOP
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

    private void setStar(int faved) {
        if (faved == 0) {
            favedImageView.setImageResource(R.drawable.ic_star_border_white_48dp);
            if (startupComplete) {
                Toast.makeText(this, fullTitleString + " " + getString(R.string.fave_removed), Toast.LENGTH_SHORT).show();
            }
        }
        if (faved == 1) {
            favedImageView.setImageResource(R.drawable.ic_star_white_48dp);
            if (startupComplete) {
                Toast.makeText(this, fullTitleString + " " + getString(R.string.fave_added), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loreActivity_fave_clickable) {
            String modTitleString = "\"" + titleString + "\"";

            //EXECUTE UPDATE QUERY
            legendsDB.execSQL(Queries.UPDATE_FAVE + modTitleString + ";");

            //GET UPDATED CURSOR
            String[] selectionArgs = { titleString };
            Cursor cursor = legendsDB.rawQuery(Queries.GET_FAVE, selectionArgs);
            cursor.moveToFirst();

            int faved = cursor.getInt(cursor.getColumnIndex(LoreLibrary.COLUMN_FAVED));
            setStar(faved);

            cursor.close();
        }
    }
}