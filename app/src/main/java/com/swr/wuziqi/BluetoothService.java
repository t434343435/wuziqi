package com.swr.wuziqi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by t4343 on 2018/2/23.
 */

public class BluetoothService {

    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final String NAME = "五子棋";

    public int mState;

    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;

    public static final int SOKETTYPE_SERVER = 0;
    public static final int SOKETTYPE_CLIENT = 1;

    public static final int MESSAGE_LOG = 0;
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_WRITE = 2;
    public static final int MESSAGE_INVALIDATE =3;

    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private MainActivity activity;
    public ConnectedThread mConnectedThread;

    public BluetoothService(MainActivity activity,Handler handler){
        mHandler = handler;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.activity = activity;
        if (mBluetoothAdapter == null) {
            addLog("设备不支持蓝牙!");
        }
    }

    public void connect(String address){
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        new Thread(new ConnectThread(device)).start();
    }

    public void accept(){
        new Thread(new AcceptThread()).start();
    }

    public void write(byte[] out) {
        ConnectedThread r;
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        r.write(out);
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mState = STATE_CONNECTING;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                addLog("客户端连接失败: "+e.getMessage());
            }
            socket = tmp;

        }

        public void run() {
            addLog("客户端连接中...");
            mBluetoothAdapter.cancelDiscovery();

            try {
                socket.connect();
            } catch (IOException e) {
                addLog("连接失败！");
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e2) {
                    addLog("unable to close() socket during connection failure: "+e2.getMessage()+"\n");
                    socket = null;
                }

            }

            if (socket != null) {
                addLog("连接建立成功");
                addLog("远程设备名称: "+socket.getRemoteDevice().getName() + "  地址: "+socket.getRemoteDevice().getAddress());
                new Thread(new ConnectedThread(socket,SOKETTYPE_CLIENT)).start();
            } else {
                addLog("Made connection, but socket is null\n");
            }
            addLog("Client ending \n");

        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                addLog( "close() of connect socket failed: "+e.getMessage() +"\n");
            }
        }
    }
    private class AcceptThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            mState = STATE_LISTEN;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                addLog("启动服务端失败");
            }
            mmServerSocket = tmp;
        }
        @Override
        public void run() {
            addLog("等待客户端接受...");
            BluetoothSocket socket = null;
            try {

                socket = mmServerSocket.accept();
            } catch (IOException e) {
                addLog("无法接受客户请求");
            }


            if (socket != null) {
                addLog("连接建立成功!");
                addLog("远程设备地址: " + socket.getRemoteDevice().getAddress());
                new Thread(new ConnectedThread(socket,SOKETTYPE_SERVER)).start();

            } else {
                addLog("Made connection, but socket is null\n");
            }
            addLog("Server ending \n");
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                addLog( "close() of connect socket failed: "+e.getMessage() +"\n");
            }
        }
    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        public int socketType;

        public ConnectedThread(BluetoothSocket socket, int socketType) {
            addLog("创建连接线程成功，你的类型是" + (socketType == SOKETTYPE_SERVER ? "服务端":"客户端"));
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            this.socketType = socketType;
            mConnectedThread = this;
            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                addLog("套接口未定义");
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mState = STATE_CONNECTED;
        }

        public void run() {
            addLog("开始建立连接线程");
            byte[] buffer = new byte[1024];
            int bytes;
            activity.mainView.qiPan = new BluetoothQiPan(BluetoothService.this);
            mHandler.obtainMessage(MESSAGE_INVALIDATE)
                    .sendToTarget();
            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes,socketType, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    addLog("已失去连接");
                    mState = STATE_NONE;
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
         //       mHandler.obtainMessage(MESSAGE_WRITE, -1, socketType, buffer)
          //              .sendToTarget();
            } catch (IOException e) {
                addLog("写操作出现异常");
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                addLog("关闭套接字失败");
            }
        }
    }

    public void addLog(String str){
        //handler junk, because thread can't update screen!
        Message msg = mHandler.obtainMessage(MESSAGE_LOG);
        Bundle b = new Bundle();
        b.putString("msg", str + '\n');
        msg.setData(b);
        mHandler.sendMessage(msg);
    }
}
