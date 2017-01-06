/*
 * This source code is licensed under the MIT-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.hjo.scalableview;

/**
 * <pre>
 * Created by ljfxyj2008 on 16/10/28.
 * Email: ljfxyj2008@gmail.com
 * GitHub: <a href="https://github.com/ljfxyj2008">https://github.com/ljfxyj2008</a>
 * HomePage: <a href="http://www.carrotsight.com">http://www.carrotsight.com</a>
 * </pre>
 *
 * When user perform scale operation on ScalableTimebarView, ticks in this view shown on screen will change apperance according to different TimebarTickCriterions.
 */



public class TimebarTickCriterion {
    /**
     * Whole timebar length (Not including two extra empty part on left and right ends)
     * 整个控件的长度 包括左边和右边的留白部分
     */
    private int viewLength;

    /**
     * How many seconds can be seen on one screen
     * 一个界面可显示的总的秒数
     */
    private int totalSecondsInOneScreen;

    /**
     * Time interval between two large ticks (Also known as key ticks)
     * 两个长刻度的的间隔
     */
    private int keyTickInSecond;

    /**
     * Time interval between two small ticks
     * 小刻度之间间隔
     */
    private int minTickInSecond;

    /**
     * How to format time string
     * 时间格式
     */
    private String dataPattern;

    public int getViewLength() {
        return viewLength;
    }

    public void setViewLength(int viewLength) {
        this.viewLength = viewLength;
    }

    public int getTotalSecondsInOneScreen() {
        return totalSecondsInOneScreen;
    }

    public void setTotalSecondsInOneScreen(int totalSecondsInOneScreen) {
        this.totalSecondsInOneScreen = totalSecondsInOneScreen;
    }

    public int getKeyTickInSecond() {
        return keyTickInSecond;
    }

    public void setKeyTickInSecond(int keyTickInSecond) {
        this.keyTickInSecond = keyTickInSecond;
    }

    public int getMinTickInSecond() {
        return minTickInSecond;
    }

    public void setMinTickInSecond(int minTickInSecond) {
        this.minTickInSecond = minTickInSecond;
    }

    public String getDataPattern() {
        return dataPattern;
    }

    public void setDataPattern(String dataPattern) {
        this.dataPattern = dataPattern;
    }
}
