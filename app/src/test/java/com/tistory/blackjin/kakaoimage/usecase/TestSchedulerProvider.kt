package com.tistory.blackjin.kakaoimage.usecase

import com.tistory.blackjin.kakaoimage.scheduler.SchedulersProvider
import io.reactivex.schedulers.Schedulers

class TestSchedulerProvider : SchedulersProvider {
    override fun io() = Schedulers.trampoline()

    override fun ui() = Schedulers.trampoline()
}