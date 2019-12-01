package com.tistory.blackjin.kakaoimage.scheduler

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}
