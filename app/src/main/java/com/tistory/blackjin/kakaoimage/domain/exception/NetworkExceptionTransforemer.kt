package com.tistory.blackjin.kakaoimage.domain.exception

import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import retrofit2.HttpException

class NetworkExceptionSingleTransformer<T> : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> =
        upstream.onErrorResumeNext {
            Single.error(
                if (it is HttpException)
                    NetworkException(it.message(), it.code())
                else it
            )
        }
}

fun <T> Single<T>.composeDomain() =
    compose(NetworkExceptionSingleTransformer())
