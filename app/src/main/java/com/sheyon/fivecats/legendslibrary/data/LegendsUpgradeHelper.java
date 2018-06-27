package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class LegendsUpgradeHelper extends SQLiteAssetHelper {

    private static final int DATABASE_VERSION = LegendsConstants.DATABASE_VERSION;
    private ArrayList<Integer> favesList = new ArrayList<>();

    public LegendsUpgradeHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //UPGRADE HAPPENS BEFORE OPENING

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

    public ArrayList<Integer> getFavesList() {
        return favesList;
    }
}
