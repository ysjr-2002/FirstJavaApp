package com.myjava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.*;

import com.Tool.Tools;
import com.mydata.Runner;
import com.mydata.RunnerDBManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.*;
import jxl.Workbook;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_create;
    Button buttona;
    Button buttonb;
    Button buttoncopy;
    TextView textView_info;

    Button button_db_clear;
    Button button_db_count;
    Button button_db_insert;
    Button button_db_search;
    Button button_excel;
    EditText editText_key;
    ImageView imageView_face;

    String root = "";

    final String TAG = "YSJ";

    private KeyReceiver keyReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        root = Environment.getExternalStorageDirectory().getPath();

        keyReceiver = new KeyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.rfid.FUN_KEY");
        this.registerReceiver(keyReceiver, intentFilter);
    }

    private void showtoast(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        button_create = (Button) findViewById(R.id.button_create);
        buttona = (Button) findViewById(R.id.button1);
        buttonb = (Button) findViewById(R.id.button2);
        buttoncopy = (Button) findViewById(R.id.button3);
        button_db_clear = (Button) findViewById(R.id.button_db_clear);
        button_db_count = (Button) findViewById(R.id.button_db_count);
        button_db_insert = (Button) findViewById(R.id.button_db_insert);
        button_db_search = (Button) findViewById(R.id.button_db_search);
        button_excel = (Button) findViewById(R.id.button_excel);
        editText_key = (EditText) findViewById(R.id.edittext_key);

        imageView_face = (ImageView) findViewById(R.id.imageView_face);
        button_create.setOnClickListener(this);
        buttona.setOnClickListener(this);
        buttonb.setOnClickListener(this);
        buttoncopy.setOnClickListener(this);
        button_db_count.setOnClickListener(this);
        button_db_insert.setOnClickListener(this);
        button_db_search.setOnClickListener(this);
        button_db_clear.setOnClickListener(this);
        button_excel.setOnClickListener(this);
        textView_info = (TextView) findViewById(R.id.textview_info);
    }

    Thread thread;

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_create:
                textView_info.setText("java is shit");
                break;
            case R.id.button1:
                newFolder("/aa");
                break;
            case R.id.button2:
                newFolder("/bb");
                break;
            case R.id.button3:
                final String source = "/download/ysj.jpg";
                String aa = root + "//aa";
                File aaFile = new File(aa);
                if (aaFile.exists() == false) {
                    aaFile.mkdir();
                }
