package com.moviles.exam.network

import com.moviles.exam.models.Student
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StudentApi {
    @GET("api/student/byCourse/{courseId}")
    suspend fun getAllStudentsByCourse(@Path("courseId") courseId: Int): List<Student>

    @GET("api/student/allStudent")
    suspend fun getAllStudents(): List<Student>

    @GET("api/student/getStudent/{id}")
    suspend fun getStudentById(@Path("id") id: Int): Student

    @POST("api/student/registerStudent")
    suspend fun registerStudent(@Body student: Student): Student

    @PUT("api/student/updateStudent/{id}")
    suspend fun updateStudent(
        @Path("id") id: Int,
        @Body student: Student
    ): Student

    @DELETE("api/student/deleteStudent/{id}")
    suspend fun deleteStudent(@Path("id") id: Int)
}