package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;


public class LegendsCursorTreeAdapter extends CursorTreeAdapter
{
    private Context mContext;
    private Cursor mCursor;

    public LegendsCursorTreeAdapter(Cursor cursor, Context context)
    {
        super(cursor, context, false);
        mContext = context;
        mCursor = cursor;
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor)
    {
        LegendsHelper legendsHelper = new LegendsHelper(mContext);
        SQLiteDatabase legendsDB = legendsHelper.getReadableDatabase();

        int categoryNumber = mCursor.getInt(mCursor.getColumnIndexOrThrow(LoreLibrary._ID));
        String [] selectionArgs = { Integer.toString(categoryNumber), Integer.toString(categoryNumber) };
        String [] union = {Queries.UNION_1, Queries.UNION_2};

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String unionQuery = qb.buildUnionQuery(union, null, null);

        groupCursor = legendsDB.rawQuery(unionQuery, selectionArgs);

        return groupCursor;
        }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.expandable_category_header, parent, false);
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded)
    {
        TextView categoryHeader = (TextView) view.findViewById(R.id.category_text_view);
        String categoryText;

        //RESET STYLES
        categoryHeader.setAllCaps(true);
        categoryHeader.setTypeface(Typeface.defaultFromStyle(0), 1);
        categoryHeader.setTextSize(2, 20);

        //CHECK IF A CATEGORY OR SUBCATEGORY HEADER IS BEING FILLED
        if (cursor.getColumnIndex(LoreLibrary.COLUMN_CATEGORY_NAME) == -1)
        {
            //FILL THE SUBCATEGORY HEADER FIRST
            categoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_SUBCAT_NAME));

            //HOWEVER IF THE SUBCAT HEADER CONTAINS UNCATEGORIZED LORE
            if (categoryText == null)
            {
                //FILL THE LORE HEADER UNDER ANY SUBCATS AND STYLE IT
                categoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_TITLE));
                categoryHeader.setText(categoryText);
                categoryHeader.setAllCaps(false);
                categoryHeader.setTypeface(Typeface.defaultFromStyle(0), 0);
                categoryHeader.setTextSize(2, 16);
                return;
            }
        }
        else
        {
            //FILL THE CATEGORY HEADER
            categoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_CATEGORY_NAME));
        }

        categoryHeader.setText(categoryText);
    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.expandable_subcat_header, parent, false);
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild)
    {
        String subcategoryText;
        TextView subcategoryHeader = (TextView) view.findViewById(R.id.subcategory_text_view);

        //RESET STYLES IN CASE VIEWS ARE RECYCLED
        subcategoryHeader.setAllCaps(false);
        subcategoryHeader.setTypeface(Typeface.defaultFromStyle(0), 0);

        subcategoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_SUBCAT_NAME));

        if (subcategoryText == null)
        {
            //ALL LORE WITHOUT SUBCATEGORIES WILL BE INSTANTIATED UNDER THEIR APPROPRIATE CATEGORIES
            subcategoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_TITLE));
            subcategoryHeader.setText(subcategoryText);
            return;
        }

        //SUBCATS WILL APPEAR IN BOLD AND CAPS
        subcategoryHeader.setText(subcategoryText);
        subcategoryHeader.setAllCaps(true);
        subcategoryHeader.setTypeface(Typeface.defaultFromStyle(0), 1);
    }
}