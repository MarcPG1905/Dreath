package protocol.channel

import protocol.Packet
import protocol.channel.features.CompressionLevel
import protocol.channel.features.Compressor
import protocol.channel.features.Encrypter
import protocol.session.Session
import java.net.SocketAddress

abstract class Channel(
    val name: String,

    val reliable: Boolean = false,
    val ordered: Boolean = false,
    val compression: CompressionLevel = CompressionLevel.NONE,
    val encrypted: Boolean = false,
    val compression: Boolean = false,
) {
    private val compressor: Compressor? = if (compression != CompressionLevel.NONE) Compressor(compression) else null
    private val encrypter: Encrypter? = /* TODO: if (encrypted) Encrypter(null) else */ null
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
    open fun handleSessionless(sourceAddress: SocketAddress, packet: Packet) {
        error("Unknown packet origin session")
    }
}
