package protocol.session

import com.marcpg.dreath.util.AtomicUInt
import protocol.Packet
import java.net.SocketAddress
import java.util.concurrent.ConcurrentHashMap

/**
 * Represents a session/connection in the protocol.
 * This keeps track of stuff like creation date, timeouts, sequence numbers, missing packets, etc.
 *
 * @param address This session's address to send/receive packet to/from.
 * @param id A unique ID assigned to this session for keeping track in the session manager.
 * @param state This session's current state.
 *
 * @author MarcPG
 * @since 0.1.0
 *
 * @see NormalSession
 * @see DummySession
 * @see LocalSession
 */
sealed class Session(
    address: SocketAddress?,
    val id: UInt,

    @Volatile var state: SessionState = SessionState.HANDSHAKING,
) {
    companion object {
        /**
         * Fake constructor which under the hood creates a [NormalSession], to keep the base [Session] sealed.
         */
        operator fun invoke(
            address: SocketAddress?,
            id: UInt,
            state: SessionState = SessionState.HANDSHAKING,
        ) = NormalSession(address, id, state)
    }

    /** This session's address to send/receive packet to/from. */
    var address: SocketAddress? = address
        set(value) {
            field = value
            if (value != null)
                SessionManager.updateAddress(this, value)
        }

    /** The time this session was initially created, which is often right after the first handshake was received. */
    val created = System.currentTimeMillis()

    /**
     * A calculated/tested MTU for this connection.
     * This is often either around 500, or 1500, depending on the connection and IPv4/IPv6.
     */
    // TODO: Calculate/test for this using RFC 8899.
    var mtu: Int? = null

    /**
     * When this session has last sent a packet to this client.
     * Used for detecting timeouts.
     */
    @Volatile var lastSeen = created
        private set

    /** The current sequence number for sending packets. */
    val sendSequence = AtomicUInt(0u)
    /** The current sequence number for receiving packets. */
    val receiveSequence = AtomicUInt(0u)

    /** List of all packets which have not yet been acknowledged by this session and may need retransmission. */
    val pendingAcknowledgements = ConcurrentHashMap<Long, Packet>()

    /**
     * Touches this session to update the [lastSeen] value and reset the always running countdown to a timeout.
     *
     * Should be called whenever receiving any packet from this client, no matter if it's data, ping, or whatever.
     */
    fun touch() { lastSeen = System.currentTimeMillis() }

    /**
     * Checks whether this session has timed out and needs to be cleaned up.
     */
    fun isTimedOut(timeout: Long) = timeout < System.currentTimeMillis() - lastSeen

    override fun toString(): String = "Session(address=$address, id=$id, state=$state)"

    override fun equals(other: Any?): Boolean = other is Session && id == other.id
    override fun hashCode(): Int = id.hashCode()
}

/**
 * A normal session for another client/server.
 *
 * @author MarcPG
 * @since 0.1.0
 */
class NormalSession(address: SocketAddress?, id: UInt, state: SessionState = SessionState.HANDSHAKING) : Session(address, id, state)

/**
 * A dummy session used for testing or for sending packets without needing an actual connection.
 *
 * @author MarcPG
 * @since 0.1.0
 */
class DummySession(address: SocketAddress?) : Session(address, 0u, SessionState.DUMMY) {
    override fun toString(): String = "DummySession(address=$address)"
}

/**
 * Usually used singleton-like to represent yourself.
 * How exactly this is used depends on whether you're a client or server:
 *
 * - **Client:** A copy of how the server stores you.
 * - **Server:** An immutable singleton with an ID of 0, representing how clients will store you.
 *
 * @author MarcPG
 * @since 0.1.0
 */
class LocalSession(id: UInt, state: SessionState = SessionState.SELF) : Session(null, id, state) {
    override fun toString(): String = "LocalSession(id=$id, state=$state)"
}
