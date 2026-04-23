package protocol.header

import com.marcpg.dreath.util.toBoolean
import protocol.channel.Channel
import protocol.channel.ChannelManager
import protocol.channel.InternalChannel
import protocol.util.RawDataDecodableObj
import protocol.util.RawDataEncodable
import java.nio.ByteBuffer

data class PacketHeader(
    val type: TypeId, // 2 bit
    val channel: Channel, // 3 bit

    // Flags (4 bit):
    val fragment: Boolean = false,
    val retransmission: Boolean = false,
    val compressed: Boolean = false,
    val encrypted: Boolean = false,
    val variant: Boolean = false,

    val sourceSessionId: UInt, // 32 bit
    val sequenceId: UInt, // 32 bit

    val acknowledgement: UInt, // 32 bit
    val acknowledgementBitfield: UInt, // 32 bit

    val tick: UInt, // 20 bit

    val length: UShort, // 10 bit
) : RawDataEncodable {
    @Suppress("SpellCheckingInspection", "GrazieInspection")
    companion object : RawDataDecodableObj<PacketHeader>(
        2,  // t : Type (0–3)
        4,  // c : Channel (0–15)
        1,  // F : Fragment (0–1)
        1,  // R : Retransmission (0–1)
        1,  // C : Compressed (0–1)
        1,  // E : Encrypted (0–1)
        1,  // V : Variant (0–1)
        19, // s : Source Session ID (0–524287)
        20, // S : Sequence ID (0–1048575)
        20, // a : Acknowledgement (0–1048575)
        27, // A : Acknowledgement Bitfield (0–134217727)
        20, // T : Tick (0–1047575)
        11, // l : Length (0–2047)
    ) { // ttcc ccFR CEVs ssss ssss ssss ssss ssSS
        // SSSS SSSS SSSS SSSS SSaa aaaa aaaa aaaa
        // aaaa aaAA AAAA AAAA AAAA AAAA AAAA AAAA
        // ATTT TTTT TTTT TTTT TTTT Tlll llll llll
        // Total header lengtH: 15 bytes / 120 bit (+8 bytes from UDP = 23 bytes total)

        override fun decode(buffer: ByteBuffer): PacketHeader {
            val d = dataFormat.decode(buffer)
            return PacketHeader(
                type = TypeId.ofId(d[0].toUByte()) ?: error("Invalid packet type (${d[0]})"),
                channel = if (d[1] == 0uL) InternalChannel else (ChannelManager.INSTANCE[d[1].toUByte()] ?: error("Invalid packet channel (${d[1]})")),
                fragment = d[2].toBoolean(),
                retransmission = d[3].toBoolean(),
                compressed = d[4].toBoolean(),
                encrypted = d[5].toBoolean(),
                variant = d[6].toBoolean(),
                sourceSessionId = d[7].toUInt(),
                sequenceId = d[8].toUInt(),
                acknowledgement = d[9].toUInt(),
                acknowledgementBitfield = d[10].toUInt(),
                tick = d[11].toUInt(),
                length = d[12].toUShort(),
            )
        }
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
        length.toULong(),
    )
}

