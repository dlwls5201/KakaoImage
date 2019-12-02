package com.tistory.blackjin.kakaoimage.ui

import android.os.Bundle
import com.tistory.blackjin.kakaoimage.R
import com.tistory.blackjin.kakaoimage.databinding.ActivityMainBinding
import com.tistory.blackjin.kakaoimage.domain.usecase.SearchImageUsecase
import com.tistory.blackjin.kakaoimage.ui.base.BaseActivity
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val usecase: SearchImageUsecase by inject()

    private val searchAdapter = SearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupRecyclerView()

        usecase.get("aoa")
            .subscribe({
                val (items, endPoint) = it
                searchAdapter.setItems(items)
            }) {
                Timber.e(it)
            }

    }

    private fun setupRecyclerView() {
        binding.rvSearchImage.adapter = searchAdapter
    }
}
