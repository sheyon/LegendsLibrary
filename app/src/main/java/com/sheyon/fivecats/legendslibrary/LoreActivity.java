package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsDatabase;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

import java.text.Normalizer;
import java.util.Locale;

public class LoreActivity extends AppCompatActivity implements View.OnClickListener
{
    private SQLiteDatabase db;

    private Boolean startupComplete = false;
    private Boolean wildcardFlag = false;

    private String searchString;
    private String titleString;
    private String fullTitleString;
    private String buzzingText;
    private String blackSignalText;
    private String categoryString;
    private int faved;

    private ScrollView scrollView;

    private ImageView favedImageView;
    private LegendsPreferences legendsPrefs;

    private static class ViewHolder {
        private LinearLayout mImageLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        legendsPrefs = LegendsPreferences.getInstance(this);

        int categoryNumber = getIntent().getIntExtra("catNumber", 0);
        titleString = getIntent().getStringExtra("loreTitle");
        searchString = getIntent().getStringExtra("searchString");

        //NULL CATCH; IT SHOULD NEVER HAPPEN, BUT IT DO
        if (titleString == null) {
            titleString = legendsPrefs.getLoreTitle();
        }

        //NULL CATCH; IT SHOULD NEVER HAPPEN, BUT IT DO
        if (categoryNumber == 0) {
            String [] catchArgs = { titleString, titleString };
            Cursor catchCursor = db.rawQuery(Queries.CAT_ID_CATCH, catchArgs);
            if (catchCursor != null) {
                catchCursor.moveToFirst();
                categoryNumber = catchCursor.getInt(catchCursor.getColumnIndex(LoreLibrary.COLUMN_CATEGORY_ID));
                catchCursor.close();
            }
        }

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] union = { Queries.SINGLE_LORE_UNION_1, Queries.SINGLE_LORE_UNION_2 };
        String joinedQuery = qb.buildUnionQuery(union, null, null);

        String[] selectionArgs = { Integer.toString(categoryNumber), titleString, Integer.toString(categoryNumber), titleString };

