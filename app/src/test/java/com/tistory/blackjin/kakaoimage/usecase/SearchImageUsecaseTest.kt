package com.tistory.blackjin.kakaoimage.usecase

import com.tistory.blackjin.kakaoimage.data.model.Meta
import com.tistory.blackjin.kakaoimage.data.model.SearchResponse
import com.tistory.blackjin.kakaoimage.data.source.SearchRepository
import com.tistory.blackjin.kakaoimage.domain.usecase.SearchImageUsecase
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchImageUsecaseTest {

    @Mock
    private lateinit var searchRepository: SearchRepository

    private lateinit var usecase: SearchImageUsecase

    @Before
    fun setUp() {
        usecase = SearchImageUsecase(
            searchRepository,
            TestSchedulerProvider()
        )
    }

    @Test
    fun `success search usecase`() {

        val query = "blackjin"

        val response = SearchResponse(listOf(), Meta(false, 10, 10))

        //given
        `when`(searchRepository.getImages(query, 1)).thenReturn(Single.just(response))

        //when
        usecase.get(query)
            .subscribe({
                val (list, isEnd) = it

                //then
                assertEquals(list, response.documents)
                assertEquals(isEnd, response.meta.isEnd)
            }) {

            }
    }
}