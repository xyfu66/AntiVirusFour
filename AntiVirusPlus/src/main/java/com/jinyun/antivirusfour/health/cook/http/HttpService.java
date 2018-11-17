package com.jinyun.antivirusfour.health.cook.http;


import com.jinyun.antivirusfour.health.cook.entity.CookDetail;
import com.jinyun.antivirusfour.health.cook.entity.CookList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by qiantao on 2016/10/13.
 * http请求接口
 */

interface HttpService {
    @GET("list")
    Observable<CookList> fetchCookList(
            @Query("id") int id,
            @Query("page") int page
    );

    @GET("show")
    Observable<CookDetail> fetchCookDetail(
            @Query("id") int id
    );

    @GET("name")
    Observable<CookList> searchCookByName(
            @Query("name") String name
    );

}
