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
import androidx.navigation.NavHostController
import com.moviles.exam.components.CourseFormDialog
import com.moviles.exam.viewmodel.CourseViewModel

@Composable
fun CourseListScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    courseViewModel: CourseViewModel = viewModel()
) {
    val uiState by courseViewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
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
                        onViewStudents = {
                            navController.navigate("students/${course.id}")
                        },
                        onMoreOptions = { /* Acción para más opciones */ }
                    )
                }
            }
        }

        if (showDialog) {
            CourseFormDialog(
                onDismiss = { showDialog = false },
                onSubmit = { input, imageUri ->
                    showDialog = false
                }
            )
        }
    }
}