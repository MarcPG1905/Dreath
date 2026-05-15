package protocol.data.types.ping

import protocol.data.PacketData
import protocol.data.PacketDataCompanion
import protocol.util.growable.GrowableByteBuffer
import java.nio.ByteBuffer

data class PingPongPD(
    val time: Long = System.currentTimeMillis(),
) : PacketData<PingPongPD> {
    override val companion: PacketDataCompanion<PingPongPD> = PingPongPD

    override fun encode(buffer: GrowableByteBuffer) {
        buffer.putLong(time)
    }

    companion object : PacketDataCompanion<PingPongPD>() {
        override fun decode(buffer: ByteBuffer) = PingPongPD(buffer.getLong())
    }
}
