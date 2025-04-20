package com.marcpg.common.modules.protocol

import java.net.InetAddress

class PegConnection(val clientId: String, val address: InetAddress, val port: Int) {
    private val channels: MutableMap<UByte, PegChannel> = mutableMapOf()

    fun addChannel(id: UByte, channel: PegChannel) {
        channels[id] = channel
    }
}
