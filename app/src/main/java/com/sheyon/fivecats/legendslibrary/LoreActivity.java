package com.sheyon.fivecats.legendslibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.widget.TextViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sheyon.fivecats.legendslibrary.data.LegendsConstants.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsConstants.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsDatabase;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

import java.text.Normalizer;
import java.util.Locale;

public class LoreActivity extends AppCompatActivity
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lore);

        legendsPrefs = LegendsPreferences.getInstance(this);
        db = LegendsDatabase.getInstance(this);

        titleString = getIntent().getStringExtra("loreTitle");
        searchString = getIntent().getStringExtra("searchString");

        //NULL FIX; IT SHOULD NEVER HAPPEN, BUT IT DO
        if (titleString == null) {
            Log.w("WARNING", "TitleString null! Using backup.");
            titleString = legendsPrefs.getLoreTitle();
        }

        //NULL FIX; IT SHOULD NEVER HAPPEN, BUT IT DO
        Cursor getTextCursor;
        String [] catNumArgs = { titleString, titleString };
        try {
            //throw new CursorIndexOutOfBoundsException("GO TO JAIL!");
            int categoryNumber = 0;
            Cursor catNumCursor = db.rawQuery(Queries.CAT_ID_CATCH, catNumArgs);

            if (catNumCursor != null && catNumCursor.moveToFirst()) {
                categoryNumber = catNumCursor.getInt(catNumCursor.getColumnIndex(LoreLibrary.COLUMN_CATEGORY_ID));
                catNumCursor.close();
            }

            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            String [] union = { Queries.SINGLE_LORE_UNION_1, Queries.SINGLE_LORE_UNION_2 };
            String joinedQuery = qb.buildUnionQuery(union, null, null);

            String[] selectionArgs = { Integer.toString(categoryNumber), titleString, Integer.toString(categoryNumber), titleString };

            getTextCursor = db.rawQuery(joinedQuery, selectionArgs);
            if (getTextCursor != null && getTextCursor.moveToFirst()) {
                buzzingText = getTextCursor.getString(getTextCursor.getColumnIndex(LoreLibrary.COLUMN_BUZZING));
                blackSignalText = getTextCursor.getString(getTextCursor.getColumnIndex(LoreLibrary.COLUMN_BLACK_SIGNAL));
                categoryString = getTextCursor.getString(getTextCursor.getColumnIndex(LoreLibrary.COLUMN_CATEGORY_NAME));
                faved = getTextCursor.getInt(getTextCursor.getColumnIndex(LoreLibrary.COLUMN_FAVED));
            }
        } catch (CursorIndexOutOfBoundsException e) {
            Log.e("ERROR", "Can't find Cat# for some reason. " + e);
            String[] oobArgs = { titleString, titleString };
            getTextCursor = db.rawQuery(Queries.OOB_QUERY, oobArgs);

            if (getTextCursor != null && getTextCursor.moveToFirst()) {
                buzzingText = getTextCursor.getString(getTextCursor.getColumnIndex(LoreLibrary.COLUMN_BUZZING));
                blackSignalText = getTextCursor.getString(getTextCursor.getColumnIndex(LoreLibrary.COLUMN_BLACK_SIGNAL));
                faved = getTextCursor.getInt(getTextCursor.getColumnIndex(LoreLibrary.COLUMN_FAVED));
            }
        }
        //FULL TITLE STRING IS FOR THE FAVE TOASTS; SAVE IT SO YOU CAN USE IT LATER
        fullTitleString = titleString;
        //TRUNCATES THE TITLE SO PREFIXES DON'T BREAK FAVES
        if (getTextCursor != null) {
            titleString = getTextCursor.getString(getTextCursor.getColumnIndex(LoreLibrary.COLUMN_TITLE));
            getTextCursor.close();
        }

        RelativeLayout relativeLayout = findViewById(R.id.loreActivity_relativeLayout);
        Toolbar toolbar = findViewById(R.id.loreActivity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        scrollView = findViewById(R.id.loreActivity_scrollView);
        RotationHandler.setupRotationLayout(this, relativeLayout, scrollView, toolbar);

        TextView titleTextView = findViewById(R.id.loreActivity_title_text_view);
        TextView categoryTextView = findViewById(R.id.loreActivity_category_text_view);
        TextView buzzingTextView = findViewById(R.id.loreActivity_buzzing_text_view);
        favedImageView = findViewById(R.id.loreActivity_fave_imageView);
        favedImageView.setOnClickListener(view -> {
            try {
                String modTitleString = "\"" + titleString + "\"";

                //EXECUTE UPDATE QUERY
                db.execSQL(Queries.UPDATE_FAVE + modTitleString + ";");

                //GET UPDATED CURSOR TO SET THE NEW FAVED STATE
                String[] selectionArgs1 = { titleString };
                Cursor getFavesCursor = db.rawQuery(Queries.GET_FAVE, selectionArgs1);
                if (getFavesCursor != null) {
                    getFavesCursor.moveToFirst();

                    int faved = getFavesCursor.getInt(getFavesCursor.getColumnIndex(LoreLibrary.COLUMN_FAVED));
                    setStar(faved);

                    getFavesCursor.close();
                }
            }
            catch (SQLiteException e) {
                Log.w("WARNING!", "Unable to update fave! " + e);
                Toast.makeText(getBaseContext(), R.string.toast_cannot_favorite, Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout blackSignalLayout = findViewById(R.id.loreActivity_BlackSignalLayout);
        TextView blackSignalTextView = findViewById(R.id.loreActivity_signal_text_view);

        titleTextView.setText(fullTitleString);
        if (categoryString != null) {
            categoryTextView.setText(categoryString);
        } else {
            switch (legendsPrefs.getLangPref()) {
                case LegendsPreferences.LANG_EN:
                    categoryTextView.setText("Taste and see…");
                    break;
                case LegendsPreferences.LANG_DE:
                    categoryTextView.setText("Koste sie und staune…");
                    break;
                case LegendsPreferences.LANG_FR:
                    categoryTextView.setText("Goûte et comprends…");
                    break;
                default:
                    categoryTextView.setText("Taste and see…");
            }
        }

        setStar(faved);
        initiateHighlighter(buzzingTextView, buzzingText, blackSignalTextView, blackSignalText);

        //A LORE MAY OR MAY NOT HAVE A BLACK SIGNAL TO DISPLAY
        if (blackSignalText != null) {
            blackSignalLayout.setVisibility(View.VISIBLE);
        }

        setFlavorImage(titleString);
        adjustFontSize(buzzingTextView, blackSignalTextView);

        startupComplete = true;
    }

    private void adjustFontSize(TextView buzzingTextView, TextView blackSignalTextView){
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
        ImageView flavorImageView = findViewById(R.id.lore_flavor_image);
        String[] selectionArgs = { titleString };
        Cursor flavorImageCursor = db.rawQuery(Queries.GET_IMAGE, selectionArgs);

        if (flavorImageCursor != null) {
            try {
                flavorImageCursor.moveToFirst();
                String imageResource = flavorImageCursor.getString(flavorImageCursor.getColumnIndex(LoreLibrary.COLUMN_IMAGE));

                if (imageResource != null){
                    flavorImageView.setImageResource(getImageId(this, imageResource));
                }

                flavorImageCursor.close();
            }
            catch (CursorIndexOutOfBoundsException e) {
                Log.w ("WARNING", "Could not find image resource, using default");
                flavorImageView.setImageResource(R.drawable.flavor_default);
            }
        }

        showFlavorImage(flavorImageView);
    }

    private static int getImageId(Context context, String imageResource) {
        return context.getResources().getIdentifier("drawable/" + imageResource, null, context.getPackageName());
    }

    private void showFlavorImage(ImageView imageView) {
        //HIDE IMAGE IF NEEDED
        if (!legendsPrefs.getImagePref()) {
            imageView.setVisibility(View.GONE);
        }
    }

    private CharSequence highlight(String originalText, TextView textView) {
        Boolean normalize = legendsPrefs.isUsingNormalization();

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
                    highlighted.charAt(spanStart - 1) == '\"' || highlighted.charAt(spanStart - 1) == '\'' || highlighted.charAt(spanStart - 1) == '/') &&
                            (highlighted.charAt(spanEnd) == ' ' || highlighted.charAt(spanEnd) == '.' || highlighted.charAt(spanEnd) == ',' ||
                                    highlighted.charAt(spanEnd) == '\'' || highlighted.charAt(spanEnd) == '-' || highlighted.charAt(spanEnd) == '?' ||
                                    highlighted.charAt(spanEnd) == '!' || highlighted.charAt(spanEnd) == ';' || highlighted.charAt(spanEnd) == ':' ||
                                    highlighted.charAt(spanEnd) == '\"' || highlighted.charAt(spanEnd) == '/') ) {
                        textView.setText(highlighted);
                    }
                    else {
                        highlighted.removeSpan(span);
                    }
                }
                else {
                    //DOUBLE WILDCARDS WILL RETURN *RESULT, *RESULT*, and RESULT*
                    if (legendsPrefs.isUsingDoubleWildcards()) {
                        textView.setText(highlighted);
                    }
                    else {
                        //SIMPLE WILDCARD WILL RETURN ONLY RESULT*
                        if ( highlighted.charAt(spanStart - 1) == ' ' || highlighted.charAt(spanStart - 1) == '\n' || highlighted.charAt(spanStart - 1) == '-' ||
                                highlighted.charAt(spanStart - 1) == '\"' || highlighted.charAt(spanStart - 1) == '\'' || highlighted.charAt(spanStart - 1) == '/') {
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        legendsPrefs.setLorePagePosition(scrollView.getScrollY());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //STUPID HACKJOB. POST DELAY SINCE THE LAYOUT STILL ISN'T COMPLETELY DRAWN BEFORE IT CAN SCROLL
        scrollView.postDelayed(() -> scrollView.scrollTo(0, legendsPrefs.getLorePagePosition()), 250);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        legendsPrefs.setLorePagePosition(0);
    }
}