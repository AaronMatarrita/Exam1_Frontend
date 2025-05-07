package com.moviles.exam.pages.students

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.moviles.exam.components.StudentCard
import com.moviles.exam.models.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    courseId: Int,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    // Datos quemados de ejemplo
    val sampleStudents = listOf(
        Student(id = 1, name = "Aaron Matarrita", email = "aaron@gmail.com", phone = "60900909", courseId = courseId),
        Student(id = 2, name = "Andrea Rodríguez", email = "andrea@gmail.com", phone = "60800808", courseId = courseId)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estudiantes del curso $courseId") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("courses") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(sampleStudents) { student ->
                StudentCard(
                    student = student,
                    onEdit = { /* Acción de editar */ },
                    onDelete = { /* Acción de eliminar */ }
                )
            }
        }
    }
}