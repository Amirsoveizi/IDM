package org.a_s.idm.domain.model

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.math.min

data class DownloadSegment(
    val url: String,
    val startPosition: Long,
    val endPosition: Long,
    val chunkSize: Int = 65536,
    val channel: Channel<Segment>,
    val get: suspend (start: Long, end: Long) -> Segment
) {
    private var startByte = startPosition
    private val endByte : Long
        get() = min(startByte + chunkSize - 1, endPosition)

    suspend fun download() {
        while (true) {
            val data = get(startByte,endByte)

            channel.send(data)

            if(endByte >= endPosition) break
            startByte += chunkSize
        }
    }
}
