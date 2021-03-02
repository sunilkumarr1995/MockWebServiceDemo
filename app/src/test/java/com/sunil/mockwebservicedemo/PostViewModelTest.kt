package com.sunil.mockwebservicedemo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sunil.mockwebservicedemo.model.api.RetrofitBuilder
import com.sunil.mockwebservicedemo.model.Post
import com.sunil.mockwebservicedemo.model.api.ApiHelperImpl
import com.sunil.mockwebservicedemo.util.Resource
import com.sunil.mockwebservicedemo.viewmodel.PostViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class PostViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PostViewModel

    private lateinit var apiHelper: ApiHelperImpl

    @Mock
    private lateinit var liveDataObserver: Observer<Resource<List<Post>>?>

    private lateinit var mockWebServer: MockWebServer


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = PostViewModel()
        viewModel.getPostDetail().observeForever(liveDataObserver)
        mockWebServer = MockWebServer()
        mockWebServer.start()
        apiHelper = ApiHelperImpl(RetrofitBuilder.apiInterface)
    }

    @Test
    fun `read sample success json file`() {
        val reader = MockResponseFileReader("jsonplaceholder_success.json")
        assertNotNull(reader.content)
    }

    @Test
    fun `JsonPlaceholder APIs and check response Code 200 returned`() {
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("jsonplaceholder_success.json").content)
        mockWebServer.enqueue(response)
        // Act
        val actualResponse = apiHelper.getPostData().test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertComplete()
            .assertValueCount(1)
            .assertNoErrors()
        // Assert
        assertEquals(
            response.toString().contains("200"),
            actualResponse.toString().contains("200")
        )
    }

    fun `parse mocked JSON response`(mockResponse: String): String {
        val reader = JSONObject(mockResponse)
        return reader.getString("status")
    }

    @Test
    fun `JsonPlaceholder APIs for failed response 400 returned`() {
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
            .setBody(MockResponseFileReader("jsonplaceholder_failure.json").content)
        mockWebServer.enqueue(response)
        // Act
        val actualResponse = apiHelper.getPostData()
        // Assert
        assertEquals(
            response.toString().contains("404"),
            actualResponse.toString().contains("404")
        )
    }

    @After
    fun tearDown() {
        viewModel.getPostDetail().removeObserver(liveDataObserver)
        mockWebServer.shutdown()
    }
    @Test
    fun `JsonPlaceholder APIs parse correctly`() {
        mockWebServer.apply {
            enqueue(MockResponse().setBody(MockResponseFileReader("jsonplaceholder_success.json").content))
        }
        apiHelper.getPostData()
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun `JsonPlaceholder APIs not parse correctly`() {
        mockWebServer.apply {
            enqueue(MockResponse().setBody(MockResponseFileReader("jsonplaceholder_success.json").content))
        }
        apiHelper.getPostData()
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertNotComplete()
    }
}
