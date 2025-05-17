package com.moviles.exam.components

import androidx.compose.foundation.layout.*
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

    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                if (name.isBlank() || email.isBlank() || phone.isBlank()) {
                    errorMessage = "Todos los campos son obligatorios"
                } else {
                    errorMessage = null
                    onSubmit(
                        StudentInputState(
                            name = name,
                            email = email,
                            phone = phone,
                            courseId = courseId
                        )
                    )
                }
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
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