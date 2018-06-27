package com.sheyon.fivecats.legendslibrary;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.sheyon.fivecats.legendslibrary.data.LegendsConstants;
import com.sheyon.fivecats.legendslibrary.data.LegendsOpenHelper;
import com.sheyon.fivecats.legendslibrary.data.LegendsOpenHelperDE;
import com.sheyon.fivecats.legendslibrary.data.LegendsOpenHelperFR;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;
import com.sheyon.fivecats.legendslibrary.data.LegendsUpgradeHelper;

import java.util.ArrayList;
import java.util.Locale;

public class LegendsApp extends Application {

    private LegendsPreferences legendsPrefs;
    private SQLiteDatabase db;
    private ArrayList<Integer> oldFavorites;

    @Override
    public void onCreate() {
        super.onCreate();
        createOrConfirmPrefs();
        initiateUpgrade();
        transferFavorites();
    }

    private void createOrConfirmPrefs() {
        //IF LANG PREFS DO NOT EXIST, CREATE THEM (DEFAULT: ENGLISH)
        legendsPrefs = LegendsPreferences.getInstance(this);
        if (legendsPrefs.doesNotContain(LegendsPreferences.PREF_LANG)) {
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
        if (legendsPrefs.doesNotContain(LegendsPreferences.PREF_NORMALIZATION)) {
            legendsPrefs.setNormalizationPref(true);
        }
    }

    private void initiateUpgrade() {
        LegendsUpgradeHelper upgradeHelper;

        //DETERMINE IF THE DB NEEDS TO BE UPGRADED; IF SO, SAVE FAVORITES
        switch (legendsPrefs.getLangPref()) {
            case LegendsPreferences.LANG_EN:
                upgradeHelper = new LegendsUpgradeHelper(this, LegendsConstants.DB_EN);
                break;
            case LegendsPreferences.LANG_DE:
                upgradeHelper = new LegendsUpgradeHelper(this, LegendsConstants.DB_DE);
                break;
            case LegendsPreferences.LANG_FR:
                upgradeHelper = new LegendsUpgradeHelper(this, LegendsConstants.DB_FR);
                break;
            default:
                upgradeHelper = new LegendsUpgradeHelper(this, LegendsConstants.DB_EN);
        }

        db = upgradeHelper.getReadableDatabase();
        oldFavorites = upgradeHelper.getFavesList();
        db.close();
    }

    private void transferFavorites() {
        if (!oldFavorites.isEmpty()) {
            SQLiteAssetHelper legendsOpenHelper;
            switch (legendsPrefs.getLangPref()) {
                case LegendsPreferences.LANG_EN:
                    legendsOpenHelper = new LegendsOpenHelper(this, true);
                    break;
                case LegendsPreferences.LANG_DE:
                    legendsOpenHelper = new LegendsOpenHelperDE(this, true);
                    break;
                case LegendsPreferences.LANG_FR:
                    legendsOpenHelper = new LegendsOpenHelperFR(this, true);
                    break;
                default:
                    legendsOpenHelper = new LegendsOpenHelper(this, true);
                    break;
            }

            try {
                db = legendsOpenHelper.getWritableDatabase();
                updateClause();
            } catch (SQLiteException e) {
                db = legendsOpenHelper.getReadableDatabase();
                Toast.makeText(this, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateClause() {
        for (int i = 0; i < oldFavorites.size(); i++) {
            String updateQuery = "UPDATE lore SET faved = 1 WHERE _id = " + oldFavorites.get(i);
            db.execSQL(updateQuery);
        }
        Log.i("INFO", "Faved lore transferred.");
    }
}
