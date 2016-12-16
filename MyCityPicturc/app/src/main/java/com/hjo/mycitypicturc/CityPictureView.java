package com.hjo.mycitypicturc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Suncere on 2016/12/16.
 */

public class CityPictureView extends View{
    Paint mpaint ;
    private int mViewWidth;//该控件的宽高
    private int mViewHeight;
    private RectF mRect;//画外圆区域
    private int msmallW=30;//每个外弧的间隙  也是小圆的直径
    private int mCount=4;//小圆的个数

    public CityPictureView(Context context) {
        this(context,null);
    }

    public CityPictureView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CityPictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mpaint=new Paint();
        mRect=new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthsize=MeasureSpec.getSize(widthMeasureSpec);
        int widthmodel=MeasureSpec.getMode(widthMeasureSpec);
        int heighsize=MeasureSpec.getSize(widthMeasureSpec);
        int heighmodel=MeasureSpec.getMode(widthMeasureSpec);

        if(widthmodel==MeasureSpec.EXACTLY){
            mViewWidth=widthsize;
        }else{
            mViewWidth=600;
        }
        if(heighmodel==MeasureSpec.EXACTLY){
            mViewHeight=heighsize;
        }else{
            mViewHeight=800;
        }

        setMeasuredDimension(mViewWidth,mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mpaint.setColor(Color.parseColor("#2F87C0"));
        mpaint.setStrokeWidth(15);
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setAntiAlias(true);

        int centre=getWidth()/2;//圆心坐标

        mRect.left=10;
        mRect.top=10;
        mRect.bottom=getWidth()-10;
        mRect.right=getWidth()-10;;

        int smallcentre=msmallW*5/2;//小圆半径   以下小圆的坐标是通过计算在45度的位置

        float itemSize=(360*1.0f-mCount*msmallW)/mCount;//每个外弧的宽度
        canvas.rotate(-30,centre,centre);//先将画布转动到-30度位置
        for(int i=0;i<mCount;i++){//旋转画外圆弧和小圆圈
            canvas.drawArc(mRect,i*(itemSize+msmallW),itemSize,false,mpaint);
            canvas.drawLine(centre+smallcentre+10,centre-smallcentre-10,centre*3/2+43-smallcentre/2,3*centre/4-60,mpaint);
        }
        canvas.rotate(30,centre,centre);//还原画布

        //画四个小圆
        int smallradius=centre/2-40;
//        int smallcentre=msmallW*4/2;//小圆半径   以下小圆的坐标是通过计算在45度的位置
        canvas.drawCircle(smallradius,smallradius,smallcentre,mpaint);//左上小圆
        canvas.drawCircle(smallradius+centre+85,smallradius,smallcentre,mpaint);//右上小圆
        canvas.drawCircle(smallradius+centre+85,smallradius+centre+85,smallcentre,mpaint);//右下小圆
        canvas.drawCircle(smallradius,smallradius+centre+85,smallcentre,mpaint);//左下小圆
        canvas.drawCircle(centre,centre,smallcentre+10,mpaint);//左下小圆



        super.onDraw(canvas);
    }
}
