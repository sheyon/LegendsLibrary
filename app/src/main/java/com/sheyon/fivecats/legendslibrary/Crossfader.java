package com.sheyon.fivecats.legendslibrary;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Crossfader extends AppCompatActivity {

    private View mContentView;
    private View mLoadingView;

    public void crossfadeView (View contentView, View loadingView) {
        mContentView = contentView;
        mLoadingView = loadingView;
        crossfade();
    }

    private void crossfade() {
        final int mShortAnimationDuration = 250;

        // Animate the loading view to 0% opacity.
        mLoadingView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .start();

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        mContentView.setAlpha(0f);
        mContentView.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Animate the content view to 100% opacity
                mContentView.animate()
                        .alpha(1f)
                        .setDuration(mShortAnimationDuration)
                        .start();
            }
        }, 100);
    }
}
