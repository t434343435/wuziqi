package com.swr.wuziqi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainActivity Cont =  this;
        final Button button1 = (Button) findViewById(R.id.button_scan);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent serverIntent = new Intent((Context) Cont, DeviceListActivity.class);
                startActivityForResult(serverIntent, 1);
            }
        });
        final Button button2 = (Button) findViewById(R.id.button_exit);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
