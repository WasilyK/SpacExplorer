package com.wasilyk.app.apod.model.datasource

import com.wasilyk.app.apod.model.entitities.Apod
import retrofit2.Response

interface DataSource {
    suspend fun fetchApod(apiKey: String): Response<Apod>
    suspend fun fetchApods(apiKey: String, startDate: String, endDate: String): Response<List<Apod>>
}