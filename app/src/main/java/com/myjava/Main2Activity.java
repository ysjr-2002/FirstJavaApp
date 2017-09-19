package com.myjava;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mydata.PostResult;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class Main2Activity extends Activity {


    @Bind(R.id.button_open)
    Button buttonOpen;
    @Bind(R.id.edittext_key)
    EditText edittextKey;
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.button_close)
    Button buttonClose;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.button_get)
    Button buttonGet;
    @Bind(R.id.button_apache_get)
    Button buttonApacheGet;
    @Bind(R.id.button_apache_post)
    Button buttonApachePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
    }

    private void showtoast(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.button_open, R.id.button_close, R.id.button_get, R.id.button_apache_get, R.id.button_apache_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_open:
                changeProgress();
                break;
            case R.id.button_close:
                Thread thread = new Thread(runnable);
                thread.start();
                break;
            case R.id.button_get:
                String temp = "";
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            getdata();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }.start();
                break;

            case R.id.button_apache_get:
                break;
            case R.id.button_apache_post:
                break;
        }
    }

    private void apache_get() {

        new Thread() {
            @Override
            public void run() {
                super.run();
            }
        }.start();
    }

    private void apache_post() {
        new Thread() {
            @Override
            public void run() {
                super.run();
            }
        }.start();
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                postdata();
            } catch (Exception ex) {
                String str = "";
            }
        }
    };


    private void getdata() throws Exception {
        String url = "http://wx.obira.cn/build/visitor/check_qr_code";
        String privateKey = "H7~c6aWd^{p/X3fq";

        String code = "0eb79e3b2ce112d799a5a68b6812003c";
        String temp = privateKey + code;
        String sign = getMD5(temp);
        String postdata = "code=" + code + "&sign=" + sign;

        url += "?" + postdata;

        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //默认值我GET
        con.setRequestMethod("GET");
        //添加请求头
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String content = response.toString();
        PostResult postResult = JSON.parseObject(content, PostResult.class);
        int status = postResult.status;
        String info = postResult.info;
    }

    private final String USER_AGENT = "Mozilla/5.0";

    private void postdata() throws Exception {
        String url = "http://wx.obira.cn/build/visitor/check_qr_code";
        String privateKey = "H7~c6aWd^{p/X3fq";

        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String code = "0eb79e3b2ce112d799a5a68b6812003c";
        String temp = privateKey + code;
        String sign = getMD5(temp);
        String postdata = "code=" + code + "&sign=" + sign;

        byte[] urlParameters = postdata.getBytes();

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(urlParameters, 0, urlParameters.length);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String content = response.toString();
        PostResult postResult = JSON.parseObject(content, PostResult.class);
        int status = postResult.status;
        String info = postResult.info;
    }

    private String getMD5(String val) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(val.getBytes());
            byte[] digest = md.digest();
            String md5 = byteArrayToHex(digest);
            return md5;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String byteArrayToHex(byte[] byteArray) {
        //首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        //new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符）
        char[] resultCharArray = new char[byteArray.length * 2];
        //遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        //字符数组组合成字符串返回
        return new String(resultCharArray);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 9876) {
                Bundle bundle = msg.getData();
                int progerss = bundle.getInt("p");
                progressBar.setProgress(progerss);
            }
        }
    };

    private void changeProgress() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    int i = 1;
                    while (true) {
                        progressBar.setProgress(i);
//                        Message message = handler.obtainMessage();
//                        message.what = 9876;
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("p",i);
//                        message.setData(bundle);
//                        handler.sendMessage(message);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //buttonClose.setText(Long.toString(System.currentTimeMillis()));
                            }
                        });

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                buttonClose.setText(Long.toString(System.currentTimeMillis()));
                            }
                        });


                        Thread.sleep(200);
                        i++;
                        if (i > 100) {
                            break;
                        }
                    }
                } catch (Exception ex) {
                    Log.d("SHIT", ex.getMessage());
                }
            }
        };
        thread.start();
    }

}
