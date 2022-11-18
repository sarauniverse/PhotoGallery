package com.dkb.codechallenge.di

import android.app.Application
import com.dkb.codechallenge.api.PhotoService
import com.dkb.codechallenge.models.repo.IPhotosRepository
import com.dkb.codechallenge.models.repo.PhotosRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhotosModule {
    @Provides
    @Singleton
    fun getPhotosRepository(photoService: PhotoService): IPhotosRepository {
        return PhotosRepositoryImpl(photoService)
    }

    @Provides
    @Singleton
    fun getPhotoService(okHttpClient: OkHttpClient): PhotoService {
        return Retrofit.Builder().apply {
            baseUrl("https://jsonplaceholder.typicode.com/")
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create())
        }.build().create(PhotoService::class.java)
    }

    @Provides
    @Singleton
    fun getHttpClient(app: Application): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(Cache(directory = File(app.cacheDir, "local_http_cache"), maxSize = 50L * 1024L * 1024L))
            .build()
    }
}