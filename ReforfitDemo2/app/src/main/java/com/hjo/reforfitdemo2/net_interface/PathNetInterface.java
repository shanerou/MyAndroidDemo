package com.hjo.reforfitdemo2.net_interface;


import com.hjo.reforfitdemo2.model.TestModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by Hjo on 2017/1/22.
 * @Part注解 的使用：替换@GET里有{}的字段
 */
public interface PathNetInterface {
//    http://10.10.10.18:7200/api/CityData/GetCityDayLevelInfo?cityName=%E5%85%B0%E5%B7%9E%E5%B8%82&PollutantName=aqi&year=2015&countIndex=1
     @GET("api/CityData/GetCityDayLevelInfo")
     Call<List<TestModel>> getTestModel(//@Part("CityData")String CityData, @Part("GetCityDayLevelInfo")String GetCityDayLevelInfo,
                                        @Query("cityName") String cityName, @Query("PollutantName") String PollutantName, @Query("year") String year,
                                        @Query("countIndex") String countIndex);
}
