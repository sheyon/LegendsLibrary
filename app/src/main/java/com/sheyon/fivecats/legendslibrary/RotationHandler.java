package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

class RotationHandler extends AppCompatActivity {

    private Context mContext;
    private ViewGroup mRootLayout;
    private ViewGroup mChildLayout;
    private Toolbar mToolbar;

    public void setupRotationLayout (Context context, ViewGroup rootLayout, ViewGroup childLayout, @Nullable Toolbar toolbar) {
        mContext = context;
        mRootLayout = rootLayout;
        mChildLayout = childLayout;
        mToolbar = toolbar;

        determineWidth();
    }

    private void determineWidth() {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int w = metrics.widthPixels;
        int h = metrics.heightPixels;
        boolean landscape;

        //SAVE THE WIDTH OF DEVICE IN PORTRAIT MODE
        float trueWidth;
        if (w > h) {
            trueWidth = h;
            landscape = true;
        }
        else {
            trueWidth = w;
            landscape = false;
        }

        //DIFFERENT LAYOUT REQUIRES DIFFERENT PARAMS
        if (landscape) {
            //APPLIES TO ABOUT AND SETTINGS ACTIVITY
            if (mRootLayout.getLayoutParams().getClass() == DrawerLayout.LayoutParams.class) {
                mRootLayout.setLayoutParams(new DrawerLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            }
            //APPLIES TO LORE ACTIVITY
            else {
                mRootLayout.setLayoutParams(new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            }
            //DARKEN THE ROOT BACKGROUND
            mRootLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.backgroundDarker));
            mChildLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.backgroundBase));
        }

        //SET SCROLLVIEW PARAMS
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams((int) trueWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
        if (mToolbar != null) {
            p.addRule(RelativeLayout.BELOW, mToolbar.getId());
        }
        p.addRule(RelativeLayout.CENTER_IN_PARENT);
        mChildLayout.setLayoutParams(p);
    }
}
