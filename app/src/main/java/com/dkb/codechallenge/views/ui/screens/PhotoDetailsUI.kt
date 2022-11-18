package com.dkb.codechallenge.views.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.dkb.codechallenge.models.data.Photo
import com.dkb.codechallenge.viewmodels.PhotoViewModel
import com.dkb.codechallenge.viewmodels.states.UIState
import com.dkb.codechallenge.views.ui.theme.DKBChallengeTheme


@Composable
fun PhotoDetails(photoViewModel: PhotoViewModel, photoId: Long) {
    DKBChallengeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val uiState = remember {
                photoViewModel.getPhoto(photoId)
            }.collectAsState(initial = UIState.STARTED()).value

            when(uiState) {
                is UIState.SUCCESS<Photo> -> {
                    LargePhoto(photo = uiState.obj)
                }
                else -> Unit
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LargePhoto(photo: Photo) {
    val headers = remember {
        LazyHeaders.Builder().addHeader("User-Agent", "Android").build()
    }
    val glideUrl = remember {
        GlideUrl(photo.url, headers)
    }
    GlideImage(
        model = photo.url,
        contentDescription = photo.title,
        modifier = Modifier.fillMaxSize()
    ) {
        it.load(glideUrl)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    }
}