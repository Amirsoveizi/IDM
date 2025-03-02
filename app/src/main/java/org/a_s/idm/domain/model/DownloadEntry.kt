package org.a_s.idm.domain.model

data class DownloadEntry(
    val id: Int = 0,
    val url: String = "",
    val path: String = "",
    val fileName    : String = "",
    var status: DownloadStatus = DownloadStatus.PENDING,
    var downloadedBytes: Long = 0,
    val totalBytes: Long = 0,
    val resume: Boolean = false,
    val addedDate: Long = System.currentTimeMillis(),
    var completedDate: Long = 0,
    val downloadPage: String = "", //option
) {
    val isFinished
        get() = totalBytes == downloadedBytes

    val progress: Int
        get() = if (totalBytes > 0) {
            (downloadedBytes * 100 / totalBytes).toInt()
        } else {
            0
        }
}