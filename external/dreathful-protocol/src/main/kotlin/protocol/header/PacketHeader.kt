package protocol.header

import protocol.channel.Channel
import protocol.util.Encodable

interface PacketHeader : Encodable {
    val type: TypeId // 2 bit
    val channel: Channel // 4 bit

    val retransmission: Boolean // 1 bit
    val variant: Boolean // 1 bit

    val sourceSessionId: UInt // 19 bit
    val sequenceId: UInt // 20 bit

    val acknowledgement: UInt // 20 bit
    val acknowledgementBitfield: UInt // 26 bit / 32 bit

    val tick: UInt // 21 bit
}
