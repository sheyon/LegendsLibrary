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

    public LegendsOpenHelper (Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        mContext = context;
        DATABASE_NAME = databaseName;
    }

    //THIS SHOULD ONLY BE CALLED BY APPLICATION CLASS
    //USED TO HELP TRANSFER OLD FAVORITES
    //BOOLEAN IS JUST TO DIFFERENTIATE THE NEW CONSTRUCTOR, ANY VALUE WORKS
    public LegendsOpenHelper (Context context, String databaseName, boolean forceUpgrade) {
        super(context, databaseName, null, DATABASE_VERSION);
        mContext = context;
        DATABASE_NAME = databaseName;

        setForcedUpgrade();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //onUpgrade FIRES BEFORE onOpen
        if (db.needUpgrade(LegendsConstants.DATABASE_VERSION)) {
            //LOG OLD FAVORITES TO AN ARRAYLIST
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
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        try {
            LegendsPreferences legendsPrefs = LegendsPreferences.getInstance(mContext);

            //PRAGMAS
            db.execSQL("PRAGMA auto_vacuum = 0;");
            //VACUUM TO PREVENT BLOAT CAUSED BY THE VIRTUAL TABLE
            db.execSQL("VACUUM;");
            db.execSQL("DROP TABLE IF EXISTS LoreSearch;");

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