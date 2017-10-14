package com.myjava;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Script;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AutoUpgrade extends AppCompatActivity {

    @Bind(R.id.button_upgrade)
    Button buttonUpgrade;
    @Bind(R.id.button_check)
    Button buttonCheck;

    static final int message_update = 6543;
    @Bind(R.id.button_ws)
    Button buttonWs;
    @Bind(R.id.button_write)
    Button buttonWrite;
    @Bind(R.id.button_read)
    Button buttonRead;
    @Bind(R.id.edittext_write)
    EditText edittextWrite;
    @Bind(R.id.edittext_read)
    EditText edittextRead;
    @Bind(R.id.button_write_share)
    Button buttonWriteShare;
    @Bind(R.id.button_read_share)
    Button buttonReadShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_upgrade);
        ButterKnife.bind(this);
    }

    private void showToast(String str) {

        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    ProgressDialog pd;
    final String tag = "SHIT->";
    CheckVersionThread checkThread;

    @OnClick({R.id.button_upgrade, R.id.button_check, R.id.button_ws, R.id.button_write, R.id.button_read,R.id.button_write_share, R.id.button_read_share})
    public void onViewClicked(View view) {

        if (view.getId() == R.id.button_upgrade) {

            showUpdateDialog();
        }
        if (view.getId() == R.id.button_check) {

            handler = new MyHandler();

            checkThread = new CheckVersionThread();
            checkThread.start();
        }
        if (view.getId() == R.id.button_ws) {

            ConnectWS ws = new ConnectWS();
            Thread my = new Thread(ws);
            my.start();
        }
        if (view.getId() == R.id.button_write) {

            writeFile();
        }
        if (view.getId() == R.id.button_read) {
            readFile();
        }

        if (view.getId() == R.id.button_read_share) {

            readShare();
        }

        if (view.getId() == R.id.button_write_share) {
            writeShare( );
        }
    }

    private void writeShare() {

        SharedPreferences p = this.getSharedPreferences("shit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = p.edit();
        editor.putString("name", edittextWrite.getText().toString());
        editor.putString("age", "37");
        editor.commit();
    }

    private void readShare() {
        SharedPreferences p = this.getSharedPreferences("shit", Context.MODE_PRIVATE);
        String name = p.getString("name", "yangshaojie");
        String age = p.getString("age", "32");
    }

    private void writeFile() {

        try {
            FileOutputStream fs = openFileOutput("shit", Context.MODE_PRIVATE);
            String str = edittextWrite.getText().toString();
            byte[] bufer = str.getBytes();
            fs.write(bufer);
            fs.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void readFile() {
        try {

            FileInputStream fs = openFileInput("shit");
            BufferedInputStream bis = new BufferedInputStream(fs);

            int len = 0;
            byte[] buffer = new byte[1024];
            StringBuilder sb = new StringBuilder();
            while ((len = fs.read(buffer, 0, buffer.length)) != -1) {

                String str = new String(buffer, 0, len);
                sb.append(str);
            }

            String content = sb.toString();
            edittextRead.setText(content);

        } catch (Exception ex) {

        }
    }

    class ConnectWS implements Runnable {

        @Override
        public void run() {
            try {
                websocketTest();
            } catch (Exception ex) {

            }
        }
    }

    MyHandler handler;

    class CheckVersionThread extends Thread {

        @Override
        public void run() {

            String path = "http://192.168.2.131:9872/version.xml";
            try {

                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);

                InputStream is = conn.getInputStream();

                ParseXmlService service = new ParseXmlService();
                HashMap<String, String> hashMap = service.test(is);

                String name = hashMap.get("name");
                String version = hashMap.get("version");
                String updateUrl = hashMap.get("url");

                Message message = handler.obtainMessage();
                message.what = message_update;
                handler.sendMessage(message);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == message_update) {
                showUpdateDialog();
            }
        }
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("版本升级");
        builder.setMessage(R.string.updateinfo);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                downLoadApk();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                showToast("取消");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void downLoadApk() {

        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("更新下载");
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File updateFile = download(pd);
                    //关闭进度对话框
                    pd.dismiss();
                    update(updateFile);
                } catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void update(File file) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        this.startActivity(intent);
    }

    private File download(ProgressDialog pd) throws Exception {
        String path = "http://192.168.2.131:9872/1.apk";

        URL url = new URL(path);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);

        int totalLen = conn.getContentLength();
        Log.i(tag, "总长度=" + totalLen);

        pd.setMax(totalLen);

        InputStream is = conn.getInputStream();
        File file = new File(Environment.getExternalStorageDirectory(), "update.apk");

        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);

        byte[] buffer = new byte[1024];
        int len;
        int total = 0;
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            total += len;
            //获取当前下载量
            pd.setProgress(total);
        }
        fos.close();
        bis.close();
        is.close();
        return file;
    }


    private void websocketTest() throws Exception {

        String url = "ws://192.168.2.196:9872/android";
        URI uri = URI.create(url);

        WebSocketClient ws = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {

                String open = "open";
            }

            @Override
            public void onMessage(String message) {

                String temp = message;
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

                Integer i = code;
            }

            @Override
            public void onError(Exception ex) {

                ex.printStackTrace();
                ;
            }
        };

        ws.connect();

    }
}
