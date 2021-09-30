package br.com.scorza5.coinconverter

import android.app.Application
import br.com.scorza5.coinconverter.data.di.DataModules
import br.com.scorza5.coinconverter.domain.di.DomainModule
import br.com.scorza5.coinconverter.presentation.di.PresentationModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
        }

        DataModules.load()
        DomainModule.load()
        PresentationModules.load()
    }
}