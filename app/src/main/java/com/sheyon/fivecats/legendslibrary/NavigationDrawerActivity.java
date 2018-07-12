package com.sheyon.fivecats.legendslibrary;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

abstract class NavigationDrawerActivity extends AppCompatActivity {

    ActionBarDrawerToggle mDrawerToggle;

    void setupDrawer(final Activity activity, Toolbar toolbar){
        final DrawerLayout mDrawerLayout = activity.findViewById(R.id.drawer_layout);
        final String[] mDrawerItems = activity.getResources().getStringArray(R.array.drawer_items);
        final ListView mDrawerList = activity.findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        DrawerAdapter drawerAdapter = new DrawerAdapter(activity, R.layout.drawer_list_item, mDrawerItems);
        mDrawerList.setAdapter(drawerAdapter);
        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerList.setOnItemClickListener((parent, view, position, id) -> {
            if (mDrawerItems[position].equals(activity.getString(R.string.drawer_legends))) {
                Intent intent = new Intent(activity, MainActivity.class);
                if (activity.getClass() != MainActivity.class) {
                    activity.startActivity(intent);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            }
            if (mDrawerItems[position].equals(activity.getString(R.string.drawer_settings))) {
                Intent intent = new Intent(activity, SettingsActivity.class);
                if (activity.getClass() != SettingsActivity.class) {
                    activity.startActivity(intent);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            }
            if (mDrawerItems[position].equals(activity.getString(R.string.drawer_about))) {
                Intent intent = new Intent(activity, AboutActivity.class);
                if (activity.getClass() != AboutActivity.class) {
                    activity.startActivity(intent);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            }
        });

        if ( activity.getActionBar() != null ){
            activity.getActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getActionBar().setHomeButtonEnabled(true);
        }

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }
}
