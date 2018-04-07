package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsDatabase;

class LegendsCursorTreeAdapter extends CursorTreeAdapter
{
    private Cursor mCursor;
    private Context mContext;

    LegendsCursorTreeAdapter(Cursor cursor, Context context) {
        super(cursor, context, false);
        mCursor = cursor;
        mContext = context;
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
        //GET NEW DATABASE IN CASE SETTINGS WERE CHANGED
        SQLiteDatabase mDb = LegendsDatabase.getInstance(mContext);

        int subcatNumber = mCursor.getInt(mCursor.getColumnIndex(LoreLibrary.COLUMN_SUBCAT_ID));

        //THE PREVIOUS CURSOR INCLUDES SUBCAT IDS WHICH MAY BE NULL. SKIP THESE ENTRIES.
        if (Integer.toString(subcatNumber) == null) {
            return null;
        }

        String [] selectionArgs = { Integer.toString(subcatNumber) };
        groupCursor = mDb.rawQuery(Queries.LORES, selectionArgs);

        return groupCursor;
        }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.expandable_category_header, parent, false);
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        TextView categoryHeader = view.findViewById(R.id.category_text_view);
        String categoryText;
        String prefixText;

        //RESET STYLES
        categoryHeader.setAllCaps(true);
        categoryHeader.setTypeface(Typeface.DEFAULT, 1);
        categoryHeader.setTextSize(2, 20);

        //FILL IN THE CATEGORY HEADER
        categoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_SUBCAT_NAME));

        //HOWEVER IF THE CAT HEADER CONTAINS UNCATEGORIZED LORE
        if (categoryText == null) {

            //TAKE THE HEADER FROM THE LORE TITLE INSTEAD AND STYLE IT
            prefixText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_PREFIX));
            categoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_TITLE));
            if (prefixText != null) {
                categoryHeader.setText("" + prefixText + categoryText);
            }
            else {
                categoryHeader.setText(categoryText);
            }
            categoryHeader.setAllCaps(false);
            categoryHeader.setTypeface(Typeface.DEFAULT, 0);
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
        String prefixText;

        TextView subcategoryHeader = view.findViewById(R.id.subcategory_text_view);

        subcategoryText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_TITLE));
        prefixText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_PREFIX));

        //LORES WILL APPEAR REGULAR SIZED AND UNBOLDED
        if (prefixText != null) {
            subcategoryHeader.setText("" + prefixText + subcategoryText);
        }
        else {
            subcategoryHeader.setText(subcategoryText);
        }
        subcategoryHeader.setAllCaps(false);
        subcategoryHeader.setTypeface(Typeface.DEFAULT, 0);
        subcategoryHeader.setTextSize(2, 16);
    }
}