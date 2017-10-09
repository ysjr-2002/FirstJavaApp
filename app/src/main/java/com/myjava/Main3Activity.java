package com.myjava;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main3Activity extends Activity {

    @Bind(R.id.button_1)
    Button button1;
    @Bind(R.id.textview_info)
    TextView textviewInfo;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);
    }

    private void showtoast(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    Handler handler = new Handler();

    @OnClick(R.id.button_1)
    public void onViewClicked(View view) {

        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        } else if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }


//        String str = "";
//        StringBuilder sb = new StringBuilder();
//        sb.append("java is shit");
//        textviewInfo.setText(sb);
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                StringBuffer buffer = new StringBuffer();
//                buffer.append("C# is good");
//                textviewInfo.setText(buffer);
//            }
//        }, 5000);
//
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//
//                final StringBuffer sb = new StringBuffer();
//                sb.append("StringBuffer is ok");
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        textviewInfo.setText(sb);
//                    }
//                });
//            }
//        };
//
//        Timer timer = new Timer();
//        timer.schedule(timerTask, 10 * 1000);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(15 * 1000);
//                } catch (Exception ex) {
//
//                }
//
//                final  String temp = "thread is ok";
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        textviewInfo.setText(temp);
//                    }
//                });
//            }
//        }).start();
    }

    private void test() {

        String str = "shit";
        String temp = str.toString();

        Integer age = 40;
        String sex = age.toString();
    }
}
