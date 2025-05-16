package com.moviles.exam.network

import android.annotation.SuppressLint
import android.content.Context
import com.moviles.exam.common.Constants.API_BASE_URL
import com.moviles.exam.utils.NetworkUtils
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

@SuppressLint("StaticFieldLeak")
object RetrofitInstance {
    private const val CACHE_SIZE = 10 * 1024 * 1024
    private lateinit var cache: Cache
    private lateinit var context: Context

    fun initCache(appContext: Context) {
        context = appContext
        val cacheDir = File(appContext.cacheDir, "http_cache_exam")
        cache = Cache(cacheDir, CACHE_SIZE.toLong())
    }

    private val client: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (NetworkUtils.isNetworkAvailable(context)) {
                    request.newBuilder()
                        .header("Cache-Control", "public, max-age=60").build()
                } else {
                    request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=604800").build()
                }
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val courseApi: CourseApi by lazy {
        retrofit.create(CourseApi::class.java)
    }

    val studentApi: StudentApi by lazy {
        retrofit.create(StudentApi::class.java)
    }
}