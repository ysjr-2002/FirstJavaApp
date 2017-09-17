package com.myjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_create;
    TextView textView_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initView() {
        button_create = (Button) findViewById(R.id.button_create);
        button_create.setOnClickListener(this);
        textView_info = (TextView) findViewById(R.id.textview_info);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_create:
                textView_info.setText("this is shit");
                break;
        }
    }
}
