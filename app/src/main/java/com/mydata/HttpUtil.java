package com.mydata;

import android.widget.Toast;

/**
 * Created by ysj on 2017/10/16.
 */

public class HttpUtil {

    public static String sendHttpRequest(String address) {
        if (!isNetworkAvailable()) {
            Toast.makeText(MyApplication.getContext(), "网络", Toast.LENGTH_SHORT).show();
        }

        return "";
    }

    private static boolean isNetworkAvailable() {
        return false;
    }
}
