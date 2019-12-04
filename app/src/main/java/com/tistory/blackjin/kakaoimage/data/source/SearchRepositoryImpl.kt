package com.tistory.blackjin.kakaoimage.data.source

import com.tistory.blackjin.kakaoimage.data.model.SearchResponse
import com.tistory.blackjin.kakaoimage.data.source.remote.SearchApi
import io.reactivex.Single

class SearchRepositoryImpl(
    private val searchApi: SearchApi
) : SearchRepository {

    override fun getImages(query: String, page: Int): Single<SearchResponse> {
        return searchApi.searchImage(query, page, PER_PAGE)
    }

    companion object {
        private const val PER_PAGE = 10
    }
}