package com.sheyon.fivecats.legendslibrary;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private UniversalDrawer universalDrawer;

    private TextView aboutHeader;
    private TextView privacyHeader;
    private TextView legalHeader;
    private TextView aboutBlurb;
    private TextView privacyBlurb;
    private TextView legalBlurb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.aboutActivity_toolbar);
        setSupportActionBar(toolbar);

        universalDrawer = new UniversalDrawer();
        universalDrawer.setupDrawer(this, toolbar);

        aboutHeader = (TextView) findViewById(R.id.header_about);
        privacyHeader = (TextView) findViewById(R.id.header_privacy);
        legalHeader = (TextView) findViewById(R.id.header_legal);

        aboutBlurb = (TextView) findViewById(R.id.textView_about);
        privacyBlurb = (TextView) findViewById(R.id.textView_privacy);
        legalBlurb = (TextView) findViewById(R.id.textView_legal);

        aboutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aboutBlurb.getVisibility() == View.VISIBLE){
                    aboutBlurb.setVisibility(View.GONE);
                }
                else {
                    aboutBlurb.setVisibility(View.VISIBLE);
                }
            }
        });

        privacyHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (privacyBlurb.getVisibility() == View.VISIBLE){
                    privacyBlurb.setVisibility(View.GONE);
                }
                else {
                    privacyBlurb.setVisibility(View.VISIBLE);
                }
            }
        });

        legalHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (legalBlurb.getVisibility() == View.VISIBLE){
                    legalBlurb.setVisibility(View.GONE);
                }
                else {
                    legalBlurb.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        universalDrawer.mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        universalDrawer.mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (universalDrawer.mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }
}
