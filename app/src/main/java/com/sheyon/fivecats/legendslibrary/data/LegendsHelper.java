package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class LegendsHelper extends SQLiteAssetHelper
{
    private static final String DATABASE_NAME = "lore_library.db";
    private static final int DATABASE_VERSION = 2;

    public LegendsHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade(DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
        //CREATE TABLE FOR FULL-TEXT-SEARCH
        db.execSQL("DROP TABLE IF EXISTS LoreSearch;");
        db.execSQL("CREATE VIRTUAL TABLE LoreSearch USING fts4 (_id, title, prefix, legend, blackLore, categoryName, faved);");
        db.execSQL(LegendsContract.Queries.POPULATE_VIRTUAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}