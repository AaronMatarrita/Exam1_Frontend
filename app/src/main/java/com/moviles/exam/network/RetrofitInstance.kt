package com.moviles.exam.network


import com.moviles.exam.common.Constants.API_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val courseApi: CourseApi by lazy {
        retrofit.create(CourseApi::class.java)
    }

    val studentApi: StudentApi by lazy {
        retrofit.create(StudentApi::class.java)
    }

    // Retrofit base
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
