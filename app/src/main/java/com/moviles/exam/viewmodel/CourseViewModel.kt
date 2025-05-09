package com.moviles.exam.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.exam.models.Course
import com.moviles.exam.models.UpdateCourseRequest
import com.moviles.exam.network.RetrofitInstance
import com.moviles.exam.utils.toFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.hamcrest.Description

data class CourseUiState(
    val isLoading: Boolean = false,
    val courses: List<Course> = emptyList(),
    val error: String? = null
)

class CourseViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState: StateFlow<CourseUiState> get() = _uiState

    init {
        fetchCourses()
    }

    fun fetchCourses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val courses = RetrofitInstance.courseApi.getCourses()
                _uiState.value = _uiState.value.copy(isLoading = false, courses = courses)
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error fetching courses: ${e.message}", e)
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun createCourse(course: Course) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val createdCourse = RetrofitInstance.courseApi.createCourse(course)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    courses = _uiState.value.courses + createdCourse
                )
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error creating course: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al crear curso: ${e.message}"
                )
            }
        }
    }



    fun createCourseWithImage(context: Context, name: String, description: String, schedule: String, professor: String, imageUri: Uri?){
        viewModelScope.launch { _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val imagePart = imageUri?.let { uri ->
                    val file = uri.toFile(context) ?: throw Exception("Error converting image URI to file")
                    MultipartBody.Part.createFormData(
                        "File",
                        file.name,
                        file.asRequestBody("File/*".toMediaTypeOrNull())
                    )
                }
                val response = RetrofitInstance.courseApi.createCourseWithImage(
                    name = name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    description = description.toRequestBody("text/plain".toMediaTypeOrNull()),
                    schedule = schedule.toRequestBody("text/plain".toMediaTypeOrNull()),
                    professor = professor.toRequestBody("text/plain".toMediaTypeOrNull()),
                    image = imagePart
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    courses = _uiState.value.courses + response
                )
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error creating course: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al crear curso: ${e.message}"
                )
            }
        }
    }

    fun updateCourseWithImage(
        context: Context,
        id: Int,
        name: String,
        description: String,
        schedule: String,
        professor: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val imagePart = imageUri?.let { uri ->
                    val file = uri.toFile(context) ?: throw Exception("Error converting image URI to file")
                    MultipartBody.Part.createFormData(
                        "File",
                        file.name,
                        file.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
                val schedulePart = schedule.toRequestBody("text/plain".toMediaTypeOrNull())
                val professorPart = professor.toRequestBody("text/plain".toMediaTypeOrNull())

                val updatedCourse = RetrofitInstance.courseApi.updateCourseWithImage(
                    id = id,
                    name = namePart,
                    description = descriptionPart,
                    schedule = schedulePart,
                    professor = professorPart,
                    image = imagePart
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    courses = _uiState.value.courses.map {
                        if (it.id == id) updatedCourse else it
                    }
                )

            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error updating course with image: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al actualizar curso con imagen: ${e.message}"
                )
            }
        }
    }

//    fun updateCourse(course: Course) {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
//            try {
//                val updatedCourse = RetrofitInstance.courseApi.updateCourse(course.id!!, course)
//
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    courses = _uiState.value.courses.map {
//                        if (it.id == updatedCourse.id) updatedCourse else it
//                    }
//                )
//            } catch (e: Exception) {
//                Log.e("CourseViewModel", "Error updating course: ${e.message}", e)
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    error = "Error al actualizar curso: ${e.message}"
//                )
//            }
//        }
//    }

    fun updateCourse(course: Course) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val request = RetrofitInstance.courseApi.updateCourseWithImage(
                    id = course.id!!,
                    name = course.name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    description = course.description.toRequestBody("text/plain".toMediaTypeOrNull()),
                    schedule = course.schedule.toRequestBody("text/plain".toMediaTypeOrNull()),
                    professor = course.professor.toRequestBody("text/plain".toMediaTypeOrNull()),
                    image = null // No hay nueva imagen
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    courses = _uiState.value.courses.map {
                        if (it.id == request.id) request else it
                    }
                )
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error updating course: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al actualizar curso: ${e.message}"
                )
            }
        }
    }

    fun deleteCourse(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                RetrofitInstance.courseApi.deleteCourse(id)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    courses = _uiState.value.courses.filter { it.id != id }
                )
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error deleting course: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al eliminar curso: ${e.message}"
                )
            }
        }
    }
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}