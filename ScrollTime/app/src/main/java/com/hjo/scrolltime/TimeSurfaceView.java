package com.hjo.scrolltime;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Suncere on 2016/12/20.
 */

public class TimeSurfaceView extends View{
    private int mViewWidth;//控件的宽高
    private int mViewHeight;

    private Paint mpaint;


    int mbgColor;  //背景色
    int mtimePointerColor;  //指针颜色
    int mscaleColor;// 刻度颜色
    int hadDataColor; //有数据的时间点颜色
    int mtextTimeColor; //刻度时间字体颜色




    public TimeSurfaceView(Context context) {
        this(context,null);
    }
    public TimeSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public TimeSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.TimeSurfaceView,defStyleAttr,0);
        mbgColor=typedArray.getColor(R.styleable.TimeSurfaceView_bgColor,getResources().getColor(R.color.bgColor));
        mtimePointerColor=typedArray.getColor(R.styleable.TimeSurfaceView_hadDataColor, Color.BLUE);
        mscaleColor=typedArray.getColor(R.styleable.TimeSurfaceView_scaleColor,getResources().getColor(R.color.bgColor));
        hadDataColor=typedArray.getColor(R.styleable.TimeSurfaceView_hadDataColor,getResources().getColor(R.color.hadDataColor));
        mtextTimeColor=typedArray.getColor(R.styleable.TimeSurfaceView_timeTextColor,getResources().getColor(R.color.bgColor));
        typedArray.recycle();

        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode==MeasureSpec.EXACTLY){
            mViewWidth=widthSize;
        }else{
            mViewWidth=MeasureSpec.AT_MOST;
        }
        if (heightMode==MeasureSpec.EXACTLY){
            mViewHeight=heightSize;
        }else{
            mViewHeight=(int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,90,getResources().getDisplayMetrics()));
        }
        setMeasuredDimension(mViewWidth,mViewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    private void  init(){

        mpaint=new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setStyle(Paint.Style.STROKE);

    }
}
