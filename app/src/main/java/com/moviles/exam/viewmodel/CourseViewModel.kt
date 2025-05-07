package com.moviles.exam.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.exam.models.Course
import com.moviles.exam.models.Student
import com.moviles.exam.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    fun getStudentsForCourse(courseId: Int): List<Student> {
        return _uiState.value.courses.firstOrNull { it.id == courseId }?.students ?: emptyList()
    }

    fun submitCourse(
        file: MultipartBody.Part,
        fields: Map<String, @JvmSuppressWildcards RequestBody>,
        isEdit: Boolean,
        courseId: Int? = null,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (isEdit && courseId != null) {
                    RetrofitInstance.courseApi.updateCourse(courseId, fields, file)
                } else {
                    RetrofitInstance.courseApi.createCourse(fields, file)
                }
                fetchCourses()
                onSuccess()
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error submitting course: ${e.message}")
                onError(e.message ?: "Error desconocido")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}