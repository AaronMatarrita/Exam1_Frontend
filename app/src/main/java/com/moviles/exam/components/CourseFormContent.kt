package com.moviles.exam.components

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.moviles.exam.models.Course
import com.moviles.exam.utils.toUri
import com.moviles.exam.viewmodel.CourseViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CourseFormContent(
    context: Context = LocalContext.current,
    courseViewModel: CourseViewModel,
    onDismiss: () -> Unit,
    initialCourse: Course,
    onSave: (Course) -> Unit,
    isEditing: Boolean = false,
    isLoading: Boolean = false,
) {
    var name by remember { mutableStateOf(initialCourse.name)  }
    var description by remember { mutableStateOf(initialCourse.description) }
    var schedule by remember { mutableStateOf(initialCourse.schedule) }
    var professor by remember { mutableStateOf(initialCourse.professor) }
    //var imageUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    //val context = LocalContext.current



    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Field: Nombre
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre del curso *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = name.isBlank()
        )

        // Field: Descripción
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = description.isBlank()
        )

        // Field: Horario
        OutlinedTextField(
            value = schedule,
            onValueChange = { schedule = it },
            label = { Text("Horario *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = schedule.isBlank()
        )

        // Field: Profesor
        OutlinedTextField(
            value = professor,
            onValueChange = { professor = it },
            label = { Text("Profesor *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = professor.isBlank()
        )

        // Field: Imagen (opcional)
        Button(
            onClick = { imagePicker.launch("image/*")},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar imagen")
        }

        selectedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(uri)
                        .build()
                ),
                contentDescription = "Imagen del curso seleccionada",
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp)
            )
        } ?: run {
            Box(modifier = Modifier.size(150.dp), contentAlignment = Alignment.Center) {
                Text("No hay imagen seleccionada")
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Save Button
        Button(
            onClick = {
                if (name.isNotBlank() && description.isNotBlank() &&
                    schedule.isNotBlank() && professor.isNotBlank()
                ) {
                    val updatedCourse = initialCourse.copy(
                        id = checkNotNull(initialCourse.id) { "El curso debe tener ID para ser editado" },
                        name = name,
                        description = description,
                        schedule = schedule,
                        professor = professor,
                        imageUrl = selectedImageUri.toString() ?: initialCourse.imageUrl
                    )
                    if (isEditing) {
                        if(selectedImageUri!=null){
                            courseViewModel.updateCourseWithImage(
                                context = context,
                                id = initialCourse.id!!,
                                name = name,
                                description = description,
                                schedule = schedule,
                                professor = professor,
                                imageUri = selectedImageUri
                            )
                        } else {
                            courseViewModel.updateCourse(updatedCourse)
                        }

                    } else {
                        courseViewModel.createCourseWithImage(
                            context = context,
                            name = name,
                            description = description,
                            schedule = schedule,
                            professor = professor,
                            imageUri = selectedImageUri
                        )
                    }

                    onDismiss()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && description.isNotBlank() &&
                    schedule.isNotBlank() && professor.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(if(isEditing)"Guardar cambios" else "Crear curso")
            }
        }
    }
}