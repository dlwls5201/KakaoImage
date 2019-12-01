package com.tistory.blackjin.kakaoimage.data.source

import com.tistory.blackjin.kakaoimage.data.model.SearchResponse
import io.reactivex.Single

interface SearchRepository {

    fun getImages(query: String, page: Int): Single<SearchResponse>
}