package com.example.filechecker.qa;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.filechecker.App;

import java.io.File;
import java.lang.ref.WeakReference;

public class TestApp extends App implements Application.ActivityLifecycleCallbacks {

    public static String TAG = "Espresso";
    private WeakReference<Activity> weakActivityRef;

    @Override
    public void onCreate() {
        super.onCreate();
        createReportFolder();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
        weakActivityRef = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        weakActivityRef = new WeakReference<>(activity);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        weakActivityRef = new WeakReference<>(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    public void startNewTaskActivity(Class<? extends Activity> activity) {
        Log.i(TAG, "Open new Activity");
        final Intent intent = new Intent(this, activity)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void createReportFolder() {
        File reportFolder = new File("/sdcard/allure-results/");
        if (reportFolder.exists()) {
            Log.i(TAG, "Report folder already exist");
        } else {
            reportFolder.mkdirs();
        }
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    @Nullable
    public Activity getActivity() {
        return weakActivityRef.get();
    }
}