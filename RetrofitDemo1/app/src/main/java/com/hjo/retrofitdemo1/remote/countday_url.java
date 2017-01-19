package com.hjo.retrofitdemo1.remote;

import com.hjo.retrofitdemo1.countday.CountDay;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hjo on 2017/1/19.
 */

public interface countday_url {
//    http://61.178.102.124:7200/GSTemp/api/CityData/GetCityDayCount?cityName=%E7%94%98%E8%82%8314%E5%9F%8E%E5%B8%82&countType=4&year=2016&countIndex=10
    @GET("CityData/GetCityDayCount?")//这里 跟以下的方法中至少要有一个有参数   如果两个都不需要  可以直接在此方法中写@GET("/")
    Call<CountDay>getCountDay(@Query("cityName")String cityName,@Query("countType")int countType,@Query("year")int year,@Query("countIndex")int countIndex);

}


