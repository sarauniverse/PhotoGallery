package com.dkb.codechallenge.views.ui.screens

sealed class Screen(val route: String) {
    object Photos: Screen("photos")
    object PhotoDetails: Screen("photos/{Id}")
}