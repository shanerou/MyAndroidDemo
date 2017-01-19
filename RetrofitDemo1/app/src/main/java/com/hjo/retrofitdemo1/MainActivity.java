package com.hjo.retrofitdemo1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.hjo.retrofitdemo1.countday.CountDay;
import com.hjo.retrofitdemo1.remote.APINet;
import com.hjo.retrofitdemo1.remote.countday_url;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView testRetorflt;
    private countday_url mCountday_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testRetorflt=(TextView)findViewById(R.id.testRetorflt);
        mCountday_url= APINet.getCountDaysfh();
        getData();
    }

    public void  getData(){
//        http://61.178.102.124:7200/GSTemp/api/CityData/GetCityDayCount?cityName=%E7%94%98%E8%82%8314%E5%9F%8E%E5%B8%82&countType=4&year=2016&countIndex=10
        mCountday_url.getCountDay("甘肃14城市",4,2016,10).enqueue(new Callback<CountDay>() {
            @Override
            public void onResponse(Call<CountDay> call, Response<CountDay> response) {
                if (response.isSuccessful()){
                    testRetorflt.setText(response.body().toString());
                }else{
                    Log.e("hjo","输出未获取到数据的原因："+response.message());
                }
            }

            @Override
            public void onFailure(Call<CountDay> call, Throwable t) {
                Log.e("hjo","获取失败");

            }
        });
    }
}
