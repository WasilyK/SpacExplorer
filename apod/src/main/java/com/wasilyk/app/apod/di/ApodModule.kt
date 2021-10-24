package com.wasilyk.app.apod.di

import com.wasilyk.app.apod.model.datasource.DataSource
import com.wasilyk.app.apod.model.datasource.DataSourceImpl
import dagger.Binds
import dagger.Module

@Module(
    includes = [
        RetrofitApiModule::class,
        DiffCallbackModule::class
    ]
)
interface ApodModule {
    @Binds
    fun bindDataSource(dataSourceImpl: DataSourceImpl): DataSource
}