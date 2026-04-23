package protocol.util

import com.marcpg.dreath.util.toByteArray
import protocol.Packet
import protocol.channel.Channel
import protocol.channel.InternalChannel
import protocol.header.PacketHeader
import protocol.header.TypeId
import protocol.session.Session
import protocol.session.SessionManager

fun createPacket(
    receiver: Session,

    type: TypeId,
    channel: Channel,

    compress: Boolean? = null,
    encrypt: Boolean? = null,
    variant: Boolean = false,

    tick: UInt = 0u, // TODO: Set actual tracked ping.

    data: ByteArray? = emptyByteArray(),
): Packet {
    // TODO: Compress data if needed.
    // TODO: Encrypt data if needed.

    return Packet(PacketHeader(
        type = type,
        channel = channel,
        fragment = false,
        retransmission = false,
        compressed = compress ?: channel.compression,
        encrypted = encrypt ?: channel.encrypted,
        variant = variant,
        sourceSessionId = SessionManager.localSession?.id ?: 0u,
        sequenceId = receiver.sendSequence.incrementAndGet(),

        // TODO: These two need to be looked at again, as they aren't tracked yet.
        acknowledgement = receiver.receiveSequence.get(),
        acknowledgementBitfield = 0u,

        tick = tick,
        length = data?.size?.toUShort() ?: 0u
    ), data)
}

fun createPing(
    receiver: Session,
    pong: Boolean,
    tick: UInt = 0u, // TODO: Set actual tracked ping.
) = createPacket(
    receiver = receiver,
    type = TypeId.PING,
    channel = InternalChannel,
    variant = pong,
    tick = tick,
    data = System.currentTimeMillis().toULong().toByteArray()
)
