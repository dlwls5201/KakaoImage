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

    private val _message = MutableLiveData<String>("")
    val message: LiveData<String> = _message

    private val _toastLiveData = MutableLiveData<Event<String>>()
    val toastLiveData: LiveData<Event<String>> = _toastLiveData

    private var currentQuery = ""
    private var currentPage = 1
    private var isEndPage = false

    private val showingItems = mutableListOf<Document>()

    fun loadImages(query: String, isAdd: Boolean = false) {

        //기존 작업 중인게 있으면 제거
        if (isLoading.value == true) {
            compositeDisposable.clear()
        }

        //데이터 추가가 아니면 current page 1 초기화
        if (!isAdd) {
            currentPage = 1
        }

        //현재 검색 값 초기화
        currentQuery = query

        searchImageUsecase.get(currentQuery, currentPage)
            .doOnSubscribe { showLoading() }
            .doOnSuccess { hideLoading() }
            .doOnError { hideLoading() }
            .subscribe({

                val (documents, isEnd) = it

                if (isAdd) {
                    hideMessage()
                    showingItems.addAll(documents)
                } else {
                    showingItems.clear()

                    if (documents.isEmpty()) {
                        showMessage("No result")
                    } else {
                        hideMessage()
                        showingItems.addAll(documents)
                    }
                }

                _items.value = showingItems
                isEndPage = isEnd

            }) {
                Timber.e(it)
                showMessage(it.message)
            }.also {
                compositeDisposable.add(it)
            }
    }

    fun loadNextImages() {

        if (isLoading.value == true) {
            showToast("loading...")
            return
        }

        if (isEndPage) {
            showToast("last page")
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

    private fun showToast(toast: String) {
        _toastLiveData.value = Event(toast)
    }

    private fun showMessage(message: String?) {
        message?.let {
            _message.value = it
        }
    }

    private fun hideMessage() {
        _message.value = ""
    }
}
