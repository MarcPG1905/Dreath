package protocol.header

import com.marcpg.dreath.util.toBoolean
import protocol.channel.Channel
import protocol.channel.ChannelManager
import protocol.channel.InternalChannel
import protocol.util.RawDataDecodableObj

data class PayloadPacketHeader(
    override val type: TypeId, // 2 bits
    override val channel: Channel, // 4 bits

    // Flags (5 bits):
    val fragment: Boolean = false,
    override val retransmission: Boolean,
    val compressed: Boolean = false,
    val encrypted: Boolean = false,
    override val variant: Boolean,

    override val sourceSessionId: UInt, // 19 bits
    override val sequenceId: UInt, // 20 bits

    override val acknowledgement: UInt, // 20 bits
    override val acknowledgementBitfield: UInt, // 26 bits

    override val tick: UInt, // 21 bits

    val dataType: UShort, // 11 bits
) : PacketHeader {
    @Suppress("SpellCheckingInspection")
    companion object : RawDataDecodableObj<PayloadPacketHeader>(
        4,  // t : Type (0–15)
        4,  // c : Channel (0–15)
        1,  // F : Fragment (0–1)
        1,  // R : Retransmission (0–1)
        1,  // C : Compressed (0–1)
        1,  // E : Encrypted (0–1)
        1,  // V : Variant (0–1)
        19, // s : Source Session ID (0–524287)
        20, // S : Sequence ID (0–1048575)
        20, // a : Acknowledgement (0–1048575)
        25, // A : Acknowledgement Bitfield (0–33554431)
        21, // T : Tick (0–2097151)
        10, // D : Data Type (0–1023)
    ) { // tttt cccc FRCE Vsss ssss ssss ssss ssss
        // SSSS SSSS SSSS SSSS SSSS aaaa aaaa aaaa
        // aaaa aaaa AAAA AAAA AAAA AAAA AAAA AAAA
        // ATTT TTTT TTTT TTTT TTTT TTDD DDDD DDDD
        // Total header length: 16 bytes / 128 bit (+8 bytes from UDP = 24 bytes total)
        override fun decode(parts: List<ULong>) = PayloadPacketHeader(
            type = TypeId.ofId(parts[0].toUByte()) ?: error("Invalid packet type (${parts[0]})"),
            channel = if (parts[1] == 0uL) InternalChannel else (ChannelManager.INSTANCE[parts[1].toUByte()] ?: error("Invalid packet channel (${parts[1]})")),
            fragment = parts[2].toBoolean(),
            retransmission = parts[3].toBoolean(),
            compressed = parts[4].toBoolean(),
            encrypted = parts[5].toBoolean(),
            variant = parts[6].toBoolean(),
            sourceSessionId = parts[7].toUInt(),
            sequenceId = parts[8].toUInt(),
            acknowledgement = parts[9].toUInt(),
            acknowledgementBitfield = parts[10].toUInt(),
            tick = parts[11].toUInt(),
            dataType = parts[12].toUShort(),
        )
    }

    override fun encode(): ByteArray = dataFormat.encode(
        type.id.toULong(),
        ChannelManager.INSTANCE.getId(channel)?.toULong() ?: 0uL,
        if (fragment) 1uL else 0uL,
        if (retransmission) 1uL else 0uL,
        if (compressed) 1uL else 0uL,
        if (encrypted) 1uL else 0uL,
        if (variant) 1uL else 0uL,
        sourceSessionId.toULong(),
        sequenceId.toULong(),
        acknowledgement.toULong(),
        acknowledgementBitfield.toULong(),
        tick.toULong(),
        dataType.toULong(),
    )
}
