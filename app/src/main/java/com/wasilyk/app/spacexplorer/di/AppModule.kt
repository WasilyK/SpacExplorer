package com.wasilyk.app.spacexplorer.di

import com.wasilyk.app.spacexplorer.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AppModule {

    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity
}