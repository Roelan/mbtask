package com.example.filechecker.qa;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import io.qameta.allure.android.runners.AllureAndroidJUnitRunner;

public class CustomTestRunner extends AllureAndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Log.i("TestRun", "[QA-Sanity]: App type is running using test runner: " + getClass().getSimpleName());
        return super.newApplication(cl, com.example.filechecker.qa.TestApp.class.getName(), context);
    }
}
