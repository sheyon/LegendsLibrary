package com.sheyon.fivecats.legendslibrary;

import com.sheyon.fivecats.legendslibrary.data.LegendsDatabase;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

import java.util.Locale;

import androidx.multidex.MultiDexApplication;

public class LegendsApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        //IF LANG PREFS DO NOT EXIST, CREATE THEM (DEFAULT: ENGLISH)
        LegendsPreferences legendsPrefs = LegendsPreferences.getInstance(this);
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
        LegendsDatabase.initiateUpgrade(this, legendsPrefs);
    }
}
