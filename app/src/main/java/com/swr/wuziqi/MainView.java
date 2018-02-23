package com.swr.wuziqi;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class MainView extends View {

    private int contentWidth;
    private int contentHeight;
    private int originX;
    private int originY;
    private int cellWidth;
    public QiPan qiPan;
    public TextView log ;
    public Handler mHandler;
    public MainView(Context context) {
        super(context);
        init();
    }

    public MainView(Context context,AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    public MainView(Context context,AttributeSet attrs,int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        init();
    }

    private void init() {
        qiPan = new QiPan();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               if(!qiPan.getIsEnd()) {
                   switch (motionEvent.getAction()) {
                       case MotionEvent.ACTION_DOWN:
                           int x = Math.round((motionEvent.getX() - originX) / cellWidth);
                           int y = Math.round((motionEvent.getY() - originY) / cellWidth);
                           if (x < -7 || y < -7 || x > 7 || y > 7) break;
                           if (Math.abs(originX + x * cellWidth - motionEvent.getX()) < cellWidth / 2
                                   && Math.abs(originY + y * cellWidth - motionEvent.getY()) < cellWidth / 2) {
                               x += 7;
                               y += 7;
                               if(qiPan.setQizi(x, y)) {
                                   switch (qiPan.getAction()) {
                                       case QiPan.QiZi_BLACK:
                                           addLog("白子   下落   (" + x + "," + y + ")");
                                           break;
                                       default:
                                           addLog("黑子   下落   (" + x + "," + y + ")");
                                   }
                                   if(qiPan.getIsEnd()){
                                       switch (qiPan.getAction()) {
                                           case QiPan.QiZi_BLACK:
                                               addLog("白子   胜利");
                                               break;
                                           default:
                                               addLog("黑子   胜利");
                                       }
                                   }
                               }
                               view.invalidate();
                           }
                           break;
                   }
               }return false;
            }
        });
    }
    @Override
    protected void onSizeChanged(int width, int height, int oldW, int oldH) {// **
        super.onSizeChanged(width, height, oldW, oldH);
        getLayout(width, height);
    }
    private void getLayout(int width,int height){
        contentWidth = width;
        contentHeight = height;
        originX = width/2;
        cellWidth = width > height ? height / 16 : width / 16;
        originY = height - 9 * cellWidth;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawChessBoard(canvas);
        drawChess(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(contentWidth/5);
        paint.setTextAlign(Paint.Align.CENTER);
        if(qiPan.getIsEnd())canvas.drawText("Game Over",originX,originY,paint);
    }

    private void drawChessBoard(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);
        for(int i=-7;i<=7;i++) {
            canvas.drawLine(originX - 7 * cellWidth,originY + i * cellWidth,originX + 7 * cellWidth,originY + i * cellWidth,paint);
            canvas.drawLine(originX + i * cellWidth,originY - 7 * cellWidth,originX + i * cellWidth,originY + 7 * cellWidth,paint);
        }
        paint.setStrokeWidth(8);
        canvas.drawLine(originX - (float)(7.5 * cellWidth) - 4,originY + (float)(7.5 * cellWidth),
                originX + (float)(7.5 * cellWidth) + 4,originY + (float)(7.5 * cellWidth),paint);
        canvas.drawLine(originX - (float)(7.5 * cellWidth) - 4,originY - (float)(7.5 * cellWidth),
                originX + (float)(7.5 * cellWidth) + 4,originY - (float)(7.5 * cellWidth),paint);
        canvas.drawLine(originX - (float)(7.5 * cellWidth),originY - (float)(7.5 * cellWidth) - 4,
                originX - (float)(7.5 * cellWidth),originY + (float)(7.5 * cellWidth) + 4,paint);
        canvas.drawLine(originX + (float)(7.5 * cellWidth),originY - (float)(7.5 * cellWidth) - 4,
                originX + (float)(7.5 * cellWidth),originY + (float)(7.5 * cellWidth) + 4,paint);

    }
    private void addLog(String str){
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("msg", str + '\n');
        msg.setData(b);
        mHandler.sendMessage(msg);
    }
    private void drawChess(Canvas canvas) {
        Bitmap black = BitmapFactory.decodeResource(this.getResources(), R.drawable.black);
        Bitmap white = BitmapFactory.decodeResource(this.getResources(), R.drawable.white);

        for (QiZi qizi: qiPan.getBlack()){
            int x = qizi.getX() - 7;
            int y = qizi.getY() - 7;
            Rect rect = new Rect(originX + x * cellWidth - cellWidth / 2,originY + y * cellWidth - cellWidth / 2,
                    originX + x * cellWidth + cellWidth / 2,originY + y * cellWidth + cellWidth / 2);
            canvas.drawBitmap(black,null,rect,null);
        }

        for (QiZi qizi: qiPan.getWhite()){
            int x = qizi.getX() - 7;
            int y = qizi.getY() - 7;
            Rect rect = new Rect(originX + x * cellWidth - cellWidth / 2,originY + y * cellWidth - cellWidth / 2,
                    originX + x * cellWidth + cellWidth / 2,originY + y * cellWidth + cellWidth / 2);
            canvas.drawBitmap(white,null,rect,null);
        }
    }

    public void reset(){
        qiPan.reset();
        addLog("开始游戏!");
    }

    public void undo(){
        if(qiPan.undo()){
            switch (qiPan.getAction()) {
                case QiPan.QiZi_BLACK:
                    addLog("黑子   悔棋");
                    break;
                default:
                    addLog("白子   悔棋");
            }
        }
    }
}
