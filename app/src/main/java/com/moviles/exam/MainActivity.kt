package com.moviles.exam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.moviles.exam.pages.course.CourseListScreen
import com.moviles.exam.ui.theme.Examen1_FrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Examen1_FrontendTheme {
                Scaffold { padding ->
                    CourseListScreen(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}