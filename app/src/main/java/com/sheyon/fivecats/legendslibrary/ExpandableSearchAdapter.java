package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;

import java.util.Locale;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;


public class ExpandableSearchAdapter extends CursorTreeAdapter {
    private Cursor mCursor;
    private String searchString;

    public ExpandableSearchAdapter(Cursor cursor, Context context, String string) {
        super(cursor, context, false);
        mCursor = cursor;
        searchString = string;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);

        if (getChildrenCursor(mCursor) != null) {
            getChildrenCursor(mCursor).close();
        }
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        int index = mCursor.getInt(mCursor.getColumnIndexOrThrow(LoreLibrary._ID));

        String[] selectionArgs = {Integer.toString(index)};
        groupCursor = legendsDB.rawQuery(Queries.SEARCH_CHILD_TABLE, selectionArgs);

        return groupCursor;
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.lore_list_item, parent, false);
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        String loreTitleText;
        String loreCategoryText;

        TextView loreTitleTV = (TextView) view.findViewById(R.id.lore_title_text_view);
        TextView loreCategoryTV = (TextView) view.findViewById(R.id.lore_category_text_view);

        loreTitleText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_TITLE));
        loreCategoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_CATEGORY_NAME));

        loreTitleTV.setText(loreTitleText);
        loreCategoryTV.setText(loreCategoryText);
    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_lore, parent, false);
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        //HIDE THE TITLE AND CATEGORY VIEWS; THE PARENT ALREADY HAS IT
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.loreActivity_toolbar);
        TextView loreTitleDupe = (TextView) view.findViewById(R.id.loreActivity_title_text_view);
        TextView loreCategoryDupe = (TextView) view.findViewById(R.id.loreActivity_category_text_view);
        toolbar.setVisibility(View.GONE);
        loreTitleDupe.setVisibility(View.GONE);
        loreCategoryDupe.setVisibility(View.GONE);

        //SET EVERYTHING TO GONE; RELEVENT LORE WILL BECOME VISIBLE IF NEEDED, WITH AN OPTION TO EXPAND
        TextView blackSignalTextView = (TextView) view.findViewById(R.id.loreActivity_signal_text_view);
        ImageView blackSignalImageView = (ImageView) view.findViewById(R.id.loreActivity_signal_image_view);
        TextView buzzingTextView = (TextView) view.findViewById(R.id.loreActivity_buzzing_text_view);
        ImageView buzzingImageView = (ImageView) view.findViewById(R.id.loreActivity_buzzing_image_view);

        blackSignalTextView.setVisibility(View.GONE);
        buzzingTextView.setVisibility(View.GONE);
        blackSignalImageView.setVisibility(View.GONE);

        String buzzingText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_BUZZING));
        String blackSignalText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_BLACK_SIGNAL));

        highlight(buzzingText, buzzingTextView);

        if (blackSignalText != null) {
            highlight(blackSignalText, blackSignalTextView);
            blackSignalImageView.setVisibility(View.VISIBLE);
        }
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
        }
        else
        {
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
}