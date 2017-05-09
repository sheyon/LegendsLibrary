package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;


public class LegendsCursorTreeAdaptor extends CursorTreeAdapter
{
    private Context mContext;
    private Cursor mCursor;

    public LegendsCursorTreeAdaptor(Cursor cursor, Context context)
    {
        super(cursor, context, false);
        mContext = context;
        mCursor = cursor;
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor)
    {
        int categoryNumber = mCursor.getInt(mCursor.getColumnIndexOrThrow(LoreLibrary._ID));

        LegendsHelper legendsHelper = new LegendsHelper(mContext);
        SQLiteDatabase legendsDB = legendsHelper.getReadableDatabase();

        groupCursor = legendsDB.rawQuery(LegendsContract.CAT_SUBCAT_QUERY + " WHERE " + LoreLibrary.BASE_CATEGORY_ID + " = " + categoryNumber, null);

        return groupCursor;
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.category_header_layout, parent, false);
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded)
    {
        TextView categoryHeader = (TextView) view.findViewById(R.id.category_text_view);

        String categoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.CATEGORY_NAME));

        categoryHeader.setText(categoryText);
    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.subcategory_child_layout, parent, false);
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild)
    {
        TextView subcategoryHeader = (TextView) view.findViewById(R.id.subcategory_text_view);

        String subcategoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.SUBCAT_NAME));

        subcategoryHeader.setText(subcategoryText);
    }
}