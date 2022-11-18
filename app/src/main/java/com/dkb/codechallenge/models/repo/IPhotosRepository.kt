package com.dkb.codechallenge.models.repo

import com.dkb.codechallenge.models.data.Photo
import com.dkb.codechallenge.models.data.Result
import kotlinx.coroutines.flow.Flow

interface IPhotosRepository {
    suspend fun getPhotos(): Flow<Result<out List<Photo>>>
    suspend fun getPhoto(id: Long): Flow<Result<out Photo>>
}