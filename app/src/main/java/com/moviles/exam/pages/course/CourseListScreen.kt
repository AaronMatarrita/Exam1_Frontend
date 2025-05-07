package com.moviles.exam.pages.course

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moviles.exam.components.CourseCard
import com.moviles.exam.models.Course

@Composable
fun CourseListScreen(modifier: Modifier = Modifier) {
    val mockCourses = listOf(
        Course(1, "Matemáticas", "Álgebra y geometría", "", "Lunes 8am", "Carlos Pérez"),
        Course(2, "Programación", "Kotlin desde cero", "", "Martes 10am", "Ana Torres"),
        Course(3, "Historia", "Historia Universal", "", "Viernes 2pm", "Luis Mora"),
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Navegar a formulario */ }) {
                Icon(Icons.Default.Add, contentDescription = "Crear curso")
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(mockCourses) { course ->
                CourseCard(
                    course = course,
                    onEdit = { /* TODO */ },
                    onDelete = { /* TODO */ }
                )
            }
        }
    }
}