package org.a_s.idm.domain.model

import org.a_s.idm.utlis.formatDecimal

data class DownloadEntry(
    val name: String,
    val downloadPage: String = "", //option
    val url: String,
    val path: String,
    val resume: Boolean,
    val size: Long,
    var progressBytes: Long,
    val addedDate: Long,
    var completedDate: Long,
    var status: DownloadStatus
) {
    val isFinished = (size == progressBytes)

    val progress: String
        get() = if (size > 0) {
            (progressBytes.toDouble() * 100 / size).formatDecimal(2)
        } else {
            "0"
        }
}

enum class DownloadStatus {
    PENDING, DOWNLOADING, COMPLETED, FAILED, PAUSED
}

