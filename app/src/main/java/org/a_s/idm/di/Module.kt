package org.a_s.idm.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.a_s.idm.data.local.IdGeneratorImpl
import org.a_s.idm.data.repository.DownloadWorkerImpl
import org.a_s.idm.domain.local.IdGenerator
import org.a_s.idm.domain.repository.DownloadWorker
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val idmModules = module {
    single<IdGenerator> { IdGeneratorImpl(context = androidApplication()) }
    single<HttpClient> { HttpClient(engineFactory = OkHttp) }
    single<DownloadWorker> {
        DownloadWorkerImpl(
            client = get(),
            idGenerator = get()
        )
    }
}