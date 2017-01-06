/*
 * This source code is licensed under the MIT-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.hjo.scalableview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.hjo.scalabletime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 入口：initTimebarLengthAndPosition--》initTimebarTickCriterionMap--》resetToStandardWidth--》onMeasure --》measureWidth--》 onDraw
public class ScalableTimebarView extends View {
    /**
     * Indicates how many pixels a tick corresponds to for 1 second at the current scale criterion
     * 每秒所占的像素点数
     */
    private float pixelsPerSecond = 0;

    /**
     * Timebar Action Listeners
     */
    private OnBarMoveListener mOnBarMoveListener;
    private OnBarScaledListener mOnBarScaledListener;

    /**
     * Screen width and height in pixel
     * 屏幕宽度
     */
    private int screenWidth, screenHeight;

    /**
     * Paint to draw timebar body, ticks and RecordBar
     * 画笔
     */
    Paint timebarPaint = new Paint();

    /**
     * Textpaint to draw time text corresponding to large ticks
     *  绘制文字画笔
     */
    TextPaint keyTickTextPaint = new TextPaint();

    /**
     * Margin between large tick and time text corresponding to it in dp
     * 文本间距
     */
    private final int TICK_TEXT_TO_TICK_MARGIN_IN_DP = 2;

    /**
     * Whole height of the ScalableTimebarView (including timebar, recordbar, time text, margins, and so on) in dp
     * 整个View高度
     */
    private final int VIEW_HEIGHT_IN_DP = 56;

    /**
     * Height of record bar in dp
     */
    private final int COLORED_RECORDBAR_HEIGHT_IN_DP = 21;

    /**
     * Time text size corresponding to large ticks(key ticks) in sp
     * 文字大小
     */
    private final int KEY_TICK_TEXT_SIZE_IN_SP = 10;

    /**
     * Height of large tick in dp
     * 高刻度高度
     */
    private final int BIG_TICK_HEIGHT_IN_DP = 9;

    /**
     * Height of small tick in dp
     * 低刻度高度
     */
    private final int SMALL_TICK_HEIGHT_IN_DP = 6;

    /**
     * Half the width of large tick in dp
     * 高刻度宽度
     */
    private final int BIG_TICK_HALF_WIDTH_IN_DP = 2;

    /**
     * Half the width of small tick in dp
     * 低刻度宽度
     */
    private final int SMALL_TICK_HALF_WIDTH_IN_DP = 1;

    /**
     * Margin between recordbar and tick text in dp
     */
    private final int COLORED_RECORDBAR_TO_TICK_TEXT_MARGIN_IN_DP = 5;

    /**
     * The white cursor bitmap width to show
     * 指针图片宽度
     */
    private final int MIDDLE_CURSOR_BITMAP_SCALED_WIDTH_IN_DP = 13;

    /**
     * Convert various dimensions from dp/sp to px
     */
    private final int BIG_TICK_HALF_WIDTH = DeviceUtil.dip2px(BIG_TICK_HALF_WIDTH_IN_DP);
    private final int BIG_TICK_HEIGHT = DeviceUtil.dip2px(BIG_TICK_HEIGHT_IN_DP);
    private final int SMALL_TICK_HALF_WIDTH = DeviceUtil.dip2px(SMALL_TICK_HALF_WIDTH_IN_DP);
    private final int SMALL_TICK_HEIGHT = DeviceUtil.dip2px(SMALL_TICK_HEIGHT_IN_DP);
    private final int KEY_TICK_TEXT_SIZE = DeviceUtil.dip2px(KEY_TICK_TEXT_SIZE_IN_SP);
    private final int TICK_TEXT_TO_TICK_MARGIN = DeviceUtil.dip2px(TICK_TEXT_TO_TICK_MARGIN_IN_DP);
    private final int VIEW_HEIGHT = DeviceUtil.dip2px(VIEW_HEIGHT_IN_DP);
    private final int COLORED_RECORDBAR_TO_TICK_TEXT_MARGIN = DeviceUtil.dip2px(COLORED_RECORDBAR_TO_TICK_TEXT_MARGIN_IN_DP);
    private final int COLORED_RECORDBAR_HEIGHT = DeviceUtil.dip2px(COLORED_RECORDBAR_HEIGHT_IN_DP);

    /**
     * The bitmap containing a white cursor located in the middle of timebar to indicate current time
     * 显示当前时间
     */
    private Bitmap middle_cursor_bitmap_original_size = BitmapFactory.decodeResource(getResources(), R.drawable.ic_video_middle);
    private Bitmap middle_cursor_bitmap = Bitmap.createScaledBitmap(middle_cursor_bitmap_original_size, DeviceUtil.dip2px(MIDDLE_CURSOR_BITMAP_SCALED_WIDTH_IN_DP),
            VIEW_HEIGHT,
            false);

    /**
     * Set whether middle white cursor visible.
     * 是否显示指针
     */
    private boolean middleCursorVisible = true;

    /**
     * Map to cache 5 scale criterions
     * 保存五种时间显示方式
     */
    private Map<Integer, TimebarTickCriterion> timebarTickCriterionMap = new HashMap<>();

    /**
     * Timebar scale criterion count
     * 时间可短数量
     */
    private int timebarTickCriterionCount = 5;

    /**
     * Indicating which scale criterions the view is using to show ticks now , corresponding to keys of "timebarTickCriterionMap"
     * 五种时间的下标
     */
    private int currentTimebarTickCriterionIndex;

    /**
     * Storage of record data. Each time segment means one visible segment to be drawn colored on recordbar.<br>
     * This is the original recordbar data set by developer.
     * 设置的五种时间
     */
    private List<RecordDataExistTimeSegment> recordDataExistTimeClipsList = new ArrayList<>();

    /**
     * Data in recordDataExistTimeClipsList are divided into different groups and cached in this map.<br>
     * Same-day segments are grouped together in the same group.<br>
     * Keys of this map is milliseconds corresponding to 00:00 on the date.
     * 存在的时间片段
     */
    private Map<Long, List<RecordDataExistTimeSegment>> recordDataExistTimeClipsListMap = new HashMap<>();
    /**
     * Detector to handle scale gesture
     * 手势滑动类
     */
    private ScaleGestureDetector scaleGestureDetector;  //


    /**
     * Indicate whether width of this view has been initialized
     * 是否初始化
     */
    private boolean notInited = true;

    /**
     * Current time the white cursor indicating in millisecond
     * 当前指针指示的毫秒数
     */
    private long currentTimeInMillisecond;

    /**
     * Time corresponding to the most left tick (earlist time) in whole ScalableTimebarView (maybe outside the screen display range)
     * View最左边的刻度
     */
    private long mostLeftTimeInMillisecond;

    /**
     * Time corresponding to the most right tick (latest time) in whole ScalableTimebarView (maybe outside the screen display range)
     * View最右边的刻度
     */
    private long mostRightTimeInMillisecond;

    /**
     * Time corresponding to the most left tick (latest time) visible on screen now
     * 在屏幕可见的最左刻度
     */
    private long screenLeftTimeInMillisecond;

    /**
     * Time corresponding to the most right tick (latest time) visible on screen now
     * 在屏幕可见的最右刻度
     */
    private long screenRightTimeInMillisecond;

    /**
     * Flag indicating whether onMeasure() is called because of scale operation (scale operation can be performed by scale gesture or clicking on zoom buttons)
     * 是否是调用扩大或缩小的控件来绘制刻度
     */
    private boolean justScaledByPressingButton = false;

    /**
     * Seconds in one day
     * 一天的秒数
     */
    public final static int SECONDS_PER_DAY = 24 * 60 * 60;

    /**
     * The length of the entire timebar view, excluding the extra half-screen empty width at both ends.
     * 整个View显示的秒数
     */
    private long WHOLE_TIMEBAR_TOTAL_SECONDS;

    public ScalableTimebarView(Context context) {
        super(context);
        init(null);
    }

    public ScalableTimebarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public ScalableTimebarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public List<RecordDataExistTimeSegment> getRecordDataExistTimeClipsList() {
        return recordDataExistTimeClipsList;
    }

    public void setRecordDataExistTimeClipsList(List<RecordDataExistTimeSegment> recordDataExistTimeClipsList) {
        this.recordDataExistTimeClipsList = recordDataExistTimeClipsList;
        arrangeRecordDataExistTimeClipsIntoMap(recordDataExistTimeClipsList);
    }

    /**
     * Get most left (earlist) time in this whole view (including the invisible part outside the screen)
     * 获取最左显示的时间
     */
    public long getMostLeftTimeInMillisecond() {
        return mostLeftTimeInMillisecond;
    }

    /**
     * Get most right (latest) time in this whole view (including the invisible part outside the screen)
     * 获取显示最右的时间
     */
    public long getMostRightTimeInMillisecond() {
        return mostRightTimeInMillisecond;
    }

    /**
     * 获取屏幕可见范围内最左的时间
     * @return
     */
    public long getScreenLeftTimeInMillisecond() {
        screenLeftTimeInMillisecond = (long) (getCurrentTimeInMillisecond() - (long) ((float) screenWidth * 1000f / 2f / pixelsPerSecond));//屏幕可见最左时间  单位为毫秒

        return screenLeftTimeInMillisecond;
    }

    public long getScreenRightTimeInMillisecond() {
        screenRightTimeInMillisecond = (long) (getCurrentTimeInMillisecond() + (long) (screenWidth * 1000f / 2f / pixelsPerSecond));
        return screenRightTimeInMillisecond;
    }

    /**
     *
     * @param clipsList
     */
    private void arrangeRecordDataExistTimeClipsIntoMap(List<RecordDataExistTimeSegment> clipsList) {
        recordDataExistTimeClipsListMap = new HashMap<>();

        if (clipsList != null) {
            for (RecordDataExistTimeSegment clipItem : clipsList) {
                for (Long dateZeroOClockItem : clipItem.getCoverDateZeroOClockList()) {
                    List<RecordDataExistTimeSegment> list = null;
                    if ((list = recordDataExistTimeClipsListMap.get(dateZeroOClockItem)) == null) {
                        list = new ArrayList<>();
                        recordDataExistTimeClipsListMap.put(dateZeroOClockItem, list);
                    }
                    list.add(clipItem);
                }

            }
        }

        invalidate();
    }

    /**
     * Initializes start time, end time and current cursor time of ScalableTimebarView.
     * This method is called only when the view is initialized.
     * If you just need to move the current time cursor position, call setCurrentTimeInMillisecond() instead.
     * 初始化开始、结束和游标显示时间
     * 该方法只在初始化时调用
     * 如果你想移动游标指示的时间，请使用方法setCurrentTimeInMillisecond() 替代
     * <p>
     *
     * @param mostLeftTime  Time corresponding to the most left tick (earlist time) in whole ScalableTimebarView (maybe outside the screen display range)
     * @param mostRightTime Time corresponding to the most right tick (latest time) in whole ScalableTimebarView (maybe outside the screen display range)
     * @param currentTime   Current time the white cursor indicating in millisecond
     */
    public void initTimebarLengthAndPosition(long mostLeftTime, long mostRightTime, long currentTime) {
        Log.e("hjo","调用 initTimebarLengthAndPosition");
        this.mostLeftTimeInMillisecond = mostLeftTime;
        this.mostRightTimeInMillisecond = mostRightTime;
        this.currentTimeInMillisecond = currentTime;
        WHOLE_TIMEBAR_TOTAL_SECONDS = (mostRightTime - mostLeftTime) / 1000;
//        Log.e("hjo","initTimebarLengthAndPosition方法内：WHOLE_TIMEBAR_TOTAL_SECONDS="+WHOLE_TIMEBAR_TOTAL_SECONDS);
        initTimebarTickCriterionMap();
        resetToStandardWidth();
    }

    public int getCurrentTimebarTickCriterionIndex() {
        return currentTimebarTickCriterionIndex;
    }

    public void setCurrentTimebarTickCriterionIndex(int currentTimebarTickCriterionIndex) {
        Log.e("hjo","调用 setCurrentTimebarTickCriterionIndex");
        this.currentTimebarTickCriterionIndex = currentTimebarTickCriterionIndex;
    }

    /**
     * 初始化刻度尺的显示时间
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        screenWidth = DeviceUtil.getScreenResolution(getContext())[0];
        screenHeight = DeviceUtil.getScreenResolution(getContext())[1];

        // By default we use scale criterion[3] to show ticks.
        // By default, the current cursor is set to 3 hours before System.currentTimeMillis(),
        // and the mostRightTimeInMillisecond of the timebar is set System.currentTimeMillis()
        currentTimeInMillisecond = System.currentTimeMillis() - 3 * 3600 * 1000;//设置默认显示的时间是当前
        mostRightTimeInMillisecond = currentTimeInMillisecond + 3 * 3600 * 1000;// 最右时间为当前系统时间

        // By default the whole view width (including the parts outside the screen) is set to 1 days length
        mostLeftTimeInMillisecond = mostRightTimeInMillisecond - 1 * 24 * 3600 * 1000;//最左为1天前时间

//        Date date=new Date();//取时间
////        date.clearTime();
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(date);
//        calendar.set(Calendar.HOUR_OF_DAY,0);
//        calendar.set(Calendar.MINUTE,0);
//        calendar.set(Calendar.SECOND,0);
//        calendar.set(Calendar.MILLISECOND,0);
//        long time1=calendar.getTimeInMillis();

        mostLeftTimeInMillisecond = mostRightTimeInMillisecond - 1 * 24 * 3600 * 1000;//
        WHOLE_TIMEBAR_TOTAL_SECONDS = (mostRightTimeInMillisecond - mostLeftTimeInMillisecond) / 1000;// 整个控件显示秒数
//        Log.e("hjo","init 方法内：WHOLE_TIMEBAR_TOTAL_SECONDS="+WHOLE_TIMEBAR_TOTAL_SECONDS);
        pixelsPerSecond = (float) (getWidth() - screenWidth) / (float) WHOLE_TIMEBAR_TOTAL_SECONDS;// 每秒所占的宽度
//        Log.e("hjo","init  pixelsPerSecond=："+pixelsPerSecond);

        initTimebarTickCriterionMap();
        setCurrentTimebarTickCriterionIndex(3);

        keyTickTextPaint.setTextSize(KEY_TICK_TEXT_SIZE);
        keyTickTextPaint.setColor(getContext().getResources().getColor(R.color.colorWhite));


        ScaleGestureDetector.OnScaleGestureListener scaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleTimebarByFactor(detector.getScaleFactor(), false);
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                justScaledByPressingButton = true;
            }


        };
        scaleGestureDetector = new ScaleGestureDetector(getContext(), scaleGestureListener);
        //gestureDetector = new GestureDetector(getContext(), gestureDetectorListener);
    }

    /**
     * Scale the view by factor
     * 根据参数测量
     *
     * @param scaleFactor        scale factor
     * @param scaleByClickButton true:scale view by click zoom buttons  为true时通过点击按钮测量View
     *                           false：scale vie by scale gesture    为false时通过手势测量
     */
    public void scaleTimebarByFactor(float scaleFactor, boolean scaleByClickButton) {
        int newWidth = (int) ((getWidth() - screenWidth) * scaleFactor);

        if (newWidth > timebarTickCriterionMap.get(0).getViewLength()) {
            setCurrentTimebarTickCriterionIndex(0);
            newWidth = timebarTickCriterionMap.get(0).getViewLength();

        } else if (newWidth < timebarTickCriterionMap.get(0).getViewLength()
                && newWidth >= getAverageWidthForTwoCriterion(0, 1)) {
            setCurrentTimebarTickCriterionIndex(0);

        } else if (newWidth < getAverageWidthForTwoCriterion(0, 1)
                && newWidth >= getAverageWidthForTwoCriterion(1, 2)) {
            setCurrentTimebarTickCriterionIndex(1);

        }
        else if (newWidth < timebarTickCriterionMap.get(2).getViewLength()) {
            setCurrentTimebarTickCriterionIndex(2);
            newWidth = timebarTickCriterionMap.get(2).getViewLength();

        }

//        else if (newWidth < getAverageWidthForTwoCriterion(1, 2)
//                && newWidth >= getAverageWidthForTwoCriterion(2, 3)) {
//            setCurrentTimebarTickCriterionIndex(2);
//
//        }
//        else if (newWidth < getAverageWidthForTwoCriterion(2, 3)
//                && newWidth >= getAverageWidthForTwoCriterion(3, 4)) {
//            setCurrentTimebarTickCriterionIndex(3);
//
//        }

//        else if (newWidth < getAverageWidthForTwoCriterion(3, 4)
//                && newWidth >= timebarTickCriterionMap.get(4).getViewLength()) {
//            setCurrentTimebarTickCriterionIndex(4);
//
//        } else if (newWidth < timebarTickCriterionMap.get(4).getViewLength()) {
//            setCurrentTimebarTickCriterionIndex(4);
//            newWidth = timebarTickCriterionMap.get(4).getViewLength();
//
//        }

        if (scaleByClickButton) {
            justScaledByPressingButton = true;
        }

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = newWidth;
        setLayoutParams(params);

    }

    /**
     * 获取两个View视图宽度的均值
     * @param criterion1Index
     * @param criterion2Index
     * @return
     */
    private float getAverageWidthForTwoCriterion(int criterion1Index, int criterion2Index) {
        int width1 = timebarTickCriterionMap.get(criterion1Index).getViewLength();
        int width2 = timebarTickCriterionMap.get(criterion2Index).getViewLength();
        return (width1 + width2) / 2;
    }


    private void initTimebarTickCriterionMap() {
        Log.e("hjo","调用 initTimebarTickCriterionMap");
        TimebarTickCriterion t0 = new TimebarTickCriterion();
        t0.setTotalSecondsInOneScreen(10 * 60);//设置一个屏幕可见的总秒数
        t0.setKeyTickInSecond(1 * 60);//两个长刻度之间的间隔秒数
        t0.setMinTickInSecond(6);//两个小刻度之间的间隔秒数
        t0.setDataPattern("HH:mm");//刻度上显示的时间格式
        t0.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t0.getTotalSecondsInOneScreen()));//设置View的宽度
        timebarTickCriterionMap.put(0, t0);

        TimebarTickCriterion t1 = new TimebarTickCriterion();
        t1.setTotalSecondsInOneScreen(60 * 60);
        t1.setKeyTickInSecond(10 * 60);
        t1.setMinTickInSecond(60);
        t1.setDataPattern("HH:mm");
        t1.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t1.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(1, t1);

        TimebarTickCriterion t2 = new TimebarTickCriterion();
        t2.setTotalSecondsInOneScreen(6 * 60 * 60);
        t2.setKeyTickInSecond(60 * 60);
        t2.setMinTickInSecond(5 * 60);
        t2.setDataPattern("HH:mm");
        t2.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t2.getTotalSecondsInOneScreen()));
        timebarTickCriterionMap.put(2, t2);

