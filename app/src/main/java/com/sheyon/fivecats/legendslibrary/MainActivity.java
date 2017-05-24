package com.sheyon.fivecats.legendslibrary;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;

public class MainActivity extends AppCompatActivity {

    public static SQLiteDatabase legendsDB;
    private Toolbar toolbar;

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
        toolbar.setTitle("Categories");
        setSupportActionBar(toolbar);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.mainActivity_tab_layout);
        tabLayout.addTab(tabLayout.newTab()); //CATEGORIES
        tabLayout.addTab(tabLayout.newTab()); //ALPHABETICAL
        tabLayout.addTab(tabLayout.newTab()); //SEARCH
        tabLayout.addTab(tabLayout.newTab()); //FAVORITES

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        tabLayout.setupWithViewPager(viewPager);

        final LegendsPagerAdapter pagerAdapter = new LegendsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_format_list_bulleted_white_48dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_sort_by_alpha_white_48dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_search_white_48dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_star_white_48dp);

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

    private void openDatabase() {
        LegendsHelper legendsHelper = new LegendsHelper(this);
        try {
            legendsDB = legendsHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            legendsDB = legendsHelper.getReadableDatabase();
            String errorCode = e.getMessage();
            Log.e("***DB ERROR", errorCode);
            Toast.makeText(this, "Database failed to open. You may not fave items.", Toast.LENGTH_LONG).show();
        }
    }

    private void setPageTitle(TabLayout.Tab tab, LegendsPagerAdapter pagerAdapter) {
        Fragment f = pagerAdapter.getItem(tab.getPosition());

        if (f.getClass() == CategoriesFragment.class) {
            toolbar.setTitle("Categories");
        }
        if (f.getClass() == AlphabeticalFragment.class) {
            toolbar.setTitle("Alphabetical");
        }
        if (f.getClass() == SearchFragment.class) {
            toolbar.setTitle("Search");
        }
        if (f.getClass() == FavoritesFragment.class) {
            toolbar.setTitle("Favorites");
        } else {
            toolbar.setTitle("Legends Library");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        legendsDB.close();
    }
}