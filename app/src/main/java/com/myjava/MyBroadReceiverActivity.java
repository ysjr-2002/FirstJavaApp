package com.myjava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Handler;

public class MyBroadReceiverActivity extends AppCompatActivity {

    NetworkChangeReceiver networkChangeReceiver;

    Button button_send_broad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_broad_receiver);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        networkChangeReceiver = new NetworkChangeReceiver();
        this.registerReceiver(networkChangeReceiver, intentFilter);


        initView();
    }

//    SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//    SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void initView() {
        button_send_broad = (Button) findViewById(R.id.button_send_broad);


        button_send_broad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String s1 = "a";
                String s2 = "a";

                if (s1 == s2) {
                    String temp = "";
                }

                if (s1.equals(s2)) {
                    String temp = "";
                }

                if (TextUtils.equals(s1, s2)) {
                    String temp = "";
                }

                int pos = s1.indexOf(s2);

                s1 = "yangshaojie";
                s2 = "jie";

                pos = s1.indexOf(s2);

                s2 = s1.substring(4);

                String str = null;
                if (str == null) {

                }
                //int len = str.length();

                Calendar calendar = Calendar.getInstance();
                long current = System.currentTimeMillis();
                Date date = new Date(current);

                String str1 = sf1.format(new Date());
                String str2 = sf2.format(new Date());

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);

                int dayofyear = calendar.get(Calendar.DAY_OF_YEAR);
                int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
                int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);


                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");

                str = df1.format(new Date());
                str = df2.format(new Date());

                final Intent intent = new Intent("you.shit");
                intent.putExtra("name", "杨林哲");
                intent.putExtra("age", 7);
                sendBroadcast(intent);

                android.os.Handler handler = new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendOrderedBroadcast(intent, null);
                    }
                }, 5000);


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(networkChangeReceiver);
    }

    private void showtoast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {

                Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
