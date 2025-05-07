package com.moviles.exam.pages.course

import CourseCard
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moviles.exam.models.Course

@Composable
fun CourseListScreen(modifier: Modifier = Modifier) {
    val mockCourses = listOf(
        Course(
            id = 1,
            name = "Matemáticas",
            description = "Álgebra y geometría",
            imageUrl = "https://images.pexels.com/photos/4145196/pexels-photo-4145196.jpeg",
            schedule = "Lunes 8am",
            professorName = "Carlos Pérez"
        ),
        Course(
            id = 2,
            name = "Programación",
            description = "Kotlin desde cero",
            imageUrl = "https://images.pexels.com/photos/1181671/pexels-photo-1181671.jpeg",
            schedule = "Martes 10am",
            professorName = "Ana Torres"
        ),
        Course(
            id = 3,
            name = "Historia",
            description = "Historia Universal",
            imageUrl = "https://images.pexels.com/photos/256417/pexels-photo-256417.jpeg",
            schedule = "Viernes 2pm",
            professorName = "Luis Mora"
        )
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
                    onViewStudents = { /* Acción de ver estudiantes */ },
                    onMoreOptions = { /* Acción para más opciones */ }
                )
            }
        }
    }
}