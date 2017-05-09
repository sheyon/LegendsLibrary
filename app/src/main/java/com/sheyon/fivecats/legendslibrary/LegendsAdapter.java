package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract;

public class LegendsAdapter extends CursorAdapter
{

    public LegendsAdapter(Context context, Cursor c)
    {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.lore_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView loreTitleTextView = (TextView) view.findViewById(R.id.list_title);
        //TextView loreCategoryTextView = (TextView) view.findViewById(R.id.list_category);

        String loreTitle = cursor.getString(cursor.getColumnIndexOrThrow(LegendsContract.LoreLibrary.LORE_TITLE));
        //set up category name join

        loreTitleTextView.setText(loreTitle);
    }
}
