package com.myjava;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.neovisionaries.ws.client.ThreadType;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;

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

    @OnClick({R.id.button_upgrade, R.id.button_check})
    public void onViewClicked(View view) {

        if (view.getId() == R.id.button_upgrade) {

            showUpdateDialog();
        }
        if (view.getId() == R.id.button_check) {

            handler = new MyHandler();

            checkThread = new CheckVersionThread();
            checkThread.start();
        }
    }

    MyHandler handler;

    @OnClick(R.id.button_ws)
    public void onViewClicked() {

        Thread my = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    websocketTest();
                } catch (java.lang.Exception ex) {

                }
            }
        });
        my.start();


    }

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

        WebSocket ws = new WebSocketFactory().createSocket("ws://192.168.2.196:9872/android");

        ws.connect();

        ws.addListener(new WebSocketListener() {
            @Override
            public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {

            }

            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {

                showToast("open");
            }

            @Override
            public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {

            }

            @Override
            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {

            }

            @Override
            public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onTextMessage(WebSocket websocket, String text) throws Exception {

            }

            @Override
            public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

            }

            @Override
            public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onThreadCreated(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

            }

            @Override
            public void onThreadStarted(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

            }

            @Override
            public void onThreadStopping(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

            }

            @Override
            public void onError(WebSocket websocket, WebSocketException cause) throws Exception {

            }

            @Override
            public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {

            }

            @Override
            public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {

            }

            @Override
            public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {

            }

            @Override
            public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {

            }

            @Override
            public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {

            }

            @Override
            public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {

            }
        });
    }
}
