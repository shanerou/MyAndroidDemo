package com.hjo.reforfitdemo2.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import static android.R.attr.id;

/**
 * Created by Hjo on 2017/1/22.
 * 万能适配
 */
    public class MyViewHolder extends  RecyclerView.ViewHolder{
        static View mview;
        public MyViewHolder(View itemView) {
            super(itemView);
            mview=itemView;;
        }
        public static  <T extends  View> T getView(int viewId){
            SparseArray<View>viewHolderlist=(SparseArray<View>)mview.getTag();
            if(viewHolderlist==null){
                viewHolderlist=new SparseArray<>();
                mview.setTag(viewHolderlist);
            }
            View childView=viewHolderlist.get(viewId);
            if(childView==null){
                childView=mview.findViewById(viewId);
                viewHolderlist.put(id,childView);
            }
            return (T) childView;
        }
    }