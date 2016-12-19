package com.hjo.MyViewDragHelperDemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    MyDragHelperView myDragHelperView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDragHelperView=(MyDragHelperView)findViewById(R.id.myDragHelperView);
        myDragHelperView.setDirectionClickListenter(new MyDragHelperView.DirectionClickListenter() {
            @Override
            public void directionIndex(int directionId) {
                Log.e("hjo","转动的id="+ directionId);
            }
        });
    }
}
