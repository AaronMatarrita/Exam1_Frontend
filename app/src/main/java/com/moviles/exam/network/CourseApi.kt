package com.moviles.exam.network

import com.moviles.exam.models.Course
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface CourseApi {
    @GET("Exam1_API/course")
    suspend fun getCourses(): List<Course>

    @Multipart
    @POST("Exam1_API/course")
    suspend fun createCourse(
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part
    )

    @Multipart
    @PUT("Exam1_API/course/{id}")
    suspend fun updateCourse(
        @Path("id") id: Int,
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part
    )
}
