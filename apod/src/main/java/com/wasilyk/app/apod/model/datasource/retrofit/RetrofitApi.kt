package com.wasilyk.app.apod.model.datasource.retrofit

import com.wasilyk.app.apod.model.entitities.Apod
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    companion object {
        const val PATH = "planetary/apod"
    }

    @GET(PATH)
    suspend fun fetchApod(
        @Query("api_key") apiKey: String
    ): Response<Apod>

    @GET(PATH)
    suspend fun fetchApods(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("thumbs") thumbs: Boolean
    ): Response<List<Apod>>
}