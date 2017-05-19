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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mainActivity_toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mainActivity_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Categories"));
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
//        tabLayout.addTab(tabLayout.newTab().setText("Alphabetical"));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        final LegendsPagerAdapter pagerAdapter = new LegendsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        legendsDB.close();
    }

}