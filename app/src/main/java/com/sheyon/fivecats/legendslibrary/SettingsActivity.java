package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelperDE;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelperFR;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class SettingsActivity extends AppCompatActivity {

    private UniversalDrawer universalDrawer;
    private int langSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settingsActivity_toolbar);
        setSupportActionBar(toolbar);

        universalDrawer = new UniversalDrawer();
        universalDrawer.setupDrawer(this, toolbar);

        setupLangSpinner();
        setupLangButton();
    }

    private void setupLangSpinner() {
        final Spinner langSpinner = (Spinner) findViewById(R.id.settings_lang_spinner);

        ArrayAdapter langSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_item);
        langSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        langSpinner.setAdapter(langSpinnerAdapter);
        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("English")) {
                    langSelection = LoreLibrary.LANG_ENG;
                }
                if (parent.getItemAtPosition(position).equals("German")) {
                    langSelection = LoreLibrary.LANG_GMN;
                }
                if (parent.getItemAtPosition(position).equals("French")) {
                    langSelection = LoreLibrary.LANG_FRN;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                langSelection = LoreLibrary.LANG_ENG;
            }
        });
    }

    private void setupLangButton() {
        Button langButtonApply = (Button) findViewById(R.id.settings_lang_button);

        langButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(getString(R.string.prefs_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.putInt(getString(R.string.prefs_lang), langSelection);
                editor.apply();
                closeDatabase();
            }
        });
    }

    private void closeDatabase() {
        legendsDB.close();
        if (LegendsHelperFR.normalized){
            LegendsHelperFR.normalized = false;
        }
        if (LegendsHelperDE.normalized){
            LegendsHelperDE.normalized = false;
        }

        switch (langSelection) {
            case 0:
                //ENGLISH
                LegendsHelper legendsHelper = new LegendsHelper(this);
                try {
                    legendsDB = legendsHelper.getWritableDatabase();
                    Toast.makeText(this, "Language changes applied.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Language changes applied.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Language changes applied.", Toast.LENGTH_SHORT).show();
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
