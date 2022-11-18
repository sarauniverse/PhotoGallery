package com.dkb.codechallenge.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dkb.codechallenge.viewmodels.PhotoViewModel
import com.dkb.codechallenge.viewmodels.PhotosViewModel
import com.dkb.codechallenge.views.ui.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val photosViewModel by viewModels<PhotosViewModel>()
    private val photoViewModel by viewModels<PhotoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation(photosViewModel = photosViewModel, photoViewModel = photoViewModel)
        }
    }
}