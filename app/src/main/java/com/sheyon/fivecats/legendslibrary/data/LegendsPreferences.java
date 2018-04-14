package com.sheyon.fivecats.legendslibrary.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class LegendsPreferences {

    private static LegendsPreferences legendsPrefs;
    private static final String PREFS_FILE_KEY = "com.sheyon.fivecats.legendslibrary.PREFS_FILE_KEY";

    public static final String DB_UPGRADE_COMPLETED = "UPGRADE_COMPLETED";
    public static final String DB_UPGRADE_COMPLETED_DE = "UPGRADE_COMPLETED_DE";
    public static final String DB_UPGRADE_COMPLETED_FR = "UPGRADE_COMPLETED_FR";

    public static final String PREF_LANG = "LANG_PREFS";
    public static final String PREF_NORMALIZATION = "NORMALIZATION_PREFS";
    public static final String PREF_WILDCARD_ON = "WILDCARD_ON_PREFS";
    public static final String PREF_DOUBLE_WILDCARD = "DOUBLE_WILDCARD_PREFS";
    public static final String PREF_SHOW_IMAGES = "SHOW_IMAGES_PREF";
    public static final String PREF_FONT_SIZE = "FONT_SIZE_PREF";
    public static final String PREF_TSW_SORTING = "TSW_SORTING_PREF";

    public static final String ALPHABETICAL_POSITION = "ALPHABETICAL_POSITION";
    public static final String LORE_TITLE = "LORE_TITLE";
    public static final String LORE_PAGE_POSITION = "LORE_PAGE_POSITION";
    public static final String SPINNER_POSITION = "SPINNER_POSITION";

    public static final int LANG_EN = 0;
    public static final int LANG_DE = 1;
    public static final int LANG_FR = 2;

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

    /*-------
    SETTERS
    -------*/

    public void setDbUpgradeCompleted(boolean val) {
        doEdit();
        mEditor.putBoolean(DB_UPGRADE_COMPLETED, val);
        doCommit();
    }

    public void setDbUpgradeCompletedDE(boolean val) {
        doEdit();
        mEditor.putBoolean(DB_UPGRADE_COMPLETED_DE, val);
        doCommit();
    }

    public void setDBUpgradeCompletedFR(boolean val) {
        doEdit();
        mEditor.putBoolean(DB_UPGRADE_COMPLETED_FR, val);
        doCommit();
    }

    public void useTswSorting(boolean val) {
        doEdit();
        mEditor.putBoolean(PREF_TSW_SORTING, val);
        doCommit();
    }

    public void setSpinnerPosition(int val) {
        doEdit();
        mEditor.putInt(SPINNER_POSITION, val);
        doCommit();
    }

    public void setLorePagePosition(int val) {
        doEdit();
        mEditor.putInt(LORE_PAGE_POSITION, val);
        doCommit();
    }

    public void setLoreTitle(String val) {
        doEdit();
        mEditor.putString(LORE_TITLE, val);
        doCommit();
    }

    public void setAlphabeticalPosition(int val) {
        doEdit();
        mEditor.putInt(ALPHABETICAL_POSITION, val);
        doCommit();
    }

    public void setFontSizePref(int val){
        doEdit();
        mEditor.putInt(PREF_FONT_SIZE, val);
        doCommit();
    }

    public void setImagePref(boolean val) {
        doEdit();
        mEditor.putBoolean(PREF_SHOW_IMAGES, val);
        doCommit();
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

    /*-------
    GETTERS
    -------*/

    public boolean isUpgradeCompleted() {
        return mPref.getBoolean(DB_UPGRADE_COMPLETED, false);
    }

    public boolean isUpgradeCompletedDE() {
        return mPref.getBoolean(DB_UPGRADE_COMPLETED_DE, false);
    }

    public boolean isUpgradeCompletedFR() {
        return mPref.getBoolean(DB_UPGRADE_COMPLETED_FR, false);
    }

    public boolean usingTswSorting() {
        return mPref.getBoolean(PREF_TSW_SORTING, false);
    }

    public int getSpinnerPosition() {
        return mPref.getInt(SPINNER_POSITION, 0);
    }

    public int getLorePagePosition() {
        return mPref.getInt(LORE_PAGE_POSITION, 0);
    }

    public String getLoreTitle() {
        return mPref.getString(LORE_TITLE, "Lilith");
    }

    public int getAlphabeticalPosition() {
        return mPref.getInt(ALPHABETICAL_POSITION, 0);
    }

    public int getFontSizePref() {
        return mPref.getInt(PREF_FONT_SIZE, 0);
    }

    public boolean getImagePref() {
        return mPref.getBoolean(PREF_SHOW_IMAGES, true);
    }

    public int getLangPref() {
        return mPref.getInt(PREF_LANG, 0);
    }

    public boolean usingNormalization() {
        return mPref.getBoolean(PREF_NORMALIZATION, true);
    }

    public boolean usingWildcards() {
        return mPref.getBoolean(PREF_WILDCARD_ON, false);
    }

    public boolean usingDoubleWildcards() {
        return mPref.getBoolean(PREF_DOUBLE_WILDCARD, false);
    }

    @SuppressLint("CommitPrefEdits")
    private void doEdit() {
        if (mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private void doCommit() {
        if (mEditor != null) {
            mEditor.apply();
        }
    }
}
