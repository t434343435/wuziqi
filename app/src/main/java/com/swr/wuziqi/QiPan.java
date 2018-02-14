package com.swr.wuziqi;

import java.util.List;

/**
 * Created by t4343 on 2018/2/2.
*/

public class QiPan {
    public static int QiZi_EMPTY = 0;
    public static int QiZi_BLACK = 1;
    public static int QiZi_WHITE = 2;
    private int lastX;
    private int lastY;
    private int currentAction;
    private List<Integer> black;
    private List<Integer> white;

    public int[][] qiZi;
    public QiPan(){
        qiZi = new int [15][15];
        for (int i=0;i<15;i++) {
            for (int j=0;j<15;j++)
                qiZi[i][j] = QiZi_EMPTY;
        }
        currentAction = QiZi_BLACK;
        lastX = lastY = -1;
    }
    public void setQizi(int x,int y){
        qiZi[x][y] = currentAction;
        lastX = x;
        lastY = y;
        currentAction = (currentAction == QiZi_BLACK ? QiZi_WHITE : QiZi_BLACK);
    }
    public boolean isEnd(){
        if(lastX == -1 && lastY == -1) return false;
        else {
            int []dirX = {1,0,1,1};
            int []dirY = {0,1,1,-1};
            for(int i = 0 ;i < 4 ;i++) {
                int sum = 0;
                for(int j = 1 ;j < 5 ; j++){
                    if(qiZi[lastX + j * dirX[i]][lastY + j * dirY[i]] == qiZi[lastX][lastY])sum++;
                        else break;
                }
                for(int j = 1 ;j < 5 ; j++){
                    if(qiZi[lastX - j * dirX[i]][lastY - j * dirY[i]] == qiZi[lastX][lastY])sum++;
                        else break;
                }
                if(sum >= 4)return true;
            }
            return false;
        }
    }
    public void undo(){

    }
    public void reset(){
        qiZi = new int [15][15];
        for (int i=0;i<15;i++) {
            for (int j=0;j<15;j++)
                qiZi[i][j] = QiZi_EMPTY;
        }
        currentAction = QiZi_BLACK;
        lastX = lastY = -1;
    }
}