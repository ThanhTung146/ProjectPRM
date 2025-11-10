package com.example.projectprm.data.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/" // Android emulator localhost
    
    private var retrofit: Retrofit? = null
    
    fun getInstance(context: Context): Retrofit {
        if (retrofit == null) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
            
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
    
    fun <T> createService(serviceClass: Class<T>, context: Context): T {
        return getInstance(context).create(serviceClass)
    }
}

// Helper functions for easy access
fun Context.authApi(): AuthApi = RetrofitClient.createService(AuthApi::class.java, this)
fun Context.bookApi(): BookApi = RetrofitClient.createService(BookApi::class.java, this)
fun Context.cartApi(): CartApi = RetrofitClient.createService(CartApi::class.java, this)
fun Context.orderApi(): OrderApi = RetrofitClient.createService(OrderApi::class.java, this)
