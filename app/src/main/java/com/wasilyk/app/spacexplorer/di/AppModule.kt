package com.wasilyk.app.spacexplorer.di

import com.wasilyk.app.apod.di.ApodModule
import com.wasilyk.app.apod.view.ApodFragment
import com.wasilyk.app.spacexplorer.MainActivity
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector

@Module(
    includes = [
        AndroidInjectionModule::class,
        ApodModule::class,
        CiceroneModule::class
    ]
)
interface AppModule {

    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun bindApodFragment(): ApodFragment
}