package com.tistory.blackjin.kakaoimage.data.source.remote

import com.tistory.blackjin.kakaoimage.data.model.SearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("v2/search/image")
    fun searchImage(
        @Query("query") query: String,
        @Query("page") page: Int, //  1 ~ 50 , default : 1
        @Query("size") size: Int // 1 ~ 80 , default : 10
    ): Single<SearchResponse>
}