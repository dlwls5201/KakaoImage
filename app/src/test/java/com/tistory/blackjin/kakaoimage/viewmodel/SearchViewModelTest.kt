package com.tistory.blackjin.kakaoimage.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tistory.blackjin.kakaoimage.data.model.Document
import com.tistory.blackjin.kakaoimage.domain.usecase.SearchImageUsecase
import com.tistory.blackjin.kakaoimage.ui.SearchViewModel
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel

    @Mock
    private lateinit var usecase: SearchImageUsecase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val item = Document("", "", "", "", 10, "", "", 10)

    @Before
    fun setUp() {
        viewModel = SearchViewModel(usecase)

        //given
        Mockito.`when`(usecase.get("blackJin", 1))
            .thenReturn(Single.just(Pair(listOf(item), false)))

        Mockito.`when`(usecase.get("blackJin", 2))
            .thenReturn(Single.just(Pair(listOf(item), true)))
    }

    @Test
    fun `load empty data`() {

        //given
        Mockito.`when`(usecase.get("empty", 1))
            .thenReturn(Single.just(Pair(listOf(), true)))

        //when
        viewModel.loadImages("empty")

        //then
        val msg = LiveDataTestUtil.getValue(viewModel.message)
        assertEquals(msg, "No result")
    }

    @Test
    fun `load next item`() {

        //when
        viewModel.loadImages("blackJin")
        viewModel.loadNextImages()

        //then
        assertEquals(viewModel.items.value?.size, 2)
    }

    @Test
    fun `load last item show toast`() {

        //when
        viewModel.loadImages("blackJin")
        viewModel.loadNextImages()
        viewModel.loadNextImages()

        //then
        assertEquals(viewModel.items.value?.size, 2)

        val toast = LiveDataTestUtil.getValue(viewModel.toastLiveData).getContentIfNotHandled()
        assertEquals(toast, "last page")
    }
}