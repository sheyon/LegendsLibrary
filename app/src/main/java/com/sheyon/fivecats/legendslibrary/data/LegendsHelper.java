package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class LegendsHelper extends SQLiteAssetHelper
{
    private static final String DATABASE_NAME = "lore_library.db";
    private static final int DATABASE_VERSION = 5;

    private Context mContext;

    //VERSION 2 = 1.1.1
    //VERSION 3 = 1.1.2
    //VERSION 4 = 1.1.4 - Samhain lore 2012 TSW/2017 SWL corrected; FR is one version behind EN and DE.
    //VERSION 5 = 1.1.5 - Bestiary image entries added; FR version synced

    LegendsHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=1;");

        //CREATE TABLE FOR FULL-TEXT-SEARCH
        db.execSQL("DROP TABLE IF EXISTS LoreSearch;");
        db.execSQL(LegendsContract.Queries.CREATE_DEFAULT_TABLE);
        db.execSQL(LegendsContract.Queries.POPULATE_VIRTUAL_TABLE);

        //CHECK FOR TSW OR SWL CATEGORY PREFERENCES AND SWAP
        LegendsDatabase.swapCategories(LegendsPreferences.getInstance(mContext), db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 4:
                LegendsDatabase.setBestiary(db);
                break;
        }
    }
}