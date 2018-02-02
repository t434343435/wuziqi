package com.swr.wuziqi;

/**
 * Created by t4343 on 2018/2/2.
 */

public class QiZi {
    private int x;
    private int y;
    private boolean isOffensive;
    public QiZi(int x, int y, boolean isOffensive)
    {
        this.x = x;
        this.y = y;
        this.isOffensive = isOffensive;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getIsOffensive() {
        return isOffensive;
    }

}
