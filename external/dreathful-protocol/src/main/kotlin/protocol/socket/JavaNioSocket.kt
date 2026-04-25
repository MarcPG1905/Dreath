package protocol.socket

import java.lang.foreign.Arena
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.StandardProtocolFamily
import java.net.StandardSocketOptions
import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.nio.channels.DatagramChannel

internal class JavaNioSocket(server: Boolean, port: Int) : Socket(server, port) {
    private var channel = createChannel()

    private fun createChannel(): DatagramChannel = DatagramChannel.open(StandardProtocolFamily.INET6).apply {
        configureBlocking(true)
        setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024 * 1024)
        setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024 * 1024)
        bind(InetSocketAddress(if (server) port else 0)) // 0 lets the OS assign a random port.
    }

    var channelClosedCounter = 0

    override fun listenOnVThread() {
        if (!channel.isOpen)
            channel = createChannel() // Can't re-open a channel, which is why we just create a new one.

        Arena.ofConfined().use { arena ->
            val segment = arena.allocate(2048)
            val buffer = segment.asByteBuffer()

            while (running) {
                try {
                    buffer.clear()
                    val sender = channel.receive(buffer) ?: continue
                    buffer.flip()

                    val packetView = buffer.slice().asReadOnlyBuffer()
                    handlePacket(sender, packetView)
                } catch (e: ClosedChannelException) {
                    channelClosedCounter++
                    if (channelClosedCounter > 10) {
                        if (channel.isOpen) {
                            log.warn("Socket could not recover and is being fully closed.")
                            closeChannel()
                        }
                    } else {
                        log.warn("Socket has been forcefully interrupted/closed ($channelClosedCounter).")
                    }
                } catch (e: Exception) {
                    if (running) // Ignore errors after socket is closed, because those are most certainly just saying that the socket was closed.
                        log.error("Could not receive packet.", e)
                }
            }
        }
    }

    override fun closeChannel() {
        if (channel.isOpen)
            channel.close()
    }

    override fun sendRawToAddress(address: SocketAddress, rawData: ByteBuffer) {
        channel.send(rawData, address)
    }
}
