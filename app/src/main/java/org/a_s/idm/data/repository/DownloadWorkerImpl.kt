package org.a_s.idm.data.repository

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import org.a_s.idm.domain.local.IdGenerator
import org.a_s.idm.domain.model.DownloadEntry
import org.a_s.idm.domain.model.DownloadStatus
import org.a_s.idm.domain.model.Segment
import org.a_s.idm.domain.model.WorkerEntry
import org.a_s.idm.domain.repository.DownloadWorker
import org.a_s.idm.utlis.Constants.TAG
import java.net.URLDecoder

class DownloadWorkerImpl(
    private val client: HttpClient,
    private val idGenerator: IdGenerator
) : DownloadWorker {

    private var workerEntry : WorkerEntry? = null

    override suspend fun initialize(url: String) {
        val response = client.request(url) {
            method = HttpMethod.Head
        }

        val contentType = response.headers[HttpHeaders.ContentType]
        val contentLength = response.headers[HttpHeaders.ContentLength]
        Log.d(TAG, "contentType : $contentType")
        Log.d(TAG, "contentLength : $contentLength")
        Log.d(TAG, "filename :" + response.getFileName())

        workerEntry = WorkerEntry(
            entry = DownloadEntry(
                id = idGenerator.getId(),
                url = url,
                path = "",
                fileName = response.getFileName(),
                status = DownloadStatus.PENDING,
                totalBytes = contentLength!!.toLong(), //TODO
                resume = true
            ),
            get = { start: Long, end: Long ->
                val bytes = client.get(url) {
                    header("Range", "bytes=${start}-${end}")
                }.body<ByteArray>()

                Segment(
                    bytes = bytes,
                    offset = start,
                    length = end - start
                )
            }
        )

    }

    override suspend fun download() {
        workerEntry?.download()
    }

    override suspend fun stop() {
        TODO("Not yet implemented")
    }

    override suspend fun retry() {
        TODO("Not yet implemented")
    }

    override suspend fun cancel() {
        TODO("Not yet implemented")
    }
}

val fileNameRegex = "/filename[^;=\\n]*=((['\"]).*?\\2|[^;\\n]*)/".toRegex()

fun HttpResponse.getFileName() : String {
    return this.headers[HttpHeaders.ContentDisposition]?.let { contentDisposition ->
        fileNameRegex.find(contentDisposition)?.groups?.get(1)?.value
    } ?: run {
        this.request.url.encodedPath.substringAfterLast('/').let { fileName ->
            URLDecoder.decode(fileName,"UTF-8")
        }
    }
}