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

public class UniversalDrawer extends AppCompatActivity {

    public ActionBarDrawerToggle mDrawerToggle;
    private Activity mActivity;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;


    public void setupDrawer(Activity activity, Toolbar toolbar){
        mActivity = activity;
        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) mActivity.findViewById(R.id.left_drawer);
        final String[] mDrawerItems = mActivity.getResources().getStringArray(R.array.drawer_items);

        // Set the adapter for the list view
        DrawerAdapter drawerAdapter = new DrawerAdapter(mActivity, R.layout.drawer_list_item, mDrawerItems);
        mDrawerList.setAdapter(drawerAdapter);
        mDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
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
                if (mDrawerItems[position].equals("Legends Library")) {
                    Intent intent = new Intent(mActivity, MainActivity.class);
                    if (mActivity.getClass() != MainActivity.class) {
                        mActivity.startActivity(intent);
                        mDrawerLayout.closeDrawer(mDrawerList);
                    }
                }
                if (mDrawerItems[position].equals("About")) {
                    Intent intent = new Intent(mActivity, AboutActivity.class);
                    if (mActivity.getClass() != AboutActivity.class) {
                        mActivity.startActivity(intent);
                        mDrawerLayout.closeDrawer(mDrawerList);
                    }
                }
            }
        });

        if ( mActivity.getActionBar() != null ){
            mActivity.getActionBar().setDisplayHomeAsUpEnabled(true);
            mActivity.getActionBar().setHomeButtonEnabled(true);
        }

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

}
