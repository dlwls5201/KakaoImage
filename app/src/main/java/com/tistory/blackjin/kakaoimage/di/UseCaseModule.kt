package com.tistory.blackjin.kakaoimage.di

import com.tistory.blackjin.kakaoimage.domain.usecase.SearchImageUsecase
import org.koin.dsl.module

val usecaseModule = module {

    single { SearchImageUsecase(get(), get()) }
}