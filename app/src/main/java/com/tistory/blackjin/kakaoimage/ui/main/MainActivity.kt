package com.tistory.blackjin.kakaoimage.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import com.tistory.blackjin.kakaoimage.R
import com.tistory.blackjin.kakaoimage.databinding.ActivityMainBinding
import com.tistory.blackjin.kakaoimage.ui.SearchAdapter
import com.tistory.blackjin.kakaoimage.ui.SearchViewModel
import com.tistory.blackjin.kakaoimage.ui.base.BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val searchViewModel: SearchViewModel by viewModel()

    private val searchAdapter = SearchAdapter()

    private val viewDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewmodel = searchViewModel
        binding.etSearchImage.requestFocus()

        setupRecyclerView()
        initRxBinding(savedInstanceState)
        initObserver()
    }

    override fun onDestroy() {
        viewDisposable.clear()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val keyword = binding.etSearchImage.text.toString()
        outState.putString(KET_SEARCH_KEYWORD, keyword)
    }

    private fun setupRecyclerView() {
        with(binding.rvSearchImage) {
            adapter = searchAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    when {
                        !recyclerView.canScrollVertically(-1) -> {
                            //Top of list
                        }
                        !recyclerView.canScrollVertically(1) -> {
                            //End of list"
                            searchViewModel.loadNextImages()
                        }
                    }
                }
            })
        }
    }

    private fun initRxBinding(savedInstanceState: Bundle?) {
        binding.etSearchImage.textChanges()
            .debounce(1000L, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (!checkAlreadySearch(savedInstanceState, it.toString())) {
                    searchViewModel.loadImages(it.toString())
                }
            }.also {
                viewDisposable.add(it)
            }
    }

    private fun checkAlreadySearch(savedInstanceState: Bundle?, query: String): Boolean {
        val keyword = savedInstanceState?.getString(KET_SEARCH_KEYWORD)

        return if (keyword.isNullOrEmpty()) {
            false
        } else {
            savedInstanceState.remove(KET_SEARCH_KEYWORD)
            keyword == query
        }
    }

    private fun initObserver() {
        searchViewModel.items.observe(this, Observer {
            searchAdapter.replaceAll(it)
        })

        searchViewModel.toastLiveData.observe(this, Observer {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {

        private const val KET_SEARCH_KEYWORD = "search_keyword"
    }
}
