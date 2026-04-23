package protocol

import protocol.header.PacketHeader
import protocol.util.RawDataDecodableObj
import protocol.util.RawDataEncodable
import java.nio.ByteBuffer

data class Packet(
    val header: PacketHeader,
    val data: ByteArray?,
) : RawDataEncodable {
    companion object : RawDataDecodableObj<Packet>() {
        override fun decode(buffer: ByteBuffer): Packet {
            val header = PacketHeader.decode(buffer)

            val payloadLength = header.length.toInt()
            val payload = ByteArray(payloadLength)
            buffer.get(payload)

            return Packet(header, payload)
        }
    }

    override fun encode(): ByteArray = if (data == null) header.encode() else header.encode() + data

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Packet

        if (header != other.header) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = header.hashCode()
        result = 31 * result + (data?.contentHashCode() ?: 0)
        return result
    }
}
