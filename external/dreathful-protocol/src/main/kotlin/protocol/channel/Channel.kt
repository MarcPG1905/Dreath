package protocol.channel

import protocol.Packet
import protocol.channel.features.CompressionLevel
import protocol.channel.features.Compressor
import protocol.channel.features.Encrypter
import protocol.session.Session
import java.net.SocketAddress

/**
 * A channel through which data can be routed through with its own rules and its own packet-handling logic.
 *
 * Not to be confused with a [Session], which represents a connections between a client and server.
 *
 * @param name This channel's unique name for mods to use a channel without needing access to its [Channel] instance.
 *
 * @param reliable If true, packets which got dropped will be retransmitted.
 * @param ordered If true, packets will always be handled in their sequence order. This will naturally introduce a small delay before packets are handled.
 * @param compression If not set to [CompressionLevel.NONE], packets will be compressed according to the level's specification.
 * @param encrypted If true, packets will be encrypted using ChaCha20-Poly1305.
 *
 * @author MarcPG
 * @since 0.1.0
 *
 * @see Compressor
 * @see Encrypter
 */
abstract class Channel(
    val name: String,

    val reliable: Boolean = false,
    val ordered: Boolean = false,
    val compression: CompressionLevel = CompressionLevel.NONE,
    val encrypted: Boolean = false,
) {
    private val compressor: Compressor? = if (compression != CompressionLevel.NONE) Compressor(compression) else null
    private val encrypter: Encrypter? = /* TODO: if (encrypted) Encrypter(null) else */ null

    /**
     * Processes some data using this channel's encryption and compression.
     * @returns A triple of (output data, compressed, encrypted).
     */
    fun processData(data: ByteArray, doNotCompress: Boolean = false, doNotEncrypt: Boolean = false): Triple<ByteArray, Boolean, Boolean> {
        var data = data

        val compress = !doNotCompress && compressor != null
        if (compress) data = compressor.compress(data)

        val encrypt = !doNotEncrypt && encrypted && encrypter != null
        // TODO: if (encrypt) data = encrypter.encrypt(data, <nonce>, <aad>)

        return Triple(data, compress, encrypt)
    }

    /** Handles this packet from an existing session/connection. */
    abstract fun handle(source: Session, packet: Packet)

    /**
     * Handles this packet from an address which did not establish a connection yet.
     *
     * By default, this throws an exception, but some implementation of [Channel] may make this actually do something.
     */
    open fun handleSessionless(sourceAddress: SocketAddress, packet: Packet) {
        error("Unknown packet origin session")
    }
}
