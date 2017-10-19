package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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

    public static void swapCategories (LegendsPreferences legendsPrefs, SQLiteDatabase db) {
        if (legendsPrefs.getTswSorting()){
            swapToTSW(db);
        }
        else {
            swapToSWL(db);
        }
    }

    private static void swapToTSW(SQLiteDatabase db) {
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
    }

    private static void swapToSWL(SQLiteDatabase db) {
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
    }
}
