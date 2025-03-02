package org.a_s.idm.domain.repository

import android.net.Uri

interface DownloadWorker {
    suspend fun initialize(url : String)
    suspend fun download()
    suspend fun stop()
    suspend fun retry()
    suspend fun cancel()
}