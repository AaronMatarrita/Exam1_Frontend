package com.moviles.exam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.moviles.exam.pages.course.CourseListScreen
import com.moviles.exam.pages.students.StudentListScreen
import com.moviles.exam.ui.theme.Examen1_FrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Examen1_FrontendTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "courses"
    ) {
        composable("courses") {
            Scaffold { padding ->
                CourseListScreen(
                    navController = navController,
                    modifier = Modifier.padding(padding)
                )
            }
        }
        composable(
            "students/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
            Scaffold { padding ->
                StudentListScreen(
                    courseId = courseId,
                    modifier = Modifier.padding(padding),
                    navController = navController,
                )
            }
        }
    }
}