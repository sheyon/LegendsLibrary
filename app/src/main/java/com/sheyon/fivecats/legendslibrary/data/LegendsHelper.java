package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class LegendsHelper extends SQLiteAssetHelper
{
    private static final String DATABASE_NAME = "lore_library.db";
    private static final int DATABASE_VERSION = 1;

    public LegendsHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}