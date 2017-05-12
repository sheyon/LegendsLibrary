package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

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

        String union1 = "SELECT lore._id AS _id, lore.CategoryID, Title, SubCatName, subcat._id\n" +
                "FROM lore\n" +
                "LEFT OUTER JOIN subcat\n" +
                "ON lore.SubCatID = subcat._id\n" +
                "WHERE lore.SubCatID IS NOT NULL AND lore.CategoryID = " + categoryNumber + "\n" +
                "GROUP BY subcat._id";

        String union2 = "SELECT lore._id AS _id, lore.CategoryID, Title, SubCatName, subcat._id\n" +
                "FROM lore\n" +
                "LEFT OUTER JOIN subcat\n" +
                "ON lore.SubCatID = subcat._id\n" +
                "WHERE lore.SubCatID IS NULL AND lore.CategoryID = " + categoryNumber + "\n" +
                "ORDER BY lore.CategoryID";

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] union = { union1, union2 };
        String unionQuery = qb.buildUnionQuery(union, null, null);

        groupCursor = legendsDB.rawQuery(unionQuery, null);

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

        String categoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_CATEGORY_NAME));

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

        subcategoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_SUBCAT_NAME));

        if (subcategoryText == null)
        {
            subcategoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_TITLE));
        }

        subcategoryHeader.setText(subcategoryText);
    }
}