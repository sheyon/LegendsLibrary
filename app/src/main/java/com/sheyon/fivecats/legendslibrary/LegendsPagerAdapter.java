package com.sheyon.fivecats.legendslibrary;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

public class LegendsPagerAdapter extends FragmentPagerAdapter {

    private int mNumOftabs;

    public LegendsPagerAdapter(FragmentManager fm, int NumOfTabs)
    {
        super(fm);
        this.mNumOftabs = NumOfTabs;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                CategoriesFragment tab1 = new CategoriesFragment();
                return tab1;
            case 1:
                AlphabeticalFragment tab2 = new AlphabeticalFragment();
                return tab2;
            case 2:
                SearchFragment tab3 = new SearchFragment();
                return tab3;

        }

        return null;
    }

    @Override
    public int getCount() {
        return mNumOftabs;
    }
}
