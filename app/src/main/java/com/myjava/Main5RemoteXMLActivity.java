package com.myjava;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mydata.HttpCallbackListener;
import com.mydata.HttpUtil;
import com.mydata.MyApplication;
import com.mydata.WeatherResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main5RemoteXMLActivity extends Activity {


    final String TAG = "aaaaaaaaaaaaaaaaaa";
    @Bind(R.id.button_get)
    Button buttonGet;
    @Bind(R.id.button_post)
    Button buttonPost;
    @Bind(R.id.button_util)
    Button buttonUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5_remote_xml);
        ButterKnife.bind(this);
    }

    private void showToast(final String content) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyApplication.getContext(), content, Toast.LENGTH_SHORT).show();
            }
        });

    }


    final String privateKey = "H7~c6aWd^{p/X3fq";

    @OnClick({R.id.button_get, R.id.button_post, R.id.button_util})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_get:
                Thread thread = new Thread(new HttpGet());
                thread.start();
                break;
            case R.id.button_post:
                Thread thread1 = new Thread(new HttpPost());
                thread1.start();
                break;
            case R.id.button_util:
                HttpUtil.sendHttpRequest("http://www.baidu.com", new HttpCallbackListener() {

                    @Override
                    public void onFinished(String response) {
                        showToast(response);
                    }

                    @Override
                    public void onError(Exception ex) {
                        ex.printStackTrace();
                    }
                });
                break;
        }
    }

    class HttpPost implements Runnable {

        @Override
        public void run() {
            try {
                String path = "http://wx.obira.cn/build/visitor/check_qr_code";
                String code = "123";
                String sign = getSign(privateKey, code);
                String postData = "code=%s&sign=%s";
                postData = String.format(postData, code, sign);

                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.close();

                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                String line = br.readLine();

                JSONObject jsonObject = new JSONObject(line);
                String info = jsonObject.getString("info");
                int status = jsonObject.getInt("status");

                boolean stop = true;

            } catch (Exception ex) {

            }
        }

        public String getMD5(String s) {
            char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            try {
                byte[] btInput = s.getBytes("utf-8");
                MessageDigest mdInst = MessageDigest.getInstance("MD5");
                mdInst.update(btInput);
                byte[] md = mdInst.digest();
                int j = md.length;
                char str[] = new char[j * 2];
                int k = 0;
                for (int i = 0; i < j; i++) {
                    byte byte0 = md[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                    str[k++] = hexDigits[byte0 & 0xf];
                }
                return new String(str);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        private String getSign(String privateKey, String code) {

            code = privateKey + code;
            String md5 = getMD5(code);
            String lowerCase = md5.toLowerCase();
            String upperCase = md5.toUpperCase();
            return md5;
        }
    }


    class HttpGet implements Runnable {

        @Override
        public void run() {

            try {
                URL url = new URL("http://www.weather.com.cn/data/cityinfo/101190404.html");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int status = conn.getResponseCode();
                int len = conn.getContentLength();

                Log.d(TAG, Integer.toString(len));

                InputStream is = conn.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                is.close();
                br.close();

                Log.d(TAG, sb.toString());

                WeatherResult result = JSON.parseObject(sb.toString(), WeatherResult.class);
                String json = JSON.toJSONString(result);
                String temp = "";


            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void JsonReader() {

            try {
                JSONObject jsonObject = new JSONObject("");
                JSONObject weatherInfo = jsonObject.getJSONObject("shit");

                String city = weatherInfo.getString("city");
                String weatherCode = weatherInfo.getString("cityid");
            } catch (JSONException ex) {

            }
        }
    }
}
