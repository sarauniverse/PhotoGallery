package com.dkb.codechallenge.viewmodels

import com.dkb.codechallenge.models.data.Photo
import com.dkb.codechallenge.models.data.Result
import com.dkb.codechallenge.models.repo.IPhotosRepository
import com.dkb.codechallenge.viewmodels.states.UIState
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotosViewModelTest {
    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Mock
    private lateinit var photoRepository: IPhotosRepository

    @Test
    fun startState_Success_Test(): Unit = runBlocking {
        whenever(photoRepository.getPhotos()).thenReturn(flow {
            emit(Result.SUCCESS(
                listOf(Photo(title = "",
                    albumId = 1,
                    id = 1,
                    thumbnailUrl = "",
                    url = "")
                )))
        })
        launch (Dispatchers.Main) {
            val photosViewModel = PhotosViewModel(photoRepository)
            val uiStates = photosViewModel.getPhotos().take(2).toList()
            Assert.assertEquals(2, uiStates.size)
            Assert.assertTrue(uiStates[0] is UIState.STARTED)
            Assert.assertTrue(uiStates[1] is UIState.SUCCESS)
        }
    }

    @Test
    fun startState_Error_Test(): Unit = runBlocking {
        launch (Dispatchers.Main) {
            whenever(photoRepository.getPhotos()).thenReturn(flow {
                emit(Result.ERROR("", null))
            })
            val photosViewModel = PhotosViewModel(photoRepository)
            val uiStates = photosViewModel.getPhotos().take(2).toList()
            Assert.assertEquals(2, uiStates.size)
            Assert.assertTrue(uiStates[0] is UIState.STARTED)
            Assert.assertTrue(uiStates[1] is UIState.ERROR)
        }
    }
}