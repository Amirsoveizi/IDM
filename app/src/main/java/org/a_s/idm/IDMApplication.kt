package org.a_s.idm

import android.app.Application
import org.a_s.idm.di.idmModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class IDMApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@IDMApplication)
            modules(idmModules)
        }
    }
}