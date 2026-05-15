package protocol.header

import com.marcpg.dreath.util.toBoolean
import protocol.channel.Channel
import protocol.channel.ChannelManager
import protocol.channel.InternalChannel
import protocol.util.RawDataDecodableObj

data class SignalPacketHeader(
    override val type: TypeId, // 4 bits
    override val channel: Channel, // 4 bits

    // Flags (2 bits):
    override val retransmission: Boolean,
    override val variant: Boolean,

    override val sourceSessionId: UInt, // 19 bits
    override val sequenceId: UInt, // 20 bits

    override val acknowledgement: UInt, // 20 bits
    override val acknowledgementBitfield: UInt, // 30 bits

    override val tick: UInt, // 21 bits
) : PacketHeader {
    @Suppress("SpellCheckingInspection")
    companion object : RawDataDecodableObj<SignalPacketHeader>(
        4,  // t : Type (0–15)
        4,  // c : Channel (0–15)
        1,  // R : Retransmission (0–1)
        1,  // V : Variant (0–1)
        19, // s : Source Session ID (0–524287)
        20, // S : Sequence ID (0–1048575)
        20, // a : Acknowledgement (0–1048575)
        30, // A : Acknowledgement Bitfield (0–1073741823)
        21, // T : Tick (0–2097151)
    ) { // tttt cccc RVss ssss ssss ssss ssss sSSS
        // SSSS SSSS SSSS SSSS Saaa aaaa aaaa aaaa
        // aaaa aAAA AAAA AAAA AAAA AAAA AAAA AAAA
        // AAAT TTTT TTTT TTTT TTTT TTTT
        // Total header length: 15 bytes / 120 bit (+8 bytes from UDP = 23 bytes total)
        override fun decode(parts: List<ULong>) = SignalPacketHeader(
            type = TypeId.ofId(parts[0].toUByte()) ?: error("Invalid packet type (${parts[0]})"),
            channel = if (parts[1] == 0uL) InternalChannel else (ChannelManager.INSTANCE[parts[1].toUByte()] ?: error("Invalid packet channel (${parts[1]})")),
            retransmission = parts[2].toBoolean(),
            variant = parts[3].toBoolean(),
            sourceSessionId = parts[4].toUInt(),
            sequenceId = parts[5].toUInt(),
            acknowledgement = parts[6].toUInt(),
            acknowledgementBitfield = parts[7].toUInt(),
            tick = parts[8].toUInt(),
        )
    }

    override fun encode(): ByteArray = dataFormat.encode(
        type.id.toULong(),
        ChannelManager.INSTANCE.getId(channel)?.toULong() ?: 0uL,
        if (retransmission) 1uL else 0uL,
        if (variant) 1uL else 0uL,
        sourceSessionId.toULong(),
        sequenceId.toULong(),
        acknowledgement.toULong(),
        acknowledgementBitfield.toULong(),
        tick.toULong(),
    )
}
