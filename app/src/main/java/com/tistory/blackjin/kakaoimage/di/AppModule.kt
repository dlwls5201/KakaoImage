package com.tistory.blackjin.kakaoimage.di


import com.tistory.blackjin.kakaoimage.scheduler.AppSchedulerProvider
import com.tistory.blackjin.kakaoimage.scheduler.SchedulersProvider
import org.koin.dsl.module

val appModule = module {

    single<SchedulersProvider> { AppSchedulerProvider() }
}
