package com.marcpg.common.modules.protocol

import java.net.InetAddress

class PegChannel(
    val id: UByte,
    val mode: ChannelMode,
    val remoteAddress: InetAddress,
    val remotePort: Int
) {
    enum class ChannelMode { SIMPLE, RELIABLE, UNRELIABLE }

    private val messageQueue: MutableList<ByteArray> = mutableListOf()

    fun sendData(data: ByteArray) {
        PegUDPSocket.send(data, remoteAddress, remotePort)
    }

    fun send(packet: PegPacket) = sendData(packet.raw())

    fun send(payload: UByteArray) = sendData(
        PegPacket.build(
            id, 0u, 0u,
            fragment = false,
            retransmission = false,
            id = 0u,
            payload = payload
        ).raw())

    fun receiveData() : ByteArray? {
        val packet = PegUDPSocket.receive()
        return packet.data
    }
}
