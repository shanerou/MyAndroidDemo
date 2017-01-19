package com.hjo.retrofitdemo1.remote;

import com.hjo.retrofitdemo1.retorfit_single.RetorfitSingle;

/**
 * Created by Hjo on 2017/1/19.
 */

public class APINet {
    private static final String URL="http://61.178.102.124:7200/GSTemp/api/";

    public static countday_url getCountDaysfh(){
        return RetorfitSingle.getRetorfit(URL).create(countday_url.class);
    }
}
