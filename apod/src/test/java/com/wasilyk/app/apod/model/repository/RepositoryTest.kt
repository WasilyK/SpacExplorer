package com.wasilyk.app.apod.model.repository

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.wasilyk.app.apod.model.datasource.DataSource
import com.wasilyk.app.apod.model.entitities.Apod
import com.wasilyk.app.core.Error
import com.wasilyk.app.core.NASA_API_KEY
import com.wasilyk.app.core.Success
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Response
import java.util.*

class RepositoryTest {
    private val dataSource = mock(DataSource::class.java)
    private val apodResponse = mock(Response::class.java) as Response<Apod>
    private val apodsResponse = mock(Response::class.java) as Response<List<Apod>>

    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = Repository(dataSource, Locale.ENGLISH)
    }

    @Test
    fun fetchApod_WhenResponseIsNotSuccessful_ReturnTrue() =
        runBlocking {
            `when`(dataSource.fetchApod(NASA_API_KEY))
                .thenReturn(apodResponse)
            `when`(apodResponse.isSuccessful)
                .thenReturn(false)

            val appState = repository.fetchApod()
            assertThat(appState, instanceOf(Error::class.java))
        }

    @Test
    fun fetchApod_WhenResponseBodyIsNull_ReturnTrue() =
        runBlocking {
            val message = "Response body is null"

            `when`(dataSource.fetchApod(NASA_API_KEY))
                .thenReturn(apodResponse)
            `when`(apodResponse.isSuccessful)
                .thenReturn(true)
            `when`(apodResponse.body())
                .thenReturn(null)

            val appState = repository.fetchApod()
            assertThat(appState, instanceOf(Error::class.java))
            assertThat(message, equalTo((appState as Error).throwable.localizedMessage))
        }

    @Test
    fun fetchApod_WhenResponseIsSuccess_ReturnTrue() =
        runBlocking {
            val apod = Apod(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                null
            )

            `when`(dataSource.fetchApod(NASA_API_KEY))
                .thenReturn(apodResponse)
            `when`(apodResponse.isSuccessful)
                .thenReturn(true)
            `when`(apodResponse.body())
                .thenReturn(apod)

            val appState = repository.fetchApod()

            assertThat(appState, instanceOf(Success::class.java))
        }

    @Test
    fun load_WhenResponseIsNotSuccessFull_ReturnTrue() {
        val loadParams = mock(LoadParams::class.java) as LoadParams<String>
        val endDate = "2021-10-26"
        val startDate = "2021-10-20"

        `when`(loadParams.key)
            .thenReturn(endDate)

        runBlocking {
            `when`(dataSource.fetchApods(NASA_API_KEY, startDate, endDate))
                .thenReturn(apodsResponse)

            `when`(apodsResponse.isSuccessful)
                .thenReturn(false)

            val loadResult = repository.load(loadParams)

            assertThat(loadResult, instanceOf(PagingSource.LoadResult.Error::class.java))
        }
    }

    @Test
    fun load_WhenResponseBodyIsNull_ReturnTrue() {
        val loadParams = mock(LoadParams::class.java) as LoadParams<String>
        val endDate = "2021-10-26"
        val startDate = "2021-10-20"
        val message = "Response body is null"

        `when`(loadParams.key)
            .thenReturn(endDate)

        runBlocking {
            `when`(dataSource.fetchApods(NASA_API_KEY, startDate, endDate))
                .thenReturn(apodsResponse)

            `when`(apodsResponse.isSuccessful)
                .thenReturn(true)

            `when`(apodsResponse.body())
                .thenReturn(null)

            val loadResult = repository.load(loadParams)

            assertThat(loadResult, instanceOf(PagingSource.LoadResult.Error::class.java))
            assertThat(message, equalTo(
                (loadResult as PagingSource.LoadResult.Error).throwable.localizedMessage)
            )
        }
    }

    @Test
    fun load_WhenResponseIsSuccess_ReturnTrue() {
        val loadParams = mock(LoadParams::class.java) as LoadParams<String>
        val endDate = "2021-10-26"
        val startDate = "2021-10-20"
        val apods = listOf(
            Apod(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                null
            )
        )

        `when`(loadParams.key)
            .thenReturn(endDate)

        runBlocking {
            `when`(dataSource.fetchApods(NASA_API_KEY, startDate, endDate))
                .thenReturn(apodsResponse)

            `when`(apodsResponse.isSuccessful)
                .thenReturn(true)

            `when`(apodsResponse.body())
                .thenReturn(apods)

            val loadResult = repository.load(loadParams)

            assertThat(loadResult, instanceOf(LoadResult.Page::class.java))

            val data = (loadResult as LoadResult.Page).data
            assertThat(data, equalTo(apods))
        }
    }
}