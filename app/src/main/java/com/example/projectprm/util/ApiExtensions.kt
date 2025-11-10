package com.example.projectprm.util

import android.app.Application
import com.example.projectprm.data.api.AuthApi
import com.example.projectprm.data.api.BookApi
import com.example.projectprm.data.api.CartApi
import com.example.projectprm.data.api.OrderApi
import com.example.projectprm.data.api.RetrofitClient

/**
 * Extension functions to get API instances
 */

fun Application.authApi(): AuthApi {
    return RetrofitClient.getInstance(this).create(AuthApi::class.java)
}

fun Application.bookApi(): BookApi {
    return RetrofitClient.getInstance(this).create(BookApi::class.java)
}

fun Application.cartApi(): CartApi {
    return RetrofitClient.getInstance(this).create(CartApi::class.java)
}

fun Application.orderApi(): OrderApi {
    return RetrofitClient.getInstance(this).create(OrderApi::class.java)
}
