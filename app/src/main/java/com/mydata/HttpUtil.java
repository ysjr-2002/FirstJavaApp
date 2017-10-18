package com.mydata;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ysj on 2017/10/16.
 */

public class HttpUtil {

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        if (!isNetworkAvailable()) {
            Toast.makeText(MyApplication.getContext(), "网络", Toast.LENGTH_SHORT).show();
            return;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {

                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setDoOutput(false);

                    int len = connection.getContentLength();

                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    InputStream is = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    is.close();

                    if (listener != null) {
                        listener.onFinished(sb.toString());
                    }

                } catch (Exception ex) {
                    listener.onError(ex);
                } finally {

                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });
        thread.start();
    }

    private static boolean isNetworkAvailable() {
        return true;
    }
}
