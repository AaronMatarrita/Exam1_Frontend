package com.moviles.exam.pages.students

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moviles.exam.viewmodel.StudentViewModel
import kotlinx.coroutines.launch

@Composable
fun StudentDetailScreen(
    studentId: Int,
    courseName: String,
    studentViewModel: StudentViewModel = viewModel()
) {
    val uiState by studentViewModel.uiState.collectAsState()
    val student = uiState.students.find { it.id == studentId }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(studentId) {
        if (student == null) {
            studentViewModel.fetchStudentById(studentId)
        }
    }

    LaunchedEffect(uiState.dataOrigin) {
        when {
            uiState.dataOrigin == "Local" && student != null -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Detalles del estudiante cargados desde el almacenamiento local.",
                        duration = SnackbarDuration.Short
                    )
                }
            }
            uiState.dataOrigin == "API" && student != null -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Detalles del estudiante cargados desde la API.",
                        duration = SnackbarDuration.Short
                    )
                }
            }
            uiState.dataOrigin.contains("Local (sin datos)") && student == null && !uiState.isLoading -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "No se encontraron los detalles del estudiante en local. Verificando conexión...",
                        duration = SnackbarDuration.Short
                    )
                }
            }
            uiState.dataOrigin.contains("Local (con error de API)") && student != null -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Mostrando detalles locales debido a un problema con la API.",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    val colors = MaterialTheme.colorScheme

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp),
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Buscando estudiante...", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                student == null && uiState.error != null -> {
                    Text(text = uiState.error!!, color = colors.error)
                }
                student != null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, colors.outlineVariant),
                        colors = CardDefaults.cardColors(containerColor = colors.surface)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = student.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = colors.onSurface
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Email, contentDescription = null, tint = colors.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(student.email, style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = colors.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(student.phone, style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = colors.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Curso: $courseName",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colors.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}