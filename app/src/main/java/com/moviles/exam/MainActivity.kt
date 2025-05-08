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
import com.moviles.exam.pages.students.StudentDetailScreen
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
            "students/{courseId}/{courseName}",
            arguments = listOf(
                navArgument("courseId") { type = NavType.IntType },
                navArgument("courseName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
            val courseName = backStackEntry.arguments?.getString("courseName") ?: ""
            Scaffold { padding ->
                StudentListScreen(
                    courseId = courseId,
                    courseName = courseName,
                    modifier = Modifier.padding(padding),
                    navController = navController,
                )
            }
        }
        composable(
            "studentDetail/{studentId}/{courseName}",
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType },
                navArgument("courseName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1
            val courseName = backStackEntry.arguments?.getString("courseName") ?: "Desconocido"

            StudentDetailScreen(studentId = studentId, courseName = courseName)
        }
    }
}