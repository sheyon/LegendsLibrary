package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sheyon.fivecats.legendslibrary.R;

import java.util.Locale;

public class LegendsDatabase {

    private static SQLiteDatabase legendsDB;

    public static SQLiteDatabase getInstance (Context context) {
        if (legendsDB == null || !legendsDB.isOpen()) {
            openDatabase(context);
        }
        return legendsDB;
    }

    private static void openDatabase(Context context) {
        LegendsPreferences legendsPrefs = LegendsPreferences.getInstance(context);

        //IF LANG PREFS DO NOT EXIST, CREATE THEM (DEFAULT: ENGLISH)
        if (!legendsPrefs.doesContain(LegendsPreferences.PREF_LANG)) {
            String lang = Locale.getDefault().getLanguage();
            switch (lang) {
                case "en":
                    legendsPrefs.setLangPref(LegendsPreferences.LANG_EN);
                    break;
                case "de":
                    legendsPrefs.setLangPref(LegendsPreferences.LANG_DE);
                    break;
                case "fr":
                    legendsPrefs.setLangPref(LegendsPreferences.LANG_FR);
                    break;
                default:
                    legendsPrefs.setLangPref(LegendsPreferences.LANG_EN);
                    break;
            }
        }

        //IF NORMALIZATION PREFS DO NOT EXIST, CREATE THEM (DEFAULT: NORMALIZED)
        if (!legendsPrefs.doesContain(LegendsPreferences.PREF_NORMALIZATION)) {
            legendsPrefs.setNormalizationPref(true);
        }

//        boolean DEBUG = true;
//
//        if (DEBUG) {
//            try {
//                LegendsHelper legendsHelper = new LegendsHelper(context);
//                legendsDB = SQLiteDatabase.openDatabase(legendsHelper.getReadableDatabase().getPath(), null, SQLiteDatabase.OPEN_READONLY);
//                if (legendsDB.isReadOnly()) {
//                    Log.d ("DEBUG", "DB is Read-Only!");
//                }
//            } catch (Exception e) {
//                Log.d ("DEBUG", "Flagrant Error! " + e);
//            }
//            return;
//        }

        //OPEN DATABASE
        switch (legendsPrefs.getLangPref()) {
            case LegendsPreferences.LANG_EN:
                LegendsHelper legendsHelper = new LegendsHelper(context);
                try {
                    legendsDB = legendsHelper.getWritableDatabase();
                } catch (SQLiteException e) {
                    legendsDB = legendsHelper.getReadableDatabase();
                    Toast.makeText(context, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
                }
                break;

            case LegendsPreferences.LANG_DE:
                LegendsHelperDE legendsHelperDE = new LegendsHelperDE(context);
                try {
                    legendsDB = legendsHelperDE.getWritableDatabase();
                } catch (SQLiteException e) {
                    legendsDB = legendsHelperDE.getReadableDatabase();
                    Toast.makeText(context, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
                }
                break;

            case LegendsPreferences.LANG_FR:
                LegendsHelperFR legendsHelperFR = new LegendsHelperFR(context);
                try {
                    legendsDB = legendsHelperFR.getWritableDatabase();
                } catch (SQLiteException e) {
                    legendsDB = legendsHelperFR.getReadableDatabase();
                    Toast.makeText(context, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    static void swapCategories(LegendsPreferences legendsPrefs, SQLiteDatabase db) {
        SwapperAsyncTask swapper = new SwapperAsyncTask();
        swapper.pushVars(legendsPrefs, db);
        swapper.execute();
    }

    private static class SwapperAsyncTask extends AsyncTask <Void, Void, Void> {
        LegendsPreferences sLegendsPrefs;
        SQLiteDatabase sDb;

        void pushVars(LegendsPreferences prefs, SQLiteDatabase db) {
            sLegendsPrefs = prefs;
            sDb = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (sLegendsPrefs.usingTswSorting()){
                    sDb.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 22;");      //Nightmares in the Dream Palace
                    sDb.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 25;");      //Reaping the Whirlwind
                    sDb.execSQL("UPDATE lore SET categoryId = 8, subcatId = 23 WHERE _id = 27;");        //Tale of Momotaro
                    sDb.execSQL("UPDATE lore SET categoryId = 8, subcatId = 23 WHERE _id = 30;");        //Abandoned
                    sDb.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 33;");      //Black Signal
                    sDb.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 36;");      //Call of the Nameless
                    sDb.execSQL("UPDATE lore SET categoryId = 8, subcatId = 23 WHERE _id = 64;");        //Tragical History of Doctor Faustus
                    sDb.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 68;");      //Trail of Shadows
                    sDb.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 130;");     //Breaks in Time
                    sDb.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 131;");     //Sleepless Lullaby
                    sDb.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 132;");     //Sinking City
                    sDb.execSQL("UPDATE lore SET title = \"Samhain 2012\" WHERE title = \"Samhain 2017\"");
                    sDb.execSQL("UPDATE image SET title = \"Samhain 2012\" WHERE title = \"Samhain 2017\"");
                } else {
                    sDb.execSQL("UPDATE lore SET categoryId = 4, subcatId = 14 WHERE _id = 22;");
                    sDb.execSQL("UPDATE lore SET categoryId = 4, subcatId = 14 WHERE _id = 25;");
                    sDb.execSQL("UPDATE lore SET categoryId = 4, subcatId = 13 WHERE _id = 27;");
                    sDb.execSQL("UPDATE lore SET categoryId = 3, subcatId = 8 WHERE _id = 30;");
                    sDb.execSQL("UPDATE lore SET categoryId = 4, subcatId = 14 WHERE _id = 33;");
                    sDb.execSQL("UPDATE lore SET categoryId = 2, subcatId = 7 WHERE _id = 36;");
                    sDb.execSQL("UPDATE lore SET categoryId = 4, subcatId = 13 WHERE _id = 64;");
                    sDb.execSQL("UPDATE lore SET categoryId = 4, subcatId = 13 WHERE _id = 68;");
                    sDb.execSQL("UPDATE lore SET categoryId = 2, subcatId = 7 WHERE _id = 130;");
                    sDb.execSQL("UPDATE lore SET categoryId = 3, subcatId = 10 WHERE _id = 131;");
                    sDb.execSQL("UPDATE lore SET categoryId = 5, subcatId = 17 WHERE _id = 132;");
                    sDb.execSQL("UPDATE lore SET title = \"Samhain 2017\" WHERE title = \"Samhain 2012\"");
                    sDb.execSQL("UPDATE image SET title = \"Samhain 2017\" WHERE title = \"Samhain 2012\"");
                }
            }
            catch (SQLiteException e) {
                e.printStackTrace();
                Log.w ("WARNING!", "Unable to swap categories!");
            }
            return null;
        }
    }
}
