package com.moviles.exam.network


import com.moviles.exam.models.Course
import com.moviles.exam.models.UpdateCourseRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface CourseApi {

    //endpoint get Course
    @GET("Exam1_API/course")
    suspend fun getCourses(): List<Course>

    @POST("Exam1_API/course")
    suspend fun createCourse(@Body course: Course): Course


    @GET("Exam1_API/course/{id}")
    suspend fun getCourseById(@Path("id") id: Int): Course


    @PUT("Exam1_API/course/{id}")
    suspend fun updateCourse(
        @Path("id") id: Int,
        @Body course: UpdateCourseRequest
    ): Course

    @DELETE("Exam1_API/course/{id}")
    suspend fun deleteCourse(@Path("id") id: Int): Response<Unit>

    @Multipart
    @POST("Exam1_API/course")
    suspend fun createCourseWithImage(
        @Part("Name") name: RequestBody,
        @Part("Description") description: RequestBody,
        @Part("Schedule") schedule: RequestBody,
        @Part("Professor") professor: RequestBody,
        @Part image: MultipartBody.Part?
    ): Course

    @Multipart
    @PUT("Exam1_API/course/{id}")
    suspend fun updateCourseWithImage(
        @Path("id") id: Int,
        @Part("Name") name: RequestBody,
        @Part("Description") description: RequestBody,
        @Part("Schedule") schedule: RequestBody,
        @Part("Professor") professor: RequestBody,
        @Part image: MultipartBody.Part?
    ): Course

}