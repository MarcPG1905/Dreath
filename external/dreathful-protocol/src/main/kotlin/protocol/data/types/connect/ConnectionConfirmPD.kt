package protocol.data.types.connect

import protocol.data.PacketData
import protocol.data.PacketDataCompanion
import protocol.util.getUInt
import protocol.util.growable.GrowableByteBuffer
import java.nio.ByteBuffer

data class ConnectionConfirmPD(
    val clientSessionId: UInt,
) : PacketData<ConnectionConfirmPD> {
    override val companion: PacketDataCompanion<ConnectionConfirmPD> = ConnectionConfirmPD

    override fun encode(buffer: GrowableByteBuffer) {
        buffer.putUInt(clientSessionId)
    }

    companion object : PacketDataCompanion<ConnectionConfirmPD>() {
        override fun decode(buffer: ByteBuffer) = ConnectionConfirmPD(buffer.getUInt())
    }
}
