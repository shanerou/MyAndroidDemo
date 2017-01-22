package com.hjo.reforfitdemo2;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.hjo.reforfitdemo2.adapter.RecycViewAdapter;
import com.hjo.reforfitdemo2.model.ComplexModel;
import com.hjo.reforfitdemo2.model.Develop;
import com.hjo.reforfitdemo2.net_interface.ComplexInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit mRetrofit;
//    private PathNetInterface mPathNetInterface;
    private ComplexInterface complexInterface;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecycViewAdapter mRecycViewAdapter;
//    List<TestModel> testModelList;

    List<Develop>mlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String url="http://10.10.10.18:7200/";//注意  这里需要以/为结尾  否则会产生一个异常
        String url=" http://10.10.10.18:89/";
        mlist=new ArrayList<>();
        mRecycViewAdapter=new RecycViewAdapter(this,mlist);
        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager linLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linLayoutManager);
        mRecyclerView.setAdapter(mRecycViewAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);


        mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();

            }
        });
        mRetrofit=getRetrofit(url);
//        mPathNetInterface=mRetrofit.create(PathNetInterface.class);
        complexInterface=mRetrofit.create(ComplexInterface.class);

//        mSwipeRefreshLayout.setRefreshing(true);

        getData();

    }

    public Retrofit getRetrofit(String url){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())//使用Gson来解析数据
                .build();
        return retrofit;
    }

    public void getData(){
        Call<ComplexModel> call=complexInterface.getComplexModel();
        call.enqueue(new Callback<ComplexModel>() {
            @Override
            public void onResponse(Call<ComplexModel> call, Response<ComplexModel> response) {
                if(response.isSuccessful()) {
                   mlist.clear();
                    mlist.addAll(response.body().getDevelop());
                    Log.e("hjo",response.body().toString());
                   mRecycViewAdapter.notifyDataSetChanged();
               }else{
                   Toast.makeText(MainActivity.this,response.message()+"     "+response.code(),Toast.LENGTH_LONG).show();
               }
               mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ComplexModel> call, Throwable t) {
                Toast.makeText(MainActivity.this,"数据获取失败",Toast.LENGTH_LONG).show();
               mSwipeRefreshLayout.setRefreshing(false);
            }
        });
//        Call<List<TestModel>> call=mPathNetInterface.getTestModel(//"CityData","GetCityDayLevelInfo",
//                "兰州市","aqi","2015","1");
//       call.enqueue(new Callback<List<TestModel>>() {
//           @Override
//           public void onResponse(Call<List<TestModel>> call, Response<List<TestModel>> response) {
//               Log.e("hjo","错误码："+response.code());
//               if(response.isSuccessful()) {
//                   testModelList.clear();
//                   testModelList.addAll(response.body());
////                   testModelList = response.body();
//                   Log.e("hjo",testModelList.toString());
//                   mRecycViewAdapter.notifyDataSetChanged();
//               }else{
//                   Toast.makeText(MainActivity.this,response.message()+"     "+response.code(),Toast.LENGTH_LONG).show();
//               }
//               mSwipeRefreshLayout.setRefreshing(false);
//           }
//           @Override
//           public void onFailure(Call<List<TestModel>> call, Throwable t) {
//               Toast.makeText(MainActivity.this,"数据获取失败",Toast.LENGTH_LONG).show();
//               mSwipeRefreshLayout.setRefreshing(false);
//           }
//       });
    }
}
