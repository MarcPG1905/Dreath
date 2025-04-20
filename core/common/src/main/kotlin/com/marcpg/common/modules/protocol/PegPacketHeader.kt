package com.marcpg.common.modules.protocol

data class PegPacketHeader(
    val channel: UByte,             //  4 bits (16)     : s
    val sequenceId: UShort,         // 12 bits (4096)   : S
    val type: UByte,                //  2 bits (4)      : t
    var fragment: Boolean,          //  1 bit  (2)      : f
    val retransmission: Boolean,    //  1 bit  (2)      : r
    val id: UShort,                 // 16 bits (65536)  : i
    var length: UShort              // 10 bits (1024)   : l
                                    // 46 bits (64 Bit) : 0
) { // ssss SSSS SSSS SSSS ttfr iiii iiii iiii iiii llll llll ll00 0000 0000 0000 0000
    companion object {
        fun ofRaw(raw: ULong) : PegPacketHeader {
            val stream = (raw shr 0 and 0xFUL).toUByte()
            val sequenceId = (raw shr 4 and 0xFFFuL).toUShort()
            val type = (raw shr 16 and 0x3UL).toUByte()
            val fragment = (raw shr 18 and 0x1UL) != 0UL
            val retransmission = (raw shr 19 and 0x1UL) != 0UL
            val id = (raw shr 20 and 0xFFFFUL).toUShort()
            val length = (raw shr 36 and 0x3FFUL).toUShort()

            return PegPacketHeader(stream, sequenceId, type, fragment, retransmission, id, length)
        }
    }

    fun raw() : ULong {
        require(channel.toInt() in 0..0b1111)
        require(sequenceId.toInt() in 0..0b1111_1111_1111)
        require(type.toInt() in 0..0b11)
        require(length.toInt() in 0..0b11_1111_1111)

        var bits = 0uL

        bits = bits or ((channel.toULong() and 0xFUL) shl 0)
        bits = bits or ((sequenceId.toULong() and 0xFFFUL) shl 4)
        bits = bits or ((type.toULong() and 0x3UL) shl 16)
        bits = bits or ((if (fragment) 1UL else 0UL) shl 18)
        bits = bits or ((if (retransmission) 1UL else 0UL) shl 19)
        bits = bits or ((id.toULong() and 0xFFFFUL) shl 20)
        bits = bits or ((length.toULong() and 0x3FFUL) shl 36)

        return bits
    }
}
