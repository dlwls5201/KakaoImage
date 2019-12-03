package com.tistory.blackjin.kakaoimage.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tistory.blackjin.kakaoimage.data.model.Document
import com.tistory.blackjin.kakaoimage.domain.usecase.SearchImageUsecase
import com.tistory.blackjin.kakaoimage.ui.base.BaseViewModel
import com.tistory.blackjin.kakaoimage.util.Event
import timber.log.Timber

class SearchViewModel(
    private val searchImageUsecase: SearchImageUsecase
) : BaseViewModel() {

    private val _items = MutableLiveData<List<Document>>(emptyList())
    val items: LiveData<List<Document>> = _items

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastLiveData = MutableLiveData<Event<String>>()
    val toastLiveData: LiveData<Event<String>> = _toastLiveData

    private var currentQuery = ""
    private var currentPage = 1
    private var isEndPage = false

    private val searchingItems = mutableListOf<Document>()

    fun loadImages(query: String, isAdd: Boolean = false) {

        if (isLoading.value == true) {
            //기존 작업 중인게 있으면 제거
            compositeDisposable.clear()
        }

        currentQuery = query

        searchImageUsecase.get(query, currentPage)
            .doOnSubscribe { showLoading() }
            .doOnSuccess { hideLoading() }
            .doOnError { hideLoading() }
            .subscribe({

                val (documents, isEnd) = it

                if (documents.isEmpty()) {
                    _toastLiveData.value = Event("데이터가 없습니다.")
                } else {
                    if (isAdd) {
                        searchingItems.addAll(documents)
                        _items.value = searchingItems
                    } else {
                        currentPage = 1
                        searchingItems.clear()
                        searchingItems.addAll(documents)
                        _items.value = documents
                    }
                }

                Timber.d("currentPage : $currentPage , searchingItems : ${searchingItems.size} , isEnd : $isEnd")
                isEndPage = isEnd

            }) {
                Timber.e(it)
            }.also {
                compositeDisposable.add(it)
            }
    }

    fun loadNextImage() {
        Timber.d("isEndItem : $isEndPage")
        if (isEndPage) {
            _toastLiveData.value = Event("last page")
        } else {
            currentPage++
            loadImages(currentQuery, true)
        }
    }

    private fun showLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }
}
