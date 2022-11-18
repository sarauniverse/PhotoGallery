package com.dkb.codechallenge.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkb.codechallenge.models.data.Photo
import com.dkb.codechallenge.models.data.Result
import com.dkb.codechallenge.models.repo.IPhotosRepository
import com.dkb.codechallenge.viewmodels.states.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val photosRepository: IPhotosRepository): ViewModel() {

    private val photo = MutableStateFlow<UIState<Photo>>(UIState.STARTED())

    fun getPhoto(id: Long): SharedFlow<UIState<Photo>> {
        photo.value = UIState.STARTED()
        viewModelScope.launch {
            photosRepository.getPhoto(id).flowOn(Dispatchers.IO).collectLatest {
                photo.value = when(it) {
                    is Result.SUCCESS -> UIState.SUCCESS(it.result)
                    is Result.ERROR -> UIState.ERROR(it.errorMessage, it.exception)
                }
            }
        }
        return photo
    }

}