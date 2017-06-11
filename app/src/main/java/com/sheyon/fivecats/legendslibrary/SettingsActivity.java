package com.sheyon.fivecats.legendslibrary;

import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelperDE;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelperFR;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class SettingsActivity extends AppCompatActivity {

    private UniversalDrawer universalDrawer;
    private CheckBox langCheckbox;
    private int langSelection;
    private LegendsPreferences legendsPrefs;
    private boolean normalizationSelection = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settingsActivity_toolbar);
        setSupportActionBar(toolbar);

        universalDrawer = new UniversalDrawer();
        universalDrawer.setupDrawer(this, toolbar);

        legendsPrefs = LegendsPreferences.getInstance(getApplicationContext());

        setupLangSpinner();
        setupLangCheckbox();
        setupLangButton();
    }

    private void setupLangSpinner() {
        Spinner langSpinner = (Spinner) findViewById(R.id.settings_lang_spinner);

        ArrayAdapter langSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_item);
        langSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        langSpinner.setAdapter(langSpinnerAdapter);
        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("English")) {
                    langSelection = LoreLibrary.LANG_EN;
                }
                if (parent.getItemAtPosition(position).equals("German")) {
                    langSelection = LoreLibrary.LANG_DE;
                }
                if (parent.getItemAtPosition(position).equals("French")) {
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
        langCheckbox = (CheckBox) findViewById(R.id.settings_lang_checkbox);
        langCheckbox.setChecked(legendsPrefs.getNormalizationPref());
    }

    private void setupLangButton() {
        Button langButtonApply = (Button) findViewById(R.id.settings_lang_button);

        langButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (langCheckbox.isChecked()) {
                    normalizationSelection = true;
                }
                else {
                    normalizationSelection = false;
                }

                legendsPrefs.setLangPref(langSelection);
                legendsPrefs.setNormalizationPref(normalizationSelection);
                restartDatabase();
            }
        });
    }

    private void restartDatabase() {
        legendsDB.close();

        switch (langSelection) {
            case 0:
                //ENGLISH
                LegendsHelper legendsHelper = new LegendsHelper(this);
                try {
                    legendsDB = legendsHelper.getWritableDatabase();
                    Toast.makeText(this, R.string.toast_lang_changes, Toast.LENGTH_SHORT).show();
                } catch (SQLiteException e) {
                    legendsDB = legendsHelper.getReadableDatabase();
                    Toast.makeText(this, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
                }
                break;

            case 1:
                //GERMAN
                LegendsHelperDE legendsHelperDE = new LegendsHelperDE(this);
                try {
                    legendsDB = legendsHelperDE.getWritableDatabase();
                    Toast.makeText(this, R.string.toast_lang_changes, Toast.LENGTH_SHORT).show();
                } catch (SQLiteException e) {
                    legendsDB = legendsHelperDE.getReadableDatabase();
                    Toast.makeText(this, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
                }
                break;

            case 2:
                //FRENCH
                LegendsHelperFR legendsHelperFR = new LegendsHelperFR(this);
                try {
                    legendsDB = legendsHelperFR.getWritableDatabase();
                    Toast.makeText(this, R.string.toast_lang_changes, Toast.LENGTH_SHORT).show();
                } catch (SQLiteException e) {
                    legendsDB = legendsHelperFR.getReadableDatabase();
                    Toast.makeText(this, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
                }
                break;
        }
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
