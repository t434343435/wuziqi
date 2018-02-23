package com.swr.wuziqi;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Handler mHandler;
    TextView log;
    MainView mainView;
    BluetoothService bluetoothService;

    public static final int MESSAGE_LOG = 0;
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_WRITE = 2;
    public static final int MESSAGE_INVALIDATE =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        Button start = findViewById(R.id.button_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainView)findViewById(R.id.game_view)).reset();
                findViewById(R.id.game_view).invalidate();
            }
        });
        Button undo = findViewById(R.id.button_undo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainView)findViewById(R.id.game_view)).undo();
                findViewById(R.id.game_view).invalidate();
            }
        });

        Button end = findViewById(R.id.button_exit);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        log = findViewById(R.id.log);
        log.setMovementMethod(new ScrollingMovementMethod());
        mHandler =  new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case MESSAGE_LOG:
                        log.append(msg.getData().getString("msg"));
                        int scroll_amount = (int) (log.getLineCount() * log.getLineHeight()) - (log.getBottom() - log.getTop());
                        log.scrollTo(0, scroll_amount);
                        break;
                    case MESSAGE_INVALIDATE:
                        mainView.invalidate();
                        break;
                    case MESSAGE_READ:
                        String message = new String((byte[]) msg.obj,0,msg.arg1);
                        String[] temp = message.split(":");
                        mainView.qiPan.setQizi(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
                        mHandler.obtainMessage(MESSAGE_INVALIDATE).sendToTarget();
                }
                return true;
            }
        });
        mainView = findViewById(R.id.game_view);
        mainView.mHandler = mHandler;
        bluetoothService = new BluetoothService(this,mHandler);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_client:
                if(bluetoothService.mState == bluetoothService.STATE_NONE) {
                    Intent intent = new Intent(this, DeviceListActivity.class);
                    startActivityForResult(intent, RESULT_FIRST_USER);
                }
                else
                    bluetoothService.addLog("不能重复建立连接！");
                break;
            case R.id.menu_server:
                if(bluetoothService.mState == bluetoothService.STATE_NONE){
                    bluetoothService.accept();
                }
                else
                    bluetoothService.addLog("不能重复建立连接！");
                break;
            case R.id.menu_cancel:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String address = data.getExtras()
                    .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            bluetoothService.connect(address);
        }
    }


}
