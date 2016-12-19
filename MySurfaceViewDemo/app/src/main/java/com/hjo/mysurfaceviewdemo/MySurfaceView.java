package com.hjo.mysurfaceviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by HJO on 2016/12/18.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;//绘图用的canvas
    private boolean misDrawing;//在子线程中是否绘图的标志位
    private Paint mPaint;
    private Path mPath;

    int x,y;

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        misDrawing=true;
        new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        misDrawing=false;

    }

    @Override
    public void run() {
        mPath.reset();
        mPath.moveTo(getLeft(),getBottom());
        while(misDrawing){
            draw();
            //画一条正弦曲线
            x+=5;
            y= (int) (100*Math.sin(x*2*Math.PI/180)+400);
            mPath.lineTo(x,y);
        }
    }

    private void  init(){
        mHolder=getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        mPaint=new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath=new Path();
    }

    private void draw(){
        try {
            mCanvas=mHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath,mPaint);
        }catch (Exception e){

        }finally {
            if(mCanvas!=null){
                mHolder.unlockCanvasAndPost(mCanvas);//将画布内容提交

            }
        }

    }
}
