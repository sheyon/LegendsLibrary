package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sbrukhanda.fragmentviewpager.FragmentViewPager;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelperDE;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelperFR;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static SQLiteDatabase legendsDB;

    private Toolbar toolbar;
    private FragmentViewPager viewPager;
    private UniversalDrawer universalDrawer;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflows, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDatabase();

        toolbar = (Toolbar) findViewById(R.id.mainActivity_toolbar);
        toolbar.setTitle(R.string.title_alphabetical);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);

        universalDrawer = new UniversalDrawer();
        universalDrawer.setupDrawer(this, toolbar);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.mainActivity_tab_layout);
        viewPager = (FragmentViewPager) findViewById(R.id.view_pager);
        final LegendsPagerAdapter pagerAdapter = new LegendsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

        setupIcons(tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                setPageTitle(tab, pagerAdapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //DO NOTHING
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //DO NOTHING
            }
        });
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

    private void openDatabase() {
        SharedPreferences settings = getSharedPreferences(getString(R.string.prefs_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        //IF LANG PREFS DO NOT EXIST, CREATE THEM
        if (!settings.contains(getString(R.string.prefs_lang))) {
            String lang = Locale.getDefault().getLanguage();
            int i;
            switch (lang) {
                case "en":
                    i = 0;
                    break;
                case "de":
                    i = 1;
                    break;
                case "fr":
                    i = 2;
                    break;
                default:
                    i = 0;
                    break;
            }
            editor.putInt(getString(R.string.prefs_lang), i);
            editor.apply();
        }

        int langPref = settings.getInt(getString(R.string.prefs_lang), 0);

        //OPEN DATABASE BASED ON PREFERED LANGUAGE SETTING
        switch (langPref) {
            case 0:
                LegendsHelper legendsHelper = new LegendsHelper(this);
                try {
                    legendsDB = legendsHelper.getWritableDatabase();
                } catch (SQLiteException e) {
                    legendsDB = legendsHelper.getReadableDatabase();
                    Toast.makeText(this, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
                }
                break;

            case 1:
                LegendsHelperDE legendsHelperDE = new LegendsHelperDE(this);
                try {
                    legendsDB = legendsHelperDE.getWritableDatabase();
                } catch (SQLiteException e) {
                    legendsDB = legendsHelperDE.getReadableDatabase();
                    Toast.makeText(this, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
                }
                break;

            case 2:
                LegendsHelperFR legendsHelperFR = new LegendsHelperFR(this);
                try {
                    legendsDB = legendsHelperFR.getWritableDatabase();
                } catch (SQLiteException e) {
                    legendsDB = legendsHelperFR.getReadableDatabase();
                    Toast.makeText(this, R.string.toast_write_db_fail, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void setupIcons(TabLayout tabLayout){
        //ICONS MUST BE SET PROGRAMATICALLY, EVEN IF THEY ARE IN THE XML
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_sort_by_alpha_white_48dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_format_list_bulleted_white_48dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_star_white_48dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_search_white_48dp);

        //FOR SPREADING OUT TAB ICONS ON TABLET SCREENS
        int w = (int)((Resources.getSystem().getDisplayMetrics().widthPixels)/Resources.getSystem().getDisplayMetrics().density);
        if ( w > 370 ) {
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    private void setPageTitle(TabLayout.Tab tab, LegendsPagerAdapter pagerAdapter) {
        Fragment f = pagerAdapter.getItem(tab.getPosition());

        if (f.getClass() == CategoriesFragment.class) {
            toolbar.setTitle(R.string.title_categories);
        }
        if (f.getClass() == AlphabeticalFragment.class) {
            toolbar.setTitle(R.string.title_alphabetical);
        }
        if (f.getClass() == FavoritesFragment.class) {
            toolbar.setTitle(R.string.title_favorites);
        }
        if (f.getClass() == SearchFragment.class) {
            toolbar.setTitle(R.string.title_search);
        }
        universalDrawer.mDrawerToggle.syncState();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        viewPager.notifyPagerVisible();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewPager.notifyPagerInvisible();
    }
}