package com.wasilyk.app.spacexplorer.di

import android.content.Context
import com.wasilyk.app.spacexplorer.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjection
import java.util.*

@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance locale: Locale
        ): AppComponent
    }
}