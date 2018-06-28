package com.sheyon.fivecats.legendslibrary;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.sheyon.fivecats.legendslibrary.data.LegendsConstants;
import com.sheyon.fivecats.legendslibrary.data.LegendsOpenHelper;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

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
    }

    private void initiateUpgrade() {
        LegendsOpenHelper upgradeHelper;

        //DETERMINE IF THE DB NEEDS TO BE UPGRADED; IF SO, SAVE FAVORITES
        switch (legendsPrefs.getLangPref()) {
            case LegendsPreferences.LANG_EN:
                upgradeHelper = new LegendsOpenHelper(this, LegendsConstants.DB_EN);
                break;
            case LegendsPreferences.LANG_DE:
                upgradeHelper = new LegendsOpenHelper(this, LegendsConstants.DB_DE);
                break;
            case LegendsPreferences.LANG_FR:
                upgradeHelper = new LegendsOpenHelper(this, LegendsConstants.DB_FR);
                break;
            default:
                upgradeHelper = new LegendsOpenHelper(this, LegendsConstants.DB_EN);
        }

        db = upgradeHelper.getReadableDatabase();
        oldFavorites = upgradeHelper.getFavesList();
        db.close();
    }

    private void transferFavorites() {
        if (!oldFavorites.isEmpty()) {
            LegendsOpenHelper legendsOpenHelper;
            switch (legendsPrefs.getLangPref()) {
                case LegendsPreferences.LANG_EN:
                    legendsOpenHelper = new LegendsOpenHelper(this, LegendsConstants.DB_EN, true);
                    break;
                case LegendsPreferences.LANG_DE:
                    legendsOpenHelper = new LegendsOpenHelper(this, LegendsConstants.DB_DE, true);
                    break;
                case LegendsPreferences.LANG_FR:
                    legendsOpenHelper = new LegendsOpenHelper(this, LegendsConstants.DB_FR, true);
                    break;
                default:
                    legendsOpenHelper = new LegendsOpenHelper(this, LegendsConstants.DB_EN, true);
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
