package hjo.myviewlearning.com;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Suncere on 2016/12/15.
 */

public class View360 extends View {
      Paint mpaint;
      Path  mpath;

    int mlineY=800;  //线段中点Y坐标
    Point mpoint=new Point(500,mlineY);//500 为线段中点
    boolean isReturnLine=false;
    boolean  isDownOrUp=true;//是向下返回还是向上

    public View360(Context context) {
        this(context,null);
    }

    public View360(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public View360(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        mpaint=new Paint();
        mpaint.setColor(Color.RED);
        mpaint.setStrokeWidth(8);
        mpaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mpath=new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p1=new Paint();
        p1.setColor(Color.GRAY);
        p1.setStrokeWidth(20);
//        mpaint.setStyle(Paint.Style.FILL);
        canvas.drawPoint(200,800,p1);
        canvas.drawPoint(800,800,p1);
//        canvas.drawLine(200,800,800,800,mpaint);


        Paint pline=new Paint();
        pline.setColor(Color.RED);
        pline.setStrokeWidth(8);
        pline.setStyle(Paint.Style.STROKE);
        Path path=new Path();
        if(isReturnLine){
//            canvas.drawLine(200,800,800,800,mpaint);
            int Dy=mlineY-mpoint.y; //滑动回去Y的相对距离
            if(Dy>0)isDownOrUp=true;//大于0说明需要向下返回
            /************不能这么用！**********/

            path.moveTo(200,800);
            path.quadTo(500,mlineY+Dy,800,800);
            if(isDownOrUp){
                Dy-=5;
            }else{

            }

            isReturnLine=false;

        }else{//下拉  画赛贝尔曲线

            path.moveTo(200,800);
            path.quadTo(500,mpoint.y,800,800);
            canvas.drawPath(path,pline);
        }


        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int downY=0;
        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                downY= (int) event.getY();
//                break;
            case MotionEvent.ACTION_MOVE:
//                mpoint.y= Math.abs( ((int)event.getY()-downY))+mlineY;//只能往下设置
                mpoint.y= (int) event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isReturnLine =true;
                break;
        }

        return true;
    }
}
