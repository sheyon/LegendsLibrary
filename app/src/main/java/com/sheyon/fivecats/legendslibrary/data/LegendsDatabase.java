package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
                db.execSQL("UPDATE lore SET title = \"Samhain 2012\" WHERE _id = 76;");
                db.execSQL("UPDATE image SET title = \"Samhain 2012\" WHERE _id = 76;");
                switch (legendsPrefs.getLangPref()) {
                    case LegendsPreferences.LANG_EN:
                        db.execSQL("UPDATE lore SET title = \"Guardians of Gaia\", categoryId = 7, subcatId = NULL WHERE _id = 74;");
                        db.execSQL("UPDATE image SET title = \"Guardians of Gaia\" WHERE _id = 74;");
                        break;
                    case LegendsPreferences.LANG_DE:
                        db.execSQL("UPDATE lore SET title = \"Bewacher von Gaia\", categoryId = 7, subcatId = NULL WHERE _id = 74;");
                        db.execSQL("UPDATE image SET title = \"Bewacher von Gaia\" WHERE _id = 74;");
                        break;
                    case LegendsPreferences.LANG_FR:
                        db.execSQL("UPDATE lore SET title = \"Gardiens de Gaia\", prefix = NULL, categoryId = 7, subcatId = NULL WHERE _id = 74;");
                        db.execSQL("UPDATE image SET title = \"Gardiens de Gaia\" WHERE _id = 74;");
                        break;
                }
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
                db.execSQL("UPDATE lore SET title = \"Samhain 2017\" WHERE _id = 76;");
                db.execSQL("UPDATE image SET title = \"Samhain 2017\" WHERE _id = 76;");
                switch (legendsPrefs.getLangPref()) {
                    case LegendsPreferences.LANG_EN:
                        db.execSQL("UPDATE lore SET title = \"Golems and the Fourth Age\", categoryId = 5, subcatId = 15 WHERE _id = 74;");
                        db.execSQL("UPDATE image SET title = \"Golems and the Fourth Age\" WHERE _id = 74;");
                        break;
                    case LegendsPreferences.LANG_DE:
                        db.execSQL("UPDATE lore SET title = \"Golems und das Vierte Zeitalter\", categoryId = 5, subcatId = 15 WHERE _id = 74;");
                        db.execSQL("UPDATE image SET title = \"Golems und das Vierte Zeitalter\" WHERE _id = 74;");
                        break;
                    case LegendsPreferences.LANG_FR:
                        db.execSQL("UPDATE lore SET title = \"golems et le Quatrième Âge\", prefix = \"Les \", categoryId = 5, subcatId = 15 WHERE _id = 74;");
                        db.execSQL("UPDATE image SET title = \"golems et le Quatrième Âge\" WHERE _id = 74;");
                        break;
                }
                Log.i ("INFO", "Categories set to SWL");
            }
        }
        catch (SQLiteException e) {
            e.printStackTrace();
            Log.w ("WARNING!", "Unable to swap categories!");
        }
    }
}
