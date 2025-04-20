package com.marcpg.common.modules.protocol

import com.marcpg.common.util.toByteArray
import com.marcpg.common.util.toULong

data class PegPacket(
    val header: PegPacketHeader, // 64 bits / 8 bytes
    val payload: UByteArray
) {
    companion object {
        fun ofRaw(raw: ByteArray) : PegPacket = PegPacket(
            PegPacketHeader.ofRaw(raw.take(8).toByteArray().toULong()),
            raw.drop(8).toByteArray().toUByteArray()
        )

        fun build(channel: UByte, sequenceId: UShort, type: UByte, fragment: Boolean, retransmission: Boolean, id: UShort, payload: UByteArray) : PegPacket {
            return PegPacket(
                PegPacketHeader(channel, sequenceId, type, fragment, retransmission, id, payload.size.toUShort()),
                payload
            )
        }
    }

    fun raw() : ByteArray = header.raw().toByteArray() + payload.toByteArray()
}
