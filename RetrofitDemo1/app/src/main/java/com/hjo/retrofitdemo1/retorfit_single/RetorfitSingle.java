package com.hjo.retrofitdemo1.retorfit_single;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Hjo on 2017/1/19.
 */
public class RetorfitSingle {
    private static Retrofit retorfilContext=null;

    public static Retrofit getRetorfit(String  url){  // 构建Retorfit
        if (retorfilContext==null){
            retorfilContext=new Retrofit.Builder()
                    .baseUrl(url)  // 传入url
                    .addConverterFactory(GsonConverterFactory.create())//使用Gson解析
                    .build();
        }
        return  retorfilContext;
    }

}
