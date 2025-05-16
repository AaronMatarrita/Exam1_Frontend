package com.moviles.exam.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.exam.data.DatabaseProvider
import com.moviles.exam.models.Student
import com.moviles.exam.models.toEntity
import com.moviles.exam.models.toModel
import com.moviles.exam.models.toUnsyncedAddEntity
import com.moviles.exam.network.RetrofitInstance
import com.moviles.exam.sync.StudentSyncManager
import com.moviles.exam.utils.NetworkUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StudentUiState(
    val isLoading: Boolean = false,
    val students: List<Student> = emptyList(),
    val error: String? = null,
    val dataOrigin: String = "API",
    val isSyncing: Boolean = false
)

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(StudentUiState())
    val uiState: StateFlow<StudentUiState> = _uiState

    private val studentApi = RetrofitInstance.studentApi
    private val studentDao = DatabaseProvider.getDatabase(application).studentDao()
    private val syncManager = StudentSyncManager(studentApi, studentDao)

    private val _isNetworkAvailable = MutableStateFlow(NetworkUtils.isNetworkAvailable(application))

    init {
        RetrofitInstance.initCache(application)
        monitorNetworkConnectivity()
    }

    private fun monitorNetworkConnectivity() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                val isNetwork = NetworkUtils.isNetworkAvailable(getApplication())
                if (isNetwork != _isNetworkAvailable.value) {
                    _isNetworkAvailable.value = isNetwork
                    if (isNetwork) {
                        syncLocalChanges()
                        // Refresh data from API when connection is restored
                        _uiState.value.students.firstOrNull()?.courseId?.let { fetchStudentsByCourse(it) }
                    } else {
                        // Refresh data from local DB when connection is lost
                        _uiState.value.students.firstOrNull()?.courseId?.let { fetchStudentsByCourse(it) }
                    }
                }
            }
        }
    }

    private suspend fun syncLocalChanges() {
        if (_uiState.value.isSyncing) return
        _uiState.update { it.copy(isSyncing = true) }
        try {
            syncManager.syncLocalChanges()
            _uiState.update { it.copy(isSyncing = false) }
            Log.d("StudentViewModel", "Sync completed.")
        } catch (e: Exception) {
            _uiState.update { it.copy(isSyncing = false, error = e.localizedMessage) }
            Log.e("StudentViewModel", "Sync error: ${e.localizedMessage}")
        }
    }

    fun fetchStudentsByCourse(courseId: Int) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val students = if (NetworkUtils.isNetworkAvailable(getApplication())) {
                    val apiData = studentApi.getAllStudentsByCourse(courseId)
                    studentDao.insertAll(apiData.map { it.toEntity().copy(isSynced = true) })
                    apiData
                } else {
                    studentDao.getStudentsByCourse(courseId).map { it.toModel() }
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        students = students.distinctBy { it.id },
                        error = students.isEmpty().takeIf { !NetworkUtils.isNetworkAvailable(getApplication()) }?.let { "No offline data." },
                        dataOrigin = if (NetworkUtils.isNetworkAvailable(getApplication())) "API" else "Local"
                    )
                }
            } catch (e: Exception) {
                Log.e("StudentViewModel", "Fetch students error (courseId: $courseId): ${e.localizedMessage ?: "Unknown"}")
                val localData = studentDao.getStudentsByCourse(courseId).map { it.toModel() }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        students = localData.distinctBy { it.id },
                        error = "Error loading students.",
                        dataOrigin = "Local"
                    )
                }
            }
        }
    }

    fun fetchStudentById(id: Int) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val student = if (NetworkUtils.isNetworkAvailable(getApplication())) {
                    val apiData = studentApi.getStudentById(id)
                    studentDao.insert(apiData.toEntity().copy(isSynced = true))
                    apiData
                } else {
                    studentDao.getStudentById(id)?.toModel()
                }
                student?.let { fetchedStudent ->
                    _uiState.update { it.copy(isLoading = false, students = listOf(fetchedStudent), error = null, dataOrigin = if (NetworkUtils.isNetworkAvailable(getApplication())) "API" else "Local") }
                } ?: _uiState.update { it.copy(isLoading = false, error = "Student not found.", dataOrigin = if (NetworkUtils.isNetworkAvailable(getApplication())) "API" else "Local") }
            } catch (e: Exception) {
                Log.e("StudentViewModel", "Fetch student error (ID: $id): ${e.localizedMessage ?: "Unknown"}")
                studentDao.getStudentById(id)?.toModel()?.let { localStudent ->
                    _uiState.update { it.copy(isLoading = false, students = listOf(localStudent), error = "Error loading student.", dataOrigin = "Local") }
                } ?: _uiState.update { it.copy(isLoading = false, error = "Error loading student.", dataOrigin = "Local") }
            }
        }
    }

    fun addStudent(student: Student) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val unsyncedEntity = student.toUnsyncedAddEntity()
                val localId = studentDao.insert(unsyncedEntity)
                if (NetworkUtils.isNetworkAvailable(getApplication())) {
                    try {
                        val newStudent = studentApi.registerStudent(student)
                        newStudent.id?.let { serverId ->
                            val updatedEntity = unsyncedEntity.copy(serverId = serverId, isSynced = true, id = localId.toInt())
                            studentDao.insert(updatedEntity)
                            fetchStudentsByCourse(student.courseId)
                        } ?: run {
                            Log.e("StudentViewModel", "Server ID null on add.")
                            _uiState.update { it.copy(isLoading = false, error = "Add error.") }
                            fetchStudentsByCourse(student.courseId)
                        }
                    } catch (e: Exception) {
                        Log.e("StudentViewModel", "Server add error: ${e.localizedMessage}")
                        _uiState.update { it.copy(isLoading = false, error = "Add error.") }
                        fetchStudentsByCourse(student.courseId)
                    }
                } else {
                    fetchStudentsByCourse(student.courseId)
                }
            } catch (e: Exception) {
                Log.e("StudentViewModel", "Local add error: ${e.localizedMessage ?: "Unknown"}")
                _uiState.update { it.copy(isLoading = false, error = "Add error.") }
            }
        }
    }


    fun updateStudent(student: Student) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            studentDao.getStudentById(student.id ?: 0)?.let { existingLocal ->
                try {
                    val updatedEntity = student.toEntity().copy(isSynced = false, isPendingUpdate = true, id = existingLocal.id)
                    studentDao.insert(updatedEntity)
                    if (NetworkUtils.isNetworkAvailable(getApplication())) {
                        existingLocal.serverId?.let { serverId ->
                            try {
                                val updatedStudent = studentApi.updateStudent(serverId, student)
                                studentDao.insert(updatedStudent.toEntity().copy(isSynced = true, isPendingDelete = false, isPendingUpdate = false, id = existingLocal.id))
                                fetchStudentsByCourse(student.courseId)
                            } catch (e: Exception) {
                                Log.e("StudentViewModel", "Server update error: ${e.localizedMessage}")
                                _uiState.update { it.copy(isLoading = false, error = "Error updating on server. Will retry.") }
                                fetchStudentsByCourse(student.courseId)
                            }
                        } ?: run {
                            studentDao.insert(student.toEntity().copy(id = existingLocal.id))
                            fetchStudentsByCourse(student.courseId)
                        }
                    } else {
                        fetchStudentsByCourse(student.courseId)
                    }
                } catch (e: Exception) {
                    Log.e("StudentViewModel", "Local update error: ${e.localizedMessage ?: "Unknown"}")
                    _uiState.update { it.copy(isLoading = false, error = "Error updating locally.") }
                }
            } ?: _uiState.update { it.copy(isLoading = false, error = "Student not found for update.") }
        }
    }

    fun deleteStudent(studentId: Int, courseId: Int) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            studentDao.getStudentById(studentId)?.let { existingLocal ->
                try {
                    if (NetworkUtils.isNetworkAvailable(getApplication())) {
                        existingLocal.serverId?.let { serverId ->
                            try {
                                studentApi.deleteStudent(serverId)
                                studentDao.deleteStudentById(studentId)
                                _uiState.update { currentState -> currentState.copy(students = currentState.students.filter { it.id != studentId }, isLoading = false, error = null) }
                            } catch (e: Exception) {
                                Log.e("StudentViewModel", "Server delete error: ${e.localizedMessage}")
                                _uiState.update { it.copy(isLoading = false, error = "Delete error.") }
                            }
                        } ?: run {
                            studentDao.deleteStudentById(studentId)
                            _uiState.update { currentState -> currentState.copy(students = currentState.students.filter { it.id != studentId }, isLoading = false, error = null) }
                        }
                    } else {
                        studentDao.insert(existingLocal.copy(isPendingDelete = true, isSynced = false))
                        _uiState.update { currentState -> currentState.copy(students = currentState.students.filter { it.id != studentId }, isLoading = false, error = null) }
                    }
                } catch (e: Exception) {
                    Log.e("StudentViewModel", "Delete mark error: ${e.localizedMessage ?: "Unknown"}")
                    _uiState.update { it.copy(isLoading = false, error = "Delete error.") }
                }
            } ?: _uiState.update { it.copy(isLoading = false, error = "Student not found.") }
        }
    }
}