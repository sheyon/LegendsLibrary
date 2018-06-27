package com.sheyon.fivecats.legendslibrary.data;

//MAYBE SOME DAY I'LL MERGE ALL THE DATABASE HELPERS
//FOR NOW THIS IS FINE WITH ME :P

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class LegendsOpenHelper extends SQLiteAssetHelper
{
    private static final String DATABASE_NAME = LegendsConstants.DB_EN;
    private static final int DATABASE_VERSION = LegendsConstants.DATABASE_VERSION;

    private Context mContext;

    //VERSION 6 = 1.6 - South Africa lore added
    //VERSION 7 = 1.7 - Anniversary lore added

    LegendsOpenHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    //THIS SHOULD ONLY BE CALLED BY APPLICATION CLASS
    //USED TO HELP TRANSFER OLD FAVORITES
    //BOOLEAN IS JUST TO DIFFERENTIATE THE NEW CONSTRUCTOR, ANY VALUE WORKS
    public LegendsOpenHelper (Context context, boolean forceUpgrade) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

        setForcedUpgrade();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //onUpgrade FIRES BEFORE onOpen

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        try {
            //PRAGMAS
            db.execSQL("PRAGMA auto_vacuum = 0;");

            //VACUUM TO PREVENT BLOAT CAUSED BY THE VIRTUAL TABLE
            db.execSQL("VACUUM;");

            //CREATE TABLE FOR FULL-TEXT-SEARCH
            db.execSQL("DROP TABLE IF EXISTS LoreSearch;");
            db.execSQL(LegendsConstants.Queries.CREATE_DEFAULT_TABLE);
            db.execSQL(LegendsConstants.Queries.POPULATE_VIRTUAL_TABLE);

            //CHECK FOR TSW OR SWL CATEGORY PREFERENCES AND SWAP
            LegendsDatabase.swapCategories(LegendsPreferences.getInstance(mContext), db);
        }
        catch (SQLiteException e) {
            Log.w ("WARNING!", e);
        }
    }
}