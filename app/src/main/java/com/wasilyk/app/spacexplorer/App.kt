package com.wasilyk.app.spacexplorer

import com.wasilyk.app.spacexplorer.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory()
            .create(
                applicationContext,
                resources.configuration.locale
            )

}