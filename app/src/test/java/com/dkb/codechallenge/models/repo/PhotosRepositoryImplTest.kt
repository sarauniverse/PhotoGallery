package com.dkb.codechallenge.models.repo

import com.dkb.codechallenge.api.PhotoService
import com.dkb.codechallenge.models.data.Result
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.Buffer
import org.junit.After
import org.junit.Assert
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PhotosRepositoryImplTest {

    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val photoService = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PhotoService::class.java)

    private val photoRepository = PhotosRepositoryImpl(photoService)

    private val mockValidPhotosResponse = "[\n" +
            "  {\n" +
            "    \"albumId\": 1,\n" +
            "    \"id\": 1,\n" +
            "    \"title\": \"accusamus beatae ad facilis cum similique qui sunt\",\n" +
            "    \"url\": \"https://via.placeholder.com/600/92c952\",\n" +
            "    \"thumbnailUrl\": \"https://via.placeholder.com/150/92c952\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"albumId\": 1,\n" +
            "    \"id\": 2,\n" +
            "    \"title\": \"reprehenderit est deserunt velit ipsam\",\n" +
            "    \"url\": \"https://via.placeholder.com/600/771796\",\n" +
            "    \"thumbnailUrl\": \"https://via.placeholder.com/150/771796\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"albumId\": 1,\n" +
            "    \"id\": 3,\n" +
            "    \"title\": \"officia porro iure quia iusto qui ipsa ut modi\",\n" +
            "    \"url\": \"https://via.placeholder.com/600/24f355\",\n" +
            "    \"thumbnailUrl\": \"https://via.placeholder.com/150/24f355\"\n" +
            "  }\n" +
            "]"

    private val mockValidPhotoResponse = "{\n" +
            "    \"albumId\": 1,\n" +
            "    \"id\": 1,\n" +
            "    \"title\": \"accusamus beatae ad facilis cum similique qui sunt\",\n" +
            "    \"url\": \"https://via.placeholder.com/600/92c952\",\n" +
            "    \"thumbnailUrl\": \"https://via.placeholder.com/150/92c952\"\n" +
            "}"

    @After
    fun afterTest() {
        mockWebServer.shutdown()
    }

    @Test
    fun getPhotos_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(mockValidPhotosResponse))
        val result = photoRepository.getPhotos().toList()

        Assert.assertEquals(3, (result[result.size-1] as Result.SUCCESS).result.size)
    }

    @Test
    fun getPhotos_ServerNotReachable() = runBlocking {
        mockWebServer.enqueue(MockResponse()
            .setBody(Buffer().write(byteArrayOf()))
            .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))
        val result = photoRepository.getPhotos().toList()

        Assert.assertTrue(result[result.size-1] is Result.ERROR)
    }

    @Test
    fun getPhoto_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(mockValidPhotoResponse))
        val result = photoRepository.getPhoto(1).toList()

        Assert.assertTrue(result[result.size-1] is Result.SUCCESS)
        Assert.assertEquals(1, (result[result.size-1] as Result.SUCCESS).result.id)
    }

    @Test
    fun getPhoto_ServerNotReachable() = runBlocking {
        mockWebServer.enqueue(MockResponse()
            .setBody(Buffer().write(byteArrayOf()))
            .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))
        val result = photoRepository.getPhoto(1).toList()

        Assert.assertTrue(result[result.size-1] is Result.ERROR)
    }

    @Test
    fun getPhoto_NotFound() = runBlocking {
        mockWebServer.enqueue(MockResponse()
            .setBody(Buffer().write(byteArrayOf()))
            .setResponseCode(404))
        val result = photoRepository.getPhoto(25).toList()

        Assert.assertTrue(result[result.size-1] is Result.ERROR)
    }
}