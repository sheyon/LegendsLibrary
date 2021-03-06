package com.sheyon.fivecats.legendslibrary;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
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

import com.sheyon.fivecats.legendslibrary.data.LegendsConstants.Languages;
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
        legendsPrefs = LegendsPreferences.getInstance(this);

        Toolbar toolbar = findViewById(R.id.settingsActivity_toolbar);
        setSupportActionBar(toolbar);

        setupDrawer(this, toolbar);

        RelativeLayout relativeLayout = findViewById(R.id.settingsActivity_relativeLayout);
        ScrollView scrollView = findViewById(R.id.settingsActivity_scrollView);
        RotationHandler.setupRotationLayout(this, relativeLayout, scrollView, toolbar);

        setupSpinner();
        setupCheckboxes();
        setupApplyButton();
        setupFontSize();
    }

    private void setupSpinner() {
        Spinner langSpinner = findViewById(R.id.settings_lang_spinner);

        ArrayAdapter langSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_item);
        langSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        langSpinner.setAdapter(langSpinnerAdapter);
        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("English")) {
                    langSelection = Languages.LANG_EN;
                }
                if (parent.getItemAtPosition(position).equals("Deutsch")) {
                    langSelection = Languages.LANG_DE;
                }
                if (parent.getItemAtPosition(position).equals("Français")) {
                    langSelection = Languages.LANG_FR;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                langSelection = Languages.LANG_EN;
            }
        });

        langSpinner.setSelection(legendsPrefs.getLangPref());
    }

    private void setupCheckboxes() {
        langCheckbox = findViewById(R.id.settings_lang_checkbox);
        wildcardOn = findViewById(R.id.settings_search_wildcard_on);
        doubleWildcard = findViewById(R.id.settings_search_double_wildcard);
        displayImages = findViewById(R.id.settings_display_images);
        tswSorting = findViewById(R.id.settings_categories);

        langCheckbox.setChecked(legendsPrefs.isUsingNormalization());
        wildcardOn.setChecked(legendsPrefs.isUsingWildcards());
        doubleWildcard.setChecked(legendsPrefs.isUsingDoubleWildcards());
        displayImages.setChecked(legendsPrefs.getImagePref());
        tswSorting.setChecked(legendsPrefs.isUsingTswSorting());
    }

    private void setupFontSize() {
        Button fontDecrement = findViewById(R.id.settings_font_decrement);
        Button fontIncrement = findViewById(R.id.settings_font_increment);
        final TextView fontSizeTextView = findViewById(R.id.settings_font_size);

        fontSize = legendsPrefs.getFontSizePref();
        updateFontSize(fontSizeTextView);

        fontDecrement.setOnClickListener(v -> {
            fontSize--;
            if (fontSize <= -1) {
                fontSize = 0;
            }
            updateFontSize(fontSizeTextView);
        });

        fontIncrement.setOnClickListener(v -> {
            fontSize++;
            if (fontSize >= 3) {
                fontSize = 2;
            }
            updateFontSize(fontSizeTextView);
        });
    }

    private void updateFontSize(TextView textView) {
        textView.setText("" + fontSize);
    }

    private void setupApplyButton() {
        Button settingsApplyButton = findViewById(R.id.settings_lang_button);

        settingsApplyButton.setOnClickListener(v -> {
            legendsPrefs.setLangPref(langSelection);
            legendsPrefs.setNormalizationPref(langCheckbox.isChecked());
            legendsPrefs.setWildcardAlwaysOnPref(wildcardOn.isChecked());
            legendsPrefs.setDoubleWildcardPref(doubleWildcard.isChecked());
            legendsPrefs.setFontSizePref(fontSize);
            legendsPrefs.setImagePref(displayImages.isChecked());
            legendsPrefs.setTswSorting(tswSorting.isChecked());

            //RESET THE SPINNER CAT NUMBER TO KEEP THE ARRAY FROM PERMANENTLY CRASHING
            legendsPrefs.setSpinnerPosition(0);
            legendsPrefs.setAlphabeticalPosition(0);

            restartDatabase();
        });
    }

    private void restartDatabase() {
        db.close();

        //WHEN SWITCHING LANGUAGES, MAKE SURE THE DATABASES ARE UPGRADED TO THE LATEST VERSION
        LegendsDatabase.initiateUpgrade(this, legendsPrefs);

        db = LegendsDatabase.getInstance(this);
        if (db.isReadOnly()) {
            Toast.makeText(this, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.toast_lang_changes, Toast.LENGTH_SHORT).show();
        }
    }
}