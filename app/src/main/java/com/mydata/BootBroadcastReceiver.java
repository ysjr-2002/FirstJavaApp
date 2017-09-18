package com.mydata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.myjava.*;

/**
 * Created by Shaojie on 2017/9/18.
 */

public class BootBroadcastReceiver  extends BroadcastReceiver {

    final String action_boot = Intent.ACTION_BOOT_COMPLETED;
    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {

            Intent newintent = new Intent(context, MainActivity.class);
            newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newintent);
        }
    }
}
