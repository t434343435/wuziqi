package com.swr.wuziqi;

import android.os.Handler;

import com.swr.wuziqi.QiPan;

import java.util.Random;

/**
 * Created by t4343 on 2018/2/22.
 */

public class BluetoothQiPan extends QiPan {
   // int firstAction;
    BluetoothService bluetoothService;
    public BluetoothQiPan(BluetoothService bluetoothService)
    {
        super();
    //    firstAction = //bluetoothService.mConnectedThread.socketType == BluetoothService.SOKETTYPE_SERVER ? QiZi_BLACK:QiZi_WHITE;
        this.bluetoothService = bluetoothService;
    }

    @Override
    public boolean setQizi(int x,int y){
    //    if(getAction() == firstAction)
            if(super.setQizi(x,y) && bluetoothService.mState == BluetoothService.STATE_CONNECTED) {
                switch (bluetoothService.mConnectedThread.socketType) {
                    case BluetoothService.SOKETTYPE_CLIENT:
                        String temp1 ="set " + x + "," + y + "\n";
                        bluetoothService.write(temp1.getBytes());
                 //       break;
                  //  case BluetoothService.SOKETTYPE_SERVER:
                        String temp2 = "set ";
                        for (QiZi qizi : black) {
                            temp2 +=  qizi.getX() + "," + qizi.getY() + ";" ;
                        }
                        temp2 += ":";
                        for (QiZi qizi : white) {
                            temp2 += qizi.getX() + "," + qizi.getY() + ";";
                        }
                        temp2+="\n";
                        bluetoothService.write(temp2.getBytes());
                        break;
                }
                return true;
            }
        return false;
    }

    @Override
    public boolean undo(){
       // if(getAction() == firstAction)
            if(bluetoothService.mState == BluetoothService.STATE_CONNECTED) {
                String temp = "undo ";
                bluetoothService.write(temp.getBytes());
                super.undo();
            }
        return true;
    }

    @Override
    public void reset(){

    }
}
