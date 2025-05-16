import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import coil.compose.AsyncImage
import com.moviles.exam.common.Constants.IMAGES_BASE_URL
import com.moviles.exam.models.Course

@Composable
fun CourseCard(
    course: Course,
    onViewStudents: () -> Unit,
    onEdit: () -> Unit,       // Nueva función para editar
    onDelete: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Column {
            val imageUrl = course.imageUrl?.let { IMAGES_BASE_URL + it }
                ?: "https://via.placeholder.com/400x300?text=Sin+imagen"

            AsyncImage(
                model = imageUrl,
                contentDescription = "Imagen del curso",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {

                // Título y descripción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = course.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = colors.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = course.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = {showDialog = true},
                        modifier = Modifier.align(Alignment.Top)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Más opciones",
                            tint = colors.onSurfaceVariant
                        )
                    }
                    // DropMenu
                    DropdownMenu(
                        expanded = showDialog,
                        onDismissRequest = { showDialog = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar Curso") },
                            onClick = {
                                onEdit()
                                showDialog = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar Curso") },
                            onClick = {
                                onDelete()
                                showDialog = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Horario y profesor
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DateRange,
                        null,
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        course.schedule,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        null,
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        course.professor,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección de estudiantes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Cantidad de estudiantes",
                            tint = colors.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Estudiantes: ${course.students.size}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurface
                        )
                    }

                    TextButton(
                        onClick = onViewStudents,
                        colors = ButtonDefaults.textButtonColors(contentColor = colors.primary)
                    ) {
                        Text("Ver estudiantes")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.List, null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}