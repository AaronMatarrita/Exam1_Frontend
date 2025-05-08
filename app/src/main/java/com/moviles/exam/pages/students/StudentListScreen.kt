package com.moviles.exam.pages.students

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.moviles.exam.components.StudentCard
import com.moviles.exam.components.StudentFormDialog
import com.moviles.exam.models.Student
import com.moviles.exam.viewmodel.StudentViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    courseId: Int,
    courseName: String,
    modifier: Modifier = Modifier,
    navController: NavController,
    studentViewModel: StudentViewModel = viewModel()
) {
    val uiState by studentViewModel.uiState.collectAsState()
    val students = uiState.students.filter { it.courseId == courseId }

    var showDialog by remember { mutableStateOf(false) }
    var studentToEdit by remember { mutableStateOf<Student?>(null) }

    LaunchedEffect(courseId) {
        studentViewModel.fetchStudentsByCourse(courseId)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar estudiante")
            }
        },
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Estudiantes de $courseName") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("courses") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(
                items = students,
                key = { it.id ?: it.hashCode() }
            ) { student ->
                StudentCard(
                    student = student,
                    onEdit = {
                        studentToEdit = student
                        showDialog = true
                    },
                    onDelete = {
                        student.id?.let { id ->
                            studentViewModel.deleteStudent(id, courseId)
                            navController.popBackStack()
                            navController.navigate("students/${courseId}/${courseName}")
                        }
                    },
                    onClick = {
                        navController.navigate("studentDetail/${student.id}/${courseName}")
                    }
                )
            }
        }
    }

    if (showDialog) {
        StudentFormDialog(
            existingStudent = studentToEdit,
            courseId = courseId,
            onDismiss = {
                showDialog = false
                studentToEdit = null
            },
            onSubmit = { input ->
                val student = Student(
                    id = studentToEdit?.id,
                    name = input.name,
                    email = input.email,
                    phone = input.phone,
                    courseId = input.courseId
                )

                if (student.id == null) {
                    studentViewModel.addStudent(student)
                } else {
                    studentViewModel.updateStudent(student)
                }

                showDialog = false
                studentToEdit = null
            }
        )
    }
}
