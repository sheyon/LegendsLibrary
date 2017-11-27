package com.sheyon.fivecats.legendslibrary;

import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsDatabase;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

public class SettingsActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private UniversalDrawer universalDrawer;
    private CheckBox langCheckbox;
    private CheckBox wildcardOn;
    private CheckBox doubleWildcard;
    private CheckBox displayImages;
    private CheckBox tswSorting;
    private int fontSize;
    private int langSelection;
    private LegendsPreferences legendsPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = LegendsDatabase.getInstance(this);

        Toolbar toolbar = findViewById(R.id.settingsActivity_toolbar);
        setSupportActionBar(toolbar);

        universalDrawer = new UniversalDrawer();
        universalDrawer.setupDrawer(this, toolbar);

        RelativeLayout relativeLayout = findViewById(R.id.settingsActivity_relativeLayout);
        ScrollView scrollView = findViewById(R.id.settingsActivity_scrollView);
        RotationHandler.setupRotationLayout(this, relativeLayout, scrollView, toolbar);

        legendsPrefs = LegendsPreferences.getInstance(getApplicationContext());

        setupLangSpinner();
        setupLangCheckbox();
        setupApplyButton();
        setupSearchCheckboxes();
        setupMiscCheckboxes();
        setupFontSize();
    }

    private void setupLangSpinner() {
        Spinner langSpinner = findViewById(R.id.settings_lang_spinner);

        ArrayAdapter langSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_item);
        langSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        langSpinner.setAdapter(langSpinnerAdapter);
        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("English")) {
                    langSelection = LoreLibrary.LANG_EN;
                }
                if (parent.getItemAtPosition(position).equals("Deutsch")) {
                    langSelection = LoreLibrary.LANG_DE;
                }
                if (parent.getItemAtPosition(position).equals("Français")) {
                    langSelection = LoreLibrary.LANG_FR;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                langSelection = LoreLibrary.LANG_EN;
            }
        });

        langSpinner.setSelection(legendsPrefs.getLangPref());
    }

    private void setupLangCheckbox() {
        langCheckbox = findViewById(R.id.settings_lang_checkbox);
        langCheckbox.setChecked(legendsPrefs.getNormalizationPref());
    }

    private void setupSearchCheckboxes() {
        wildcardOn = findViewById(R.id.settings_search_wildcard_on);
        wildcardOn.setChecked(legendsPrefs.getWildcardAlwaysOnPref());

        doubleWildcard = findViewById(R.id.settings_search_double_wildcard);
        doubleWildcard.setChecked(legendsPrefs.getDoubleWildcardPref());
    }

    private void setupMiscCheckboxes() {
        displayImages = findViewById(R.id.settings_display_images);
        displayImages.setChecked(legendsPrefs.getImagePref());

        //IF TSW SORTING PREFS DO NOT EXIST, CREATE THEM (DEFAULT: FALSE)
        if (!legendsPrefs.doesContain(LegendsPreferences.PREF_TSW_SORTING)) {
            legendsPrefs.setTswSorting(false);
        }
        tswSorting = findViewById(R.id.settings_categories);
        tswSorting.setChecked(legendsPrefs.getTswSorting());
    }

    private void setupFontSize() {
        Button fontDecrement = findViewById(R.id.settings_font_decrement);
        Button fontIncrement = findViewById(R.id.settings_font_increment);
        final TextView fontSizeTextView = findViewById(R.id.settings_font_size);

        //IF FONT SIZE PREFS DO NOT EXIST, CREATE THEM (DEFAULT: 0)
        if (!legendsPrefs.doesContain(LegendsPreferences.PREF_FONT_SIZE)) {
            legendsPrefs.setFontSizePref(0);
        }

        fontSize = legendsPrefs.getFontSizePref();
        updateFontSize(fontSizeTextView);

        fontDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fontSize--;
                if (fontSize <= -1) {
                    fontSize = 0;
                }
                updateFontSize(fontSizeTextView);
            }
        });

        fontIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fontSize++;
                if (fontSize >= 3) {
                    fontSize = 2;
                }
                updateFontSize(fontSizeTextView);
            }
        });
    }

    private void updateFontSize(TextView textView) {
        textView.setText("" + fontSize);
    }

    private void setupApplyButton() {
        Button settingsApplyButton = findViewById(R.id.settings_lang_button);

        settingsApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                legendsPrefs.setLangPref(langSelection);
                legendsPrefs.setNormalizationPref(langCheckbox.isChecked());
                legendsPrefs.setWildcardAlwaysOnPref(wildcardOn.isChecked());
                legendsPrefs.setDoubleWildcardPref(doubleWildcard.isChecked());
                legendsPrefs.setFontSizePref(fontSize);
                legendsPrefs.setImagePref(displayImages.isChecked());

                //RESET THE SPINNER CAT NUMBER TO KEEP THE ARRAY FROM PERMANENTLY CRASHING
                legendsPrefs.setTswSorting(tswSorting.isChecked());
                legendsPrefs.setSpinnerCatNumber(0);

                restartDatabase();
            }
        });
    }

    private void restartDatabase() {
        db.close();

        db = LegendsDatabase.getInstance(this);
        Toast.makeText(this, R.string.toast_lang_changes, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        universalDrawer.mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        universalDrawer.mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (universalDrawer.mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }
}