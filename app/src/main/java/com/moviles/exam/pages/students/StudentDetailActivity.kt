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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.moviles.exam.viewmodel.StudentViewModel

@Composable
fun StudentDetailScreen(
    studentId: Int,
    courseName: String,
    studentViewModel: StudentViewModel = viewModel()
) {
    val uiState by studentViewModel.uiState.collectAsState()
    val student = uiState.students.find { it.id == studentId }

    LaunchedEffect(studentId) {
        if (student == null) {
            studentViewModel.fetchStudentById(studentId)
        }
    }

    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (student == null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Buscando estudiante...", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
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
