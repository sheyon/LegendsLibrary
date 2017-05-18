package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;


public class ExpandableSearchAdapter extends CursorTreeAdapter
{
    private Cursor mCursor;

    public ExpandableSearchAdapter(Cursor cursor, Context context) {
        super(cursor, context, false);
        mCursor = cursor;
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

        String [] selectionArgs = { Integer.toString(index) };
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
        TextView loreTitleDupe = (TextView) view.findViewById(R.id.loreActivity_title_text_view);
        TextView loreCategoryDupe = (TextView) view.findViewById(R.id.loreActivity_category_text_view);
        loreTitleDupe.setVisibility(View.GONE);
        loreCategoryDupe.setVisibility(View.GONE);

        //IN CASE OF RECYCLING, HIDE THE BLACK SIGNAL STUFF TO BEGIN
        TextView blackSignalTextview = (TextView) view.findViewById(R.id.loreActivity_signal_text_view);
        ImageView blackSignalImageview = (ImageView) view.findViewById(R.id.loreActivity_signal_image_view);
        blackSignalTextview.setVisibility(View.GONE);
        blackSignalImageview.setVisibility(View.GONE);

        String buzzingText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_BUZZING));
        String blackSignalText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_BLACK_SIGNAL));

        TextView buzzingTextview = (TextView) view.findViewById(R.id.loreActivity_buzzing_text_view);
        buzzingTextview.setText(buzzingText);
        blackSignalTextview.setText(blackSignalText);

        if (blackSignalText != null) {
            blackSignalTextview.setVisibility(View.VISIBLE);
            blackSignalImageview.setVisibility(View.VISIBLE);
        }
    }
}