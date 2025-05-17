package com.moviles.exam.models

data class UpdateCourseRequest (
    val Name: String,
    val Description: String,
    val Schedule: String,
    val Professor: String
)