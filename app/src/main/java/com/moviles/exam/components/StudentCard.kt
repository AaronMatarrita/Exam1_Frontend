package com.moviles.exam.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.moviles.exam.models.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCard(
    student: Student,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    var isHovered by remember { mutableStateOf(false) }

    // Animaciones
    val elevation by animateFloatAsState(
        targetValue = if (isHovered) 4f else 1f,
        animationSpec = tween(durationMillis = 200)
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isHovered) colors.surfaceVariant.copy(alpha = 0.5f) else colors.surface,
        animationSpec = tween(durationMillis = 200)
    )

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .semantics {
                contentDescription = "Tarjeta del estudiante ${student.name}"
            },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colors.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
        onClick = { isHovered = !isHovered }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar del estudiante
            StudentAvatar(student = student)

            Spacer(modifier = Modifier.width(16.dp))

            // Información del estudiante
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = student.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = student.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }

            // Botones de acción
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    icon = Icons.Default.Edit,
                    contentDescription = "Editar estudiante",
                    color = colors.primary,
                    onClick = onEdit
                )

                Spacer(modifier = Modifier.width(8.dp))

                ActionButton(
                    icon = Icons.Default.Delete,
                    contentDescription = "Eliminar estudiante",
                    color = colors.error,
                    onClick = onDelete
                )
            }
        }
    }
}

@Composable
private fun StudentAvatar(student: Student) {
    val colors = MaterialTheme.colorScheme
    val initials = student.name.split(" ")
        .take(2)
        .joinToString("") { it.take(1).uppercase() }

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(getAvatarColor(student.id.toString()))
            .semantics {
                contentDescription = "Avatar de ${student.name}"
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    contentDescription: String,
    color: Color,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    IconButton(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .scale(scale)
            .size(40.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = color
        )
    }

    // Restablecer el estado después de la animación
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

// Función para generar un color consistente basado en el ID del estudiante
private fun getAvatarColor(id: String): Color {
    val colors = listOf(
        Color(0xFF5C6BC0), // Indigo
        Color(0xFF26A69A), // Teal
        Color(0xFFEF5350), // Red
        Color(0xFF66BB6A), // Green
        Color(0xFFFFCA28), // Amber
        Color(0xFF42A5F5), // Blue
        Color(0xFFAB47BC), // Purple
        Color(0xFF8D6E63), // Brown
        Color(0xFF78909C)  // Blue Grey
    )

    // Usar el hashCode del ID para seleccionar un color consistente
    val index = id.hashCode().absoluteValue % colors.size
    return colors[index]
}

// Extensión para obtener el valor absoluto de Int
private val Int.absoluteValue: Int
    get() = if (this < 0) -this else this
