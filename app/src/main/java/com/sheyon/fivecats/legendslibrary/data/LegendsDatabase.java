package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.sheyon.fivecats.legendslibrary.R;

import java.util.Locale;

public class LegendsDatabase{

    private static SQLiteDatabase legendsDB;
    private Context mContext;

    public SQLiteDatabase getInstance (Context context) {
        mContext = context;
        if (legendsDB == null || !legendsDB.isOpen()) {
            openDatabase();
        }
        return legendsDB;
    }

    private void openDatabase() {
        LegendsPreferences legendsPrefs = LegendsPreferences.getInstance(mContext);

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
            case 0:
                LegendsHelper legendsHelper = new LegendsHelper(mContext);
                try {
                    legendsDB = legendsHelper.getWritableDatabase();
                } catch (SQLiteException e) {
                    legendsDB = legendsHelper.getReadableDatabase();
                    Toast.makeText(mContext, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
                }
                break;

            case 1:
                LegendsHelperDE legendsHelperDE = new LegendsHelperDE(mContext);
                try {
                    legendsDB = legendsHelperDE.getWritableDatabase();
                } catch (SQLiteException e) {
                    legendsDB = legendsHelperDE.getReadableDatabase();
                    Toast.makeText(mContext, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
                }
                break;

            case 2:
                LegendsHelperFR legendsHelperFR = new LegendsHelperFR(mContext);
                try {
                    legendsDB = legendsHelperFR.getWritableDatabase();
                } catch (SQLiteException e) {
                    legendsDB = legendsHelperFR.getReadableDatabase();
                    Toast.makeText(mContext, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
