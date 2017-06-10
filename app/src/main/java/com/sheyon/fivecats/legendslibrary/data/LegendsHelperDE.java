package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.Locale;

public class LegendsHelperDE extends SQLiteAssetHelper
{
    private static final String DATABASE_NAME = "lore_library_DE.db";
    private static final int DATABASE_VERSION = 2;

    public static boolean normalized = false;

    public LegendsHelperDE (Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade(DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL("DROP TABLE IF EXISTS LoreSearch;");

        String language = Locale.getDefault().getLanguage();

        if (language.equals("de")){
            db.execSQL("CREATE VIRTUAL TABLE LoreSearch USING fts4 (_id, prefix, title, legend, blackLore, categoryName, faved);");
            db.execSQL(LegendsContract.Queries.POPULATE_VIRTUAL_TABLE_FR_DE_NATIVE);
        }

        else {
            normalized = true;
            db.execSQL("CREATE VIRTUAL TABLE LoreSearch USING fts4 (_id, prefix, title, ASCII_title, legend, ASCII_legend, blackLore, ASCII_blackLore, categoryName, faved);");
            db.execSQL(LegendsContract.Queries.POPULATE_VIRTUAL_TABLE_FR_DE_NORMALIZED);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}