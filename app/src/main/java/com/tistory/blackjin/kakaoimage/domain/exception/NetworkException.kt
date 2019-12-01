package com.tistory.blackjin.kakaoimage.domain.exception

class NetworkException(
    message: String,
    val code: Int
) : Throwable(message)
