package protocol.channel.features

import net.jpountz.lz4.LZ4Compressor
import net.jpountz.lz4.LZ4Factory
import net.jpountz.lz4.LZ4SafeDecompressor
import protocol.Packet
import protocol.header.PacketHeader
import protocol.session.SessionManager

/**
 * LZ4-based compression for compressing packets of a minimum length.
 *
 * @param level The level at which to compress data.
 *
 * @author MarcPG
 * @since 0.1.0
 */
class Compressor(val level: CompressionLevel) {
    companion object {
        private val factory: LZ4Factory = LZ4Factory.fastestInstance()
    }

    private val compressor: LZ4Compressor = level.compressor(factory)
    private val decompressor: LZ4SafeDecompressor = factory.safeDecompressor()

    /**
     * Compresses this piece of data using LZ4, if the data's size is under [CompressionLevel.minSize]. Otherwise, it just returns the input.
     * @param data The uncompressed input data.
     * @return The compressed data, if >= [CompressionLevel.minSize], otherwise the input array.
     */
    fun compress(data: ByteArray): ByteArray {
        if (data.size < level.minSize)
            return data
        return compressor.compress(data)
    }

    /**
     * Decompresses this piece of data using LZ4 and a set MTU.
     * @param data The compressed input data.
     * @param mtu The MTU to assume for this packet to safe performance.
     * @return The decompressed data.
     */
    fun decompress(data: ByteArray, mtu: Int): ByteArray {
        return decompressor.decompress(data, mtu - PacketHeader.dataFormat.byteLength - 8)
    }

    /**
     * Decompresses this packet using LZ4 and the channel's MTU.
     *
     * Fails if the packet does not have any data or is not compressed.
     * @param packet The packet to decompress.
     * @return The decompressed data.
     */
    fun decompressPacket(packet: Packet): ByteArray {
        require(packet.header.compressed) { "Packet is not compressed" }
        requireNotNull(packet.data) { "Packet does not have any data to decompress" }

        // TODO: Replace 1024 with a safe default. Likely gonna be around 512 or so.
        return decompress(packet.data, SessionManager[packet.header.sourceSessionId]?.mtu ?: 1024)
    }
}

/**
 * The level at which to compress data.
 *
 * @param minSize How big data needs to be before compression starts happening (inclusive).
 * @param compressor Creates the compressor to be used for this level from an existing factory.
 */
enum class CompressionLevel(val minSize: Int, val compressor: LZ4Factory.() -> LZ4Compressor) {
    /** Does not compress at all. */
    NONE(Int.MAX_VALUE, { fastCompressor() }),

    /** Compresses all over 1000 bytes / 1 kB. Uses normal fast compression. */
    RARE(1000, { fastCompressor() }),

    /** Compresses all data over 500 bytes / 0.5 kB. Uses normal fast compression. */
    NORMAL(500, { fastCompressor() }),

    /** Compresses all data over 200 bytes / 0.2 kB. Uses normal fast compression. */
    OFTEN(200, { fastCompressor() }),

    /** Compresses all data. Uses normal fast compression. */
    ALWAYS(0, { fastCompressor() }),

    /**
     * Compressed all data and uses the highest possible compression level.
     *
     * This is significantly slower than all other compression levels, but also creates significantly smaller output in pattern-heavy data.
     */
    EXTREME(0, { highCompressor(17) }),
}
