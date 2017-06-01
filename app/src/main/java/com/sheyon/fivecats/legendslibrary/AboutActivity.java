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

    private TextView aboutBlurb;
    private TextView privacyBlurb;
    private TextView legalBlurb;

    private TextView aboutExpander;
    private TextView privacyExpander;
    private TextView legalExpander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.aboutActivity_toolbar);
        setSupportActionBar(toolbar);

        universalDrawer = new UniversalDrawer();
        universalDrawer.setupDrawer(this, toolbar);

        TextView aboutHeader = (TextView) findViewById(R.id.header_about);
        TextView privacyHeader = (TextView) findViewById(R.id.header_privacy);
        TextView legalHeader = (TextView) findViewById(R.id.header_legal);

        aboutBlurb = (TextView) findViewById(R.id.textView_about);
        privacyBlurb = (TextView) findViewById(R.id.textView_privacy);
        legalBlurb = (TextView) findViewById(R.id.textView_legal);

        aboutExpander = (TextView) findViewById(R.id.about_expand_collapse);
        privacyExpander = (TextView) findViewById(R.id.privacy_expand_collapse);
        legalExpander = (TextView) findViewById(R.id.legal_expand_collapse);

        aboutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aboutBlurb.getVisibility() == View.VISIBLE){
                    aboutBlurb.setVisibility(View.GONE);
                    aboutExpander.setText("+");
                }
                else {
                    aboutBlurb.setVisibility(View.VISIBLE);
                    aboutExpander.setText("-");
                }
            }
        });

        privacyHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (privacyBlurb.getVisibility() == View.VISIBLE){
                    privacyBlurb.setVisibility(View.GONE);
                    privacyExpander.setText("+");
                }
                else {
                    privacyBlurb.setVisibility(View.VISIBLE);
                    privacyExpander.setText("-");
                }
            }
        });

        legalHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (legalBlurb.getVisibility() == View.VISIBLE){
                    legalBlurb.setVisibility(View.GONE);
                    legalExpander.setText("+");
                }
                else {
                    legalBlurb.setVisibility(View.VISIBLE);
                    legalExpander.setText("-");
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
