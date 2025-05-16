package com.moviles.exam.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.exam.models.Student
import com.moviles.exam.network.RetrofitInstance
import com.moviles.exam.network.RetrofitInstance.studentApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StudentUiState(
    val isLoading: Boolean = false,
    val students: List<Student> = emptyList(),
    val error: String? = null
)

class StudentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StudentUiState())
    val uiState: StateFlow<StudentUiState> get() = _uiState

    fun fetchStudentById(id: Int) {
        viewModelScope.launch {
            try {
                val student = studentApi.getStudentById(id)
                _uiState.update { current ->
                    // Evita duplicados
                    if (current.students.none { it.id == student.id }) {
                        current.copy(students = current.students + student)
                    } else {
                        current
                    }
                }
            } catch (e: Exception) {
                // Maneja el error apropiadamente (log o UI)
                e.printStackTrace()
            }
        }
    }

    fun fetchStudents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val students = RetrofitInstance.studentApi.getAllStudents()
                _uiState.value = _uiState.value.copy(isLoading = false, students = students)
            } catch (e: Exception) {
                Log.e("StudentViewModel", "Error fetching students: ${e.message}", e)
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun fetchStudentsByCourse(courseId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val students = RetrofitInstance.studentApi.getAllStudentsByCourse(courseId)
                _uiState.value = _uiState.value.copy(isLoading = false, students = students)
            } catch (e: Exception) {
                Log.e("StudentViewModel", "Error fetching students by course: ${e.message}", e)
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun addStudent(student: Student) {
        viewModelScope.launch {
            try {
                RetrofitInstance.studentApi.registerStudent(student)
                fetchStudentsByCourse(student.courseId)
            } catch (e: Exception) {
                Log.e("StudentViewModel", "Error adding student: ${e.message}", e)
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateStudent(student: Student) {
        student.id?.let { id ->
            viewModelScope.launch {
                try {
                    RetrofitInstance.studentApi.updateStudent(id, student)
                    fetchStudentsByCourse(student.courseId)
                } catch (e: Exception) {
                    Log.e("StudentViewModel", "Error updating student: ${e.message}", e)
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
            }
        }
    }

    fun deleteStudent(id: Int, courseId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null) // Indica que se está cargando
            try {
                RetrofitInstance.studentApi.deleteStudent(id)

                val updatedStudents = RetrofitInstance.studentApi.getAllStudentsByCourse(courseId)
                _uiState.value = _uiState.value.copy(isLoading = false, students = updatedStudents)
            } catch (e: Exception) {
                Log.e("StudentViewModel", "Error deleting student: ${e.message}", e)
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }


    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun getStudentsByCourse(courseId: Int): List<Student> {
        return _uiState.value.students.filter { it.courseId == courseId }
    }
}
