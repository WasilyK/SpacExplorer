package com.wasilyk.app.apod.model.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wasilyk.app.apod.model.datasource.DataSource
import com.wasilyk.app.apod.model.entitities.Apod
import com.wasilyk.app.core.AppState
import com.wasilyk.app.core.DATE_FORMAT
import com.wasilyk.app.core.Error
import com.wasilyk.app.core.NASA_API_KEY
import com.wasilyk.app.core.Success
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    private val dataSource: DataSource,
    private val locale: Locale
) : PagingSource<String, Apod>() {

    companion object {
        const val NUMBER_APODS_IN_REQUEST = 6
    }

    suspend fun fetchApod(): AppState =
        try {
            val response = dataSource.fetchApod(NASA_API_KEY)
            if(response.isSuccessful) {
                val body = response.body()
                if(body != null) {
                    Success(listOf(body))
                } else {
                    throw Throwable("Response body is null")
                }
            } else {
                throw HttpException(response)
            }
        } catch (t: Throwable) {
            Error(t)
        }

    override fun getRefreshKey(state: PagingState<String, Apod>): String? =
        state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.addDays(NUMBER_APODS_IN_REQUEST + 1)
                ?: page?.nextKey?.subtractDays(NUMBER_APODS_IN_REQUEST - 1)
        }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Apod> {
        try {
            val endDate = params.key ?: getCurrentDate()
            val startDate = endDate.subtractDays(NUMBER_APODS_IN_REQUEST)
            val response = dataSource.fetchApods(NASA_API_KEY, startDate, endDate)
            if (response.isSuccessful) {
                val body = response.body()
                    ?: throw Throwable("Response body is null")
                val apods = body.reversed()
                return LoadResult.Page(
                    data = apods,
                    prevKey = calcPrevKey(endDate),
                    nextKey = calcNextKey(startDate)
                )
            } else {
                throw HttpException(response)
            }
        } catch (t: Throwable) {
            return LoadResult.Error(t)
        }
    }

    private suspend fun getCurrentDate(): String {
        try {
            val response = dataSource.fetchApod(NASA_API_KEY)
            if (response.isSuccessful) {
                val body = response.body()
                    ?: throw Throwable("Response body is null")
                return body.date
            } else {
                throw HttpException(response)
            }
        } catch (t: Throwable) {
            throw t
        }
    }

    private fun calcPrevKey(endDate: String): String? {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, locale)
        val today = dateFormat.format(Date())
        if (today == endDate || today == endDate.addDays(1)) {
            return null
        }
        return endDate.addDays(NUMBER_APODS_IN_REQUEST + 1)
    }

    private fun calcNextKey(startDate: String) =
        startDate.subtractDays(1)

    private fun String.subtractDays(daysCount: Int): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, locale)
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(this)
            ?: throw Throwable("Can't parse date $this")
        calendar.add(Calendar.DAY_OF_YEAR, daysCount * (-1))
        return dateFormat.format(calendar.time)
    }

    private fun String.addDays(daysCount: Int): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, locale)
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(this)
            ?: throw Throwable("Can't parse date $this")
        calendar.add(Calendar.DAY_OF_YEAR, daysCount)
        return dateFormat.format(calendar.time)
    }
}

