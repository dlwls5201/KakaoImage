package com.tistory.blackjin.kakaoimage.domain.usecase

import com.tistory.blackjin.kakaoimage.data.source.SearchRepository
import com.tistory.blackjin.kakaoimage.scheduler.SchedulersProvider

class SearchImageUsecase(
    private val searchRepository: SearchRepository,
    private val schedulerProvider: SchedulersProvider
) {

    fun get(query: String, page: Int = 1) =
        searchRepository
            .getImages(query, page)
            .subscribeOn(schedulerProvider.io())
            .map {
                Pair(it.documents, it.meta.isEnd)
            }
            .observeOn(schedulerProvider.ui())
}
