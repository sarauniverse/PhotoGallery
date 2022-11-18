package com.dkb.codechallenge.models.repo

import com.dkb.codechallenge.api.PhotoService
import com.dkb.codechallenge.models.data.Photo
import com.dkb.codechallenge.models.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PhotosRepositoryImpl(
    private val photoService: PhotoService): IPhotosRepository {
    override suspend fun getPhotos(): Flow<Result<out List<Photo>>> = flow {
        try {
            val photos = photoService.getPhotos().body()
            if(photos != null) {
                emit(Result.SUCCESS(photos))
            }
            else {
                emit(Result.ERROR("Error fetching photos", null))
            }
        }
        catch (ex: Exception) {
            emit(Result.ERROR("Exception while fetching the photos", ex))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getPhoto(id: Long): Flow<Result<out Photo>> = flow {
        try {
            val photo = photoService.getPhoto(id).body()
            if(photo != null) {
                emit(Result.SUCCESS(photo))
            }
            else {
                emit(Result.ERROR("Error fetching photos", null))
            }
        }
        catch (ex: Exception) {
            emit(Result.ERROR("Exception while fetching the photos", ex))
        }
    }.flowOn(Dispatchers.IO)
}