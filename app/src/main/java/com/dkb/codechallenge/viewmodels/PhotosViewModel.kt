package com.dkb.codechallenge.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkb.codechallenge.models.data.Photo
import com.dkb.codechallenge.models.data.Result
import com.dkb.codechallenge.models.repo.IPhotosRepository
import com.dkb.codechallenge.viewmodels.states.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val photosRepository: IPhotosRepository) : ViewModel() {

    private val photos = MutableStateFlow<UIState<List<Photo>>>(UIState.STARTED())

    fun getPhotos(): Flow<UIState<List<Photo>>> {
        return photos
    }

    init {
        viewModelScope.launch {
            photosRepository.getPhotos().flowOn(Dispatchers.IO).collectLatest {
                photos.value = when(it) {
                    is Result.SUCCESS -> UIState.SUCCESS(it.result)
                    is Result.ERROR -> UIState.ERROR(it.errorMessage, it.exception)
                }
            }
        }
    }
}