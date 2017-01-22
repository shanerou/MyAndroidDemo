package com.hjo.reforfitdemo2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hjo.reforfitdemo2.R;
import com.hjo.reforfitdemo2.model.Develop;

import java.util.List;

/**
 * Created by Hjo on 2017/1/22.
 */
public class RecycViewAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Develop> mlist;

    Context mContext;

    public RecycViewAdapter(Context context,List<Develop> list){
        mlist=list;
        mContext=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_layout,null);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Develop testModel=mlist.get(position);
        ((TextView)holder.getView(R.id.cityName)).setText(testModel.getAddress());
        ((TextView)holder.getView(R.id.time)).setText(testModel.getAge()+"");
        ((TextView)holder.getView(R.id.level)).setText(testModel.getName());
        ((TextView)holder.getView(R.id.vaule)).setText(testModel.getTimePoint());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


}
