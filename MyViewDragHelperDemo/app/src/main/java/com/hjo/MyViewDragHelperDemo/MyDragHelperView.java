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
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Suncere on 2016/12/18.
 */
public class MyDragHelperView extends RelativeLayout {
    private ViewDragHelper mViewDragHelper;
    private View mdragView;
    private Point  ImageViewPoint;//保存ImageView的初始位置

    private boolean isStartMove;

    private int mViewWidth;//view的宽高
    private int mViewHeight;
    private Context mContext;
    private HashMap<String ,Boolean> mDirection;
    private String mtempDirection="Nothing";//保存上一次正在转动的方向名

    private static final  String  TopLeft="TopLeft";
    private static final  String  Top="Top";
    private static final  String  TopRight="TopRight";

    private static final  String  Left="Left";
    private static final  String  Right="Right";

    private static final  String  BottomLeft="BottomLeft";
    private static final  String  BottomRight="BottomRight";
    private static final  String  Bottom="Bottom";

    private static final  String  Nothing="Nothing";

    //嗯 是时候写接口了
    private DirectionClickListenter mDirectionClickListenter;
    int mindex=0;

    public MyDragHelperView(Context context) {
        this(context,null);

    }

    public MyDragHelperView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyDragHelperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelperCallback() ) ;
        //保存八个方向   正在转动的方向设置为true  最后一个为没有正在转动的方向
        mDirection=new HashMap<String ,Boolean >();
        mDirection.put("TopLeft",false);
        mDirection.put("Top",false);
        mDirection.put("TopRight",false);

        mDirection.put("Left",false);
        mDirection.put("Right",false);

        mDirection.put("BottomLeft",false);
        mDirection.put("BottomRight",false);
        mDirection.put("Bottom",false);

        mDirection.put("Nothing",true);

//        Log.e("hjo"," 该布局的宽高："+this.getWidth()+"  "+this.getHeight());
    }

    public  void setDirectionClickListenter(DirectionClickListenter listenter){
        this.mDirectionClickListenter =listenter;
    }

    private class ViewDragHelperCallback  extends  ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {

            return child == mdragView;
        }
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            return super.clampViewPositionHorizontal(child, left, dx);
//            Log.d("DragLayout", "clampViewPositionHorizontal " + left + "," + dx);
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
                if (!mDirection.get(Nothing)){//没有正在转动的方向
                    mindex=0;
                    setOnlyDirection(Nothing);
                }
                invalidate();
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            int  wdx=left - ImageViewPoint.x;//图片相对原始位置的位移
            int wdy=top-ImageViewPoint.y;

            //    Log.e("hjo"," ImageViewPoint.x =" +ImageViewPoint.x  +"  ImageViewPoint.y= "+ImageViewPoint.y );
//                Log.e("hjo","相对位移  wdx =" +wdx+"  wdy= "+wdy );
//                Log.e("hjo","当前view的位置：  left="+left +"  top="+top+"  dx="+dx+"   dy="+dy);

//            1-云台转上 、2-云台转下 、3-云台转左 、4-云台转右、 11-云台左上 、12-云台右上 13-云台左下 、14-云台右下、7-镜头拉近、8-镜头拉远、9-镜头近焦、10-镜头远焦
            if(isStartMove) {//手势按下
                if (wdy < -100) {  //向  左上  上  右上 某个方向移动
                    if ((wdx > -190 && wdx < 190)) {
                        if (Math.abs(dx) > 60 || Math.abs(dy) > 60) {//如果dx dy中有一个移动很快 说明只是用户一直在滑动  不需要处理
                            Log.d("hjo", "  滑动太快不处理！ ");
                        } else {//   1-云台转上
                            if (!mDirection.get(Top)) {// 为false 还没有执行转动
                                mindex=1;
                                setOnlyDirection(Top);
                                showToast("向上转动");
                                Log.d("hjo", " 向上转动mindex="+mindex);
                            }
                        }
                    } else {
                        if (wdx < 0) {// 11-云台左上
                            if (!mDirection.get(TopLeft)) {
                                mindex=11;
                                setOnlyDirection(TopLeft);
                                showToast("左上转动");
                                Log.e("hjo", " 左上转动 ");
                            }
                        } else { //12-云台右上
                            if (!mDirection.get(TopRight)) {
                                showToast("右上转动");
                                mindex=12;
                                setOnlyDirection(TopRight);
                                Log.d("hjo", " 右上转动  ");
                            }
                        }
                    }
                }
                //向下移动  左下  下  右下
                else if (wdy > 100) {
                    if ((wdx > -190 && wdx < 190)) { // 2-云台转下
                        if (Math.abs(dx) > 60 || Math.abs(dy) > 60) {//如果dx dy中有一个移动很快 说明只是用户一直在滑动  不需要处理
                            Log.e("hjo", "  滑动太快不处理！ ");
                        } else {//向上移动
                            if (!mDirection.get(Bottom)) {// 为false 还没有执行转动
                                mindex=2;
                                setOnlyDirection(Bottom);
                                showToast("向下转动");
                                Log.d("hjo", " 向下转动");
                            }
                        }

                    } else {
                        if (wdx < 0) {//  13-云台左下
                            if (!mDirection.get(BottomLeft)) {
                                mindex=13;
                                setOnlyDirection(BottomLeft);
                                showToast("左下转动");
                                Log.d("hjo", " 左下转动 ");
                            }
                        } else { //14-云台右下
                            if (!mDirection.get(BottomRight)) {
                                showToast("右下转动");
                                mindex=14;
                                setOnlyDirection(BottomRight);
                                Log.d("hjo", " 右下转动  ");
                            }
                        }
                    }
                }

                // 左右移动
                else {
                    if (wdx<-80){//  3-云台转左
                        if (!mDirection.get(Left)) {
                            showToast("左转动");
                            mindex=3;
                            setOnlyDirection(Left);
                            Log.d("hjo", " 左转动  ");
                        }
                    }else if(wdx>80){   //  4-云台转右
                        if (!mDirection.get(Right)) {
                            showToast("右转动");
                            mindex=4;
                            setOnlyDirection(Right);
                            Log.d("hjo", " 右转动  ");
                        }
                    }

                }

            }

        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
//            Log.e("hjo"," capturedChild 被捕获");
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
//        Log.e("hjo"," ImageView的坐标："+mdragView.getLeft()+"  "+mdragView.getTop() );
//        Log.e("hjo"," 该布局的宽高："+this.getLeft()+"  "+this.getTop());
//        Log.e("hjo"," 该布局的宽高："+this.getRight()+"  "+this.getBottom());
    }
    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)){//与手指释放后结合使用
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //手势按下时开始做位置移动判断，抬起时中心图片回到起始点，这时产生的位置移动不做位移判断
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isStartMove=true;
                break;
            case MotionEvent.ACTION_UP:
                isStartMove=false;
                break;
        }

        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return  mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    private void  showToast(String  content){
        Toast.makeText(mContext,content,Toast.LENGTH_SHORT).show();
    }

    /***
     * 将正在执行转动的方向名 传入
     * @param direction
     */
    private  void setOnlyDirection(String direction){
        mDirectionClickListenter.directionIndex(mindex);
        if (mDirection.containsKey(direction)){
            mDirection.remove(direction);
            mDirection.put(direction,true);

            //将上一次转动的方向设置为false
            mDirection.remove(mtempDirection);
            mDirection.put(mtempDirection,false);
            mtempDirection=direction;

        }
    }

    public interface DirectionClickListenter   {
        void directionIndex(int directionId );
    }

}
