package com.dkb.codechallenge.api

import com.dkb.codechallenge.models.data.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PhotoService {

    @GET("/photos")
    suspend fun getPhotos(): Response<List<Photo>>

    @GET("/photos/{id}")
    suspend fun getPhoto(@Path("id") id: Long): Response<Photo>
}