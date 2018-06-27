package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class LegendsOpenHelperFR extends SQLiteAssetHelper
{
    private static final String DATABASE_NAME = LegendsConstants.DB_FR;
    private static final int DATABASE_VERSION = LegendsConstants.DATABASE_VERSION;

    private Context mContext;

    LegendsOpenHelperFR(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public LegendsOpenHelperFR (Context context, boolean forceUpgrade) {
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
            db.execSQL("DROP TABLE IF EXISTS LoreSearch;");

            //CHECK FOR TSW OR SWL CATEGORY PREFERENCES AND SWAP
            LegendsPreferences legendsPrefs = LegendsPreferences.getInstance(mContext);
            LegendsDatabase.swapCategories(legendsPrefs, db);

            boolean normalizationOn = legendsPrefs.isUsingNormalization();
            if (normalizationOn){
                db.execSQL(LegendsConstants.Queries.CREATE_ASCII_TABLE);
                db.execSQL(LegendsConstants.Queries.POPULATE_VIRTUAL_TABLE_FR_DE_NORMALIZED);
            } else {
                db.execSQL(LegendsConstants.Queries.CREATE_DEFAULT_TABLE);
                db.execSQL(LegendsConstants.Queries.POPULATE_VIRTUAL_TABLE);
            }
        }
        catch (SQLiteException e) {
            Log.w ("WARNING!", e);
        }
    }
}