package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.sheyon.fivecats.legendslibrary.R;

import java.util.ArrayList;

public class LegendsDatabase {

    private static SQLiteDatabase legendsDB;

    public static SQLiteDatabase getInstance(Context context) {
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
        final LegendsOpenHelper[] legendsOpenHelper = new LegendsOpenHelper[1];

        LanguageAction enOpenHelper = () -> legendsOpenHelper[0] = new LegendsOpenHelper(context, LegendsConstants.DB_EN);
        LanguageAction deOpenHelper = () -> legendsOpenHelper[0] = new LegendsOpenHelper(context, LegendsConstants.DB_DE);
        LanguageAction frOpenHelper = () -> legendsOpenHelper[0] = new LegendsOpenHelper(context, LegendsConstants.DB_FR);

        languageSwitch(legendsPrefs, enOpenHelper, deOpenHelper, frOpenHelper);

        try {
            legendsDB = legendsOpenHelper[0].getWritableDatabase();
        } catch (SQLiteException e) {
            legendsDB = legendsOpenHelper[0].getReadableDatabase();
            Toast.makeText(context, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
        }
    }

    static void swapCategories(LegendsPreferences legendsPrefs, SQLiteDatabase db) {
        try {
            if (legendsPrefs.isUsingTswSorting()) {
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
                db.execSQL("UPDATE lore SET title = \"Samhain 2014\" WHERE _id = 78;");
                db.execSQL("UPDATE image SET title = \"Samhain 2014\" WHERE _id = 78;");

                LanguageAction lore2TswEn = () -> {
                    db.execSQL("UPDATE lore SET title = \"Guardians of Gaia\", categoryId = 7, subcatId = NULL WHERE _id = 74;");
                    db.execSQL("UPDATE image SET title = \"Guardians of Gaia\" WHERE _id = 74;");
                };
                LanguageAction lore2TswDe = () -> {
                    db.execSQL("UPDATE lore SET title = \"Bewacher von Gaia\", categoryId = 7, subcatId = NULL WHERE _id = 74;");
                    db.execSQL("UPDATE image SET title = \"Bewacher von Gaia\" WHERE _id = 74;");
                };
                LanguageAction lore2TswFr = () -> {
                    db.execSQL("UPDATE lore SET title = \"Gardiens de Gaia\", prefix = NULL, categoryId = 7, subcatId = NULL WHERE _id = 74;");
                    db.execSQL("UPDATE image SET title = \"Gardiens de Gaia\" WHERE _id = 74;");
                };
                languageSwitch(legendsPrefs, lore2TswEn, lore2TswDe, lore2TswFr);

                Log.i("INFO", "Categories set to TSW");
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
                db.execSQL("UPDATE lore SET title = \"Samhain 2018\" WHERE _id = 78;");
                db.execSQL("UPDATE image SET title = \"Samhain 2018\" WHERE _id = 78;");

                LanguageAction lore2SwlEn = () -> {
                    db.execSQL("UPDATE lore SET title = \"Golems and the Fourth Age\", categoryId = 5, subcatId = 15 WHERE _id = 74;");
                    db.execSQL("UPDATE image SET title = \"Golems and the Fourth Age\" WHERE _id = 74;");
                };
                LanguageAction lore2SwlDe = () -> {
                    db.execSQL("UPDATE lore SET title = \"Golems und das Vierte Zeitalter\", categoryId = 5, subcatId = 15 WHERE _id = 74;");
                    db.execSQL("UPDATE image SET title = \"Golems und das Vierte Zeitalter\" WHERE _id = 74;");
                };
                LanguageAction lore2SwlFr = () -> {
                    db.execSQL("UPDATE lore SET title = \"golems et le Quatrième Âge\", prefix = \"Les \", categoryId = 5, subcatId = 15 WHERE _id = 74;");
                    db.execSQL("UPDATE image SET title = \"golems et le Quatrième Âge\" WHERE _id = 74;");
                };
                languageSwitch(legendsPrefs, lore2SwlEn, lore2SwlDe, lore2SwlFr);

                Log.i("INFO", "Categories set to SWL");
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.w("WARNING!", "Unable to swap categories!");
        }
    }

    public synchronized static void initiateUpgrade(Context context, LegendsPreferences legendsPrefs) {
        final LegendsOpenHelper[] upgradeHelper = new LegendsOpenHelper[1];
        SQLiteDatabase db;

        LanguageAction enUpgradeHelper = () -> {
            if (!legendsPrefs.isUpgradeCompleted()) { upgradeHelper[0] = new LegendsOpenHelper(context, LegendsConstants.DB_EN); }
        };
        LanguageAction deUpgradeHelper = () -> {
            if (!legendsPrefs.isUpgradeCompletedDE()) { upgradeHelper[0] = new LegendsOpenHelper(context, LegendsConstants.DB_DE); }
        };
        LanguageAction frUpgradeHelper = () -> {
            if (!legendsPrefs.isUpgradeCompletedFR()) { upgradeHelper[0] = new LegendsOpenHelper(context, LegendsConstants.DB_FR); }
        };

        languageSwitch(legendsPrefs, enUpgradeHelper, deUpgradeHelper, frUpgradeHelper);

        //ABORT THE REST OF THE BLOCK IF UPGRADE IS NOT NEEDED
        if (upgradeHelper[0] == null ) {
            return;
        }

        try {
            db = upgradeHelper[0].getWritableDatabase();
            ArrayList<Integer> oldFavorites = upgradeHelper[0].getFavesList();

            //AssetHelper AUTOMATICALLY INCREMENTS THE DATABASE IF VERSIONS DO NOT MATCH, UNDO THAT CHANGE SO THE DATABASE CAN FORCE UPGRADE
            db.setVersion(LegendsConstants.DATABASE_VERSION - 1);
            upgradeHelper[0].setForcedUpgrade(LegendsConstants.DATABASE_VERSION);
            db.close();

            db = upgradeHelper[0].getWritableDatabase();
            if (!oldFavorites.isEmpty()) {
                for (int i = 0; i < oldFavorites.size(); i++) {
                    String updateQuery = "UPDATE lore SET faved = 1 WHERE _id = " + oldFavorites.get(i);
                    db.execSQL(updateQuery);
                }
                Log.i("INFO", "Old favorites transferred.");
            }
            //SET AS COMPLETED SO THIS WILL NEVER LAUNCH AGAIN FOR THIS VERSION
            LanguageAction DbCompleteEn = () -> legendsPrefs.setDbUpgradeCompleted(true);
            LanguageAction DbCompleteDe = () -> legendsPrefs.setDbUpgradeCompletedDE(true);
            LanguageAction DbCompleteFr = () -> legendsPrefs.setDBUpgradeCompletedFR(true);
            languageSwitch(legendsPrefs, DbCompleteEn, DbCompleteDe, DbCompleteFr);

        } catch (SQLiteException e) {
            Log.e("ERROR", "" + e);
            db = upgradeHelper[0].getReadableDatabase();
            Toast.makeText(context, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    private static void languageSwitch(LegendsPreferences legendsPrefs, LanguageAction en, LanguageAction de, LanguageAction fr) {
        switch (legendsPrefs.getLangPref()) {
            case LegendsPreferences.LANG_EN:
                en.execute();
                break;
            case LegendsPreferences.LANG_DE:
                de.execute();
                break;
            case LegendsPreferences.LANG_FR:
                fr.execute();
                break;
            default:
                en.execute();
                break;
        }
    }

    @FunctionalInterface
    interface LanguageAction {
        void execute();
    }
}
