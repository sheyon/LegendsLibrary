package com.sheyon.fivecats.legendslibrary;

import android.content.res.Resources;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import com.sbrukhanda.fragmentviewpager.FragmentViewPager;

public class MainActivity extends NavigationDrawerActivity {

    private FragmentViewPager viewPager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflows, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.mainActivity_toolbar);
        toolbar.setTitle(R.string.title_alphabetical);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);

        setupDrawer(this, toolbar);

        final TabLayout tabLayout = findViewById(R.id.mainActivity_tab_layout);
        final LegendsPagerAdapter pagerAdapter = new LegendsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        setupIcons(tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                setPageTitle(tab, pagerAdapter, toolbar);
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

    private void setPageTitle(TabLayout.Tab tab, LegendsPagerAdapter pagerAdapter, Toolbar toolbar) {
        Fragment f = pagerAdapter.instantiateFragment(tab.getPosition());

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
        mDrawerToggle.syncState();
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