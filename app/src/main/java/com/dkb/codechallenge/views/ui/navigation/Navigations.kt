package com.dkb.codechallenge.views.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dkb.codechallenge.viewmodels.PhotoViewModel
import com.dkb.codechallenge.viewmodels.PhotosViewModel
import com.dkb.codechallenge.views.ui.screens.PhotoDetails
import com.dkb.codechallenge.views.ui.screens.Photos
import com.dkb.codechallenge.views.ui.screens.Screen

@Composable
fun Navigation(photosViewModel: PhotosViewModel, photoViewModel: PhotoViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Photos.route) {
        composable(route = Screen.Photos.route) {
            Photos(photosViewModel = photosViewModel) {
                navController.navigate(Screen.PhotoDetails.route.substituteParams(mapOf("Id" to it)))
            }
        }
        composable(route = Screen.PhotoDetails.route, arguments = listOf(
            navArgument("Id") {
                type = NavType.LongType
            }
        )) {
            PhotoDetails(photoViewModel = photoViewModel, photoId = it.arguments?.getLong("Id") ?: -1L)
        }
    }
}

fun String.substituteParams(paramMap: Map<String, Any>): String {
    var replacedString = this
    paramMap.forEach {
        replacedString = replacedString.replace("{${it.key}}", "${it.value}")
    }
    return replacedString
}