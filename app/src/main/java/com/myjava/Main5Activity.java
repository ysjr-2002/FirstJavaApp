package com.myjava;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main5Activity extends Activity {

    @Bind(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        ButterKnife.bind(this);
    }

    private void showToast(Object object) {
        Toast.makeText(this, object.toString(), Toast.LENGTH_SHORT).show();
    }

    ProgressDialog pd = null;

    @OnClick(R.id.button)
    public void onViewClicked() {

//        MyClass my = new MyClass();
//        int number = my.getNumber(10, 10);
//        showToast(number);
//
//        my.test();
//
//        String str = my.getString();
//        showToast(str);
//
//
//        com.obria.MyClass ok = new com.obria.MyClass();
//        String shit = ok.getShit();
//        showToast(shit);

//        thread1.setName("thread1");
//        thread1.start();
//
//        Thread2 thread2 = new Thread2();
//        thread2.setName("thread2");
//        thread2.start();
//
//        Thread thread3 = new Thread(new MyRunnable());
//        thread3.setName("thread3");
//        thread3.start();

        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMax(100);
        pd.setTitle("this is my title");

        DownloadTask test = new DownloadTask();
        test.execute();
        test(100);
        test(200, 1, 2, 3);
    }

    final String TAG = "TAG";

    private void test(int x, int... args) {

        Log.d(TAG, String.valueOf(x));
        if (args != null) {
            Log.d(TAG, String.valueOf(args.length));
        } else {
            Log.d(TAG, "shitshitshitshitshitshitshitshitshitshitshitshitshitshitshitshitshit");
        }
    }

    private void test() {

    }

    private Thread thread1 = new Thread(new Runnable() {
        @Override
        public void run() {
            String str = "";
            String name = Thread.currentThread().getName();
        }
    });

    class Thread2 extends Thread {
        public void run() {
            String str = "";
            String name = Thread.currentThread().getName();
        }
    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            String str = "";
            String name = Thread.currentThread().getName();
        }
    }

    class DownloadTask extends AsyncTask<Void, Integer, Boolean> {

        /**
         * 显示进度对话框
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        /**
         * 处理下载任务
         *
         * @param voids
         * @return
         */
        @Override
        protected Boolean doInBackground(Void... voids) {

            try {

                int i = 1;
                while (i <= 100) {
                    i++;
                    Thread.sleep(100);
                    publishProgress(i);
                }
                return true;
            } catch (Exception ex) {

                return false;
            }
        }

        /**
         * 在这里更新下载进度
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int p = values[0];
            pd.setProgress(p);
        }

        /**
         * 关闭进度对话框
         *
         * @param aBoolean
         */
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            pd.dismiss();
        }
    }
}
