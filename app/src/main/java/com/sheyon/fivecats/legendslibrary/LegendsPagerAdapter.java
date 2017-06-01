package com.sheyon.fivecats.legendslibrary;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

class LegendsPagerAdapter extends com.sbrukhanda.fragmentviewpager.adapters.FragmentStatePagerAdapter {

    private int mNumOftabs;

    public LegendsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    LegendsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOftabs = NumOfTabs;
    }

    @Override
    public Fragment instantiateFragment(int position) {
        switch (position) {
            case 0:
                AlphabeticalFragment tab1 = new AlphabeticalFragment();
                return tab1;
            case 1:
                CategoriesFragment tab2 = new CategoriesFragment();
                return tab2;
            case 2:
                FavoritesFragment tab3 = new FavoritesFragment();
                return tab3;
            case 3:
                SearchFragment tab4 = new SearchFragment();
                return tab4;
        }
        return null;
    }

    //@Override
    Fragment getItem(int position) {
        switch (position) {
            case 0:
                AlphabeticalFragment tab1 = new AlphabeticalFragment();
                return tab1;
            case 1:
                CategoriesFragment tab2 = new CategoriesFragment();
                return tab2;
            case 2:
                FavoritesFragment tab3 = new FavoritesFragment();
                return tab3;
            case 3:
                SearchFragment tab4 = new SearchFragment();
                return tab4;
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOftabs;
    }
}
