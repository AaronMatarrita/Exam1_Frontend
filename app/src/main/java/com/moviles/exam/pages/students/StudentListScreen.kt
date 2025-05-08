package com.moviles.exam.pages.students

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.moviles.exam.components.StudentCard
import com.moviles.exam.components.StudentFormDialog
import com.moviles.exam.models.Student
import com.moviles.exam.viewmodel.CourseViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    courseId: Int,
    modifier: Modifier = Modifier,
    navController: NavController,
    courseViewModel: CourseViewModel = viewModel()
) {
    val students = remember(courseId, courseViewModel.uiState.value) {
        courseViewModel.getStudentsForCourse(courseId)
    }
    var showDialog by remember { mutableStateOf(false) }
    var studentToEdit by remember { mutableStateOf<Student?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar estudiante")
            }
        },
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Estudiantes del curso $courseId") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("courses") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(students) { student ->
                StudentCard(
                    student = student,
                    onEdit = { /* Acción de editar */ },
                    onDelete = { /* Acción de eliminar */ }
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
            onSubmit = {

            }
        )
    }

}
