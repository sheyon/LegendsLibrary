//package com.sheyon.fivecats.legendslibrary;
//
//import android.app.Application;
//import android.util.Log;
//
//import com.crashlytics.android.Crashlytics;
//import com.crashlytics.android.core.CrashlyticsCore;
//import com.crashlytics.android.core.CrashlyticsListener;
//
//import io.fabric.sdk.android.Fabric;
//
//public class LegendsLibraryApp extends Application {
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        initializeCrashlytics();
//    }
//
//    private void initializeCrashlytics() {
//        final CrashlyticsListener listener = new CrashlyticsListener() {
//            @Override
//            public void crashlyticsDidDetectCrashDuringPreviousExecution(){
//                Log.v("***DEBUG", "ERROR LAST SESSION");
//            }
//        };
//
//        final CrashlyticsCore core = new CrashlyticsCore
//                .Builder()
//                .listener(listener)
//                .build();
//
//        Fabric.with(this, new Crashlytics.Builder().core(core).build());
//    }
//}