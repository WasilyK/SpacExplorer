package com.wasilyk.app.apod.di

import com.google.gson.Gson
import com.wasilyk.app.apod.model.datasource.retrofit.RetrofitApi
import com.wasilyk.app.core.BASE_URL
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RetrofitApiModule {

    @Provides
    fun provideRetrofitApi(): RetrofitApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitApi::class.java)

}