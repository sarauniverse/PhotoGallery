package com.dkb.codechallenge.views.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.dkb.codechallenge.R
import com.dkb.codechallenge.models.data.Photo
import com.dkb.codechallenge.viewmodels.PhotosViewModel
import com.dkb.codechallenge.viewmodels.states.UIState
import com.dkb.codechallenge.views.ui.theme.DKBChallengeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Photos(photosViewModel: PhotosViewModel, onClick: (Long) -> Unit) {
    DKBChallengeTheme {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.main_appbar_title), color = MaterialTheme.colorScheme.background) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ))
        }, modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = MaterialTheme.colorScheme.background
            ) {
                val uiState =  remember {
                    photosViewModel.getPhotos()
                }.collectAsState(initial = UIState.STARTED()).value

                when(uiState) {
                    is UIState.SUCCESS<List<Photo>> -> {
                        PhotoGrid(photos = uiState.obj, onClick)
                    }
                    else -> Unit
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoGrid(photos: List<Photo>, onClick: (Long) -> Unit) {
    val state = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 96.dp), state = state
    ) {
        items(items = photos, key = { it.id }) { photo ->
            PhotoThumbnail(photo, onClick)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoThumbnail(photo: Photo, onClick:(Long) -> Unit) {
    val headers = remember {
        LazyHeaders.Builder().addHeader("User-Agent", "Android").build()
    }
    val glideUrl = remember {
        GlideUrl(photo.thumbnailUrl, headers)
    }
    GlideImage(
        model = photo.thumbnailUrl,
        contentDescription = photo.title,
        modifier = Modifier
            .height(96.dp)
            .width(96.dp)
            .padding(1.dp)
            .clickable {
                onClick(photo.id)
            }
    ) {
        it.load(glideUrl)
            .centerCrop()
            .placeholder(R.drawable.gray_bg)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    }
}