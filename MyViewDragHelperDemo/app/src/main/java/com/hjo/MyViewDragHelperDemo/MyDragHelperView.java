package com.hjo.MyViewDragHelperDemo;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Suncere on 2016/12/18.
 */
public class MyDragHelperView extends RelativeLayout {
    private ViewDragHelper mViewDragHelper;
    private View mdragView;
    private Point  ImageViewPoint;//保存ImageView的初始位置

    private int mViewWidth;//view的宽高
    private int mViewHeight;

    public MyDragHelperView(Context context) {
        this(context,null);
    }

    public MyDragHelperView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyDragHelperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelperCallback() ) ;
//        Log.e("hjo"," 该布局的宽高："+this.getWidth()+"  "+this.getHeight());
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthsize=MeasureSpec.getSize(widthMeasureSpec);
//        int widthmode=MeasureSpec.getMode(widthMeasureSpec);
//        int heightsize=MeasureSpec.getSize(heightMeasureSpec);
//        int heightmode=MeasureSpec.getMode(heightMeasureSpec);
//
//        if (widthmode==MeasureSpec.EXACTLY){
//            mViewWidth=widthsize;
//        }else{
//            mViewWidth=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 400, getResources().getDisplayMetrics());
//        }
//        if (heightmode==MeasureSpec.EXACTLY){
//            mViewHeight=heightsize;
//        }else{
//            mViewHeight=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 400, getResources().getDisplayMetrics());
//        }
//        setMeasuredDimension(mViewWidth,mViewHeight);
//    }

    private class ViewDragHelperCallback  extends  ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {

            return child == mdragView;
        }
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            return super.clampViewPositionHorizontal(child, left, dx);
            Log.d("DragLayout", "clampViewPositionHorizontal " + left + "," + dx);
            final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - mdragView.getWidth();
            final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
            return newLeft;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
//            return super.clampViewPositionVertical(child, top, dy);
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - mdragView.getHeight();
            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;
        }

        //手指释放后回调
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (releasedChild == mdragView) {
                mViewDragHelper.settleCapturedViewAt(ImageViewPoint.x, ImageViewPoint.y);
                invalidate();
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if(changedView==mdragView){
                Log.e("hjo","当前view的位置：  left="+left +"  top="+top+"  dx="+dx+"   dy="+dy);
            }
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            Log.e("hjo"," capturedChild 被捕获");
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mdragView=this.getChildAt(0);//get ImageView
        ImageViewPoint=new Point();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ImageViewPoint.x=mdragView.getLeft();
        ImageViewPoint.y=mdragView.getTop();
        Log.e("hjo"," 该布局的宽高："+this.getLeft()+"  "+this.getTop());
        Log.e("hjo"," 该布局的宽高："+this.getRight()+"  "+this.getBottom());
    }
    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)){//与手指释放后结合使用
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return  mViewDragHelper.shouldInterceptTouchEvent(ev);
    }


}
