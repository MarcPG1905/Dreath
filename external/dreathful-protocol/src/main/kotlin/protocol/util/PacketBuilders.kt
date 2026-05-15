package protocol.util

import protocol.Packet
import protocol.channel.Channel
import protocol.channel.InternalChannel
import protocol.data.PacketData
import protocol.data.types.ping.PingPongPD
import protocol.header.PayloadPacketHeader
import protocol.header.SignalPacketHeader
import protocol.header.TypeId
import protocol.session.Session
import protocol.session.SessionManager
import protocol.util.growable.GrowableByteBufferPool

fun <D : PacketData<D>> createPacket(
    receiver: Session,

    type: TypeId,
    channel: Channel,

    doNotCompress: Boolean = false,
    doNotEncrypt: Boolean = false,
    variant: Boolean = false,

    tick: UInt = 0u, // TODO: Set actual tracked tick.

    data: D? = null,
): Packet<*> {
    val buffer = GrowableByteBufferPool.Default_MTU.acquire()
    val processedData = data?.let { channel.processData(buffer.apply { it.encode(this) }.toByteArray(), doNotCompress, doNotEncrypt) }
    buffer.release()

    val header = if (processedData == null) SignalPacketHeader(
        type = type,
        channel = channel,

        retransmission = false,
        variant = variant,

        sourceSessionId = SessionManager.localSession?.id ?: 0u,
        sequenceId = receiver.sendSequence.incrementAndGet(),

        // TODO: These two need to be looked at again, as they aren't tracked yet.
        acknowledgement = receiver.receiveSequence.get(),
        acknowledgementBitfield = 0u,

        tick = tick,
    ) else PayloadPacketHeader(
        type = type,
        channel = channel,

        fragment = false,
        retransmission = false,
        compressed = processedData.compressed,
        encrypted = processedData.encrypted,
        variant = variant,

        sourceSessionId = SessionManager.localSession?.id ?: 0u,
        sequenceId = receiver.sendSequence.incrementAndGet(),

        // TODO: These two need to be looked at again, as they aren't tracked yet.
        acknowledgement = receiver.receiveSequence.get(),
        acknowledgementBitfield = 0u,

        tick = tick,
        dataType = data.companion.id,
    )

    return Packet(header, data).apply { this@apply.processedData = processedData }
}

fun createPing(
    receiver: Session,
    pong: Boolean,
    tick: UInt = 0u, // TODO: Set actual tracked tick.
) = createPacket(
    receiver = receiver,
    type = TypeId.PING,
    channel = InternalChannel,
    variant = pong,
    tick = tick,
    data = PingPongPD()
)
