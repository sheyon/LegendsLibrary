package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;


class LegendsCursorTreeAdapter extends CursorTreeAdapter
{
    private Cursor mCursor;

    LegendsCursorTreeAdapter(Cursor cursor, Context context) {
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
        int subcatNumber = mCursor.getInt(mCursor.getColumnIndex(LoreLibrary.COLUMN_SUBCAT_ID));

        //THE PREVIOUS CURSOR INCLUDES SUBCAT IDS WHICH MAY BE NULL. SKIP THESE ENTRIES.
        if (Integer.toString(subcatNumber) == null) {
            return null;
        }

        String [] selectionArgs = { Integer.toString(subcatNumber) };
        groupCursor = MainActivity.legendsDB.rawQuery(Queries.LORES, selectionArgs);

        return groupCursor;
        }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.expandable_category_header, parent, false);
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        TextView categoryHeader = (TextView) view.findViewById(R.id.category_text_view);
        String categoryText;

        //RESET STYLES
        categoryHeader.setAllCaps(true);
        categoryHeader.setTypeface(Typeface.defaultFromStyle(0), 1);
        categoryHeader.setTextSize(2, 20);

        //FILL IN THE CATEGORY HEADER
        categoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_SUBCAT_NAME));

        //HOWEVER IF THE CAT HEADER CONTAINS UNCATEGORIZED LORE
        if (categoryText == null) {

            //TAKE THE HEADER FROM THE LORE TITLE INSTEAD AND STYLE IT
            categoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_TITLE));
            categoryHeader.setText(categoryText);
            categoryHeader.setAllCaps(false);
            categoryHeader.setTypeface(Typeface.defaultFromStyle(0), 0);
            categoryHeader.setTextSize(2, 16);
            return;
        }

        categoryHeader.setText(categoryText);
    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.expandable_subcat_header, parent, false);
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        String subcategoryText;
        TextView subcategoryHeader = (TextView) view.findViewById(R.id.subcategory_text_view);

        subcategoryText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_TITLE));

        //LORES WILL APPEAR REGULAR SIZED AND UNBOLDED
        subcategoryHeader.setText(subcategoryText);
        subcategoryHeader.setAllCaps(false);
        subcategoryHeader.setTypeface(Typeface.defaultFromStyle(0), 0);
        subcategoryHeader.setTextSize(2, 16);
    }
}