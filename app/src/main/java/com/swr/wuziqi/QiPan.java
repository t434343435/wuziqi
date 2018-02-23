package com.swr.wuziqi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by t4343 on 2018/2/2.,,,,,,,,,
*/

public class QiPan {
    public final static int QiZi_BLACK = 0;
    public final static int QiZi_WHITE = 1;
    boolean isEnd;
    private int action;
    protected ArrayList<QiZi> black;
    protected ArrayList<QiZi> white;

    public QiPan(){
        black = new ArrayList<>();
        white = new ArrayList<>();
        action = QiZi_BLACK;
        isEnd = false;
    }

    public boolean setQizi(int x,int y){
        QiZi newQizi = new QiZi(x,y);
        if(black.contains(newQizi) || white.contains(newQizi))return false;
        else {
            if (black.size() == white.size()) {
                black.add(newQizi);
                action = QiZi_WHITE;
            }
            else {
                white.add(newQizi);
                action = QiZi_BLACK;
            }
            if (end()) isEnd = true;
        }
        return true;
    }

    public boolean end(){

        if(black.isEmpty() && white.isEmpty()) return false;
        else {
            int []dirX = {1,0,1,1};
            int []dirY = {0,1,1,-1};
            for(int i = 0 ;i < 4 ;i++) {
                int sum = 0;
                if( black.size() == white.size()) {
                    int lastX = white.get(white.size() - 1).getX();
                    int lastY = white.get(white.size() - 1).getY();
                    for(int j = 1 ;j < 5 ; j++){
                        if(white.contains(new QiZi(lastX + j * dirX[i],lastY + j * dirY[i])))sum++;
                        else break;
                    }
                    for(int j = 1 ;j < 5 ; j++){
                        if(white.contains(new QiZi(lastX - j * dirX[i],lastY - j * dirY[i])))sum++;
                        else break;
                    }
                }
                else{
                    int lastX = black.get(black.size() - 1).getX();
                    int lastY = black.get(black.size() - 1).getY();
                    for(int j = 1 ;j < 5 ; j++){
                        if(black.contains(new QiZi(lastX + j * dirX[i],lastY + j * dirY[i])))sum++;
                        else break;
                    }
                    for(int j = 1 ;j < 5 ; j++){
                        if(black.contains(new QiZi(lastX - j * dirX[i],lastY - j * dirY[i])))sum++;
                        else break;
                    }
                }
                if(sum >= 4)return true;
            }
            return false;
        }
    }

    public boolean undo(){
        if(isEnd)return false;
        if(black.isEmpty() && white.isEmpty()) return false;
        if( black.size() == white.size()) {
            white.remove(white.get(white.size() - 1));
            action = QiZi_WHITE;
        }
        else{
            black.remove(black.get(black.size() - 1));
            action = QiZi_BLACK;
        }
        return true;
    }

    public boolean getIsEnd(){
        return isEnd;
    }

    public void reset(){
        white.clear();
        black.clear();
        isEnd = false;
    }

    public ArrayList<QiZi> getBlack() {
        return black;
    }

    public ArrayList<QiZi> getWhite() {
        return white;
    }

    public int getAction() {
        return action;
    }

}