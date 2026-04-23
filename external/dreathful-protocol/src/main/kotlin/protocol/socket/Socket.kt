package protocol.socket

import com.marcpg.dreath.log.LoggerOwnerImpl
import protocol.Packet
import protocol.channel.InternalChannel
import protocol.session.LocalSession
import protocol.session.Session
import protocol.session.SessionManager
import java.net.SocketAddress
import java.nio.ByteBuffer

/**
 * Base abstract class for any socket which can receive and send data.
 *
 * @param server Whether this socket is running on a server or not.
 *
 * @author MarcPG
 * @since 0.1.0
 */
sealed class Socket(val server: Boolean, val port: Int) : LoggerOwnerImpl("Socket") {
    /** Whether this socket is currently running or not. */
    var running = false
        private set

    /** The virtual thread this socket is running on. */
    protected var thread: Thread? = null

    /** Called inside this socket's virtual thread to start listening for incoming packets. */
    protected abstract fun listenOnVThread()

    /** Closes this socket and its channel(s). */
    protected abstract fun closeChannel()

    /** Sends raw data to an address. */
    abstract fun sendRawToAddress(address: SocketAddress, rawData: ByteBuffer)

    /** Sends raw data to a session/connection. */
    open fun sendRaw(session: Session, rawData: ByteBuffer) = sendRawToAddress(session.address!!, rawData)

    init {
        if (server) SessionManager.localSession = LocalSession(0u)
    }

    /**
     * Opens this socket and starts listening for packets.
     *
     * Will fail if called while the socket is already running.
     */
    fun open() {
        check(!running) { "Socket is already running" }
        running = true

        log.fine("Starting UDP socket on port $port.")

        thread = Thread.startVirtualThread { listenOnVThread() }
    }

    /**
     * Closes this socket and starts listening for packets.
     *
     * Will fail if called while the socket is already running.
     */
    fun close() {
        check(running) { "Socket is not running" }
        running = false

        log.fine("Stopping UDP socket running on port $port.")

        closeChannel()
    }

    /** Sends a packet to an address. */
    fun sendToAddress(address: SocketAddress, packet: Packet) = sendRawToAddress(address, ByteBuffer.wrap(packet.encode()))

    /** Sends a packet to a session/connection. */
    fun send(session: Session, packet: Packet) = sendRaw(session, ByteBuffer.wrap(packet.encode()))

    fun connectToServer(address: SocketAddress) {
        require(!server) { "Only clients can connect to servers" }

        InternalChannel.connectToServer(address)
    }

    protected fun handlePacket(address: SocketAddress, buffer: ByteBuffer) {
        if (buffer.remaining() == 0) return

        val packet = Packet.decode(buffer)
        val header = packet.header

        val session = SessionManager[header.sourceSessionId]
        require(session == null || address == session.address) { "Origin address and session address do not match" }

        if (session == null) {
            header.channel.handleSessionless(address, packet)
        } else {
            header.channel.handle(session, packet)
        }
    }
}
