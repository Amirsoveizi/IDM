package org.a_s.idm.domain.model


data class Segment(
    val bytes: ByteArray,
    val offset: Long,
    val length: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Segment

        if (!bytes.contentEquals(other.bytes)) return false
        if (offset != other.offset) return false
        if (length != other.length) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + offset.hashCode()
        result = 31 * result + length.hashCode()
        return result
    }


}
