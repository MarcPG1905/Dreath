package protocol

import protocol.data.PacketData
import protocol.data.PacketDataManager
import protocol.data.ProcessedData
import protocol.header.PacketHeader
import protocol.header.PayloadPacketHeader
import protocol.header.SignalPacketHeader
import protocol.util.DecodableObj
import protocol.util.Encodable
import protocol.util.encodeToByteArray
import protocol.util.toBinary
import java.nio.ByteBuffer

data class Packet<D : PacketData<D>>(
    val header: PacketHeader,
    val data: D?,
) : Encodable {
    companion object : DecodableObj<Packet<*>> {
        override fun decode(buffer: ByteBuffer): Packet<*> {
            if (buffer.remaining() >= 16) {
                val header = PayloadPacketHeader.decode(buffer)
                val dataType = PacketDataManager.INSTANCE.getUnknown(header.dataType) ?: error("Unknown data type ${header.dataType.toBinary(4)}")
                return Packet(header, dataType.decode(buffer))
            } else {
                return Packet(SignalPacketHeader.decode(buffer), null)
            }
        }
    }

    var processedData: ProcessedData? = null
        set(value) {
            require(field == null) { "Cannot assign processedData twice"}
            require(data != null) { "Cannot set processed data of a packet without data"}

            field = value
        }

    override fun encode(): ByteArray = if (data == null) header.encode() else header.encode() + data.encodeToByteArray()
}
