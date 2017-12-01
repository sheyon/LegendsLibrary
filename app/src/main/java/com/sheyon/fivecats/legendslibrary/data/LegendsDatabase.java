package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
        if (legendsPrefs.getTswSorting()){
            swapToTSW(db);
        }
        else {
            swapToSWL(db);
        }
    }

    private static void swapToTSW(SQLiteDatabase db) {
        try {
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
        catch (SQLiteException e) {
            Log.w ("WARNING!", "Unable to swap! " + e);
        }
    }

    private static void swapToSWL(SQLiteDatabase db) {
        try {
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
        catch (SQLiteException e) {
            Log.w ("WARNING!", "Unable to swap! " + e);
        }
    }

    static void setBestiary (SQLiteDatabase db) {
        db.execSQL("UPDATE image SET resourceLink = \"best_akab\" WHERE _id = 82;");
        db.execSQL("UPDATE image SET resourceLink = \"best_arthropods\" WHERE _id = 83;");
        db.execSQL("UPDATE image SET resourceLink = \"best_blajini\" WHERE _id = 84;");
        db.execSQL("UPDATE image SET resourceLink = \"best_boogeyman\" WHERE _id = 85;");
        db.execSQL("UPDATE image SET resourceLink = \"best_cultists\" WHERE _id = 86;");
        db.execSQL("UPDATE image SET resourceLink = \"best_deathless\" WHERE _id = 87;");
        db.execSQL("UPDATE image SET resourceLink = \"best_draug_deep1s\" WHERE _id = 88;");
        db.execSQL("UPDATE image SET resourceLink = \"best_draug_drones\" WHERE _id = 89;");
        db.execSQL("UPDATE image SET resourceLink = \"best_draug_lords\" WHERE _id = 90;");
        db.execSQL("UPDATE image SET resourceLink = \"best_familiars\" WHERE _id = 91;");
        db.execSQL("UPDATE image SET resourceLink = \"best_fauns\" WHERE _id = 92;");
        db.execSQL("UPDATE image SET resourceLink = \"best_filth_humans\" WHERE _id = 93;");
        db.execSQL("UPDATE image SET resourceLink = \"best_filth_nature\" WHERE _id = 94;");
        db.execSQL("UPDATE image SET resourceLink = \"best_filth_guardians\" WHERE _id = 95;");
        db.execSQL("UPDATE image SET resourceLink = \"best_ghouls\" WHERE _id = 96;");
        db.execSQL("UPDATE image SET resourceLink = \"best_golems\" WHERE _id = 97;");
        db.execSQL("UPDATE image SET resourceLink = \"best_jinn\" WHERE _id = 98;");
        db.execSQL("UPDATE image SET resourceLink = \"best_kyonshi\" WHERE _id = 99;");
        db.execSQL("UPDATE image SET resourceLink = \"best_little1s\" WHERE _id = 100;");
        db.execSQL("UPDATE image SET resourceLink = \"best_mummies\" WHERE _id = 101;");
        db.execSQL("UPDATE image SET resourceLink = \"best_namahage\" WHERE _id = 102;");
        db.execSQL("UPDATE image SET resourceLink = \"best_oni\" WHERE _id = 103;");
        db.execSQL("UPDATE image SET resourceLink = \"best_orochi_tech\" WHERE _id = 104;");
        db.execSQL("UPDATE image SET resourceLink = \"best_padurii\" WHERE _id = 105;");
        db.execSQL("UPDATE image SET resourceLink = \"best_rakshasa\" WHERE _id = 106;");
        db.execSQL("UPDATE image SET resourceLink = \"best_revenants\" WHERE _id = 107;");
        db.execSQL("UPDATE image SET resourceLink = \"best_sasquatch\" WHERE _id = 108;");
        db.execSQL("UPDATE image SET resourceLink = \"best_scarecrows\" WHERE _id = 109;");
        db.execSQL("UPDATE image SET resourceLink = \"best_hell_soldiers\" WHERE _id = 110;");
        db.execSQL("UPDATE image SET resourceLink = \"best_spectres\" WHERE _id = 111;");
        db.execSQL("UPDATE image SET resourceLink = \"best_fire_spirits\" WHERE _id = 112;");
        db.execSQL("UPDATE image SET resourceLink = \"best_incubii\" WHERE _id = 113;");
        db.execSQL("UPDATE image SET resourceLink = \"best_vampire_masters\" WHERE _id = 114;");
        db.execSQL("UPDATE image SET resourceLink = \"best_vampire_ss\" WHERE _id = 115;");
        db.execSQL("UPDATE image SET resourceLink = \"best_vampires\" WHERE _id = 116;");
        db.execSQL("UPDATE image SET resourceLink = \"best_wendigo\" WHERE _id = 117;");
        db.execSQL("UPDATE image SET resourceLink = \"best_werewolves\" WHERE _id = 118;");
        db.execSQL("UPDATE image SET resourceLink = \"best_wisps\" WHERE _id = 119;");
        db.execSQL("UPDATE image SET resourceLink = \"best_zmei\" WHERE _id = 120;");
        db.execSQL("UPDATE image SET resourceLink = \"best_zombies\" WHERE _id = 121;");
        Log.i("INFO", "Bestiary added");
    }
}
