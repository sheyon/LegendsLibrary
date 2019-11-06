package com.sheyon.fivecats.legendslibrary;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;

class LegendsPagerAdapter extends com.sbrukhanda.fragmentviewpager.adapters.FragmentStatePagerAdapter {

    private int mNumOftabs;

    LegendsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOftabs = NumOfTabs;
    }

    @Override
    public Fragment instantiateFragment(int position) {
        switch (position) {
            case 0:
                return new AlphabeticalFragment();
            case 1:
                return new CategoriesFragment();
            case 2:
                return new FavoritesFragment();
            case 3:
                return new SearchFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOftabs;
    }
}