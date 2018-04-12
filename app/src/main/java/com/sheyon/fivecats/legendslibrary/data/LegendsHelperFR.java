package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

class LegendsHelperFR extends SQLiteAssetHelper
{
    private static final String DATABASE_NAME = "lore_library_FR.db";
    private static final int DATABASE_VERSION = 6;

    private Context mContext;

    LegendsHelperFR (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

        //DO ONCE; ALL PREVIOUS DB UPGRADES DEPRECATED
        LegendsPreferences pref = LegendsPreferences.getInstance(context);
        if (!pref.getUpgradeCompletedFR()) {
            setForcedUpgrade();
            LegendsPreferences.getInstance(mContext).setDbUPgradeCompletedFR(true);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        try {
            //PRAGMAS
            db.execSQL("PRAGMA auto_vacuum = 0;");

            //VACUUM TO PREVENT BLOAT CAUSED BY THE VIRTUAL TABLE
            db.execSQL("VACUUM;");
            db.execSQL("DROP TABLE IF EXISTS LoreSearch;");

            //CHECK FOR TSW OR SWL CATEGORY PREFERENCES AND SWAP
            LegendsPreferences legendsPrefs = LegendsPreferences.getInstance(mContext);
            LegendsDatabase.swapCategories(legendsPrefs, db);

            boolean normalizationOn = legendsPrefs.getNormalizationPref();
            if (normalizationOn){
                db.execSQL(LegendsContract.Queries.CREATE_ASCII_TABLE);
                db.execSQL(LegendsContract.Queries.POPULATE_VIRTUAL_TABLE_FR_DE_NORMALIZED);
            } else {
                db.execSQL(LegendsContract.Queries.CREATE_DEFAULT_TABLE);
                db.execSQL(LegendsContract.Queries.POPULATE_VIRTUAL_TABLE);
            }
        }
        catch (SQLiteException e) {
            Log.w ("WARNING!", e);
        }
    }
}