//        TimebarTickCriterion t3 = new TimebarTickCriterion();
//        t3.setTotalSecondsInOneScreen(24 * 60 * 60);
//        t3.setKeyTickInSecond(6 * 60 * 60);
//        t3.setMinTickInSecond(30 * 60);
//        t3.setDataPattern("HH:mm");
//        t3.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t3.getTotalSecondsInOneScreen()));
//        timebarTickCriterionMap.put(3, t3);

//        TimebarTickCriterion t4 = new TimebarTickCriterion();
//        t4.setTotalSecondsInOneScreen(6 * 24 * 60 * 60);
//        t4.setKeyTickInSecond(24 * 60 * 60);
//        t4.setMinTickInSecond(2 * 60 * 60);
//        t4.setDataPattern("MM.dd");
//        // t4.dataPattern = "MM.dd HH:mm:ss";
//        t4.setViewLength((int) ((float) screenWidth * WHOLE_TIMEBAR_TOTAL_SECONDS / (float) t4.getTotalSecondsInOneScreen()));
//        timebarTickCriterionMap.put(4, t4);

        timebarTickCriterionCount = timebarTickCriterionMap.size();
    }


    /**
     * Reset view width to default value
     * 重新设置默认的View宽度
     */
    private void resetToStandardWidth() {
        Log.e("hjo","调用 resetToStandardWidth");
        setCurrentTimebarTickCriterionIndex(2);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getViewLength();//设置默认显示的视图
        setLayoutParams(params);
    }

    /**
     * Get current time indicating by white cursor bitmap
     *  获取中间指针指示的时间  毫秒
     */
    public long getCurrentTimeInMillisecond() {
        return currentTimeInMillisecond;
    }

    /**
     * Set current time cursor to specific time point.
     * If you are playing a video and use this ScalableTimebarView to indicate play progress,
     * then you should call this method every minute to update the showing current time.
     * 设置当前指针指示的时间
     * @param currentTimeInMillisecond current time you want to indicate by cursor
     */
    public void setCurrentTimeInMillisecond(long currentTimeInMillisecond) {
        this.currentTimeInMillisecond = currentTimeInMillisecond;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("hjo","调用 onMeasure");
        setMeasuredDimension(measureWidth(widthMeasureSpec), VIEW_HEIGHT);

        if (justScaledByPressingButton && mOnBarScaledListener != null) {
            justScaledByPressingButton = false;
            mOnBarScaledListener.onBarScaleFinish(getScreenLeftTimeInMillisecond(), getScreenRightTimeInMillisecond(), currentTimeInMillisecond);
        }
    }


    /**
     * Measure width of this view.
     * Left end and right end are both reserved for half the width of the screen
     *
     * @param widthMeasureSpec widthMeasureSpec
     * @return measured width
     */
    private int measureWidth(int widthMeasureSpec) {
        Log.e("hjo","调用 measureWidth");
        int measureMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureSize = MeasureSpec.getSize(widthMeasureSpec);
        int result = getSuggestedMinimumWidth();
        switch (measureMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = measureSize + screenWidth;
                pixelsPerSecond = measureSize / (float) WHOLE_TIMEBAR_TOTAL_SECONDS;
//                Log.e("hjo","measureWidth  pixelsPerSecond=："+pixelsPerSecond);
                if (mOnBarScaledListener != null) {
                    mOnBarScaledListener.onBarScaled(getScreenLeftTimeInMillisecond(), getScreenRightTimeInMillisecond(), currentTimeInMillisecond);
                }
                break;
            default:
                break;
        }
//        Log.e("hjo","测量的坐标：left="+getLeft()+"   top="+getTop()+"   right="+getRight() );
//        Log.e("hjo","建议的最小宽度：measureSize="+measureSize );
        return result;
    }

    /**
     * Convert time in millisecond to display string.
     * The display string style is controlled by current scale criterion.
     *
     * @param value time in millisecond need to convert
     * @return display string
     * 设置时间显示格式
     */
    private String getTimeStringFromLong(long value) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getDataPattern());
        return timeFormat.format(value);
    }

    /**
     * Set if you want middle cursor bitmap to be visible
     * @param middleCursorVisible true:visible
     *                            false:invisible
     *                            是否显示中间指针
     */
    public void setMiddleCursorVisible(boolean middleCursorVisible) {
        this.middleCursorVisible = middleCursorVisible;
        invalidate();
    }

    /**
     * 绘制逻辑
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("hjo","调用 onDraw");
        super.onDraw(canvas);

        if (notInited) {
            notInited = false;
            resetToStandardWidth();
            return;
        }

        pixelsPerSecond = (float)(getWidth() - screenWidth) / (float) WHOLE_TIMEBAR_TOTAL_SECONDS;//
//        Log.e("hjo","ondraw  getWidth()=："+getWidth());

        //Get the number of milliseconds between the phone's local time zone and UTC
        Calendar cal = Calendar.getInstance();
        int zoneOffsetInSeconds = cal.get(Calendar.ZONE_OFFSET) / 1000;//获取时区差;
//        Log.e("hjo","时区："+zoneOffsetInSeconds);
        //左边和右边各多显示一格
        long forStartUTC = (long) (currentTimeInMillisecond / 1000 - screenWidth / pixelsPerSecond / 2 - timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getMinTickInSecond());
        long forEndUTC = (long) (currentTimeInMillisecond / 1000 + screenWidth / pixelsPerSecond / 2 + timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getMinTickInSecond());




        //UTC世界标准时间  ：世界标准时间+时区差=本地时间
        long forStartLocalTimezone = forStartUTC + zoneOffsetInSeconds;
        long forEndLocalTimezone = forEndUTC + zoneOffsetInSeconds;

//        Log.e("hjo","开始绘制的时间：forStartLocalTimezone="+forStartLocalTimezone);
//        Log.e("hjo","介素绘制的时间：forEndLocalTimezone="+forEndLocalTimezone);

        //Look for the first tick to show on screen  寻找可看到的第一个时间刻度
        long firstTickToSeeInSecondUTC = -1;
        for (long i = forStartLocalTimezone; i <= forEndLocalTimezone; i++) {
            if (i % timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getMinTickInSecond() == 0) {
                firstTickToSeeInSecondUTC = i - zoneOffsetInSeconds; //这一行不理解  ？？？
                break;
            }
        }
//        Log.e("hjo","可见的第一个时间刻度：firstTickToSeeInSecondUTC=  "+firstTickToSeeInSecondUTC);

//        当获取的时间小于开始的时间时不画刻度
        /**
         * Draw timebar body , large ticks, small ticks, and time text corresponding to large ticks
         * 绘制刻度和时间text
         */
        int totalTickToDrawInOneScreen = (int) (screenWidth / pixelsPerSecond / timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getMinTickInSecond()) + 2;
        float keytextY = getHeight() - BIG_TICK_HEIGHT - TICK_TEXT_TO_TICK_MARGIN;
        for (int i = -20; i <= totalTickToDrawInOneScreen + 10; i++) {
            long drawTickTimeInSecondUTC = firstTickToSeeInSecondUTC + i * timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getMinTickInSecond();
            long drawTickTimeInSecondLocalTimezone = drawTickTimeInSecondUTC + zoneOffsetInSeconds;

            if (drawTickTimeInSecondLocalTimezone % timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getKeyTickInSecond() == 0) {//关键刻度
                //draw large ticks
                if (drawTickTimeInSecondLocalTimezone<mostRightTimeInMillisecond/1000) {
                    if (drawTickTimeInSecondLocalTimezone - mostLeftTimeInMillisecond / 1000 < 8 * 60 * 60 ) {//隐藏掉在00:00前绘制的刻度   因为多出一个屏幕的刻度 而每个屏幕显示8个钟的秒数
                        timebarPaint.setColor(getContext().getResources().getColor(R.color.Nocolor));
                        keyTickTextPaint.setColor(getContext().getResources().getColor(R.color.Nocolor));
                    }else{
                        timebarPaint.setColor(getContext().getResources().getColor(R.color.color_33ffffff));
                        keyTickTextPaint.setColor(getContext().getResources().getColor(R.color.colorWhite));
                    }
                }else{
                    if ( drawTickTimeInSecondLocalTimezone - mostRightTimeInMillisecond / 1000 > (8 * 60 * 60+1) ) {//隐藏掉在00:00前绘制的刻度   因为多出一个屏幕的刻度 而每个屏幕显示8个钟的秒数
                        timebarPaint.setColor(getContext().getResources().getColor(R.color.Nocolor));
                        keyTickTextPaint.setColor(getContext().getResources().getColor(R.color.Nocolor));
                    }else{
                        timebarPaint.setColor(getContext().getResources().getColor(R.color.color_33ffffff));
                        keyTickTextPaint.setColor(getContext().getResources().getColor(R.color.colorWhite));
                    }
                }


//                timebarPaint.setColor(getContext().getResources().getColor(R.color.color_33ffffff));
                timebarPaint.setStyle(Paint.Style.FILL);
                float startX = pixelsPerSecond * (drawTickTimeInSecondUTC - mostLeftTimeInMillisecond / 1000) + screenWidth / 2f;


                RectF largeTickRect = new RectF(startX - BIG_TICK_HALF_WIDTH / 2, getHeight() - BIG_TICK_HEIGHT, (startX + BIG_TICK_HALF_WIDTH / 2), getHeight());
                canvas.drawRect(largeTickRect, timebarPaint);

                //draw time text
                String keytext = getTimeStringFromLong(drawTickTimeInSecondUTC * 1000);
                if(keytext.equals("00:00")){
                    Log.e("hjoooooo","等于00:00时 drawTickTimeInSecondLocalTimezone="+drawTickTimeInSecondLocalTimezone);
                    Log.e("hjoooooo","等于00:00时 screenWidth / pixelsPerSecond="+screenWidth / pixelsPerSecond);
                    Log.e("hjoooooo"," mostLeftTimeInMillisecond/1000="+mostLeftTimeInMillisecond/1000);
                    Log.e("hjoooooo"," mostRightTimeInMillisecond/1000="+mostRightTimeInMillisecond/1000);
                    Log.e("hjoooooo"," mostLeftTimeInMillisecond/1000-drawTickTimeInSecondLocalTimezone="+(mostLeftTimeInMillisecond/1000-drawTickTimeInSecondLocalTimezone));
                    Log.e("hjoooooo"," mostRightTimeInMillisecond/1000-drawTickTimeInSecondLocalTimezone="+(mostRightTimeInMillisecond/1000-drawTickTimeInSecondLocalTimezone));
                }
                float keyTextWidth = keyTickTextPaint.measureText(keytext);
                float keytextX = startX - keyTextWidth / 2;


                canvas.drawText(keytext,
                        keytextX,
                        keytextY,
                        keyTickTextPaint);
            } else if (drawTickTimeInSecondLocalTimezone % timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getMinTickInSecond() == 0) {
                //draw small ticks
                if (drawTickTimeInSecondLocalTimezone<mostRightTimeInMillisecond/1000) {
                    if (drawTickTimeInSecondLocalTimezone - mostLeftTimeInMillisecond / 1000 < 8 * 60 * 60 ) {//隐藏掉在00:00前绘制的刻度   因为多出一个屏幕的刻度 而每个屏幕显示8个钟的秒数
                        timebarPaint.setColor(getContext().getResources().getColor(R.color.Nocolor));
                        keyTickTextPaint.setColor(getContext().getResources().getColor(R.color.Nocolor));
                    }else{
                        timebarPaint.setColor(getContext().getResources().getColor(R.color.color_33ffffff));
                        keyTickTextPaint.setColor(getContext().getResources().getColor(R.color.colorWhite));
                    }
                }else{
                    if ( drawTickTimeInSecondLocalTimezone - mostRightTimeInMillisecond / 1000 > (8 * 60 * 60+1) ) {//隐藏掉在00:00前绘制的刻度   因为多出一个屏幕的刻度 而每个屏幕显示8个钟的秒数
                        timebarPaint.setColor(getContext().getResources().getColor(R.color.Nocolor));
                        keyTickTextPaint.setColor(getContext().getResources().getColor(R.color.Nocolor));
                    }else{
                        timebarPaint.setColor(getContext().getResources().getColor(R.color.color_33ffffff));
                        keyTickTextPaint.setColor(getContext().getResources().getColor(R.color.colorWhite));
                    }
                }

                timebarPaint.setStyle(Paint.Style.FILL);
                float startX = pixelsPerSecond * (drawTickTimeInSecondUTC - mostLeftTimeInMillisecond / 1000) + screenWidth / 2f;

                RectF smallTickRect = new RectF(startX - SMALL_TICK_HALF_WIDTH / 2, getHeight() - SMALL_TICK_HEIGHT, (startX + SMALL_TICK_HALF_WIDTH / 2), getHeight());
                canvas.drawRect(smallTickRect, timebarPaint);
            }
        }

        /**
         * Draw recordbar
         */
        //Draw grey background
        //绘制中间背景
        long startDrawTimeInSeconds = firstTickToSeeInSecondUTC + (-20) * timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getMinTickInSecond();//灰色背景从哪个时间点开始，单位是毫秒
        float startX = pixelsPerSecond * (startDrawTimeInSeconds - mostLeftTimeInMillisecond / 1000) + screenWidth / 2f;
        RectF cloudRecordTimeClipsBarBackgroundRectF = new RectF(startX,
                getHeight() - BIG_TICK_HEIGHT - TICK_TEXT_TO_TICK_MARGIN - KEY_TICK_TEXT_SIZE - COLORED_RECORDBAR_TO_TICK_TEXT_MARGIN - COLORED_RECORDBAR_HEIGHT,
                (startX + screenWidth + timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getMinTickInSecond() * 40 * pixelsPerSecond),
                getHeight() - BIG_TICK_HEIGHT - TICK_TEXT_TO_TICK_MARGIN - KEY_TICK_TEXT_SIZE - COLORED_RECORDBAR_TO_TICK_TEXT_MARGIN);
        timebarPaint.setColor(getContext().getResources().getColor(R.color.colorGrayTransparent));
        timebarPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(cloudRecordTimeClipsBarBackgroundRectF, timebarPaint);


        //画有数据的时间段颜色
        if (recordDataExistTimeClipsList != null && recordDataExistTimeClipsList.size() > 0) {
            //Draw colored record segments
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat zeroTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startDrawTimeDateString = dateFormat.format(startDrawTimeInSeconds * 1000);
            String zeroTimeString = startDrawTimeDateString + " 00:00:00";

            long screenLastSecondToSee = (long) (startDrawTimeInSeconds + screenWidth / pixelsPerSecond + 30 * timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getMinTickInSecond()) * 1000L;

            Date startDate;
            try {

                //Find the date of most left (earlist) time on screen. Then get the list in cloudRecordExistTimeClipsListMap whose key is 00:00 of this date.
                //Get the first item in the list, using it as start condition to search which record segment should be the first to draw on recordbar.
                startDate = zeroTimeFormat.parse(zeroTimeString);
                List<RecordDataExistTimeSegment> startList = recordDataExistTimeClipsListMap.get(startDate.getTime());
                if (startList == null) {
                    int afterFindDays = 1;
                    long findTimeInMilliseconds = startDate.getTime();
                    long newFindStartMilliseconds = findTimeInMilliseconds;
                    while (startList == null && newFindStartMilliseconds < screenLastSecondToSee) {
                        newFindStartMilliseconds = findTimeInMilliseconds + (long) SECONDS_PER_DAY * 1000L * (long) afterFindDays;
                        startList = recordDataExistTimeClipsListMap.get(newFindStartMilliseconds);
                        afterFindDays++;
                    }
                }

                if (startList != null && startList.size() > 0) {
                    int thisDateFirstClipStartIndex = recordDataExistTimeClipsList.indexOf(startList.get(0));

                    //The first record segment should be drawn is found. Now let's start to draw colorful record segments!
                    long endDrawTimeInSeconds = (long) (startDrawTimeInSeconds
                            + screenWidth / pixelsPerSecond
                            + timebarTickCriterionMap.get(currentTimebarTickCriterionIndex).getMinTickInSecond() * 30);

                    timebarPaint.setColor(getContext().getResources().getColor(R.color.colorRecordGreen));
                    timebarPaint.setStyle(Paint.Style.FILL);

                    for (int i = thisDateFirstClipStartIndex; i < recordDataExistTimeClipsList.size(); i++) {
                        float leftX = pixelsPerSecond * (recordDataExistTimeClipsList.get(i).getStartTimeInMillisecond() - mostLeftTimeInMillisecond) / 1000 + screenWidth / 2f;
                        float rightX = pixelsPerSecond * (recordDataExistTimeClipsList.get(i).getEndTimeInMillisecond() - mostLeftTimeInMillisecond) / 1000 + screenWidth / 2f;
                        RectF rectF = new RectF(leftX,
                                getHeight() - BIG_TICK_HEIGHT - TICK_TEXT_TO_TICK_MARGIN - KEY_TICK_TEXT_SIZE - COLORED_RECORDBAR_TO_TICK_TEXT_MARGIN - COLORED_RECORDBAR_HEIGHT,
                                rightX,
                                getHeight() - BIG_TICK_HEIGHT - TICK_TEXT_TO_TICK_MARGIN - KEY_TICK_TEXT_SIZE - COLORED_RECORDBAR_TO_TICK_TEXT_MARGIN);
                        canvas.drawRect(rectF, timebarPaint);
                        if (recordDataExistTimeClipsList.get(i).getEndTimeInMillisecond() >= endDrawTimeInSeconds * 1000) {
                            break;
                        }
                    }
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        /**
         * Draw white cursor bitmap indicating current time in the middle of screen
         */
        if (middleCursorVisible) {
            timebarPaint.setColor(getContext().getResources().getColor(R.color.colorRecordGreen));
            timebarPaint.setStyle(Paint.Style.FILL);
            canvas.drawBitmap(middle_cursor_bitmap,
                    (currentTimeInMillisecond / 1000L - mostLeftTimeInMillisecond / 1000L) * pixelsPerSecond + screenWidth / 2f - middle_cursor_bitmap.getWidth() / 2,
                    0,
                    timebarPaint);
        }

        /**
         * According to the currentTimeInMillisecond variable, layout the view in appropriate location
         */
        layout((int) (0 - (currentTimeInMillisecond - mostLeftTimeInMillisecond) / 1000 * pixelsPerSecond),
                getTop(),
                getWidth() - (int) ((currentTimeInMillisecond - mostLeftTimeInMillisecond) / 1000 * pixelsPerSecond),
                getTop() + getHeight());

    }

    /**
     * Coordinates when draging timebar
     */
    float lastX, lastY;

    /**
     * Current operation on this view
     */
    private int mode = NONE;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        if (scaleGestureDetector.isInProgress()) {
            return true;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ZOOM) {

                } else if (mode == DRAG) {
                    int dx = (int) (event.getRawX() - lastX);
                    int dy = (int) (event.getRawY() - lastY);

                    if (dx == 0) {
                        return true;
                    }

                    int top = getTop();
                    int left = getLeft() + dx;
                    int right = left + getWidth();

                    if (left >= 0) {
                        left = 0;
                        right = getWidth();
                    }

                    if (right < screenWidth) {
                        right = screenWidth;
                        left = right - getWidth();
                    }
//                    Log.e("hjo","滑动后view的坐标：left="+left+"   top="+top+"   right="+right+"  bottom="+(top + getHeight()) );
                    layout(left, top, right, top + getHeight()); //移动iew
                    invalidate();

                    lastX = event.getRawX();
                    lastY = event.getRawY();

                    //pixels to move   移动当前指针显示的秒数
                    int deltaX = (0 - left);
                    int timeBarLength = getWidth() - screenWidth;
                    currentTimeInMillisecond = mostLeftTimeInMillisecond + deltaX * WHOLE_TIMEBAR_TOTAL_SECONDS * 1000 / timeBarLength;
                    if (mOnBarMoveListener != null) {
                        mOnBarMoveListener.onBarMove( getScreenLeftTimeInMillisecond(), getScreenRightTimeInMillisecond(), currentTimeInMillisecond );
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                if (mode == DRAG) {
                    int deltaX_up = (0 - getLeft());
                    int timeBarLength_up = getWidth() - screenWidth;
                    currentTimeInMillisecond = mostLeftTimeInMillisecond + deltaX_up * WHOLE_TIMEBAR_TOTAL_SECONDS * 1000 / timeBarLength_up;

                    if (mOnBarMoveListener != null) {
                        mOnBarMoveListener.OnBarMoveFinish(getScreenLeftTimeInMillisecond(), getScreenRightTimeInMillisecond(), currentTimeInMillisecond);
                    }
                }
                mode = NONE;
                break;
        }
        return true;
    }


    /**
     * If user click on zoomIn/zoomOut button, this method should be called to handle scale.
     * Different from scale gesture, clicking on zoomIn/zoomOut button will scale the ScalableTimebarView to one of 5 standard scale criterions.
     *
     * @param zoomIn true：zoomIn
     *               false：zoomOut
     */
    public void scaleByPressingButton(boolean zoomIn) {
        /**
         * Standard view length of current scale criterion (maybe not equals to current view real length)
         */
        int currentCriterionViewLength = timebarTickCriterionMap.get(getCurrentTimebarTickCriterionIndex()).getViewLength();//当前所在刻度标准的默认长度（不含两端空出的screenWidth）

        /**
         * Current view real length (Excluding one screen width empty parts on both end)
         */
        int currentViewLength = getWidth() - screenWidth;

        //Currently the view length is equal to one of 5 scale criterions standard length. So jump to next criterion standard length.
        if (currentViewLength == currentCriterionViewLength) {
            if (zoomIn) {//zoom in
                int newCriteriaIndex = getCurrentTimebarTickCriterionIndex() - 1;
                if (newCriteriaIndex < 0) {
                    return;
                } else {
                    setCurrentTimebarTickCriterionIndex(newCriteriaIndex);
                    int newWidth = timebarTickCriterionMap.get(newCriteriaIndex).getViewLength();
                    justScaledByPressingButton = true;

                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.width = newWidth;
                    setLayoutParams(params);
                }
            } else {//zoom out

                int newCriteriaIndex = getCurrentTimebarTickCriterionIndex() + 1;
                if (newCriteriaIndex >= timebarTickCriterionCount) {
                    return;
                } else {
                    setCurrentTimebarTickCriterionIndex(newCriteriaIndex);
                    int newWidth = timebarTickCriterionMap.get(newCriteriaIndex).getViewLength();
                    justScaledByPressingButton = true;

                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.width = newWidth;
                    setLayoutParams(params);
                }
            }
        } else {//Currently the view length is NOT equal to one of 5 scale criterions standard length. So maybe don't jump to new scale criterion, just recover to standard length of current scale criterion
            if (currentViewLength > currentCriterionViewLength) {//currentViewLength > currentCriterionViewLength

                if (zoomIn) {//zoom in, jump to next scale criterion
                    int newCriteriaIndex = getCurrentTimebarTickCriterionIndex() - 1;
                    if (newCriteriaIndex < 0) {
                        return;
                    } else {
                        setCurrentTimebarTickCriterionIndex(newCriteriaIndex);
                        int newWidth = timebarTickCriterionMap.get(newCriteriaIndex).getViewLength();
                        justScaledByPressingButton = true;

                        ViewGroup.LayoutParams params = getLayoutParams();
                        params.width = newWidth;
                        setLayoutParams(params);
                    }
                } else {//zoom out, just recover to standard length of current scale criterion
                    int newWidth = timebarTickCriterionMap.get(getCurrentTimebarTickCriterionIndex()).getViewLength();
                    justScaledByPressingButton = true;

                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.width = newWidth;
                    setLayoutParams(params);
                }

            } else {//currentViewLength < currentCriterionViewLength

                if (zoomIn) {//zoom in, just recover to standard length of current scale criterion
                    int newWidth = timebarTickCriterionMap.get(getCurrentTimebarTickCriterionIndex()).getViewLength();
                    justScaledByPressingButton = true;

                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.width = newWidth;
                    setLayoutParams(params);
                } else {//zoom out, just to next scale criterion
                    int newCriteriaIndex = getCurrentTimebarTickCriterionIndex() + 1;
                    if (newCriteriaIndex >= timebarTickCriterionCount) {
                        return;
                    } else {
                        setCurrentTimebarTickCriterionIndex(newCriteriaIndex);
                        int newWidth = timebarTickCriterionMap.get(newCriteriaIndex).getViewLength();
                        justScaledByPressingButton = true;

                        ViewGroup.LayoutParams params = getLayoutParams();
                        params.width = newWidth;
                        setLayoutParams(params);
                    }
                }
            }
        }
    }

    /**
     * Listener while moving and finish move
     */
    public interface OnBarMoveListener {
        /**
         * Callback when the timebar is being moved horizontal(slide)
         *
         * @param screenLeftTime  most left time on screen (only visible part of this view)
         * @param screenRightTime most right time on screen (only visible part of this view)
         * @param currentTime     current time
         */
        void onBarMove(long screenLeftTime, long screenRightTime, long currentTime);

        /**
         * Callback when the timebar finish moving
         *
         * @param screenLeftTime  most left time on screen (only visible part of this view)
         * @param screenRightTime most right time on screen (only visible part of this view)
         * @param currentTime     current time
         */
        void OnBarMoveFinish(long screenLeftTime, long screenRightTime, long currentTime);
    }

    public void setOnBarMoveListener(OnBarMoveListener onBarMoveListener) {
        mOnBarMoveListener = onBarMoveListener;
    }

    /**
     * Listener while scale and finish scale
     */
    public interface OnBarScaledListener {
        /**
         * Called when timebar is being scaled
         *
         * @param screenLeftTime  most left time on screen (only visible part of this view)
         * @param screenRightTime most right time on screen (only visible part of this view)
         * @param currentTime     current time
         */
        void onBarScaled(long screenLeftTime, long screenRightTime, long currentTime);

        /**
         * Called when finish scaling
         *
         * @param screenLeftTime  most left time on screen (only visible part of this view)
         * @param screenRightTime most right time on screen (only visible part of this view)
         * @param currentTime     current time
         */
        void onBarScaleFinish(long screenLeftTime, long screenRightTime, long currentTime);
    }

    public void setOnBarScaledListener(OnBarScaledListener onBarScaledListener) {
        mOnBarScaledListener = onBarScaledListener;
    }
}

