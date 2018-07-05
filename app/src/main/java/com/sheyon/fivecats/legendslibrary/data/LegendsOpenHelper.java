package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.sheyon.fivecats.legendslibrary.data.LegendsConstants.Queries;

import java.util.ArrayList;

public class LegendsOpenHelper extends SQLiteAssetHelper
{
    private static final int DATABASE_VERSION = LegendsConstants.DATABASE_VERSION;

    private String DATABASE_NAME;
    private Context mContext;
    private ArrayList<Integer> favesList = new ArrayList<>();

    LegendsOpenHelper (Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        mContext = context;
        DATABASE_NAME = databaseName;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //onUpgrade FIRES BEFORE onOpen
        //LOG OLD FAVORITES TO AN ARRAYLIST
        try {
            Cursor faves = db.rawQuery("SELECT _id FROM lore WHERE faved = 1", null);
            if (faves != null && faves.moveToFirst()) {
                int i = 0;
                do {
                    favesList.add(i, faves.getInt(faves.getColumnIndexOrThrow(LegendsConstants.LoreLibrary._ID)));
                    i++;
                }
                while (faves.moveToNext());
                faves.close();
            }
        } catch (SQLiteException e) {
            Log.w ("WARNING!", "" + e);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        LegendsPreferences legendsPrefs = LegendsPreferences.getInstance(mContext);

        try {
            //IF DB NEEDS AN UPGRADE, SKIP
            if (DATABASE_NAME.equals(LegendsConstants.DB_EN)) {
                if (!legendsPrefs.isUpgradeCompleted()) {
                    return;
                }
            }
            if (DATABASE_NAME.equals(LegendsConstants.DB_DE)) {
                if (!legendsPrefs.isUpgradeCompletedDE()) {
                    return;
                }
            }
            if (DATABASE_NAME.equals(LegendsConstants.DB_FR)) {
                if (!legendsPrefs.isUpgradeCompletedFR()) {
                    return;
                }
            }

            //PRAGMAS
            db.execSQL("PRAGMA auto_vacuum = 0;");
            //VACUUM TO PREVENT BLOAT CAUSED BY THE VIRTUAL TABLE
            db.execSQL("DROP TABLE IF EXISTS LoreSearch;");
            db.execSQL("VACUUM;");

            switch (DATABASE_NAME) {
                case LegendsConstants.DB_EN:
                    //CREATE TABLE FOR FULL-TEXT-SEARCH
                    db.execSQL(Queries.CREATE_DEFAULT_TABLE);
                    db.execSQL(Queries.POPULATE_VIRTUAL_TABLE);
                    break;
                case LegendsConstants.DB_DE:
                case LegendsConstants.DB_FR:
                    boolean normalizationOn = legendsPrefs.isUsingNormalization();
                    if (normalizationOn) {
                        db.execSQL(Queries.CREATE_ASCII_TABLE);
                        db.execSQL(Queries.POPULATE_VIRTUAL_TABLE_FR_DE_NORMALIZED);
                    } else {
                        db.execSQL(Queries.CREATE_DEFAULT_TABLE);
                        db.execSQL(Queries.POPULATE_VIRTUAL_TABLE);
                    }
                    break;
            }
            LegendsDatabase.swapCategories(legendsPrefs, db);
        }
        catch (SQLiteException e) {
            Log.w ("WARNING!", e);
        }
    }

    public ArrayList<Integer> getFavesList() {
        return favesList;
    }
}