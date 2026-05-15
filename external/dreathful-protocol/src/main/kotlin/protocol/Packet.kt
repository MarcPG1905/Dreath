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

    var processedData: ProcessedData? = null
        set(value) {
            require(field == null) { "Cannot assign processedData twice"}
            require(data != null) { "Cannot set processed data of a packet without data"}

            field = value
        }

    override fun encode(): ByteArray = if (data == null) header.encode() else header.encode() + data
}
