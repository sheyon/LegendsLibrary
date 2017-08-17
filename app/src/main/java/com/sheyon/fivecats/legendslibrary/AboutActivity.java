package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    private UniversalDrawer universalDrawer;

    private TextView aboutBlurb;
    private LinearLayout contactLayout;
    private TextView privacyBlurb;
    private TextView legalBlurb;
    private TextView thanksBlurb;

    private ImageView aboutExpander;
    private ImageView contactExpander;
    private ImageView privacyExpander;
    private ImageView legalExpander;
    private ImageView thanksExpander;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.aboutActivity_toolbar);
        setSupportActionBar(toolbar);

        universalDrawer = new UniversalDrawer();
        universalDrawer.setupDrawer(this, toolbar);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.aboutActivity_relativeLayout);
        ScrollView scrollView = (ScrollView) findViewById(R.id.aboutActivity_scrollView);
        RotationHandler rotationHandler = new RotationHandler();
        rotationHandler.setupRotationLayout(this, relativeLayout, scrollView, toolbar);

        TextView aboutHeader = (TextView) findViewById(R.id.header_about);
        TextView contactHeader = (TextView) findViewById(R.id.header_contact);
        TextView privacyHeader = (TextView) findViewById(R.id.header_privacy);
        TextView legalHeader = (TextView) findViewById(R.id.header_legal);
        TextView thanksHeader = (TextView) findViewById(R.id.header_thanks);

        aboutBlurb = (TextView) findViewById(R.id.textView_about);
        contactLayout = (LinearLayout) findViewById(R.id.contact_icon_layout);
        privacyBlurb = (TextView) findViewById(R.id.textView_privacy);
        legalBlurb = (TextView) findViewById(R.id.textView_legal);
        thanksBlurb = (TextView) findViewById(R.id.textView_thanks);

        aboutExpander = (ImageView) findViewById(R.id.about_expand_collapse);
        contactExpander = (ImageView) findViewById(R.id.contact_expand_collapse);
        privacyExpander = (ImageView) findViewById(R.id.privacy_expand_collapse);
        legalExpander = (ImageView) findViewById(R.id.legal_expand_collapse);
        thanksExpander = (ImageView) findViewById(R.id.thanks_expand_collapse);

        ImageView iconEmail = (ImageView) findViewById(R.id.email_icon);
        ImageView iconTwitter = (ImageView) findViewById(R.id.twitter_icon);
        ImageView iconTumblr = (ImageView) findViewById(R.id.tumblr_icon);

        aboutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aboutBlurb.getVisibility() == View.VISIBLE){
                    aboutBlurb.setVisibility(View.GONE);
                    aboutExpander.setImageResource(R.drawable.ic_keyboard_arrow_down_white_18dp);
                }
                else {
                    aboutBlurb.setVisibility(View.VISIBLE);
                    aboutExpander.setImageResource(R.drawable.ic_keyboard_arrow_up_white_18dp);
                }
            }
        });

        contactHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contactLayout.getVisibility() == View.VISIBLE){
                    contactLayout.setVisibility(View.GONE);
                    contactExpander.setImageResource(R.drawable.ic_keyboard_arrow_down_white_18dp);
                }
                else {
                    contactLayout.setVisibility(View.VISIBLE);
                    contactExpander.setImageResource(R.drawable.ic_keyboard_arrow_up_white_18dp);
                }
            }
        });

        privacyHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (privacyBlurb.getVisibility() == View.VISIBLE){
                    privacyBlurb.setVisibility(View.GONE);
                    privacyExpander.setImageResource(R.drawable.ic_keyboard_arrow_down_white_18dp);
                }
                else {
                    privacyBlurb.setVisibility(View.VISIBLE);
                    privacyExpander.setImageResource(R.drawable.ic_keyboard_arrow_up_white_18dp);
                }
            }
        });

        legalHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (legalBlurb.getVisibility() == View.VISIBLE){
                    legalBlurb.setVisibility(View.GONE);
                    legalExpander.setImageResource(R.drawable.ic_keyboard_arrow_down_white_18dp);
                }
                else {
                    legalBlurb.setVisibility(View.VISIBLE);
                    legalExpander.setImageResource(R.drawable.ic_keyboard_arrow_up_white_18dp);
                }
            }
        });

        thanksHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thanksBlurb.getVisibility() == View.VISIBLE){
                    thanksBlurb.setVisibility(View.GONE);
                    thanksExpander.setImageResource(R.drawable.ic_keyboard_arrow_down_white_18dp);
                }
                else {
                    thanksBlurb.setVisibility(View.VISIBLE);
                    thanksExpander.setImageResource(R.drawable.ic_keyboard_arrow_up_white_18dp);
                }
            }
        });

        iconEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] email = { "markelsmythe@gmail.com" };
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, email);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Legends Library App");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                else {
                    Toast.makeText(context, R.string.toast_no_email, Toast.LENGTH_SHORT).show();
                }
            }
        });

        iconTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                try {
                    // get the Twitter app if possible
                    getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=2204276767"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MarkelSmythe"));
                }
                startActivity(intent);
            }
        });

        iconTumblr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                try {
                    // get the Tumblr app if possible
                    getPackageManager().getPackageInfo("com.tumblr", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tumblr://x-callback-url/blog?blogName=swl-library-app"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Tumblr app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://swl-library-app.tumblr.com/"));
                }
                startActivity(intent);
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