        db = new LegendsDatabase().getInstance(this);
        Cursor cursor = db.rawQuery(joinedQuery, selectionArgs);
        if (cursor != null) {
            cursor.moveToFirst();

            buzzingText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_BUZZING));
            blackSignalText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_BLACK_SIGNAL));
            categoryString = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_CATEGORY_NAME));
            faved = cursor.getInt(cursor.getColumnIndex(LoreLibrary.COLUMN_FAVED));

            //FULL TITLE STRING IS FOR THE FAVE TOASTS; SAVE IT SO YOU CAN USE IT LATER
            fullTitleString = titleString;
            //TRUNCATES THE TITLE SO PREFIXES DON'T BREAK FAVES
            titleString = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_TITLE));

            cursor.close();
        }

        setContentView(R.layout.activity_lore);

        Toolbar toolbar = (Toolbar) findViewById(R.id.loreActivity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        scrollView = (ScrollView) findViewById(R.id.loreActivity_scrollView);

        TextView titleTextView = (TextView) findViewById(R.id.loreActivity_title_text_view);
        TextView categoryTextView = (TextView) findViewById(R.id.loreActivity_category_text_view);
        TextView buzzingTextView = (TextView) findViewById(R.id.loreActivity_buzzing_text_view);
        favedImageView = (ImageView) findViewById(R.id.loreActivity_fave_imageView);

        LinearLayout blackSignalLayout = (LinearLayout) findViewById(R.id.loreActivity_BlackSignalLayout);
        TextView blackSignalTextView = (TextView) findViewById(R.id.loreActivity_signal_text_view);

        LinearLayout faveClickable = (LinearLayout) findViewById(R.id.loreActivity_fave_clickable);
        ViewHolder holder = new ViewHolder();
        holder.mImageLayout = faveClickable;
        holder.mImageLayout.setOnClickListener(this);

        titleTextView.setText(fullTitleString);
        categoryTextView.setText(categoryString);

        setStar(faved);
        initiateHighlighter(buzzingTextView, buzzingText, blackSignalTextView, blackSignalText);

        //A LORE MAY OR MAY NOT HAVE A BLACK SIGNAL TO DISPLAY
        if (blackSignalText != null) {
            blackSignalLayout.setVisibility(View.VISIBLE);
        }

        setFlavorImage(titleString);
        adjustFontSize(buzzingTextView, blackSignalTextView);

        determineWidth();

        startupComplete = true;
    }

    private void adjustFontSize(TextView buzzingTextView, TextView blackSignalTextView){
        //IF FONT SIZE PREFS DO NOT EXIST, CREATE THEM (DEFAULT: 0)
        if (!legendsPrefs.doesContain(LegendsPreferences.PREF_FONT_SIZE)) {
            legendsPrefs.setFontSizePref(0);
        }

        switch (legendsPrefs.getFontSizePref()) {
            //SetLineSpacingMultiplier does not seem to work from the Styles.XML; SET PROGRAMATICALLY!
            case 0:
                TextViewCompat.setTextAppearance(buzzingTextView, R.style.LoreDefault);
                TextViewCompat.setTextAppearance(blackSignalTextView, R.style.LoreDefault);
                break;
            case 1:
                buzzingTextView.setLineSpacing(0, 1.1f);
                blackSignalTextView.setLineSpacing(0, 1.1f);
                TextViewCompat.setTextAppearance(buzzingTextView, R.style.LoreBigger);
                TextViewCompat.setTextAppearance(blackSignalTextView, R.style.LoreBigger);
                break;
            case 2:
                buzzingTextView.setLineSpacing(0, 1.25f);
                blackSignalTextView.setLineSpacing(0, 1.25f);
                TextViewCompat.setTextAppearance(buzzingTextView, R.style.LoreBiggest);
                TextViewCompat.setTextAppearance(blackSignalTextView, R.style.LoreBiggest);
                break;
            default:
                TextViewCompat.setTextAppearance(buzzingTextView, R.style.LoreDefault);
                TextViewCompat.setTextAppearance(blackSignalTextView, R.style.LoreDefault);
                break;
        }
    }

    private void initiateHighlighter(TextView buzzingTextView, String buzzingText, TextView blackSignalTextView, String blackSignalText) {
        //DID YOU COME HERE FROM THE SEARCH TAB? IF NOT, SEARCH STRING SHOULD BE NULL
        if (searchString == null) {
            buzzingTextView.setText(buzzingText);
            blackSignalTextView.setText(blackSignalText);
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
            highlight(buzzingText, buzzingTextView);
            if (blackSignalText != null) {
                highlight(blackSignalText, blackSignalTextView);
            }
        }
    }

    private void setFlavorImage(String titleString) {
        ImageView flavorImageView = (ImageView) findViewById(R.id.lore_flavor_image);
        String[] selectionArgs = { titleString };
        Cursor flavorImageCursor = db.rawQuery(Queries.GET_IMAGE, selectionArgs);

        if (flavorImageCursor != null)
        {
            flavorImageCursor.moveToFirst();
            String imageResource = flavorImageCursor.getString(flavorImageCursor.getColumnIndex(LoreLibrary.COLUMN_IMAGE));

            if (imageResource != null){
                flavorImageView.setImageResource(getImageId(this, imageResource));
            }

            flavorImageCursor.close();
        }

        showFlavorImage(flavorImageView);
    }

    static int getImageId(Context context, String imageResource) {
        return context.getResources().getIdentifier("drawable/" + imageResource, null, context.getPackageName());
    }

    private void showFlavorImage(ImageView imageView) {
        LegendsPreferences legendsPreferences = LegendsPreferences.getInstance(this);

        //IF PREFS DON'T EXIST, CREATE THEM. (DEFAULT: SHOW IMAGES)
        if (!legendsPreferences.doesContain(LegendsPreferences.PREF_SHOW_IMAGES)) {
            legendsPreferences.setImagePref(true);
        }

        //HIDE IMAGE IF NEEDED
        if (!legendsPreferences.getImagePref()) {
            imageView.setVisibility(View.GONE);
        }
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
            return null;
        }
        else {
            Spannable highlighted = new SpannableString(originalText);

            //THIS PREVENTS THE HIGHLIGHTER FROM FINDING A SINGLE HIT THEN ABORTING, RESULTING IN AN EMPTY STRING
            textView.setText(originalText);

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
            db.execSQL(Queries.UPDATE_FAVE + modTitleString + ";");

            //GET UPDATED CURSOR TO SET THE NEW FAVED STATE
            String[] selectionArgs = { titleString };
            Cursor cursor = db.rawQuery(Queries.GET_FAVE, selectionArgs);
            if (cursor != null) {
                cursor.moveToFirst();

                int faved = cursor.getInt(cursor.getColumnIndex(LoreLibrary.COLUMN_FAVED));
                setStar(faved);

                cursor.close();
            }
        }
    }

    private void determineWidth() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.loreActivity_relativeLayout);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int w = metrics.widthPixels;
        int h = metrics.heightPixels;
        boolean portrait;

        //SAVE THE WIDTH OF DEVICE IN PORTRAIT MODE
        float trueWidth;
        if (w > h) {
            trueWidth = h;
            portrait = false;
        }
        else {
            trueWidth = w;
            portrait = true;
        }

        //SO THE TOOLBAR STRETCHES ACROSS THE LENGTH OF THE SCREEN
        relativeLayout.setLayoutParams(new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        //SET SCROLLVIEW PARAMS
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams((int) trueWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.BELOW, R.id.loreActivity_toolbar);
        p.addRule(RelativeLayout.CENTER_IN_PARENT);
        scrollView.setLayoutParams(p);

        if (!portrait) {
            relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundDarker));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        legendsPrefs.setLorePagePosition(scrollView.getScrollY());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Stupid hackjob. Why can't this work the proper way?
        scrollView = (ScrollView) findViewById(R.id.loreActivity_scrollView);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, legendsPrefs.getLorePagePosition());
                    }
                }, 200);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        legendsPrefs.setLorePagePosition(0);
    }
}