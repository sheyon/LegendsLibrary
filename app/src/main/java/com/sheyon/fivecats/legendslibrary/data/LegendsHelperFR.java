package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class LegendsHelperFR extends SQLiteAssetHelper
{
    private static final String DATABASE_NAME = "lore_library_FR.db";
    private static final int DATABASE_VERSION = 2;

    private Context mContext;

    public LegendsHelperFR (Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade(DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL("DROP TABLE IF EXISTS LoreSearch;");

        LegendsPreferences legendsPrefs = LegendsPreferences.getInstance(mContext);
        boolean normalizationOn = legendsPrefs.getNormalizationPref();

        if (normalizationOn){
            db.execSQL(LegendsContract.Queries.CREATE_ASCII_TABLE);
            db.execSQL(LegendsContract.Queries.POPULATE_VIRTUAL_TABLE_FR_DE_NORMALIZED);
        }

        else {
            db.execSQL(LegendsContract.Queries.CREATE_DEFAULT_TABLE);
            db.execSQL(LegendsContract.Queries.POPULATE_VIRTUAL_TABLE);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}