//                String density = "/aa/ysj.jpg";
//                fileCopy(source, density);

                final int copymax = 10000;
                dialog = new ProgressDialog(this);
                dialog.setTitle("数据导入");
                dialog.setIcon(R.mipmap.ic_launcher_round);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMax(copymax);
                dialog.show();

                thread = new Thread() {
                    int step = 1;

                    @Override
                    public void run() {
                        super.run();
                        while (step <= copymax) {
                            String key = Integer.toString(step);
                            String temp = "//aa//" + Integer.toString(step) + ".jpg";
                            fileCopy(source, temp);
                            Runner runner = new Runner();
                            runner.setName(key);
                            runner.setCode(key);
                            runner.setPhoto(temp);
                            runner.setConfirm(getCurrentDateTime());
                            insert(runner);
                            dialog.setProgress(step);
                            step++;
                            try {
                                Thread.sleep(0);
                            } catch (Exception ex) {
                            }
                        }

                        dialog.cancel();
                    }
                };
                thread.start();
                break;
            case R.id.button_db_clear:
                RunnerDBManager.getInstance(this).delete();
                deleteDir(new File(root + "//aa//"));
                showtoast("删除成功");
                break;
            case R.id.button_db_count:
                int count = RunnerDBManager.getInstance(this).count();
                showtoast(Integer.toString(count));
                break;
            case R.id.button_db_insert:
                Runner runner = new Runner();
                runner.setName("ysj");
                runner.setCode("123");
                runner.setPhoto("ysj.jpg");
                runner.setConfirm(getCurrentDateTime());
                insert(runner);
                break;
            case R.id.button_db_search:
                String key = editText_key.getText().toString();
                search(key);
                break;
            case R.id.button_excel:
                imporexcel();
                break;
        }
    }

    private void imporexcel() {

        //仅支持读取excel 97-2003
        String path = root + "/download/runner.xls";
        try {

            Workbook book = Workbook.getWorkbook(new File(path));
            final Sheet sheet = book.getSheet(0);
            final int Rows = sheet.getRows();
            int Cols = sheet.getColumns();
            Log.d(TAG, "当前工作表的名字:" + sheet.getName());
            Log.d(TAG, "总行数:" + Rows + ", 总列数:" + Cols);

            final int copymax = Rows - 1;

            dialog = new ProgressDialog(this);
            dialog.setTitle("数据导入");
            dialog.setIcon(R.mipmap.ic_launcher_round);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(copymax);
            dialog.show();

            final String source = "/download/ysj.jpg";
            thread = new Thread() {
                int step = 1;

                @Override
                public void run() {
                    super.run();
//                    while (step <= copymax) {
//                        String key = Integer.toString(step);
//                        String temp = "//aa//" + Integer.toString(step) + ".jpg";
//                        fileCopy(source, temp);
//                        Runner runner = new Runner();
//                        runner.setName(key);
//                        runner.setCode(key);
//                        runner.setPhoto(temp);
//                        runner.setConfirm(getCurrentDateTime());
//                        insert(runner);
//                        dialog.setProgress(step);
//                        step++;
//                        try {
//                            Thread.sleep(0);
//                        } catch (Exception ex) {
//                        }
//                    }

                    try {
                        for (int i = 1; i < Rows; i++) {

                            String name = ReadData(sheet, i, 0);
                            String code = ReadData(sheet, i, 1);
                            String photo = ReadData(sheet, i, 2);
                            Log.d(TAG, name + " " + code + " " + photo);

                            String temp = "//aa//" + Integer.toString(i) + ".jpg";
                            dialog.setProgress(i);
                            Runner runner = new Runner();
                            runner.setName(name);
                            runner.setCode(code);
                            runner.setPhoto(temp);
                            runner.setConfirm(getCurrentDateTime());
                            insert(runner);

                            Thread.sleep(500);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    dialog.cancel();
                }
            };
            thread.start();


        } catch (java.io.IOException ex) {

            ex.printStackTrace();
        } catch (jxl.read.biff.BiffException ex) {
            ex.printStackTrace();
        }
    }

    public static String ReadData(Sheet excelSheet, int row, int col) {
        try {
            String CellData = "";
            Cell cell = excelSheet.getRow(row)[col];
            CellData = cell.getContents().toString();
            return CellData;
        } catch (Exception e) {
            return "";
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    private void insert(Runner runner) {
        RunnerDBManager.getInstance(this).insert(runner);
    }

    private static String getCurrentDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String temp = df.format(new Date());
        return temp;
    }

    private void search(String key) {
        editText_key.clearFocus();
        Runner runner = RunnerDBManager.getInstance(this).getRunner(key);
        if (runner != null) {
            String name = runner.getName();
            String code = runner.getCode();
            String photo = runner.getPhoto();
            String confirm = runner.getConfirm();
            String temp = String.format("name->%s, code->%s, photo->%s, confirm->%s", name, code, photo, confirm);
//            showtoast(temp);
            String path = root + photo;
            Bitmap bitmap = getLoacalBitmap(path);
            imageView_face.setImageBitmap(bitmap);
        } else {
            showtoast("查询记录为空");
        }
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    ProgressDialog dialog;

    private void fileCopy(String source, String density) {

        source = root + source;
        density = root + density;
        Log.d(TAG, density);
        Tools.copyFile(source, density);
    }

    private void newFolder(String name) {
        String path = root + name;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        if (file.exists()) {
            showtoast("目录创建成功");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aa:
                showtoast("aa");
                break;
            case R.id.bb:
                showtoast("bb");
                break;
            case R.id.cc:
                showtoast("cc");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d(TAG, "shit->" + Integer.toString(keyCode));
        return super.onKeyDown(keyCode, event);
    }

    private class KeyReceiver extends BroadcastReceiver {
        private String TAG = "KeyReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            int keyCode = intent.getIntExtra("keyCode", 0);
            boolean keyDown = intent.getBooleanExtra("keydown", false);
//			Log.e("down", ""+keyDown);
            if (keyDown) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_F1:
                        showtoast("f1");
                        break;
                    case KeyEvent.KEYCODE_F2:
                        showtoast("f2");
                        break;
                    case KeyEvent.KEYCODE_F3:
                        showtoast("f3");
                        break;
                    case KeyEvent.KEYCODE_F5:
                        showtoast("f5");
                        break;
                    case KeyEvent.KEYCODE_F4:
                        showtoast("f6");
                        break;
                }
            }
        }
    }
}
