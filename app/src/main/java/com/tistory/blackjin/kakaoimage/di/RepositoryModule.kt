package com.tistory.blackjin.kakaoimage.di

import com.tistory.blackjin.kakaoimage.data.source.SearchRepository
import com.tistory.blackjin.kakaoimage.data.source.SearchRepositoryImpl
import com.tistory.blackjin.kakaoimage.data.source.remote.SearchApi
import org.koin.dsl.module
import retrofit2.Retrofit

val repositoryModule = module {

    //api
    single { get<Retrofit>().create(SearchApi::class.java) }

    //repository
    single<SearchRepository> { SearchRepositoryImpl(get()) }
}
