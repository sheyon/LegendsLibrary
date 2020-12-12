package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

class RotationHandler {

    public static void setupRotationLayout (Context context, ViewGroup rootLayout, ViewGroup childLayout, @Nullable Toolbar toolbar) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
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
            if (rootLayout.getLayoutParams().getClass() == DrawerLayout.LayoutParams.class) {
                rootLayout.setLayoutParams(new DrawerLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            }
            //APPLIES TO LORE ACTIVITY
            else {
                rootLayout.setLayoutParams(new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            }
            //DARKEN THE ROOT BACKGROUND
            rootLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundDarker));
            childLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundBase));

            //SET SCROLLVIEW PARAMS
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams((int) trueWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
            if (toolbar != null) {
                p.addRule(RelativeLayout.BELOW, toolbar.getId());
            }
            p.addRule(RelativeLayout.CENTER_IN_PARENT);
            childLayout.setLayoutParams(p);
        }
    }
}