package com.marcpg.common.modules.protocol

import com.marcpg.common.Game
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

object PegUDPSocket {
    private val socket: DatagramSocket = DatagramSocket()

    init {
        socket.soTimeout = 3000
    }

    fun startListener() {
        thread {
            Game.LOG.info("Starting UDP socket for PegProtocol...")
            while (Game.running) {
                try {
                    val packet = receive()
                    val data = packet.data
                    Game.LOG.info("Received packet from ${packet.address}:${packet.port} containing: ${data.toHexString(HexFormat.UpperCase)}")
                } catch (ignored: SocketTimeoutException) {
                } catch (e: Exception) {
                    Game.LOG.info("Error receiving packet: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

    fun sendHandshake() {
        Game.LOG.info("Sending handshake...")
        val channel = PegChannel(1u, PegChannel.ChannelMode.SIMPLE, InetAddress.getByName(Game.CLI_ARGS.data[1]), Game.CLI_ARGS.data[2].toInt())
        channel.send("Hello, handshake!".toByteArray().toUByteArray())
        Game.LOG.info("Sent handshake!")
    }

    fun send(packet: ByteArray, address: InetAddress, port: Int) {
        val datagramPacket = DatagramPacket(packet, packet.size, address, port)
        socket.send(datagramPacket)
    }

    fun receive() : DatagramPacket {
        val buffer = ByteArray(1024)
        val packet = DatagramPacket(buffer, buffer.size)
        socket.receive(packet)
        return packet
    }

    fun close() {
        if (!socket.isClosed)
            socket.close()
    }
}
