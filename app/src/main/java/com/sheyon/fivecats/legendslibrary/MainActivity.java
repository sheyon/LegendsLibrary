package com.sheyon.fivecats.legendslibrary;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;

public class MainActivity extends AppCompatActivity {

    public static SQLiteDatabase legendsDB;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.mainActivity_toolbar);
        toolbar.setTitle("Categories");
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mainActivity_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_format_list_bulleted_white_48dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_sort_by_alpha_white_48dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_search_white_48dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_star_white_48dp));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        final LegendsPagerAdapter pagerAdapter = new LegendsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                setPageTitle(tab);
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

        //OPEN DATABASE
        LegendsHelper legendsHelper = new LegendsHelper(this);
        legendsDB = legendsHelper.getReadableDatabase();
    }

    private void setPageTitle(TabLayout.Tab tab)
    {
        switch (tab.getPosition()) {
            case 0:
                toolbar.setTitle("Categories");
                break;
            case 1:
                toolbar.setTitle("Alphabetical");
                break;
            case 2:
                toolbar.setTitle("Search");
                break;
            case 3:
                toolbar.setTitle("Favorites");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        legendsDB.close();
    }
}