package com.swr.wuziqi;

/**
 * Created by t4343 on 2018/2/14.
 */

public class QiZi {
    private int x;
    private int y;
    public QiZi(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Object o){
        if(o instanceof QiZi)
            if(x == ((QiZi) o).getX() && y == ((QiZi) o).getY())
                return true;
        return false;
    }

}
