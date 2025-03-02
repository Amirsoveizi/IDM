package org.a_s.idm.domain.model

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.a_s.idm.utlis.Constants.TAG
import java.io.FileOutputStream

data class WorkerEntry(
    val entry: DownloadEntry,
//    val jobs: MutableList<Job>,
//    var channel: Channel<Segment>? = null,
//    val outputStream: FileOutputStream,
    val maxConcurrentDownloads: Int = 3,
//    val downloadStatus: BooleanArray = BooleanArray(maxConcurrentDownloads),
    val get: suspend (start: Long, end: Long) -> Segment
) {
    suspend fun download() {
        val channel: Channel<Segment> = Channel();
        withContext(Dispatchers.IO) {
            val mainJob = launch {
                val job = launch {
                    val part = DownloadSegment(
                        url = entry.url,
                        startPosition = 0,
                        endPosition = entry.totalBytes,
                        channel = channel,
                        get = get
                    )
                    part.download()
                }

                channel.consumeEach { segment: Segment ->
                    Log.d(TAG, "consume -> download : ${bytesToMB(segment.offset)}MB")
//                    outputStream.write(segment.bytes,segment.offset,segment.length)
                }

            }
        }
    }
}

fun bytesToMB(bytes: Long): Double {
    return bytes / (1024.0 * 1024.0) // Convert to MB
}
