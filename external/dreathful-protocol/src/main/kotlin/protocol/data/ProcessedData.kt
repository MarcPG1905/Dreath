package protocol.data

data class ProcessedData(
    val rawData: ByteArray,
    val compressed: Boolean,
    val encrypted: Boolean,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProcessedData

        if (compressed != other.compressed) return false
        if (encrypted != other.encrypted) return false
        if (!rawData.contentEquals(other.rawData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = compressed.hashCode()
        result = 31 * result + encrypted.hashCode()
        result = 31 * result + rawData.contentHashCode()
        return result
    }
}