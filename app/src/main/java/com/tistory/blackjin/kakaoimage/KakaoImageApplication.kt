package com.tistory.blackjin.kakaoimage

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.tistory.blackjin.kakaoimage.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import timber.log.Timber

class KakaoImageApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initFresco()
        setupKoin()

    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initFresco() {
        Fresco.initialize(this)
    }

    private fun setupKoin() {
        startKoin {
            logger(
                if (BuildConfig.DEBUG) {
                    AndroidLogger()
                } else {
                    EmptyLogger()
                }
            )

            androidContext(this@KakaoImageApplication)

            modules(
                listOf(
                    appModule, networkModule, repositoryModule, usecaseModule, viewModel
                )
            )
        }
    }
}