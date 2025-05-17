package com.moviles.exam.components

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moviles.exam.pages.course.CourseListScreen
import com.moviles.exam.viewmodel.CourseViewModel

//@Composable
//fun AppNavigation() {
//    val navController = rememberNavController()
//    val courseViewModel: CourseViewModel = viewModel()
//
//    NavHost(
//        navController = navController,
//        startDestination = "courseList"
//    ) {
//        composable("courseList") {
//            CourseListScreen(
//                navController = navController,
//                courseViewModel = courseViewModel
//            )
//        }
//        composable("courseForm") {
//            CourseFormContent(
//                courseViewModel = courseViewModel,
//                onBack = { navController.popBackStack() }
//            )
//        }
//    }
//}