package com.sheyon.fivecats.legendslibrary;

import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import com.sheyon.fivecats.legendslibrary.data.LegendsContract;
import com.sheyon.fivecats.legendslibrary.data.LegendsDatabase;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

public class SettingsActivity extends NavigationDrawerActivity {

    private SQLiteDatabase db;
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

        setupDrawer(this, toolbar);

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
                    langSelection = LegendsContract.Languages.LANG_EN;
                }
                if (parent.getItemAtPosition(position).equals("Deutsch")) {
                    langSelection = LegendsContract.Languages.LANG_DE;
                }
                if (parent.getItemAtPosition(position).equals("Français")) {
                    langSelection = LegendsContract.Languages.LANG_FR;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                langSelection = LegendsContract.Languages.LANG_EN;
            }
        });

        langSpinner.setSelection(legendsPrefs.getLangPref());
    }

    private void setupLangCheckbox() {
        langCheckbox = findViewById(R.id.settings_lang_checkbox);
        langCheckbox.setChecked(legendsPrefs.usingNormalization());
    }

    private void setupSearchCheckboxes() {
        wildcardOn = findViewById(R.id.settings_search_wildcard_on);
        wildcardOn.setChecked(legendsPrefs.usingWildcards());

        doubleWildcard = findViewById(R.id.settings_search_double_wildcard);
        doubleWildcard.setChecked(legendsPrefs.usingDoubleWildcards());
    }

    private void setupMiscCheckboxes() {
        displayImages = findViewById(R.id.settings_display_images);
        displayImages.setChecked(legendsPrefs.getImagePref());

        //IF TSW SORTING PREFS DO NOT EXIST, CREATE THEM (DEFAULT: FALSE)
        if (!legendsPrefs.doesContain(LegendsPreferences.PREF_TSW_SORTING)) {
            legendsPrefs.useTswSorting(false);
        }
        tswSorting = findViewById(R.id.settings_categories);
        tswSorting.setChecked(legendsPrefs.usingTswSorting());
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
                legendsPrefs.useTswSorting(tswSorting.isChecked());
                legendsPrefs.setSpinnerPosition(0);

                restartDatabase();
            }
        });
    }

    private void restartDatabase() {
        db.close();

        db = LegendsDatabase.getInstance(this);
        if (db.isReadOnly()) {
            Toast.makeText(this, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, R.string.toast_lang_changes, Toast.LENGTH_SHORT).show();
        }
    }
}