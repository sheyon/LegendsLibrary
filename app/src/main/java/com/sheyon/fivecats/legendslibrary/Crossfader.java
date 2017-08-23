package com.sheyon.fivecats.legendslibrary;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

class Crossfader extends AppCompatActivity {

    public static void crossfadeView (View contentView, View loadingView) {
        crossfade(contentView, loadingView);
    }

    private static void crossfade(final View contentView, View loadingView) {
        final int mShortAnimationDuration = 250;

        // Animate the loading view to 0% opacity.
        loadingView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .start();

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Animate the content view to 100% opacity
                contentView.animate()
                        .alpha(1f)
                        .setDuration(mShortAnimationDuration)
                        .start();
            }
        }, 100);
    }
}
