package com.wasilyk.app.apod.model.datasource

import com.wasilyk.app.apod.model.datasource.retrofit.RetrofitApi
import com.wasilyk.app.apod.model.entitities.Apod
import retrofit2.Response
import javax.inject.Inject

class DataSourceImpl @Inject constructor(
    private val retrofitApi: RetrofitApi
) : DataSource {

    override suspend fun fetchApod(apiKey: String) =
        retrofitApi.fetchApod(apiKey)

    override suspend fun fetchApods(apiKey: String, startDate: String, endDate: String) =
        retrofitApi.fetchApods(apiKey, startDate, endDate, true)
}