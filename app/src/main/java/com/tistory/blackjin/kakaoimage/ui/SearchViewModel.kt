package com.tistory.blackjin.kakaoimage.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tistory.blackjin.kakaoimage.data.model.SearchResponse
import com.tistory.blackjin.kakaoimage.domain.usecase.SearchImageUsecase
import com.tistory.blackjin.kakaoimage.ui.base.BaseViewModel

class SearchViewModel(
    private val searchImageUsecase: SearchImageUsecase
) : BaseViewModel() {

    private val _items = MutableLiveData<List<SearchResponse>>(emptyList())
    val items: LiveData<List<SearchResponse>> = _items

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

}
