package com.mydata;

import android.app.Application;
import android.content.Context;
import android.os.Message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;

/**
 * Created by ysj on 2017/10/16.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {

        return context;
    }
}
