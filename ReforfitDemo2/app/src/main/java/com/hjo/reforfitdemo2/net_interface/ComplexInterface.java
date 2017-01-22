package com.hjo.reforfitdemo2.net_interface;

import com.hjo.reforfitdemo2.model.ComplexModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Hjo on 2017/1/22.
 */

public interface ComplexInterface  {
//    http://10.10.10.18:89/api/Develop/temp
    @GET("api/Develop/temp")
    Call<ComplexModel> getComplexModel();
}
