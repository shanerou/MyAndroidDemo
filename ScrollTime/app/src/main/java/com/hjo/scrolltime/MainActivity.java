package com.hjo.scrolltime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SrollTimeView.OnScrollListener{
    SrollTimeView msrollTimeView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msrollTimeView=(SrollTimeView)findViewById(R.id.srolltimeView);
        msrollTimeView.setScrollListener(this);
        //添加时间片段
        List<SrollTimeView.TimePart> time=new ArrayList<>();
        time.add(new SrollTimeView.TimePart(0,0,0,5,0,0));
        msrollTimeView.addTimePart(time);

    }

    @Override
    public void onScroll(int hour, int min, int sec) {

    }

    @Override
    public void onScrollFinish(int hour, int min, int sec) {
        Log.d("--onScrollFinish--","hour "+hour+" min "+min+" sec "+sec);
    }
}









//        12-20 16:43:34.070: E/hjo(9866): getStartTime= 2016-12-19 22:52:24
//        12-20 16:43:34.070: E/hjo(9866): getEndTime= 2016-12-20 0:5:8
//
//        12-20 16:43:34.070: E/hjo(9866): getStartTime= 2016-12-20 0:5:8
//        12-20 16:43:34.070: E/hjo(9866): getEndTime= 2016-12-20 1:17:55
//
//        12-20 16:43:34.070: E/hjo(9866): getStartTime= 2016-12-20 1:17:55
//        12-20 16:43:34.070: E/hjo(9866): getEndTime= 2016-12-20 2:30:38
//
//        12-20 16:43:34.070: E/hjo(9866): getStartTime= 2016-12-20 2:30:38
//        12-20 16:43:34.070: E/hjo(9866): getEndTime= 2016-12-20 3:43:23
//
//        12-20 16:43:34.070: E/hjo(9866): getStartTime= 2016-12-20 3:43:23
//        12-20 16:43:34.070: E/hjo(9866): getEndTime= 2016-12-20 4:56:9
//
//        12-20 16:43:34.070: E/hjo(9866): getStartTime= 2016-12-20 4:56:9
//        12-20 16:43:34.070: E/hjo(9866): getEndTime= 2016-12-20 6:8:53
//
//        12-20 16:43:34.070: E/hjo(9866): getStartTime= 2016-12-20 6:8:53
//        12-20 16:43:34.070: E/hjo(9866): getEndTime= 2016-12-20 7:21:40
//
//        12-20 16:43:34.070: E/hjo(9866): getStartTime= 2016-12-20 7:21:40
//        12-20 16:43:34.070: E/hjo(9866): getEndTime= 2016-12-20 8:34:24
//
//        12-20 16:43:34.080: E/hjo(9866): getStartTime= 2016-12-20 8:34:24
//        12-20 16:43:34.080: E/hjo(9866): getEndTime= 2016-12-20 9:47:43
//
//        12-20 16:43:34.080: E/hjo(9866): getStartTime= 2016-12-20 9:47:43
//        12-20 16:43:34.080: E/hjo(9866): getEndTime= 2016-12-20 10:59:59
//
//        12-20 16:43:34.080: E/hjo(9866): getStartTime= 2016-12-20 10:59:59
//        12-20 16:43:34.080: E/hjo(9866): getEndTime= 2016-12-20 12:12:38
//
//        12-20 16:43:34.080: E/hjo(9866): getStartTime= 2016-12-20 12:12:38
//        12-20 16:43:34.080: E/hjo(9866): getEndTime= 2016-12-20 13:25:19
//
//        12-20 16:43:34.080: E/hjo(9866): getStartTime= 2016-12-20 13:25:19
//        12-20 16:43:34.080: E/hjo(9866): getEndTime= 2016-12-20 14:38:34
//
//        12-20 16:43:34.080: E/hjo(9866): getStartTime= 2016-12-20 14:38:34
//        12-20 16:43:34.080: E/hjo(9866): getEndTime= 2016-12-20 15:51:17
//
//        12-20 16:43:34.080: E/hjo(9866): getStartTime= 2016-12-20 15:51:17
//        12-20 16:43:34.080: E/hjo(9866): getEndTime= 2016-12-20 16:41:16