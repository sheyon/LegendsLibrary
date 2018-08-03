package com.sheyon.fivecats.legendslibrary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends NavigationDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.aboutActivity_toolbar);
        setSupportActionBar(toolbar);

        setupDrawer(this, toolbar);

        RelativeLayout relativeLayout = findViewById(R.id.aboutActivity_relativeLayout);
        ScrollView scrollView = findViewById(R.id.aboutActivity_scrollView);
        RotationHandler.setupRotationLayout(this, relativeLayout, scrollView, toolbar);

        TextView aboutHeader = findViewById(R.id.header_about);
        TextView contactHeader = findViewById(R.id.header_contact);
        TextView privacyHeader = findViewById(R.id.header_privacy);
        TextView legalHeader = findViewById(R.id.header_legal);
        TextView thanksHeader = findViewById(R.id.header_thanks);

        final TextView aboutBlurb = findViewById(R.id.textView_about);
        final LinearLayout contactLayout = findViewById(R.id.contact_icon_layout);
        final TextView privacyBlurb = findViewById(R.id.textView_privacy);
        final TextView legalBlurb = findViewById(R.id.textView_legal);
        final TextView thanksBlurb = findViewById(R.id.textView_thanks);

        final ImageView aboutExpander = findViewById(R.id.about_expand_collapse);
        final ImageView contactExpander = findViewById(R.id.contact_expand_collapse);
        final ImageView privacyExpander = findViewById(R.id.privacy_expand_collapse);
        final ImageView legalExpander = findViewById(R.id.legal_expand_collapse);
        final ImageView thanksExpander = findViewById(R.id.thanks_expand_collapse);

        ImageView iconEmail = findViewById(R.id.email_icon);
        ImageView iconTwitter = findViewById(R.id.twitter_icon);
        ImageView iconTumblr = findViewById(R.id.tumblr_icon);

        aboutHeader.setOnClickListener(v -> {
            if (aboutBlurb.getVisibility() == View.VISIBLE){
                aboutBlurb.setVisibility(View.GONE);
                aboutExpander.setImageResource(R.drawable.ic_keyboard_arrow_down_white_18dp);
            }
            else {
                aboutBlurb.setVisibility(View.VISIBLE);
                aboutExpander.setImageResource(R.drawable.ic_keyboard_arrow_up_white_18dp);
            }
        });

        contactHeader.setOnClickListener(v -> {
            if (contactLayout.getVisibility() == View.VISIBLE){
                contactLayout.setVisibility(View.GONE);
                contactExpander.setImageResource(R.drawable.ic_keyboard_arrow_down_white_18dp);
            }
            else {
                contactLayout.setVisibility(View.VISIBLE);
                contactExpander.setImageResource(R.drawable.ic_keyboard_arrow_up_white_18dp);
            }
        });

        privacyHeader.setOnClickListener(v -> {
            if (privacyBlurb.getVisibility() == View.VISIBLE){
                privacyBlurb.setVisibility(View.GONE);
                privacyExpander.setImageResource(R.drawable.ic_keyboard_arrow_down_white_18dp);
            }
            else {
                privacyBlurb.setVisibility(View.VISIBLE);
                privacyExpander.setImageResource(R.drawable.ic_keyboard_arrow_up_white_18dp);
            }
        });

        legalHeader.setOnClickListener(v -> {
            if (legalBlurb.getVisibility() == View.VISIBLE){
                legalBlurb.setVisibility(View.GONE);
                legalExpander.setImageResource(R.drawable.ic_keyboard_arrow_down_white_18dp);
            }
            else {
                legalBlurb.setVisibility(View.VISIBLE);
                legalExpander.setImageResource(R.drawable.ic_keyboard_arrow_up_white_18dp);
            }
        });

        thanksHeader.setOnClickListener(v -> {
            if (thanksBlurb.getVisibility() == View.VISIBLE){
                thanksBlurb.setVisibility(View.GONE);
                thanksExpander.setImageResource(R.drawable.ic_keyboard_arrow_down_white_18dp);
            }
            else {
                thanksBlurb.setVisibility(View.VISIBLE);
                thanksExpander.setImageResource(R.drawable.ic_keyboard_arrow_up_white_18dp);
            }
        });

        iconEmail.setOnClickListener(v -> {
            String[] email = { "markelsmythe@gmail.com" };
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Legends Library App");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            else {
                Toast.makeText(getBaseContext(), R.string.toast_no_email, Toast.LENGTH_SHORT).show();
            }
        });

        iconTwitter.setOnClickListener(v -> {
            Intent intent;
            try {
                // get the Twitter app if possible
                getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=879362303623352320"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                // no Twitter app, revert to browser
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/legends_library"));
            }
            startActivity(intent);
        });

        iconTumblr.setOnClickListener(v -> {
            Intent intent;
            try {
                //OPEN BROWSER
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://swl-library-app.tumblr.com/"));
                //CANNOT LAUNCH DIRECTLY INTO TUMBLR APP ANYMORE?
//                getPackageManager().getPackageInfo("com.tumblr", 0);
//                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tumblr://x-callback-url/blog?blogName=swl-library-app"));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                //BROWSER ERROR. DO NOTHING
                Log.w ("WARNING", "Could not open the Tumblr URL.");
                return;
            }
            startActivity(intent);
        });
    }
}
