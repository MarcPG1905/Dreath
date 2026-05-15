package protocol.data

import protocol.util.growable.GrowableByteBuffer
import java.nio.ByteBuffer

interface PacketData<T : PacketData<T>> {
    val companion: PacketDataCompanion<T>

    fun encode(buffer: GrowableByteBuffer)
}

abstract class PacketDataCompanion<T : PacketData<T>> {
    val id: UShort by lazy { PacketDataManager.INSTANCE.getOrAdd(this@PacketDataCompanion) }

    abstract fun decode(buffer: ByteBuffer): T
}
