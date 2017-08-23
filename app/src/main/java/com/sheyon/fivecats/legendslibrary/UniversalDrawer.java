package com.sheyon.fivecats.legendslibrary;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

class UniversalDrawer extends AppCompatActivity {

    public ActionBarDrawerToggle mDrawerToggle;

    public void setupDrawer(final Activity activity, Toolbar toolbar){
        final DrawerLayout mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        final String[] mDrawerItems = activity.getResources().getStringArray(R.array.drawer_items);
        final ListView mDrawerList = (ListView) activity.findViewById(R.id.left_drawer);

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

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
            }
        });

        if ( activity.getActionBar() != null ){
            activity.getActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getActionBar().setHomeButtonEnabled(true);
        }

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }
}
