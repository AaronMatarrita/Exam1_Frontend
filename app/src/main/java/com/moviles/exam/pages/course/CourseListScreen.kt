package com.moviles.exam.pages.course

import CourseCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moviles.exam.viewmodel.CourseViewModel

@Composable
fun CourseListScreen(
    modifier: Modifier = Modifier,
    courseViewModel: CourseViewModel = viewModel()
) {
    val uiState by courseViewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Navegar a formulario */ }) {
                Icon(Icons.Default.Add, contentDescription = "Crear curso")
            }
        },
        modifier = modifier
    ) { innerPadding ->
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else if (uiState.error != null) {
            Text("Error: ${uiState.error}", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.error)
        } else {
            LazyColumn(contentPadding = innerPadding) {
                items(uiState.courses) { course ->
                    CourseCard(
                        course = course,
                        onViewStudents = { /* Acción de ver estudiantes */ },
                        onMoreOptions = { /* Acción para más opciones */ }
                    )
                }
            }
        }
    }
}