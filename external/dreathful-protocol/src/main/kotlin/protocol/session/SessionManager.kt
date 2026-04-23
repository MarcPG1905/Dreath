package protocol.session

import com.marcpg.dreath.util.findInRangeForBitsNotInOrNull
import java.net.SocketAddress
import java.util.concurrent.ConcurrentHashMap

/**
 * Depending on the environment, this can serve two purposes:
 *
 * - **Server:** A list of all clients connected to this server.
 * - **Client:** A singleton list with the server this client is connected to.
 *
 * @author MarcPG
 * @since 0.1.0
 */
object SessionManager {
    const val SESSION_BITS = 4

    /** This socket's local session. */
    var localSession: LocalSession? = null

    private val sessionsById = ConcurrentHashMap<UInt, Session>()
    private val sessionsByAddress = ConcurrentHashMap<SocketAddress, Session>()

    operator fun get(id: UInt): Session? = sessionsById[id]
    operator fun get(address: SocketAddress): Session? = sessionsByAddress[address]

    fun all(): List<Session> = sessionsById.values.toList()

    fun forceAdd(session: Session) {
        sessionsById[session.id] = session
        session.address?.let { sessionsByAddress[it] = session }
    }

    fun getOrAddNormal(address: SocketAddress): Session = sessionsByAddress[address] ?: addNormal(address)

    fun addNormal(address: SocketAddress) = add(address) { NormalSession(address, it) }
    fun addLocal(address: SocketAddress) = add(address) { LocalSession(it) }

    private fun add(address: SocketAddress, constructor: (UInt) -> Session): Session {
        // Also reserve 0uL as a default which can never be taken and is used as a placeholder.
        val freeId = findInRangeForBitsNotInOrNull(SESSION_BITS, sessionsById.keys.map { it.toULong() } + 0uL)?.toUInt()
            ?: error("No free ID available for session")

        val session = constructor(freeId)
        sessionsById[freeId] = session
        sessionsByAddress[address] = session
        return session
    }

    internal fun updateAddress(session: Session, newAddress: SocketAddress) {
        sessionsByAddress[newAddress] = session
        sessionsByAddress.remove(session.address)
    }

    fun remove(id: UInt) = remove(sessionsById[id] ?: error("No session found for ID $id"))
    fun remove(address: SocketAddress) = remove(sessionsByAddress[address] ?: error("No session found for address $address"))

    fun remove(session: Session) {
        sessionsById.remove(session.id)
        sessionsByAddress.remove(session.address)
    }
}
