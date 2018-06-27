package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.sheyon.fivecats.legendslibrary.R;

public class LegendsDatabase {

    private static SQLiteDatabase legendsDB;

    public static SQLiteDatabase getInstance (Context context) {
        if (legendsDB == null || !legendsDB.isOpen()) {
            openDatabase(context);
        }
        return legendsDB;
    }

    private synchronized static void openDatabase(Context context) {
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
        LegendsPreferences legendsPrefs = LegendsPreferences.getInstance(context);
        SQLiteAssetHelper legendsOpenHelper;

        switch (legendsPrefs.getLangPref()) {
            case LegendsPreferences.LANG_EN:
                legendsOpenHelper = new LegendsOpenHelper(context, LegendsConstants.DB_EN);
                break;
            case LegendsPreferences.LANG_DE:
                legendsOpenHelper = new LegendsOpenHelper(context, LegendsConstants.DB_DE);
                break;
            case LegendsPreferences.LANG_FR:
                legendsOpenHelper = new LegendsOpenHelper(context, LegendsConstants.DB_FR);
                break;
            default:
                legendsOpenHelper = new LegendsOpenHelper(context, LegendsConstants.DB_EN);
                break;
        }

        try {
            legendsDB = legendsOpenHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            legendsDB = legendsOpenHelper.getReadableDatabase();
            Toast.makeText(context, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
        }
    }

    static void swapCategories(LegendsPreferences legendsPrefs, SQLiteDatabase db) {
        try {
            if (legendsPrefs.isUsingTswSorting()){
                db.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 22;");      //Nightmares in the Dream Palace
                db.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 25;");      //Reaping the Whirlwind
                db.execSQL("UPDATE lore SET categoryId = 8, subcatId = 23 WHERE _id = 27;");        //Tale of Momotaro
                db.execSQL("UPDATE lore SET categoryId = 8, subcatId = 23 WHERE _id = 30;");        //Abandoned
                db.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 33;");      //Black Signal
                db.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 36;");      //Call of the Nameless
                db.execSQL("UPDATE lore SET categoryId = 8, subcatId = 23 WHERE _id = 64;");        //Tragical History of Doctor Faustus
                db.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 68;");      //Trail of Shadows
                db.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 130;");     //Breaks in Time
                db.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 131;");     //Sleepless Lullaby
                db.execSQL("UPDATE lore SET categoryId = 8, subcatId = NULL WHERE _id = 132;");     //Sinking City
                db.execSQL("UPDATE lore SET title = \"Samhain 2012\" WHERE title = \"Samhain 2017\"");
                db.execSQL("UPDATE image SET title = \"Samhain 2012\" WHERE title = \"Samhain 2017\"");
                Log.i ("INFO", "Categories set to TSW");
            } else {
                db.execSQL("UPDATE lore SET categoryId = 4, subcatId = 14 WHERE _id = 22;");
                db.execSQL("UPDATE lore SET categoryId = 4, subcatId = 14 WHERE _id = 25;");
                db.execSQL("UPDATE lore SET categoryId = 4, subcatId = 13 WHERE _id = 27;");
                db.execSQL("UPDATE lore SET categoryId = 3, subcatId = 8 WHERE _id = 30;");
                db.execSQL("UPDATE lore SET categoryId = 4, subcatId = 14 WHERE _id = 33;");
                db.execSQL("UPDATE lore SET categoryId = 2, subcatId = 7 WHERE _id = 36;");
                db.execSQL("UPDATE lore SET categoryId = 4, subcatId = 13 WHERE _id = 64;");
                db.execSQL("UPDATE lore SET categoryId = 4, subcatId = 13 WHERE _id = 68;");
                db.execSQL("UPDATE lore SET categoryId = 2, subcatId = 7 WHERE _id = 130;");
                db.execSQL("UPDATE lore SET categoryId = 3, subcatId = 10 WHERE _id = 131;");
                db.execSQL("UPDATE lore SET categoryId = 5, subcatId = 17 WHERE _id = 132;");
                db.execSQL("UPDATE lore SET title = \"Samhain 2017\" WHERE title = \"Samhain 2012\"");
                db.execSQL("UPDATE image SET title = \"Samhain 2017\" WHERE title = \"Samhain 2012\"");
                Log.i ("INFO", "Categories set to SWL");
            }
        }
        catch (SQLiteException e) {
            e.printStackTrace();
            Log.w ("WARNING!", "Unable to swap categories!");
        }


//        SwapperAsyncTask swapper = new SwapperAsyncTask();
//        swapper.pushVars(legendsPrefs, db);
//        swapper.execute();
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
                if (sLegendsPrefs.isUsingTswSorting()){
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
                    Log.i ("INFO", "Categories set to TSW");
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
                    Log.i ("INFO", "Categories set to SWL");
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
