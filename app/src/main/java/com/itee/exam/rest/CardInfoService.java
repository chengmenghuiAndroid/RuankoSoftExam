package com.itee.exam.rest;

import com.itee.exam.vo.CardQueryResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by xin on 2015-08-11-0011.
 */
public interface CardInfoService {

    /**
     * 查询银行卡信息
     *
     * @param cardnum
     * @return
     */
    @GET("/datatiny/cardinfo/cardinfo")
    void queryCardInfo(@Query("cardnum") String cardnum, Callback<CardQueryResult> callback);
}