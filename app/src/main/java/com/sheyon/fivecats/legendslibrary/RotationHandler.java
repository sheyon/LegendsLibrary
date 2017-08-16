package com.sheyon.fivecats.legendslibrary;

import android.app.Activity;
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

    private ViewGroup mRootLayout;
    private ViewGroup mChildLayout;
    private Activity mActivity;
    private Toolbar mToolbar;

    public void setupRotationLayout (Activity activity, ViewGroup rootLayout, ViewGroup childLayout, @Nullable Toolbar toolbar) {
        mActivity = activity;
        mRootLayout = rootLayout;
        mChildLayout = childLayout;
        mToolbar = toolbar;

        determineWidth();
    }

    private void determineWidth() {
        DisplayMetrics metrics = mActivity.getResources().getDisplayMetrics();
        int w = metrics.widthPixels;
        int h = metrics.heightPixels;
        boolean portrait;

        //SAVE THE WIDTH OF DEVICE IN PORTRAIT MODE
        float trueWidth;
        if (w > h) {
            trueWidth = h;
            portrait = false;
        }
        else {
            trueWidth = w;
            portrait = true;
        }

        //THE LORE ACTIVITY REQUIRES DIFFERENT PARAMS BECAUSE IT DOES NOT USE THE DRAWER LAYOUT
        if (mActivity.getClass() == LoreActivity.class) {
            mRootLayout.setLayoutParams(new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }
        else {
            mRootLayout.setLayoutParams(new DrawerLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }

        //SET SCROLLVIEW PARAMS
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams((int) trueWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
        if (mToolbar != null) {
            p.addRule(RelativeLayout.BELOW, mToolbar.getId());
        }
        p.addRule(RelativeLayout.CENTER_IN_PARENT);
        mChildLayout.setLayoutParams(p);

        if (!portrait) {
            mRootLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.backgroundDarker));
            mChildLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.backgroundBase));
        }
    }
}
