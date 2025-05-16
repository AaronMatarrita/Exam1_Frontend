package com.moviles.exam.pages.course

import CourseCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
//import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
//import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.moviles.exam.components.CourseFormContent
import com.moviles.exam.models.Course
import androidx.navigation.NavHostController
import com.moviles.exam.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    courseViewModel: CourseViewModel = viewModel()
) {

    val uiState by courseViewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    var showDeleteConfirm by remember { mutableStateOf (false) }

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedCourse by remember { mutableStateOf<Course?>(null) }


    fun handleDelete(course: Course) {
        courseViewModel.deleteCourse(course.id!!)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear curso")
            }
        },
        modifier = modifier
    ) { innerPadding ->
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else if (uiState.error != null) {
            ErrorMessage(error = uiState.error!!)
        //Text("Error: ${uiState.error}", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.error)
        } else {
            LazyColumn(contentPadding = innerPadding) {
                items(uiState.courses) { course ->
                    CourseCard(
                        course = course,
                        onViewStudents = { /* Acción de ver estudiantes */ },
                        //onMoreOptions = { /* Acción para más opciones */ }
                        onEdit = {
                            selectedCourse = course
                            showEditDialog = true
                        },
                        onDelete = {
                            selectedCourse = course
                            showDeleteConfirm = true
                        }
                    )
                }
            }
        }

        if (showEditDialog && selectedCourse != null) {
            AlertDialog(

                onDismissRequest = { showEditDialog = false },
                title = {
                    Text(
                        "Editar Curso",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                text = {
                    CourseFormContent(
                        initialCourse = selectedCourse!!,
                        courseViewModel = courseViewModel,
                        onDismiss = { showEditDialog = false },
                        onSave = { updatedCourse ->
                            courseViewModel.updateCourse(updatedCourse)
                            showEditDialog = false
                        },
                        isEditing = true,
                    )
                },
                confirmButton = {}, // Eliminamos los botones por defecto
                dismissButton = {},
                shape = RoundedCornerShape(12.dp),
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        if (showDeleteConfirm && selectedCourse != null) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("Eliminar curso") },
                text = { Text("¿Estás seguro de que deseas eliminar '${selectedCourse?.name}'? Esta acción no se puede deshacer.") },
                confirmButton = {
                    Button(
                        onClick = {
                            val courseId = selectedCourse?.id ?: return@Button
                            courseViewModel.deleteCourse(courseId)
                            showDeleteConfirm = false
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirm = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
private fun ErrorMessage(error: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Warning,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Error: $error",
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
    }
}