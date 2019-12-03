package com.tistory.blackjin.kakaoimage.ui

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import com.tistory.blackjin.kakaoimage.R
import com.tistory.blackjin.kakaoimage.databinding.ActivityMainBinding
import com.tistory.blackjin.kakaoimage.ui.base.BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val searchViewModel: SearchViewModel by viewModel()

    private val searchAdapter = SearchAdapter()

    private val viewDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("onCreate")

        binding.viewmodel = searchViewModel
        binding.etSearchImage.requestFocus()

        setupRecyclerView()
        initRxBinding()

        searchViewModel.items.observe(this, Observer {
            searchAdapter.replaceAll(it)
        })

        searchViewModel.toastLiveData.observe(this, Observer {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        Timber.e("onDestroy")
        viewDisposable.clear()
        super.onDestroy()
    }

    private fun setupRecyclerView() {
        with(binding.rvSearchImage) {
            adapter = searchAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    //check for scroll down
                    if (dy > 0) {
                        val layoutManager = this@with.layoutManager as LinearLayoutManager
                        val totalItemCount = layoutManager.itemCount
                        val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

                        //Timber.d("totalItemCount : $totalItemCount , lastVisible : $lastVisible")
                        if (lastVisible >= totalItemCount - 1) {
                            //Timber.e("last")
                            searchViewModel.loadNextImage()
                        }
                    }
                }
            })
        }
    }

    private fun initRxBinding() {
        binding.etSearchImage.textChanges()
            .debounce(1000L, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Timber.d("$it")
                searchViewModel.loadImages(it.toString())
            }.also {
                viewDisposable.add(it)
            }
    }
}
