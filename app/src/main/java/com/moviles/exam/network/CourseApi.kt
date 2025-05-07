package com.moviles.exam.network


import com.moviles.exam.models.Course
import retrofit2.http.GET

interface CourseApi {
    @GET("Exam1_API/course")
    suspend fun getCourses(): List<Course>
}