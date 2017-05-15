package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;

import java.util.Arrays;


public class SubcategoryAdapter extends CursorTreeAdapter
{
    private Context sContext;
    private Cursor sCursor;

    public SubcategoryAdapter(Cursor cursor, Context context)
    {
        super(cursor, context, false);
        sContext = context;
        sCursor = cursor;
        String str = Arrays.toString(sCursor.getColumnNames());
        Log.v ("***COLUMN NAMES", ""+str);
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor)
    {
        LegendsHelper legendsHelper = new LegendsHelper(sContext);
        SQLiteDatabase legendsDB = legendsHelper.getReadableDatabase();

        int subcatNumber = sCursor.getInt(sCursor.getColumnIndexOrThrow(LoreLibrary._ID));
        Log.v ("***COLUMN INDEX", ""+subcatNumber);
        String [] selectionArgs = { Integer.toString(subcatNumber) };

        groupCursor = legendsDB.rawQuery(Queries.SUBCAT_LORE, selectionArgs);

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

        subcategoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_TITLE));
        subcategoryHeader.setText(subcategoryText);

    }
}