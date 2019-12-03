package com.tistory.blackjin.kakaoimage.di

import com.tistory.blackjin.kakaoimage.ui.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {

    viewModel { SearchViewModel(get()) }
}