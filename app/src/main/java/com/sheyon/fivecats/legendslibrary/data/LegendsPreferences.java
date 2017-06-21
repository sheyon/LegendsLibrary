package com.sheyon.fivecats.legendslibrary.data;

import android.content.Context;
import android.content.SharedPreferences;

public class LegendsPreferences {

    private static LegendsPreferences legendsPrefs;
    private static final String PREFS_FILE_KEY = "com.sheyon.fivecats.legendslibrary.PREFS_FILE_KEY";

    public static final String PREF_LANG = "LANG_PREFS";
    public static final String PREF_NORMALIZATION = "NORMALIZATION_PREFS";
    public static final String PREF_WILDCARD_ON = "WILDCARD_ON_PREFS";
    public static final String PREF_DOUBLE_WILDCARD = "DOUBLE_WILDCARD_PREFS";

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    //CONSTRUCTOR
    public static LegendsPreferences getInstance(Context context) {
        if (legendsPrefs == null) {
            legendsPrefs = new LegendsPreferences(context);
        }
        return legendsPrefs;
    }

    private LegendsPreferences (Context context) {
        mPref = context.getSharedPreferences(PREFS_FILE_KEY, Context.MODE_PRIVATE);
    }

    public void setLangPref(int val) {
        doEdit();
        mEditor.putInt(PREF_LANG, val);
        doCommit();
    }

    public void setNormalizationPref(boolean val) {
        doEdit();
        mEditor.putBoolean(PREF_NORMALIZATION, val);
        doCommit();
    }

    public void setWildcardAlwaysOnPref(boolean val) {
        doEdit();
        mEditor.putBoolean(PREF_WILDCARD_ON, val);
        doCommit();
    }

    public void setDoubleWildcardPref(boolean val) {
        doEdit();
        mEditor.putBoolean(PREF_DOUBLE_WILDCARD, val);
        doCommit();
    }

    public boolean doesContain(String prefKey){
        doEdit();
        return mPref.contains(prefKey);
    }

    public int getLangPref() {
        return mPref.getInt(PREF_LANG, 0);
    }

    public boolean getNormalizationPref() {
        return mPref.getBoolean(PREF_NORMALIZATION, true);
    }

    public boolean getWildcardAlwaysOnPref() {
        return mPref.getBoolean(PREF_WILDCARD_ON, false);
    }

    public boolean getDoubleWildcardPref() {
        return mPref.getBoolean(PREF_DOUBLE_WILDCARD, false);
    }

    private void doEdit() {
        if (mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private void doCommit() {
        if (mEditor != null) {
            mEditor.apply();
            mEditor = null;
        }
    }
}
