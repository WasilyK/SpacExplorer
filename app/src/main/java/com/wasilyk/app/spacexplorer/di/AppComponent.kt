package com.wasilyk.app.spacexplorer.di

import com.wasilyk.app.spacexplorer.App
import dagger.Component
import dagger.android.AndroidInjector

@Component(modules = [AppModule::class])
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    interface Factory {
        fun create() : AppComponent
    }
}