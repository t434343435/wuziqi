package com.swr.wuziqi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by t4343 on 2018/2/2.
*/

public class QiPan {
    public static int QiZi_BLACK = 0;
    public static int QiZi_WHITE = 1;
    private boolean isEnd;
    private ArrayList<QiZi> black;
    private ArrayList<QiZi> white;

    public QiPan(){
        black = new ArrayList<>();
        white = new ArrayList<>();
        isEnd = false;
    }

    public void setQizi(int x,int y){
        QiZi newQizi = new QiZi(x,y);
        if(black.contains(newQizi) || white.contains(newQizi))return;
        else {
            if (black.size() == white.size())
                black.add(newQizi);
            else
                white.add(newQizi);
            if (end()) isEnd = true;
        }
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

    public void undo(){
        if(isEnd)return;
        if(black.isEmpty() || white.isEmpty()) return;
        if( black.size() == white.size()) {
            white.remove(white.get(white.size() - 1));
        }
        else{
            black.remove(black.get(white.size() - 1));
        }
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
}