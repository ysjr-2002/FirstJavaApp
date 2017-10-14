package com.myjava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by ysj on 2017/10/14.
 */


public class MyCustomBroadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String name = intent.getStringExtra("name");
        int age = intent.getIntExtra("age",100);
        String str = String.format("my name is %s, my age is %s", name,age);

        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();


    }
}
