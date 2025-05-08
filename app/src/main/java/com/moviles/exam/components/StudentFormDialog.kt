package com.moviles.exam.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.moviles.exam.models.Student

@Composable
fun StudentFormDialog(
    onDismiss: () -> Unit,
    onSubmit: (StudentInputState) -> Unit,
    courseId: Int,
    existingStudent: Student? = null
) {
    var name by remember { mutableStateOf(existingStudent?.name ?: "") }
    var email by remember { mutableStateOf(existingStudent?.email ?: "") }
    var phone by remember { mutableStateOf(existingStudent?.phone ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onSubmit(
                    StudentInputState(
                        name = name,
                        email = email,
                        phone = phone,
                        courseId = courseId
                    )
                )
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        },
        title = {
            Text(
                text = if (existingStudent == null) "Crear Estudiante" else "Editar Estudiante",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}

data class StudentInputState(
    val name: String,
    val email: String,
    val phone: String,
    val courseId: Int
)